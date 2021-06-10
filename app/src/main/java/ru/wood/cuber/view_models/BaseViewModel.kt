package ru.wood.cuber.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel: ViewModel() {
    var parentJob: Job = Job()

    override fun onCleared() {
        super.onCleared()
        unsubscribe()
    }

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
    val currentDate:String
    init {
        currentDate=getDate()
    }

    private fun getDate(): String{
        val sdf = SimpleDateFormat("dd/MM/yyyy ")
        val currentDate = sdf.format(Date())
        return currentDate
    }

    //abstract fun addNew(name:String)
    //abstract fun refreshList()
}