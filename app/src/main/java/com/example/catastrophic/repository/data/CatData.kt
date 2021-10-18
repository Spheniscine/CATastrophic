package com.example.catastrophic.repository.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatData(
    @field:Json(name = "url") val url: String
)