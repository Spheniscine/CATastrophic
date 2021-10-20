package com.example.catastrophic.repository

import android.util.Log
import android.util.SparseArray
import androidx.core.util.getOrElse
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.source.CatApiService
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

interface CatProvider {
    val numCats: Int
    suspend fun getCatData(position: Int): CatData?
}

class CatRepository(private val apiService: CatApiService): CatProvider {
    companion object {
        const val PAGE_SIZE = 20
        const val ENDLESS_CATS = 300_000 // well, nearly so anyway
    }
    override val numCats: Int = ENDLESS_CATS
    private val coroutineScope = MainScope()
    private val pages = List(numCats / PAGE_SIZE) { pageNum ->
        lazy {
            coroutineScope.async(context = Dispatchers.IO) {
                val response = apiService.getCats(PAGE_SIZE, pageNum + 1)
                response.errorBody()?.let { body ->
                    Log.e("CatRepository", "error fetching data: $body")
                    return@async null
                }
                response.body()!!
            }
        }
    }


    override suspend fun getCatData(position: Int): CatData? {
        val pageNum = position / PAGE_SIZE

        val page = pages[pageNum].value.await()

        return CatData("bad")
        //return page?.get(position % PAGE_SIZE)
    }

}