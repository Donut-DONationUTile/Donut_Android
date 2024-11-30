package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.response.mypage.ResponseGiverMyPage
import org.gdgoc.donut.data.remote.response.mypage.ResponseReceiverMyPage
import retrofit2.http.GET
import retrofit2.http.Header

interface MyPageService {
    @GET("mypage/giver")
    suspend fun getGiverMyPageInfo(
        @Header("Authorization") accessToken : String
    ): ResponseGiverMyPage

    @GET("mypage/receiver")
    suspend fun getReceiverMyPageInfo(
        @Header("Authorization") accessToken : String
    ): ResponseReceiverMyPage
}