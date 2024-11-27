package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiver
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDetail
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryReceiver
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.time.LocalDateTime

interface HistoryService {
    @GET("history/receiver/info")
    suspend fun getReceiverHistoryInfo(
        @Header("Authorization") accessToken : String
    ): ResponseHistoryReceiver

    @GET("history/giver/info/{donateDate}")
    suspend fun getGiverHistoryInfo(
        @Header("Authorization") accessToken : String,
        @Path("donateDate") donateDate: LocalDateTime
    ): ResponseHistoryGiver

    @GET("history/giver/info/detail/{giftId}")
    suspend fun getGiverHistoryDetailInfo(
        @Header("Authorization") accessToken : String,
        @Path("giftId") giftId: Long
    ): ResponseHistoryGiverDetail
}