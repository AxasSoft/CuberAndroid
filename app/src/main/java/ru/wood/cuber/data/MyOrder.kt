package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MyOrder (
        @PrimaryKey (autoGenerate = true)
        override var id: Long =0,
        var name: String?,
        val date:String?,
        val quantity: Int? =null// ??????????
        ):BaseItem() {

        }