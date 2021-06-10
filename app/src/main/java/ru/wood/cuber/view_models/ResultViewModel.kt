package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab
import ru.wood.cuber.data.params_classes.NewParams
import ru.wood.cuber.interactors.DeleteVolumes
import ru.wood.cuber.interactors.LoadResult
import ru.wood.cuber.interactors.SaveOne
import ru.wood.cuber.interactors.VolumeByLength
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val loadResult: LoadResult,
    private val saveOne: SaveOne,
    private val volume: VolumeByLength,
    private val deleteVolumes: DeleteVolumes
):BaseViewModel()  {
    var liveData = MutableLiveData<List<TreePosition>> ()

    fun getListPosition(containerId: Long){
        loadResult(containerId){
            liveData.value=it
        }
    }

    fun saveVolume(position: VolumesTab){
        saveOne(position){
            Loger.log("id of saved Volume-position = $it")
        }
    }
    suspend fun getVolume (container: Long, length: Double): Double{
        var newParams= NewParams (
            containerOfTrees = container,
            length = length)
        return volume.run(newParams)
    }
    fun clearBd(container: Long){
        deleteVolumes(container){
            if (it>0){
                Loger.log("bd is cleard at Volumes in this container")
            }
        }
    }
}