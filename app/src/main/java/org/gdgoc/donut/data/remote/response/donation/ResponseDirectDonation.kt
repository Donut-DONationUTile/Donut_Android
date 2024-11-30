package org.gdgoc.donut.data.remote.response.donation

data class ResponseDirectDonation(
    val code: Int,
    val message: String,
    val data: ResponseAssignReceiverData?
)

data class ResponseDirectDonationData(
    val giftboxId: Int
)

