package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.data.CatDataPage
import com.example.catastrophic.repository.source.local.CatPageDao
import com.example.catastrophic.repository.source.remote.CatApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class CatRepositoryTest {

    val pageSize = CatRepositoryImpl.PAGE_SIZE

//    companion object {
//        @BeforeClass @JvmStatic
//        fun setup() {
//            Timber.plant(TestTree())
//        }
//    }

    @Test
    fun `getCatData returns data from CatApiService`() {
        val catData = CatData(url = "some_url")
        val response = Response.success(List(pageSize) { catData })
        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(any(), any()) } returns response
        val repository = CatRepositoryImpl(apiService, mockk(relaxUnitFun = true))

        runBlocking {
            val data = repository.getCatData(0)
            assertEquals(catData, data)
        }
    }

    fun testPaging(position: Int, pageNum: Int, indexInPage: Int) {
        val list = List(pageSize) { i -> CatData(url = "url_$i") }
        val response = Response.success(list)
        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(CatRepositoryImpl.PAGE_SIZE, pageNum) } returns response
        val repository = CatRepositoryImpl(apiService, mockk(relaxUnitFun = true))

        runBlocking {
            val data = repository.getCatData(position)
            assertEquals(list[indexInPage], data)
        }
    }

    @Test
    fun `getCatData pages correctly`() {
        // assuming page size is set to 20

        testPaging(0, 1, 0)
        testPaging(19, 1, 19)
        testPaging(20, 2, 0)
        testPaging(317, 16, 17)
    }

    @Test
    fun `getCatData caches previous pages`() {
        val catData = CatData(url = "some_url")
        val response = Response.success(List(pageSize) { catData })
        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(any(), any()) } returns response
        val repository = CatRepositoryImpl(apiService, mockk(relaxUnitFun = true))

        runBlocking {
            repository.getCatData(0)
            repository.getCatData(1)
            repository.getCatData(2)
            repository.getCatData(0)

            // assuming page size is 20, all calls should only query the API service once
            coVerify(exactly = 1) { apiService.getCats(any(), any()) }
        }
    }

    /** Retrofit would throw UnknownHostException if Internet is not accessible */
    @Test
    fun `getCatData falls back to local database if CatApiService throws`() {
        val catData = CatData(url = "some_url")
        val catPage = CatDataPage(0, List(pageSize) { catData })

        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(any(), any()) }.throws(UnknownHostException())
        val catPageDao: CatPageDao = mockk()
        coEvery { catPageDao.loadSingle(0) } returns catPage
        val repository = CatRepositoryImpl(apiService, catPageDao)

        runBlocking {
            val data = repository.getCatData(0)
            assertEquals(catData, data)
        }
    }

    @Test
    fun `getCatData caches pages in local database`() {
        val catData = CatData(url = "some_url")
        val response = Response.success(List(pageSize) { catData })
        val catPage = CatDataPage(0, List(pageSize) { catData })

        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(any(), any()) } returns response
        val catPageDao: CatPageDao = mockk(relaxUnitFun = true)

        val repository = CatRepositoryImpl(apiService, catPageDao)

        runBlocking {
            repository.getCatData(0)
            coVerify { catPageDao.insert(eq(catPage)) }
        }
    }

//    /** Retrofit would throw UnknownHostException if Internet is not accessible */
//    @Test
//    fun `getCatData returns null if CatApiService throws`() {
//        val apiService: CatApiService = mockk()
//        coEvery { apiService.getCats(any(), any()) }.throws(UnknownHostException())
//        val repository = CatRepository(apiService, mockk())
//
//        runBlocking {
//            val data = repository.getCatData(0)
//            assertEquals(null, data)
//        }
//    }
}