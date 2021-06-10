package ru.wood.cuber.interactors

import ru.wood.cuber.Loger
import ru.wood.cuber.data.MyOrderContentsTab
import ru.wood.cuber.interactors.ParamsClasses.NewParams2
import ru.wood.cuber.repositories.RepositoryContainRedact
import ru.wood.cuber.repositories.RepositoryContains
import javax.inject.Inject


class ChangeContainParams @Inject constructor(val repository: RepositoryContainRedact): UseCase<Int, NewParams2>(){

    override suspend fun run(params: NewParams2) :Int{
        val id=params.id!!
        val name=params.name!!
        val weight=params.weight!!
        return repository.changeParams(id,name,weight)
    }
}