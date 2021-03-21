package com.example.androiddevchallenge.domain

import com.google.gson.annotations.SerializedName

data class Pexel(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("photos") val photos: List<Photos>,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("next_page") val next_page: String
)

data class Photos(
    @SerializedName("id") val id: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String,
    @SerializedName("photographer") val photographer: String,
    @SerializedName("photographer_url") val photographer_url: String,
    @SerializedName("photographer_id") val photographer_id: Int,
    @SerializedName("avg_color") val avg_color: String,
    @SerializedName("src") val src: Src,
    @SerializedName("liked") val liked: Boolean
)

data class Src(
    @SerializedName("original") val original: String,
    @SerializedName("large2x") val large2x: String,
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("small") val small: String,
    @SerializedName("portrait") val portrait: String,
    @SerializedName("landscape") val landscape: String,
    @SerializedName("tiny") val tiny: String
)