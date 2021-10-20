package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.source.CatApiService
import com.example.catastrophic.testutils.TestTree
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException

class CatRepositoryTest {

    val pageSize = CatRepository.PAGE_SIZE

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
        val repository = CatRepository(apiService)

        runBlocking {
            val data = repository.getCatData(0)
            assertEquals(catData, data)
        }
    }

    fun testPaging(position: Int, pageNum: Int, indexInPage: Int) {
        val list = List(pageSize) { i -> CatData(url = "url_$i") }
        val response = Response.success(list)
        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(CatRepository.PAGE_SIZE, pageNum) } returns response
        val repository = CatRepository(apiService)

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
        val repository = CatRepository(apiService)

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
    fun `getCatData returns null if CatApiService throws`() {
        val apiService: CatApiService = mockk()
        coEvery { apiService.getCats(any(), any()) }.throws(UnknownHostException())
        val repository = CatRepository(apiService)

        runBlocking {
            val data = repository.getCatData(0)
            assertEquals(null, data)
        }
    }
}