package com.graceful.skyward.web.dto
data class Residence(
    val residenceId: Int,
    val ownerId: String,
    val dimensionId: Int,
    val cityId: Int,
    val area: Area
)
