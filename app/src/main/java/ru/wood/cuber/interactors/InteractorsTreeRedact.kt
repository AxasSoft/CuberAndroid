package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.ContainerContentsTab
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import ru.wood.cuber.repositories.RepositoryTreeRedact
import ru.wood.cuber.repositories.RepositoryTrees
import javax.inject.Inject


class SaveListTree @Inject constructor(val repository: RepositoryTreeRedact): UseCase<List<Long>, List<TreePosition>>(){

    override suspend fun run(params: List<TreePosition>) :List<Long>{
        val list=repository.saveList(params)
        return list
    }
}

class SaveTreeContentList @Inject constructor(val repository: RepositoryTreeRedact): UseCase<Boolean, List<ContainerContentsTab>>(){

    override suspend fun run(params: List<ContainerContentsTab>) :Boolean{
        val longs=repository.saveContentList(params)
        Loger.log("id of saved position $longs")
        return longs.isNotEmpty()
    }
}
class UpdateTreePositions @Inject constructor(val repository: RepositoryTreeRedact): UseCase<Boolean,  NewParams>() {
    override suspend fun run(params: NewParams): Boolean{
        val diameter=params.diameter
        val length =params.length
        val list =params.idList

        val ok = repository.updatePositions(diameter!!,length!!,list!!)
        return ok>0
    }
}
class OnePositionById @Inject constructor(val repository: RepositoryTreeRedact): UseCase<TreePosition, Long?>() {
    override suspend fun run(params: Long?): TreePosition {
        return repository.getOne(params!!)
    }
}

class DeleteByLimit @Inject constructor(val repository: RepositoryTreeRedact): UseCase<Boolean, Limit>() {
    override suspend fun run(params: Limit): Boolean {
        val container=params.id
        val diameter=params.diameter
        val length =params.length
        val limit =params.limit
        Loger.log(limit)
        val ok=repository.deleteByLimit(container!!, diameter, length, limit)
        return ok>0
    }
}
class GetPositionsList @Inject constructor(val repository: RepositoryTreeRedact) : UseCase<List<Long>, NewParams>() {
    override suspend fun run( params: NewParams): List<Long> {
        val diameter=params.diameter
        val length =params.length
        val container =params.containerOfTrees
        return repository.idList(container!!,diameter!!,length!!)
    }
}
class ListPosition @Inject constructor(val repository: RepositoryTreeRedact) : UseCase<List<TreePosition>, List<Long>>() {
    override suspend fun run( params: List<Long>): List<TreePosition> {
        return repository.listPosition(params)
    }
}