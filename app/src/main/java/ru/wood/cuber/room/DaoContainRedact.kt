package ru.wood.cuber.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DaoContainRedact {

    @Query("UPDATE MyСontainer SET name=:name , weight=:weight WHERE id =:id ")
    fun changeParam(id: Long, name: String, weight: Long) :Int
}