package org.gdgoc.donut.data.remote.response.history

data class ResponseHistoryReceiver(
    val code: Int,
    val message: String,
    val `data`: ResponseHistoryReceiverData?
)

data class ResponseHistoryReceiverData(
    val amount: Int,
    val availability: Boolean,
    val giftList: List<ResponseHistoryReceiverGift>?
)

data class ResponseHistoryReceiverGift(
    val giftId: Long,
    val product: String,
    val price: Int,
    val dueDate: String,
    val isUsed: String
)