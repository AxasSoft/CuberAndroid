package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.*
import ru.wood.cuber.interactors.ParamsClasses.Limit
import ru.wood.cuber.interactors.ParamsClasses.NewParams
import ru.wood.cuber.repositories.RepositoryTrees
import javax.inject.Inject

class LoadTrees @Inject constructor(val repository: RepositoryTrees): UseCase<List<TreePosition>, Long?>(){

    override suspend fun run(params: Long?): List<TreePosition> {
        return repository.loadList(params!!)
    }
}


class SimpleLoadTrees @Inject constructor(val repository: RepositoryTrees): UseCase<List<TreePosition>, Long?>(){

    override suspend fun run(params: Long?): List<TreePosition> {
        return repository.loadSimpleList(params!!)
    }
}

class SaveOneTree @Inject constructor(val repository: RepositoryTrees): UseCase<Long, TreePosition>(){

    override suspend fun run(params: TreePosition) :Long{
        val idOfConteiner=repository.saveOne(params)
        return idOfConteiner
    }
}

class SaveTreeContent @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, ContainerContentsTab>(){

    override suspend fun run(params: ContainerContentsTab) :Boolean{
        val ok=repository.saveContent(params)
        return ok!=0L
    }
}


class DeleteOneTree @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, TreePosition>() {

    override suspend fun run(params: TreePosition): Boolean {
        val length=params.length
        val diameter=params.diameter
        val ok = repository.delete(length,diameter)
        return ok > 0
    }
}

class DeleteForContainer @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, List<Long>>() {

    override suspend fun run(params: List<Long>): Boolean {
        val ok = repository.deleteForContainer(params)
        return ok > 0
    }
}

class ClearOneContain @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContent(params)
        return ok > 0
    }
}

class UpdateTreeLength @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, NewParams>() {
    override suspend fun run(params: NewParams): Boolean{
        val container=params.containerOfTrees
        val length=params.length
        val ok = repository.updateLength(container!!,length!!)
        return ok>0
    }
}
class UpdateVolume @Inject constructor(val repository: RepositoryTrees): UseCase<Boolean, NewParams>() {
    override suspend fun run(params: NewParams): Boolean{
        val id=params.id
        val volume=params.volume
        val ok = repository.updateVolumes(id!!,volume!!)
        Loger.log("updating positions "+repository.updateVolumes(id!!,volume!!))
        return ok>0
    }
}


