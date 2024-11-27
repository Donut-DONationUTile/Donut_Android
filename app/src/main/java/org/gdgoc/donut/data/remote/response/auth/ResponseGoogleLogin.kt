package org.gdgoc.donut.data.remote.response.auth

data class ResponseGoogleLogin(
    var access_token: String,
    var expires_in: Int,
    val refresh_token: String,
    var scope: String,
    var token_type: String,
    val id_token: String
    )