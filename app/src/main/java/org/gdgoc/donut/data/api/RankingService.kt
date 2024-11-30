package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.response.ranking.ResponseNumberRanking
import org.gdgoc.donut.data.remote.response.ranking.ResponsePriceRanking
import retrofit2.http.GET
import retrofit2.http.Header

interface RankingService {
    @GET("ranking/giver/info/price")
    suspend fun getPriceRankingInfo(
        @Header("Authorization") accessToken : String
    ): ResponsePriceRanking

    @GET("ranking/giver/info/number")
    suspend fun getNumberRankingInfo(
        @Header("Authorization") accessToken : String
    ): ResponseNumberRanking
}