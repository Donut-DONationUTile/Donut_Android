package org.gdgoc.donut.data.remote.response.mypage

data class ResponseGiverMyPage(
    val code: Int,
    val message: String,
    val `data`: ResponseGiverMyPageData?
)

data class ResponseGiverMyPageData(
    val years: Int,
    val donation: Double,
    val stats: ResponseGiverMyPageStatData?
)

data class ResponseGiverMyPageStatData(
    val unreceived: Int,
    val received: Int,
    val message: Int
)