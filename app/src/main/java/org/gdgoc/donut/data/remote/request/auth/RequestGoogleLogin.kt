package org.gdgoc.donut.data.remote.request.auth

data class RequestGoogleLogin(
    private val client_id: String,
    private val client_secret: String,
    private val code: String,
    private val grant_type: String,
    private val redirect_uri: String,
)
