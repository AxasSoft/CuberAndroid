package ru.wood.cuber.repositories

import ru.wood.cuber.Loger
import ru.wood.cuber.room.DaoContainRedact
import javax.inject.Inject

class RepositoryContainRedact @Inject constructor(val dao: DaoContainRedact) {

    fun changeParams(id: Long, name: String, weight: Long): Int{
        Loger.log("$id $name $weight"+"*-------------------------")
        Loger.log(dao.changeParam(id, name, weight).toString()+"*-------------------------")
        return dao.changeParam(id, name, weight)
    }
}