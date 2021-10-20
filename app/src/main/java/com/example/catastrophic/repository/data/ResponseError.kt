package com.example.catastrophic.repository.data

import okhttp3.ResponseBody
import java.lang.Exception

data class ResponseError(val responseBody: String): Exception()