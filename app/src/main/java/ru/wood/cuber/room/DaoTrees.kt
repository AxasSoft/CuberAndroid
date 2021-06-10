package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition

@Dao
interface DaoTrees {
    @Insert
    fun save(tree: TreePosition) : Long

    @Insert
    fun saveContentTab(containerContentsTab: ContainerContentsTab) : Long

    @Delete
    fun delete(tree: TreePosition): Int

    @Query("DELETE FROM TreePosition WHERE id IN (SELECT idOfTreePosition FROM ContainerContentsTab WHERE idOfContainer IN (:containers))")
    fun deleteForContainer(containers: List<Long>): Int

    @Query("DELETE FROM TreePosition " +
            "WHERE (TreePosition.length=:length AND TreePosition.diameter=:diameter)")
    fun delete(length: Double?, diameter: Int?): Int

    @Query("DELETE FROM ContainerContentsTab " +
            "WHERE ContainerContentsTab.idOfContainer=:idOfContainer")
    fun deleteContent(idOfContainer: Long) : Int

    @Query("SELECT *, count(TreePosition.id) AS quantity " +
            "FROM TreePosition " +
            "LEFT JOIN ContainerContentsTab " +
            "ON TreePosition.id==ContainerContentsTab.idOfTreePosition " +
            "WHERE ContainerContentsTab.idOfContainer=:container " +
            "GROUP BY TreePosition.diameter, TreePosition.length")
    fun load(container: Long) : List<TreePosition>

    @Query("UPDATE TreePosition SET length = :newLength WHERE id IN (SELECT idOfTreePosition FROM ContainerContentsTab WHERE idOfContainer=:currentContainer) ")
    fun updateLength(currentContainer: Long, newLength: Double): Int

    @Query("UPDATE TreePosition SET volume=:newVolume WHERE id =:id")
    fun updateOneVolume(id:Long, newVolume: Double): Int

    @Query("SELECT * FROM TreePosition WHERE id IN (SELECT idOfTreePosition FROM ContainerContentsTab WHERE idOfContainer=:container)")
    fun loadSimpleList(container: Long) : List<TreePosition>

}