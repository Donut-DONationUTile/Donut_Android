package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.request.auth.RequestSendFCMToken
import org.gdgoc.donut.data.remote.request.auth.RequestSignInGiver
import org.gdgoc.donut.data.remote.request.auth.RequestSignInReceiver
import org.gdgoc.donut.data.remote.request.auth.RequestSignUpReceiver
import org.gdgoc.donut.data.remote.response.auth.ResponseSendFCMToken
import org.gdgoc.donut.data.remote.response.auth.ResponseSignInGiver
import org.gdgoc.donut.data.remote.response.auth.ResponseSignInReceiver
import org.gdgoc.donut.data.remote.response.auth.ResponseSignUpReceiver
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/receiver/signup")
    suspend fun signUpReceiver(
        @Body receiverSignUpInfo: RequestSignUpReceiver
    ): ResponseSignUpReceiver

    @POST("auth/receiver/signin")
    suspend fun signInReceiver(
        @Body receiverSignInInfo: RequestSignInReceiver
    ): ResponseSignInReceiver

    @POST("auth/giver/signin")
    suspend fun signInGiver(
        @Body giverSignInInfo: RequestSignInGiver
    ): ResponseSignInGiver

    @POST("fcm/token")
    suspend fun sendFCMToken(
        @Header("Authorization") accessToken : String,
        @Body fcmTokenInfo: RequestSendFCMToken
    ): ResponseSendFCMToken
}