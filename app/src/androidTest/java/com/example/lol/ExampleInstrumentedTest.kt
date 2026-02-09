package com.example.lol

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// Содержит набор тестов для проверки поведения соответствующего модуля.
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun useAppContext() {
        // Context of the app under test.
        // Контекст тестируемого приложения.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.lol", appContext.packageName)
    }
}