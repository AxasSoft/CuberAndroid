package ru.wood.cuber.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class VolumesTab (
    @PrimaryKey( autoGenerate = true)
    val id: Long=0,
    val idOfContainer: Long,
    val length: Double,
    val diameter: Int,
    val volume : Double=0.00
)