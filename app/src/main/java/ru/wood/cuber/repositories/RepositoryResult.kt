package ru.wood.cuber.repositories

import ru.wood.cuber.Loger
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab
import ru.wood.cuber.room.DaoResults
import javax.inject.Inject

class RepositoryResult @Inject constructor(val dao: DaoResults) {
    fun loadResult(container: Long) : List<TreePosition>{
        return dao.loadResult(container)
    }

    fun saveOne(position : VolumesTab): Long{
        Loger.log("saveOne Volume position: $position \n , id=${dao.saveOneVolume(position)}")
        return dao.saveOneVolume(position)
    }

    fun volumeByLength(length: Double, container: Long): Double{
        Loger.log("volumeByLength = ${dao.groupByLength(length, container)} \n length = $length , container=$container")
        return dao.groupByLength(length, container)
    }

    fun deleteVolumes (container: Long): Int{
        Loger.log("deleteVolumes result = ${dao.delete(container)}")
        return dao.delete(container)
    }
}