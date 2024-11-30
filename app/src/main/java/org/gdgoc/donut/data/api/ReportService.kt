package org.gdgoc.donut.data.api

import org.gdgoc.donut.data.remote.request.report.RequestReport
import org.gdgoc.donut.data.remote.response.report.ResponseReport
import org.gdgoc.donut.data.remote.response.report.ResponseReportUnused
import org.gdgoc.donut.data.remote.response.report.ResponseReportUsed
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportService {
    @POST("report/use")
    suspend fun reportUsed(
        @Header("Authorization") accessToken: String,
        @Query("giftId") giftId: Long,
    ): ResponseReportUsed

    @POST("report/cheat")
    suspend fun reportCheat(
        @Header("Authorization") accessToken: String,
        @Body reportedItemInfo: RequestReport,
    ): ResponseReport

    @PATCH("report/{giftId}")
    suspend fun reportUnused(
        @Header("Authorization") accessToken: String,
        @Path("giftId") giftId: Long,
    ): ResponseReportUnused
}