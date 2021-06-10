package ru.wood.cuber.repositories

import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.room.DaoTreeRedact
import javax.inject.Inject

class RepositoryTreeRedact @Inject constructor(val dao : DaoTreeRedact) {

    fun getOne(id: Long): TreePosition{
        return dao.onePositionById(id)
    }

    fun saveList(list: List<TreePosition>): List<Long>{
        return dao.saveList(list)
    }

    fun saveContentList(contentTab: List<ContainerContentsTab>) : List<Long>{
        return dao.saveContentTabList(contentTab)
    }

    fun updatePositions(newDiameter: Int,newLength: Double, ids :List<Long>): Int{
        return dao.updatePositions(newDiameter,newLength, ids)
        return 0
    }

    fun deleteByLimit(container: Long, diameter: Int, length: Double, limit: Int) : Int{
        return dao.deleteByLimit(container, diameter, length, limit)
    }

    fun idList(currentContainer: Long, diameter: Int,length: Double): List<Long>{
        return dao.getPositions(currentContainer, diameter, length)
    }

    fun listPosition( list: List<Long>): List<TreePosition>{
        return dao.listPosition(list)
    }
}