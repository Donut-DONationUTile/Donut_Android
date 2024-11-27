package org.gdgoc.donut.data.remote.response.auth

data class ResponseSignInReceiver(
    val code: Int,
    val message: String,
    val `data`: ResponseSignInReceiverData?
)

data class ResponseSignInReceiverData(
    val accesstoken: String,
    val refreshtoken: String,
    val name: String
)