package org.gdgoc.donut.data.remote.response.auth

data class ResponseSendFCMToken(
    val code: Int,
    val message: String,
    val data: ResponseSendMsgData?
)

data class ResponseSendMsgData(
    val token: String
)