package ru.wood.cuber.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import ru.wood.cuber.volume.Volume
import javax.inject.Inject

@HiltViewModel
class TreeRedactViewModel @Inject constructor(
        private val saveList: SaveListTree,
        private val updateParams: UpdateTreePositions,
        private val getOne: OnePositionById,
        private val deleteByLimit: DeleteByLimit,
        private val getPosiitonList: GetPositionsList,
        private val saveMoreContent: SaveTreeContentList,
        private val listPosition: ListPosition,
        private val updateVolume:UpdateVolume

        ) : BaseViewModel() {
    var onePositionLiveData = MutableLiveData<TreePosition>()
    var paramsIsSaved= MutableLiveData<Boolean>()
    var redactFinished= MutableLiveData<Boolean>()
    val callbackThread = MutableLiveData<Boolean>()

    fun getOneTree(id: Long){
        Loger.log("example id $id")
        getOne(id){
            Loger.log("example $it")
            onePositionLiveData.value=it
        }
    }

    fun saveNewParams(
            container:Long,
            lastLength: Double,
            lastdiameter: Int,
            newLength: Double,
            newDiameter: Int){
        val lastParams= NewParams(
                containerOfTrees =container,
                length=lastLength,
                diameter=lastdiameter
        )
        getPosiitonList(lastParams){

            val newParams= NewParams(
                    containerOfTrees=container,
                    length = newLength,
                    diameter = newDiameter,
                    idList = it
            )
            Loger.log("new params for update ${newParams.containerOfTrees} ${newParams.length} ${newParams.diameter} ${newParams.idList}")
            update(newParams, it)
        }
    }

    private fun update(newParams: NewParams, id: List<Long>){
        updateParams(newParams){
            if (it){
                CoroutineScope(Dispatchers.IO).launch {
                    val result= async(Dispatchers.IO){
                        listPosition.run(id)
                    }
                    val listOfTreePosition=result.await()
                    Loger.log("listOfTreePosition $listOfTreePosition")
                    changeVolumes(listOfTreePosition)
                    paramsIsSaved.postValue(true)
                }
                /*listPosition(id){
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!WHERE?
                    changeVolumes(it)
                    Loger.log("positions for update $it")

                }*/
            }
        }
    }
    private fun changeVolumes(list: List<TreePosition>){
        Loger.log("changeVolumes $list")
        parentJob=GlobalScope.launch {
            for (x in list.indices){
                Loger.log("----------------------indice $x")
                val newVolume= Volume.calculateOne(
                        list[x].diameter!!,
                        list[x].length!!,
                        list[x].quantity)
                val param= NewParams(id = list[x].id, volume = newVolume)
                val result=async(Dispatchers.IO) {
                    updateVolume.run(param)
                }
                result.await()
                Loger.log("-result of changeValue ------$newVolume------------ ${list[x]}    ${result.await()}")
                callbackThread.postValue(true)
            }
        }
    }

    fun addPositions(
            container:Long,count: Int, length: Double, diameter: Int){
        val list: MutableList<TreePosition> = ArrayList()
        for (x in 1..count){
            list.add(TreePosition(length = length, diameter =diameter ))
        }

        saveList(list){ listId->
            listPosition(listId){ listPos->changeVolumes(listPos) }
                saveContentList(idOfContain =container,
                        idList = listId)
        }
    }
    fun limitDelete(container: Long, diameter: Int, length: Double, limit: Int){
        val limit= Limit(container, diameter, length, limit)
        deleteByLimit(limit){
            if (it){
                redactFinished.value=true
                //refreshList(container)
            }
        }
    }
    private fun saveContentList(idOfContain: Long,idList: List<Long>){
        val list : MutableList<ContainerContentsTab> = ArrayList()
        for (id in idList){
            list.add(ContainerContentsTab(
                    idOfContainer = idOfContain,
                    idOfTreePosition = id
            )
            )
        }
        saveMoreContent(list){
            if (it){
                redactFinished.value=true
                //refreshList(idOfContain)
            }
        }
    }
}