package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.response.home.ResponseHomeGiver
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiver
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBoxItem
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGiftItem
import org.gdgoc.donut.data.remote.response.home.ResponseWalletDetailItem
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiver
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HomeService {
    @GET("home/giver")
    suspend fun getGiverHomeInfo(
        @Header("Authorization") accessToken : String
    ): ResponseHomeGiver

    @GET("wallet")
    suspend fun getGiverWalletInfo(
        @Header("Authorization") accessToken : String
    ): ResponseWalletGiver

    @GET("wallet/{giftId}")
    suspend fun getWalletGiftInfo(
        @Header("Authorization") accessToken : String,
        @Path("giftId") boxId: Long
    ): ResponseWalletDetailItem

    @GET("home/receiver")
    suspend fun getReceiverHomeInfo(
        @Header("Authorization") accessToken : String
    ): ResponseHomeReceiver

    @GET("home/receiver/box/{boxId}")
    suspend fun getReceiverHomeBoxInfo(
        @Header("Authorization") accessToken : String,
        @Path("boxId") boxId: Long
    ): ResponseHomeReceiverBoxItem

    @GET("home/receiver/gift/{giftId}")
    suspend fun getReceiverHomeGiftInfo(
        @Header("Authorization") accessToken : String,
        @Path("giftId") giftId: Long
    ): ResponseHomeReceiverGiftItem
}