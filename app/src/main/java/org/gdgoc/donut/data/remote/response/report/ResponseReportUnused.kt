package org.gdgoc.donut.data.remote.response.report

data class ResponseReportUnused(
    val code: Int,
    val message: String,
    val `data`: ResponseReportUnusedData?
)

data class ResponseReportUnusedData(
    val isLast: Boolean
)