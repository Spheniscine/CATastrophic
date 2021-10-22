package com.github.spheniscine.catastrophic.repository.data

import java.lang.Exception

/** Exception thrown (and re-caught) when OkHttp Response from remote sources is an error */
data class ResponseError(val responseBody: String): Exception()