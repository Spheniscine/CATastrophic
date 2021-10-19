package com.example.catastrophic.repository.source

import com.example.catastrophic.BuildConfig
import com.example.catastrophic.repository.data.CatData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface CatApiService {
    companion object {
        private fun retrofit(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

            return Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        fun create(): CatApiService {
            return retrofit().create(CatApiService::class.java)
        }
    }

    @Headers("x-api-key: ${BuildConfig.CAT_API_KEY}")
    @GET("images/search")
    suspend fun getCats(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        //@Query("mime_types") mimeTypes: String = "png",
        @Query("order") order: String = "Rand"): Response<List<CatData>>
}