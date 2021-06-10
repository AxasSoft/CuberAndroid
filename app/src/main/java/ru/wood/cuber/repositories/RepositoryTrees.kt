package ru.wood.cuber.repositories

import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.room.DaoTrees
import javax.inject.Inject

class RepositoryTrees @Inject constructor(val dao: DaoTrees) {

    fun getListTreesForExample() : List<TreePosition>{
        val tree1= TreePosition(1, 5.0, 18,  0.651,)
        val tree2= TreePosition(2, 5.0, 18,  0.951)
        val tree3= TreePosition(3, 5.0, 20,  0.751)
        val tree4= TreePosition(4, 8.0, 18,  0.651)
        val tree5= TreePosition(5, 5.0, 21, 0.251)
        val tree6= TreePosition(6, 5.0, 24,  0.751)
        val tree7= TreePosition(7, 5.0, 30,  0.651)

        val list: List<TreePosition> = arrayListOf(tree1,tree2,tree3,tree4,tree5,tree6,tree7);
        return list
    }
    fun saveOne(one: TreePosition): Long{
        return dao.save(one)
    }
    fun saveContent(contentTab: ContainerContentsTab): Long{
        return dao.saveContentTab(contentTab)
    }
    fun loadList(containId: Long): List<TreePosition> {
        return dao.load(containId)
    }

    fun  delete(length: Double?, diameter: Int?): Int{
        val resultDelete=dao.delete(length,diameter)
        Loger.log("resultDelete = $resultDelete")
        return resultDelete
    }
    fun deleteContent(idOfContainer: Long): Int{
        val resultContentDelete = dao.deleteContent(idOfContainer)
        Loger.log("resultContentDelete = $resultContentDelete")
        return resultContentDelete
    }
    fun updateLength(currentContainer: Long, newLength: Double): Int{
        return dao.updateLength(currentContainer, newLength)
    }
    fun updateVolumes(id: Long, newVolume: Double): Int{
        return dao.updateOneVolume(id = id, newVolume = newVolume)
    }

    fun loadSimpleList(container: Long): List<TreePosition>{
        return dao.loadSimpleList(container)
    }
    fun deleteForContainer(container: List<Long>): Int{
        return dao.deleteForContainer(container)
    }
}