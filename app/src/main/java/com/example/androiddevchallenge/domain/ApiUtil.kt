package com.example.androiddevchallenge.domain

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory


fun getOkHttp(): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
    builder.addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )
    return builder
}

inline fun <reified T> createApi(url: String, client: OkHttpClient): T {
    return Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(T::class.java)
}