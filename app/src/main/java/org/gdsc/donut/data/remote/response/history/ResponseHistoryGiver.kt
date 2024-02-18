package org.gdsc.donut.data.remote.response.history

data class ResponseHistoryGiver(
    val code: Int,
    val message: String,
    val `data`: ResponseHistoryReceiverData?
)

data class ResponseHistoryGiverData(
    val period: Int,
    val totalAmount: Long,
    val unreceived: Long,
    val received: Long,
    val msg: Long,
    val giftList: List<ResponseHistoryReceiverGift>?
)

data class ResponseHistoryGiverDonationList(
    val giftId: Long,
    val product: String,
    val price: Int,
    val dueDate: String,
    val status: String,
    val isAssigned: Boolean
)