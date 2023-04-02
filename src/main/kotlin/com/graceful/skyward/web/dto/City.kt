package com.graceful.skyward.web.dto

import org.springframework.data.relational.core.mapping.Column

data class City(
        val cityId: Int,
        val dimensionId: Int,
        val cityName: String,
        val area: Area
    )
