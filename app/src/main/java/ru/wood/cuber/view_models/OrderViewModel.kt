package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrder
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.interactors.*
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
        private val listForExample: LoadForExample,
        private val loadlist: LoadOrderList,
        private val save: SaveOneOrder,
        private val delete: DeleteOneOrder,
        private val deleteContent: ClearOneOrder,
        private val deleteContainers: DeleteContainers,
        private val loadContainerlist: LoadContains,
        private val deleteTrees:DeleteForContainer,
        private val containersId: ContainersIdByOrder
        ) :BaseViewModel() {

    var liveData = MutableLiveData<List<MyOrder>>()

    fun getExampleList(){
        listForExample(null){
            liveData.value=it
        }
    }
    fun refreshList(){
        loadlist(null){
            liveData.value=it
        }
    }

    fun addNew(name: String){
        val one = MyOrder(
                name = name,
                date = currentDate)

        save(one){
            val ok=it
            if (ok){
                refreshList()
            }
        }
    }

    fun deletePosition(one: MyOrder){
        delete(one){
            if (it){
                refreshList()
                clearContent(one.id)
            }
        }
    }

    private fun clearContent(order: Long){
        containersId(order){
            val list : List<Long> =it
            deleteTrees(list)
            deleteContainers(order){
                deleteContent(order)
            }
        }
    }
    suspend fun containerslist(order: Long):List<MyСontainer> {
        return loadContainerlist.run(order)!!
    }
}