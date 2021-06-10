package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.wood.cuber.Loger
import ru.wood.cuber.data.*
import ru.wood.cuber.interactors.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import ru.wood.cuber.volume.Volume
import java.util.*
import javax.inject.Inject
@HiltViewModel
class TreesViewModel @Inject constructor (
        private val save: SaveOneTree,
        private val saveContent: SaveTreeContent,
        private val loadlist: LoadTrees,
        private val deleteOne: DeleteOneTree,
        private val updateLength: UpdateTreeLength,
        private val loadOneContainer:LoadOne,
        private val simpleiList:SimpleLoadTrees,
        private val updateVolume:UpdateVolume
        ) : BaseViewModel () {
    var liveData = MutableLiveData<List<TreePosition>>()
    var containerLive = MutableLiveData<MyСontainer>()
    var diameters : MutableList<Int> = arrayListOf()

    var commonLength: Double?=null

    fun refreshList(idOfContain: Long){
        loadlist(idOfContain){
            Loger.log("let`s refreshList $it ---- for container $idOfContain")
            Collections.sort(it, kotlin.Comparator { o1, o2 -> o1.id.compareTo(o2.id) })


            liveData.value=it
        }
    }
    
    fun addNew(container: Long, diameter: Int?, volume: Double){
        val one = TreePosition(
                length = commonLength!!,
                diameter = diameter,
            volume=volume
        )

        save(one){
            val ok=it!=0L
            if (ok){
                if (!diameters.contains(diameter)){
                    diameters.add(diameter!!)
                }
                saveContent(
                        idOfContain= container,
                        idOfTree= it)
            }
        }
    }

    private fun saveContent(idOfContain: Long, idOfTree: Long){
        saveContent(ContainerContentsTab(
                idOfContainer = idOfContain,
                idOfTreePosition = idOfTree
        )){ contentSaved ->
            if(contentSaved){
                refreshList(idOfContain)
            }
        }
    }

    fun deletePosition (one : TreePosition, idOfContain:  Long){
        deleteOne(one){
            if (it){
                refreshList(idOfContain)
            }
        }
    }

    fun changeCommonLength(length : Double, container:  Long){
        val newLength = NewParams(containerOfTrees = container, length=length)
        updateLength(newLength){
            if (it){
                //changeVolumes(container,length)
                simpleiList(container){
                    Loger.log("positions for update $it")
                    changeVolumes(it)

                }
                refreshList(container)
            }
        }
    }
    private fun changeVolumes(list: List<TreePosition>){
        viewModelScope.launch(Dispatchers.Main) {
            for (x in list.indices){
                Loger.log("----------------------$x")
                val newVolume=Volume.calculateOne(
                        list[x].diameter!!,
                        list[x].length!!,
                        list[x].quantity)
                val param= NewParams(id = list[x].id, volume = newVolume)
                val result=async(Dispatchers.IO) {
                    updateVolume.run(param)
                }
               result.await()
                Loger.log("-------------- $list[x]")

            }
        }
    }

    fun loadContainer (id: Long){
        loadOneContainer(id){
            containerLive.value=it
        }
    }

    suspend fun loadList(container: Long): List<TreePosition>{
        return simpleiList.run(container)
    }
}