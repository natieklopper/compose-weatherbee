package com.example.androiddevchallenge.domain

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherBeeApi {
    private val appId get() = "92535f276b064f6f008ce34da134a215"

    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: String = "",
        @Query("lon") lon: String = "",
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") ai: String = appId
    ): Response<WeatherBee>
}

object WeatherApiAdapter {
    private const val baseUrl = "https://api.openweathermap.org/data/2.5/"

    val apiClient: WeatherBeeApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkHttp())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherBeeApi::class.java)

    private fun getOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        return builder.build()
    }
}
