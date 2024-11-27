package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.request.auth.RequestGoogleLogin
import org.gdgoc.donut.data.remote.response.auth.ResponseGoogleLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleService {
    @POST("oauth2/v4/token")
    suspend fun signInWithGoogle(
        @Body googleLoginInfo: RequestGoogleLogin
    ): ResponseGoogleLogin
}