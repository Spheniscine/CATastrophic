package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.data.CatDataPage
import com.example.catastrophic.repository.data.ResponseError
import com.example.catastrophic.repository.source.local.AppDatabase
import com.example.catastrophic.repository.source.local.CatPageDao
import com.example.catastrophic.repository.source.remote.CatApiService
import kotlinx.coroutines.*
import timber.log.Timber

interface CatProvider {
    val numCats: Int
    suspend fun getCatData(position: Int): CatData?
}

interface CatRepository: CatProvider

class CatRepositoryImpl(private val apiService: CatApiService, private val catPageDao: CatPageDao): CatRepository {
    companion object {
        const val PAGE_SIZE = 20
        const val ENDLESS_CATS = 300_000 // well, nearly so anyway
    }
    override val numCats: Int = ENDLESS_CATS
    private val coroutineScope = MainScope()

    private val pages = List(numCats / PAGE_SIZE) { pageNum ->
        lazy {
            coroutineScope.async(context = Dispatchers.IO) {
                val result = runCatching {
                    val response = apiService.getCats(PAGE_SIZE, pageNum + 1)
                    response.errorBody()?.use { body ->
                        throw ResponseError(body.string())
                    }
                    response.body()!!
                }

                result.onFailure {
                    Timber.e("error fetching data: $it")
                }

                val apiData = result.getOrNull()
                if(apiData != null) {
                    catPageDao.insert(CatDataPage(pageNum, apiData))
                    apiData
                } else catPageDao.loadSingle(pageNum)?.data
            }
        }
    }


    override suspend fun getCatData(position: Int): CatData? {
        val pageNum = position / PAGE_SIZE

        val page = pages[pageNum].value.await()

        return page?.get(position % PAGE_SIZE)
    }

}