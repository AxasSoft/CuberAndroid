package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoTreeRedact {

    @Insert
    fun saveList(trees: List<TreePosition>) : List<Long>

    @Insert
    fun saveContentTabList(containerContentsTab: List<ContainerContentsTab>) : List<Long>

    @Query("SELECT * FROM TreePosition WHERE id=:id")
    fun onePositionById(id: Long): TreePosition

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Query("UPDATE TreePosition SET length = :newLength, diameter=:newDiameter WHERE id IN (:idList) ")
    fun updatePositions(newDiameter: Int,newLength: Double, idList :List<Long>): Int

    @Query("DELETE FROM TreePosition WHERE TreePosition.id IN (SELECT TreePosition.id FROM TreePosition LEFT JOIN ContainerContentsTab ON ContainerContentsTab.idOfTreePosition=TreePosition.id WHERE (ContainerContentsTab.idOfContainer=:container AND TreePosition.diameter=:diameter AND TreePosition.length=:length) LIMIT :limit)")
    fun deleteByLimit(container: Long, diameter: Int, length: Double, limit: Int): Int

    @Query("SELECT id FROM TreePosition WHERE id IN(SELECT idOfTreePosition " +
            "FROM ContainerContentsTab " +
            "WHERE idOfContainer=:currentContainer) " +
            "AND diameter=:diameter AND length=:length")
    fun getPositions (currentContainer: Long, diameter: Int, length: Double): List<Long>

    @Query("SELECT * FROM TreePosition WHERE id IN (:list)")
    fun listPosition(list: List<Long>): List<TreePosition>

}