package com.example.catastrophic.repository.source.remote

import androidx.room.TypeConverter
import com.example.catastrophic.BuildConfig
import com.example.catastrophic.repository.data.CatData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val SAMPLE_RESPONSE = """[{"breeds":[],"id":"1gd","url":"https://cdn2.thecatapi.com/images/1gd.png","width":640,"height":480},{"breeds":[],"id":"630","url":"https://cdn2.thecatapi.com/images/630.png","width":1024,"height":768},{"breeds":[],"id":"ag6","url":"https://cdn2.thecatapi.com/images/ag6.png","width":1024,"height":1024},{"breeds":[],"id":"b95","url":"https://cdn2.thecatapi.com/images/b95.png","width":500,"height":375},{"breeds":[],"id":"bd8","url":"https://cdn2.thecatapi.com/images/bd8.png","width":500,"height":333},{"breeds":[],"id":"MTUwNzA3Mw","url":"https://cdn2.thecatapi.com/images/MTUwNzA3Mw.png","width":500,"height":741},{"breeds":[],"id":"MTk1NTUyNg","url":"https://cdn2.thecatapi.com/images/MTk1NTUyNg.png","width":426,"height":566},{"breeds":[{"weight":{"imperial":"6 - 12","metric":"3 - 5"},"id":"sphy","name":"Sphynx","cfa_url":"http://cfa.org/Breeds/BreedsSthruT/Sphynx.aspx","vetstreet_url":"http://www.vetstreet.com/cats/sphynx","vcahospitals_url":"https://vcahospitals.com/know-your-pet/cat-breeds/sphynx","temperament":"Loyal, Inquisitive, Friendly, Quiet, Gentle","origin":"Canada","country_codes":"CA","country_code":"CA","description":"The Sphynx is an intelligent, inquisitive, extremely friendly people-oriented breed. Sphynx commonly greet their owners  at the front door, with obvious excitement and happiness. She has an unexpected sense of humor that is often at odds with her dour expression.","life_span":"12 - 14","indoor":0,"lap":1,"alt_names":"Canadian Hairless, Canadian Sphynx","adaptability":5,"affection_level":5,"child_friendly":4,"dog_friendly":5,"energy_level":3,"grooming":2,"health_issues":4,"intelligence":5,"shedding_level":1,"social_needs":5,"stranger_friendly":5,"vocalisation":5,"experimental":0,"hairless":1,"natural":0,"rare":1,"rex":0,"suppressed_tail":0,"short_legs":0,"wikipedia_url":"https://en.wikipedia.org/wiki/Sphynx_(cat)","hypoallergenic":1,"reference_image_id":"BDb8ZXb1v"}],"id":"oMrk721sl","url":"https://cdn2.thecatapi.com/images/oMrk721sl.png","width":1224,"height":648},{"breeds":[],"id":"y8p-R0Ecd","url":"https://cdn2.thecatapi.com/images/y8p-R0Ecd.png","width":372,"height":313},{"breeds":[],"id":"UKc2FuK7J","url":"https://cdn2.thecatapi.com/images/UKc2FuK7J.png","width":2232,"height":1920},{"breeds":[],"id":"xuR2bw32K","url":"https://cdn2.thecatapi.com/images/xuR2bw32K.png","width":2232,"height":1920},{"breeds":[],"id":"8x4MlAzwH","url":"https://cdn2.thecatapi.com/images/8x4MlAzwH.png","width":2232,"height":1920},{"breeds":[],"id":"1b7bVFd_Q","url":"https://cdn2.thecatapi.com/images/1b7bVFd_Q.png","width":2232,"height":1920},{"breeds":[],"id":"MmblEizST","url":"https://cdn2.thecatapi.com/images/MmblEizST.png","width":2232,"height":1920},{"breeds":[],"id":"Xgus-uqRQ","url":"https://cdn2.thecatapi.com/images/Xgus-uqRQ.png","width":980,"height":980},{"breeds":[],"id":"nIoQyeLs8","url":"https://cdn2.thecatapi.com/images/nIoQyeLs8.png","width":2232,"height":1920},{"breeds":[],"id":"fp1n_W9qh","url":"https://cdn2.thecatapi.com/images/fp1n_W9qh.png","width":2232,"height":1920},{"breeds":[],"id":"3_AcqYjBE","url":"https://cdn2.thecatapi.com/images/3_AcqYjBE.png","width":483,"height":239},{"breeds":[],"id":"kfgFxLP7U","url":"https://cdn2.thecatapi.com/images/kfgFxLP7U.png","width":2232,"height":1920},{"breeds":[],"id":"UOX__3Q-5","url":"https://cdn2.thecatapi.com/images/UOX__3Q-5.png","width":800,"height":800}]"""

class CatApiServiceTest {

    @Test
    fun `getCats test`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody(SAMPLE_RESPONSE))
        server.start()

        val baseUrl = server.url("/")

        val apiService = CatApiService.create(baseUrl.toString())
        runBlocking {
            val response = apiService.getCats(20, 1)
            assertTrue(response.isSuccessful, "response failed")

            val request = server.takeRequest(100, TimeUnit.MILLISECONDS)
            assertTrue(request!!.path!!.startsWith("/images/search?limit=20&page=1"),
                "request path incorrect")
            assertEquals(BuildConfig.CAT_API_KEY, request.getHeader("x-api-key"),
                "CatApi key incorrect")

            val body = response.body()

            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(List::class.java, CatData::class.java)
            val adapter = moshi.adapter<List<CatData>>(type)
            val expected = adapter.fromJson(SAMPLE_RESPONSE)
            assertEquals(expected, body, "response body incorrect")
        }
    }
}