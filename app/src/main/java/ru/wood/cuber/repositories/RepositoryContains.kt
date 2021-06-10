package ru.wood.cuber.repositories

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrderContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.room.DaoContains
import javax.inject.Inject

class RepositoryContains @Inject constructor(val dao: DaoContains){

    fun getListContainersForExample(): List<MyСontainer> {
        val container1 = MyСontainer(1, "20.05.2021", "IESU4321",32.3,112)
        val container2 = MyСontainer(2, "22.05.2021", "IESU4322",50.3,111)
        val container3 = MyСontainer(3, "24.05.2021", "IESU4323",32.3,112)
        val container4 = MyСontainer(4, "25.05.2021", "IESU4324",10.3,100)
        val container5 = MyСontainer(5, "27.05.2021", "IESU4325",60.3,95)
        val container6 = MyСontainer(6, "29.05.2021", "IESU4326",40.3,112)
        val container7 = MyСontainer(7, "30.05.2021", "IESU4327",32.3,86)

        val list: List<MyСontainer> =
            arrayListOf(container1,container2,container3,container4,container5,container6,container7)
        return list
    }

    fun saveOne(one: MyСontainer): Long{
        return dao.save(one)
    }
    fun saveContent(contentTab: MyOrderContentsTab): Long{
        return dao.saveContentTab(contentTab)
    }

    fun loadList(calculateId: Long): List<MyСontainer> {
        return dao.load(calculateId)
    }
    fun loadOne( id: Long) : MyСontainer{
        return dao.loadOne(id)
    }

    fun  deleteOne(one: MyСontainer): Int{
        val resultDelete=dao.delete(one)
        Loger.log("resultDelete = $resultDelete")
        return resultDelete
    }
    fun deleteContent(idOfCalculates: Long): Int{
        val resultContentDelete = dao.deleteContent(idOfCalculates)
        return resultContentDelete
    }
    fun getQuantity(container: Long): Int{
        return dao.calculateQuantity(container)
    }
    fun getAll(): List<MyСontainer>{
        return dao.loadAll()
    }
    fun deleteContainers(order : Long): Int{
        return dao.deleteContainers(order)
    }
    fun containersIdByOrder(order : Long): List<Long>{
        return dao.containersIdByOrder(order)
    }
    fun containerId(name: String): Long{
        return dao.getIdByName(name)
    }
}