package ru.wood.cuber.volume

import ru.wood.cuber.data.TreePosition

object Volume {
    fun calculateOne(diameter: Int, length_: Double, quantity: Int): Double{
        val diameter : Int = diameter
        val length : Number
        if (length_ % 1.0 == 0.0){
            length=length_.toInt()
        } else length=length_.toDouble()
        
        val coefficient: Number?
        val devider =40
        if (diameter<=devider){
            coefficient= Map1.coefficient[diameter]?.get(length)
        } else coefficient= Map2.coefficient[diameter]?.get(length)

        if (coefficient==null){
            return 0.00

        } else {
            val result =(coefficient as Double)*quantity
            return result
        }
    }
    suspend fun total(list : List<TreePosition>):Double{
        var sum : Double=0.0
        for (posiiton in list){
            val diameter = posiiton.diameter!!
            val length  =posiiton.length!!
            val result =calculateOne(diameter,length,1)?:0.0
            sum+=result
        }
        return sum
    }
}