package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyСontainer (
    @PrimaryKey(autoGenerate = true)
    override var id:Long=0,
    val date: String?,
    val name:String?,
    val volume: Double?=null,
    val quantity: Int?=null,
    val weight: Long?=3200
):BaseItem()
