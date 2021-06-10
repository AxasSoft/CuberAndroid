package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.wood.cuber.data.MyOrder
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.LoadAllContains
import ru.wood.cuber.interactors.LoadContains
import ru.wood.cuber.interactors.LoadTrees
import ru.wood.cuber.interactors.OneOrder
import ru.wood.cuber.room.DaoExcel
import ru.wood.cuber.volume.Volume
import javax.inject.Inject

@HiltViewModel
class ExcelViewModel @Inject constructor(
    private val listOfContainers: LoadAllContains,
    private val onOrder: OneOrder,
    private val listOfTrees: LoadTrees
) :BaseViewModel() {
    val liveData = MutableLiveData<MutableList<List<String>>>()

    private suspend fun getContainersList(): List<MyСontainer>{
        val list: List<MyСontainer> =listOfContainers.run(null)
        return list

    }

    private suspend fun getOrder(containerId: Long) : MyOrder{
        val order=onOrder.run(containerId)
        return order
    }

    private suspend fun getTreePositions(containerId: Long) : List<TreePosition>{
        val list : List<TreePosition> = listOfTrees.run(containerId)
        return list
    }



    suspend fun getAllRow() {
        var keyCount=1
        val allRow : MutableList<List<String>> = arrayListOf()

        val listOfContain:List<MyСontainer> = getContainersList()
        for (element in listOfContain){
            var container: MyСontainer= element
            val name =container.name
            val id=container.id

            val order: MyOrder =getOrder(id)
            val listOfTrees : List<TreePosition> = getTreePositions(id)

            for (i in listOfTrees.indices){
                val row : MutableList<String> = arrayListOf()
                var key="";
                var date="";
                var order_no="";
                var container_no="";
                var length="";
                var diameter="";
                var pieces="";
                var volume=""

                val lengthTemp=listOfTrees[i].length
                val diameterTemp=listOfTrees[i].diameter
                val quantityTemp=listOfTrees[i].quantity
                val volumeTemp= Volume.calculateOne(diameter = diameterTemp!!,
                    length_ = lengthTemp!!,
                    quantity = quantityTemp)

                key=keyCount.toString()
                keyCount++


                if (order.date==null || order.name==null){
                    println("order.date==null || order.name==null")
                    return
                }
                date=order.date
                order_no= order.name!!
                container_no=name!!
                length=lengthTemp.toString()
                diameter=diameterTemp.toString()
                pieces=quantityTemp.toString()
                if (volumeTemp==null){
                    volume=""
                }else volume=String.format("%.2f", volumeTemp)
                row.apply {
                    add(key); add(date); add( order_no); add(container_no); add(length); add(diameter);
                    add(pieces); add(volume)
                }
                allRow.add(row)
            }
        }
        liveData.postValue(allRow)
    }
}