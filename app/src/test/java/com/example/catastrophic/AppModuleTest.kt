package com.example.catastrophic

import io.mockk.mockk
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

/** basic dependency injection sanity check for applications that use Koin */
class AppModuleTest: KoinTest {

    @Test
    fun `check app module`() {
        val app: App = mockk()
        checkModules {
            modules(app.appModule())
        }
    }
}