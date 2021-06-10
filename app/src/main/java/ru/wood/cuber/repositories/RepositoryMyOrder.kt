package ru.wood.cuber.repositories

import ru.wood.cuber.data.MyOrder
import ru.wood.cuber.room.DaoMyOrder
import javax.inject.Inject

class RepositoryMyOrder @Inject constructor(val dao: DaoMyOrder)  {

    fun getListCalculatesForExample(): List<MyOrder> {
        val container1 = MyOrder(1, "Расчет №1", "22.05.2021",5)
        val container2 = MyOrder(2, "Расчет №2", "22.05.2021",1)
        val container3 = MyOrder(3, "Расчет №3", "22.05.2021",2)
        val container4 = MyOrder(4, "Расчет №4", "22.05.2021",4)
        val container5 = MyOrder(5, "Расчет №5", "22.05.2021",5)

        val list: List<MyOrder> =
            arrayListOf(container1,container2,container3,container4,container5)
        return list
    }

    fun saveOne(one: MyOrder): Long{
        return dao.save(one)
    }

    fun loadList() : List<MyOrder>{
        return dao.load()
    }

    fun  deleteOne(one: MyOrder): Int{
        return deleteOne(one.id)
    }

    fun  deleteOne(id: Long): Int{
        return dao.delete(id)
    }

    fun getOrder(container: Long): MyOrder{
        return dao.getOrderForContainer(container)
    }
    fun orderId( container: Long): Long{
        return dao.getOrderId(container)
    }
}