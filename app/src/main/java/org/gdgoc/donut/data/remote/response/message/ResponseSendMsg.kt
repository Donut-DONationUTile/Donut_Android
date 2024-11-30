package org.gdgoc.donut.data.remote.response.message

data class ResponseSendMsg(
    val code: Int,
    val message: String,
    val data: ResponseSendMsgData?
)

data class ResponseSendMsgData(
    val msg: String
)