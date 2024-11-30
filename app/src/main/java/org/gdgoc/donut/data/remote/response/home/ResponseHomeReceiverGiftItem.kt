package org.gdgoc.donut.data.remote.response.home

data class ResponseHomeReceiverGiftItem(
    val code: Int,
    val message: String,
    val `data`: ResponseHomeReceiverGiftItemData?
)

data class ResponseHomeReceiverGiftItemData(
    val product: String,
    val price: Int,
    val dueDate: String,
    val imgUrl: String,
    val store: String,
    val status: String,
    val boxId: Long
)