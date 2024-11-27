package org.gdgoc.donut.data.remote.response.donation

data class ResponseAddToWallet(
    val code: Int,
    val message: String,
    val data: ResponseAddToWalletData?
)

data class ResponseAddToWalletData(
    val giftId: Int
)