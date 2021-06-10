package ru.wood.cuber.type

sealed class Failure:MyFailure()  {
    object error
}
open class MyFailure{
    open var message:String?=null
}