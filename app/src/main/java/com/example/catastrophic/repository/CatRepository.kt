package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.data.CatDataPage
import com.example.catastrophic.repository.data.ResponseError
import com.example.catastrophic.repository.source.local.AppDatabase
import com.example.catastrophic.repository.source.local.CatPageDao
import com.example.catastrophic.repository.source.remote.CatApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    /** helper class to fetch a page of cat data and cache it in memory and in database */
    private inner class CatPageFetcher(val pageNum: Int) {
        private var result: List<CatData>? = null
        private val mutex = Mutex()

        suspend fun get(): List<CatData>? {
            result?.let { return it }
            mutex.withLock {
                result?.let { return it }
                Dispatchers.IO {
                    result = run {
                        // try to fetch from remote source (Cat API)
                        val res = runCatching {
                            val response = apiService.getCats(PAGE_SIZE, pageNum + 1)
                            response.errorBody()?.use { body ->
                                throw ResponseError(body.string())
                            }
                            response.body()!!
                        }

                        res.onFailure {
                            Timber.e("error fetching data: $it")
                        }

                        val apiData = res.getOrNull()
                        if (apiData != null) {
                            // remote source fetch successful, cache in database and return
                            catPageDao.insert(CatDataPage(pageNum, apiData))
                            apiData
                        } else {
                            // remote source fetch failed, try to fetch from local database
                            catPageDao.loadSingle(pageNum)?.data
                        }
                    }
                }
                return result
            }
        }
    }

    private val pageFetchers =
        List(numCats / PAGE_SIZE) { pageNum -> CatPageFetcher(pageNum) }


    override suspend fun getCatData(position: Int): CatData? {
        val pageNum = position / PAGE_SIZE

        val page = pageFetchers[pageNum].get()

        return page?.get(position % PAGE_SIZE)
    }

}