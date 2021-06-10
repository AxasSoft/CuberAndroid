package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContainerContentsTab (
        @PrimaryKey (autoGenerate = true)
        override var id:Long=0,
        val idOfContainer: Long?=null,
        val idOfTreePosition: Long?=null,

        //Если нужно будет менять длину именно для всего расчета, а не котнейнера
        val idOfOrder: Long?=null
) :BaseItem()