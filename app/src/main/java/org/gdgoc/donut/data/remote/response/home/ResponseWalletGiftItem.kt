package org.gdgoc.donut.data.remote.response.home

data class ResponseWalletDetailItem(
    val code: Int,
    val message: String,
    val `data`: ResponseWalletDetailItemData?
)

data class ResponseWalletDetailItemData(
    val product: String,
    val price: Int,
    val dueDate: String,
    val imgUrl: String,
    val store: String,
    val status: String,
    val boxId: Long
)