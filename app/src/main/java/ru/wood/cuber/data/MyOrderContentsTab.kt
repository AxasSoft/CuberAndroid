package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MyOrderContentsTab (
        @PrimaryKey(autoGenerate = true)
        override var id: Long=0,
        val idOfOrder: Long?,
        val idOfContainers: Long?
        ):BaseItem()