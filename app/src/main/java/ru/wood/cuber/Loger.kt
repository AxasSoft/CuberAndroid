package ru.wood.cuber

import android.util.Log

const val MYLOG="myLog"
object Loger {
    private val logIsTrue: Boolean=true

    fun log(message: Any?){
        if (!logIsTrue){return}
        Log.d(MYLOG, message.toString())
    }
    fun log(message: Any?, tag: String){
        if (!logIsTrue){return}
        Log.d(tag, message.toString())
    }
}