package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.source.CatApiService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class CatRepositoryTest {

    val pageSize = CatRepository.PAGE_SIZE

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
        testPaging(0, 1, 0)
        testPaging(19, 1, 19)
        testPaging(20, 2, 0)
        testPaging(317, 16, 17)
    }

    
}