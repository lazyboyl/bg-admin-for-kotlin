package com.github.lazyboyl.vcm.kotlin.web.core.entity

data class Flight(var flightId:Int,
                  var flightName:String){
    constructor():this(0,"")
}