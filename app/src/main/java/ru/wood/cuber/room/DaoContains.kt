package ru.wood.cuber.room

import androidx.room.*
import ru.wood.cuber.data.MyOrderContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoContains {
    @Insert
    fun save(myСontainer: MyСontainer): Long

    @Insert
    fun saveContentTab(contentTab: MyOrderContentsTab): Long

    @Delete
    fun delete(myСontainer: MyСontainer): Int

    @Query("DELETE FROM MyOrderContentsTab WHERE MyOrderContentsTab.idOfOrder=:idOfOrder")
    fun deleteContent(idOfOrder: Long): Int

    @Query("DELETE FROM MyСontainer WHERE id IN (SELECT idOfContainers FROM MyOrderContentsTab WHERE idOfOrder=:order )")
    fun deleteContainers(order: Long) : Int

    @Query("SELECT idOfContainers FROM MyOrderContentsTab WHERE MyOrderContentsTab.idOfOrder=:order")
    fun containersIdByOrder(order : Long): List<Long>

    @Query("SELECT * FROM MyСontainer LEFT JOIN MyOrderContentsTab ON MyСontainer.id==MyOrderContentsTab.idOfContainers  WHERE MyOrderContentsTab.idOfOrder=:orderId")
    fun load(orderId: Long): List<MyСontainer>

    @Query("SELECT * FROM MyСontainer WHERE id=:id ")
    fun loadOne(id: Long): MyСontainer

//--------------------------------------------------------------------------------------------------
    @Query("SELECT count(TreePosition.id) AS quantity FROM TreePosition INNER JOIN ContainerContentsTab ON idOfTreePosition=TreePosition.id WHERE idOfContainer=:container ")
    fun calculateQuantity(container: Long): Int

   /* @Query("SELECT count(TreePosition.id) AS quantity FROM TreePosition WHERE id IN (SELECT idOfTreePosition FROM ContainerContentsTab WHERE idOfContainer=:container)")
    fun calculateQuantity(container: Long): Int

    @Query("SELECT count(TreePosition.id) AS quantity FROM TreePosition LEFT JOIN ContainerContentsTab ON TreePosition.id==ContainerContentsTab.idOfTreePosition WHERE ContainerContentsTab.idOfContainer=:container")
    fun calculateQuantity(container: Long): Int*/

    @Query("SELECT * FROM MyСontainer")
    fun loadAll(): List<MyСontainer>

    @Query("SELECT id FROM MyСontainer WHERE name=:name LIMIT 1")
    fun getIdByName(name: String): Long
}
