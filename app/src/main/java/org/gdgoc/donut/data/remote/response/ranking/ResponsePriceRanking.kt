package org.gdgoc.donut.data.remote.response.ranking

data class ResponsePriceRanking(
    val code: Int,
    val message: String,
    val `data`: List<ResponsePriceRankingData>?
)

data class ResponsePriceRankingData(
    val rank: Int,
    val name: String,
    val price: Long
)