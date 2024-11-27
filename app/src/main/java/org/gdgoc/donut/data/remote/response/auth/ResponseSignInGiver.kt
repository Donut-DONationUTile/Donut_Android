package org.gdgoc.donut.data.remote.response.auth

data class ResponseSignInGiver(
    val code: Int,
    val message: String,
    val `data`: ResponseSignInGiverData?
)

data class ResponseSignInGiverData(
    val accesstoken: String,
    val refreshtoken: String,
    val name: String
)