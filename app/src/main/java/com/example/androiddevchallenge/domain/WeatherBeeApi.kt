/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
