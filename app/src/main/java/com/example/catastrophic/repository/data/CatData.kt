package com.example.catastrophic.repository.data

import com.squareup.moshi.Json

data class CatData(
    @field:Json(name = "url") val url: String
)