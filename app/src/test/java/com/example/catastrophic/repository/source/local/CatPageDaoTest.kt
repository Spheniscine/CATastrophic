package com.example.catastrophic.repository.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.repository.data.CatDataPage
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class CatPageDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var catPageDao: CatPageDao

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()

        catPageDao = database.catPageDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `insert and retrieve test`() {
        val page = CatDataPage(0, listOf(CatData(url = "some_url")))
        runBlocking {
            assertEquals(null, catPageDao.loadSingle(0),
                "should return null before insertion")
            catPageDao.insert(page)
            assertEquals(page, catPageDao.loadSingle(0),
                "failed to retrieve page after insertion")
        }
    }
}