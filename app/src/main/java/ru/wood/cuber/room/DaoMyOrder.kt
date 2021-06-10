package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.MyOrder

@Dao
interface DaoMyOrder {

    @Insert
    fun save(myCalculations: MyOrder) : Long

    @Query("SELECT * FROM MyOrder")
    fun load() : List<MyOrder>

    @Query("DELETE FROM MyOrder WHERE id=:id")
    fun delete(id: Long): Int

    @Query("SELECT *  FROM MyOrder LEFT JOIN MyOrderContentsTab ON MyOrderContentsTab.idOfOrder=MyOrder.id WHERE idOfContainers=:container")
    fun getOrderForContainer(container: Long): MyOrder

    @Query("SELECT MyOrderContentsTab.idOfOrder FROM MyOrderContentsTab WHERE idOfContainers=:container ")
    fun getOrderId (container: Long): Long
}