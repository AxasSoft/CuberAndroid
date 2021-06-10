package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrder
import ru.wood.cuber.repositories.RepositoryMyOrder
import javax.inject.Inject


class LoadForExample @Inject constructor(val repository: RepositoryMyOrder): UseCase<List<MyOrder>, Int?>(){

    override suspend fun run(params: Int?): List<MyOrder> {
        return repository.getListCalculatesForExample()
    }
}

class LoadOrderList @Inject constructor(val repository: RepositoryMyOrder): UseCase<List<MyOrder>, Int?>(){

    override suspend fun run(params: Int?): List<MyOrder> {
        return repository.loadList()
    }
}

class SaveOneOrder @Inject constructor(val repository: RepositoryMyOrder): UseCase<Boolean, MyOrder>(){

    override suspend fun run(params: MyOrder) :Boolean{
        val ok=repository.saveOne(params)
        Loger.log("id of saved position $ok")
        return ok!=0L
    }
}

class DeleteOneOrder @Inject constructor(val repository: RepositoryMyOrder): UseCase<Boolean, MyOrder>(){

    override suspend fun run(params: MyOrder) :Boolean{
        val ok=repository.deleteOne(params)
        Loger.log("id of saved position $ok")
        return ok>0
    }
}

class DeleteOneOrderById @Inject constructor(val repository: RepositoryMyOrder): UseCase<Boolean, Long>(){

    override suspend fun run(params: Long) :Boolean{
        val ok=repository.deleteOne(params)
        Loger.log("id of saved position $ok")
        return ok>0
    }
}

class OneOrder @Inject constructor(val repository: RepositoryMyOrder): UseCase<MyOrder, Long>(){

    override suspend fun run(params: Long): MyOrder {
        return repository.getOrder(params)
    }
}

class OrderId @Inject constructor(val repository: RepositoryMyOrder): UseCase<Long, Long>(){

    override suspend fun run(params: Long): Long {
        return repository.orderId(params)
    }
}

