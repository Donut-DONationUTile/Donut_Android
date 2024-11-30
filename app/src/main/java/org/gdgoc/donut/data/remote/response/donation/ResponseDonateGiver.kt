package org.gdgoc.donut.data.remote.response.donation

data class ResponseDonateGiver(
    val code: Int,
    val message: String,
    val data: ResponseDonateGiverData?
)

data class ResponseDonateGiverData(
    val giftId: Int
)