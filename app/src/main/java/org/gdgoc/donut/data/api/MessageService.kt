package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.request.message.RequestSendMsg
import org.gdgoc.donut.data.remote.response.message.ResponseSendMsg
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageService {
    @POST("message/receiver")
    suspend fun sendMessage(
        @Header("Authorization") accessToken: String,
        @Body receiverMsgInfo: RequestSendMsg
    ): ResponseSendMsg
}