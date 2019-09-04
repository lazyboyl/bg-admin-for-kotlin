package com.github.lazyboyl.vcm.kotlin.web.core.mapper

import com.github.lazyboyl.vcm.kotlin.web.core.dto.FlightDto
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Flight
import org.mapstruct.Mapper

@Mapper
interface FlightConverter {
    fun convertToDto(flight: Flight) : FlightDto
    fun convertToModel(flightDto: FlightDto) : Flight
}