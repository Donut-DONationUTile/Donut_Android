package org.gdgoc.donut.data.remote.response.report

data class ResponseReport(
    val code: Int,
    val message: String,
    val `data`: ResponseReportData?
)

data class ResponseReportData(
    val reportId: Int,
    val giftId: Int,
    val isLast: Boolean
)