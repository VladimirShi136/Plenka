package com.app.plenka.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("total") val total: Int?,
    @SerializedName("items") val items: List<VideoDto>?
)

data class VideoDto(
    @SerializedName("url") val url: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("site") val site: String?
)
