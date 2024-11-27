package org.gdgoc.donut.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "http://34.64.34.125:8080/api/"
    private const val Google_URL = "https://www.googleapis.com"

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val donutRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val googleRetrofit = Retrofit.Builder()
        .baseUrl(Google_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val authService: AuthService = donutRetrofit.create(AuthService::class.java)
    val googleService: GoogleService = googleRetrofit.create(GoogleService::class.java)
    val donationService: DonationService = donutRetrofit.create(DonationService::class.java)
    val historyService: HistoryService = donutRetrofit.create(HistoryService::class.java)
    val homeService: HomeService = donutRetrofit.create(HomeService::class.java)
    val rankingService: RankingService = donutRetrofit.create(RankingService::class.java)
    val reportService: ReportService = donutRetrofit.create(ReportService::class.java)
    val myPageService: MyPageService = donutRetrofit.create(MyPageService::class.java)
    val messageService: MessageService = donutRetrofit.create(MessageService::class.java)
}