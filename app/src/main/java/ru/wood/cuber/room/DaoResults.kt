package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab

@Dao
interface DaoResults {

    @Query("SELECT *, count(TreePosition.id) AS quantity " +
            "FROM TreePosition " +
            "LEFT JOIN ContainerContentsTab " +
            "ON TreePosition.id==ContainerContentsTab.idOfTreePosition " +
            "WHERE ContainerContentsTab.idOfContainer=:container " +
            "GROUP BY TreePosition.length")
    fun loadResult(container: Long) : List<TreePosition>

    @Insert
    fun saveOneVolume(position : VolumesTab): Long

    @Query("SELECT SUM(TreePosition.volume) FROM TreePosition LEFT JOIN ContainerContentsTab ON idOfContainer=:container WHERE idOfTreePosition=TreePosition.id AND TreePosition.length=:length")
    fun groupByLength(length: Double, container: Long):Double

    @Query("DELETE FROM VolumesTab WHERE idOfContainer=:container")
    fun delete(container: Long): Int




}