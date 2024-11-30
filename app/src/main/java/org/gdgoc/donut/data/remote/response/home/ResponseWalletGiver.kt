package org.gdgoc.donut.data.remote.response.home

data class ResponseWalletGiver(
    val code: Int,
    val message: String,
    val `data`: ResponseWalletGiverData?
)

data class ResponseWalletGiverData(
    val receiver: Int,
    val amount: Double,
    val cu: Int,
    val gs25: Int,
    val seveneleven: Int,
    val impendingList: List<ResponseWalletImpendingList>?,
    val giftList: List<ResponseWalletGiftList>?
)

data class ResponseWalletImpendingList(
    val giftId: Long,
    val days: Int,
    val store: String,
    val dueDate: String,
    val product: String,
    val price: Int
)

data class ResponseWalletGiftList(
    val giftId: Long,
    val days: Int,
    val store: String,
    val dueDate: String,
    val product: String,
    val price: Int
)