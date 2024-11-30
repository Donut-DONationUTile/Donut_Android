package org.gdgoc.donut.data.remote.response.home

data class ResponseHomeGiver(
    val code: Int,
    val message: String,
    val `data`: ResponseHomeGiverData?
)

data class ResponseHomeGiverData(
    val receivers: Int,
    val donated: Double,
    val need: Double
)