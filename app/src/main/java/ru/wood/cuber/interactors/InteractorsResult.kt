package ru.wood.cuber.interactors

import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab
import ru.wood.cuber.data.params_classes.NewParams
import ru.wood.cuber.repositories.RepositoryResult
import javax.inject.Inject


class LoadResult @Inject constructor(val repository: RepositoryResult): UseCase<List<TreePosition>, Long>(){

    override suspend fun run(params: Long): List<TreePosition> {
        return repository.loadResult(params)
    }
}

class SaveOne @Inject constructor(val repository: RepositoryResult): UseCase<Long, VolumesTab>(){

    override suspend fun run(params: VolumesTab): Long {
       return repository.saveOne(params)
    }
}

class VolumeByLength @Inject constructor(val repository: RepositoryResult): UseCase<Double, NewParams>(){

    override suspend fun run(params: NewParams): Double {
        val length=params.length
        val container=params.containerOfTrees
        return repository.volumeByLength(length, container)
    }
}

class DeleteVolumes @Inject constructor(val repository: RepositoryResult): UseCase<Int, Long>(){

    override suspend fun run(params: Long): Int {
        return repository.deleteVolumes(params)
    }
}