package com.github.lazyboyl.vcm.kotlin.web.core.dto

data class FlightDto(var flightId:Int,
                     var flightName:String){
    constructor():this(0,"")
}