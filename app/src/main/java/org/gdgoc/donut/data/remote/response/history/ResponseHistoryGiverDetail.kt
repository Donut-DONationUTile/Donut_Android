package org.gdgoc.donut.data.remote.response.history

data class ResponseHistoryGiverDetail(
    val code: Int,
    val message: String,
    val `data`: ResponseHistoryGiverDetailData?
)

data class ResponseHistoryGiverDetailData(
    val product: String,
    val amount: Int,
    val dueDate: String,
    val store: String,
    val receiver: String,
    val giver: String,
    val isAssigned: Boolean,
    val status: String,
    val message: String,
    val donateDate: String,
    val receivedDate: String
)