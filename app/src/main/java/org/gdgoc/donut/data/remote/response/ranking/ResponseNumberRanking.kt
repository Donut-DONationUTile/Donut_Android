package org.gdgoc.donut.data.remote.response.ranking

data class ResponseNumberRanking(
    val code: Int,
    val message: String,
    val `data`: List<ResponseNumberRankingData>?
)

data class ResponseNumberRankingData(
    val rank: Int,
    val name: String,
    val number: Long
)