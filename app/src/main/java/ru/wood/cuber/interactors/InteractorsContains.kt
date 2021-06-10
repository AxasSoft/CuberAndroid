package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrderContentsTab
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.repositories.RepositoryContains
import javax.inject.Inject

class LoadContains @Inject constructor(val repository: RepositoryContains): UseCase<List<MyСontainer>, Long?>(){

    override suspend fun run(params: Long?): List<MyСontainer> {
        return repository.loadList(params!!)
    }
}

class SaveOneContain @Inject constructor(val repository: RepositoryContains): UseCase<Long, MyСontainer>(){

    override suspend fun run(params: MyСontainer) :Long{
        val idOfConteiner=repository.saveOne(params)
        return idOfConteiner
    }
}

class SaveContent @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, MyOrderContentsTab>(){

    override suspend fun run(params: MyOrderContentsTab) :Boolean{
        val ok=repository.saveContent(params)
        return ok!=0L
    }
}

class DeleteOneContain @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, MyСontainer>() {

    override suspend fun run(params: MyСontainer): Boolean {
        val ok = repository.deleteOne(params)
        return ok > 0
    }
}

class DeleteContainers @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContainers(params)
        return ok > 0
    }
}

class ContainersIdByOrder @Inject constructor(val repository: RepositoryContains): UseCase<List<Long>, Long>() {

    override suspend fun run(params: Long): List<Long> {
       return repository.containersIdByOrder(params)
    }
}

class ClearOneOrder @Inject constructor(val repository: RepositoryContains): UseCase<Boolean, Long>() {

    override suspend fun run(params: Long): Boolean {
        val ok = repository.deleteContent(params)
        return ok > 0
    }
}

class LoadOne @Inject constructor(val repository: RepositoryContains): UseCase<MyСontainer, Long>() {

    override suspend fun run(params: Long): MyСontainer {
        return repository.loadOne(params)
    }
}

class CommonQuantity @Inject constructor(val repository: RepositoryContains): UseCase<Int, Long>() {

    override suspend fun run(params: Long): Int {
        return repository.getQuantity(params)
    }
}

class LoadAllContains @Inject constructor(val repository: RepositoryContains): UseCase<List<MyСontainer>, Nothing?>(){

    override suspend fun run(params: Nothing?): List<MyСontainer> {
        return repository.getAll()
    }
}

class ContainerId @Inject constructor(val repository: RepositoryContains): UseCase<Long, String>(){

    override suspend fun run(params: String): Long {
        return repository.containerId(params)
    }
}