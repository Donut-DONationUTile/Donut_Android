package org.gdgoc.donut.data.remote.response.auth

data class ResponseSignUpReceiver(
    val code: Int,
    val message: String,
    val `data`: String?
)