package com.example.catastrophic.repository

import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.source.CatApiService

interface CatApiRepository {
    suspend fun getCatData(): List<CatData>
}

class CatApiRepositoryImpl(private val service: CatApiService): CatApiRepository {
    override suspend fun getCatData(): List<CatData> {
        val response = service.getCats(21, 1)
        return response.body().orEmpty()
    }
}

//TODO: Move to tests
class MockCatApiRepository: CatApiRepository {
    override suspend fun getCatData(): List<CatData> {
        return listOf(
            CatData(url = "https://cdn2.thecatapi.com/images/d5.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/43n.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/640.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/bd5.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/d0g.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/MTUwODEyMg.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/MTkzNjMxNQ.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/tHbftm4i3.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/oErsLgoFu.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/2WA0h3d3W.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/vIRMUNHzA.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/AmHUJI8DY.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/gmK_l5MWm.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/mAGfq3LKj.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/2IpZYS6fW.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/drM-F4x01.png"),
            CatData(url = "https://example.com/dontexist.png"),
            CatData(url = "https://cdn2.thecatapi.com/images/qg0_IodJp.png"),
        )
    }
}