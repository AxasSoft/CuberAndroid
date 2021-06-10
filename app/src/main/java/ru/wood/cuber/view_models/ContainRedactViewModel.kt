package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.interactors.ChangeContainParams
import ru.wood.cuber.interactors.LoadOne
import ru.wood.cuber.interactors.ParamsClasses.NewParams2
import javax.inject.Inject

@HiltViewModel
class ContainRedactViewModel @Inject constructor(
    private val loadOneContainer: LoadOne,
    private val updateParam: ChangeContainParams
):BaseViewModel(){

    var containerLive = MutableLiveData<MyСontainer>()
    var paramsIsSaved= MutableLiveData<Boolean>()

    fun saveNewParams(container: Long ,name: String, weight: Long){
        val newParams=NewParams2(container,name, weight)
        updateParam(newParams){
            if (it>0){
                paramsIsSaved.value=true
            }
        }
    }

    fun loadContainer (id: Long){
        loadOneContainer(id){
            containerLive.value=it
        }
    }
}