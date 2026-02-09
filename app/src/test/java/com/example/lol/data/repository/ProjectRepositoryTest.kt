
package com.example.lol.data.repository

import com.example.lol.data.network.api.MatuleApi
import com.example.lol.testdata.TestJsonResponses
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Unit-тесты для ProjectRepository с использованием MockWebServer.
 */
// Содержит набор тестов для проверки поведения соответствующего модуля.
class ProjectRepositoryTest {
    
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: ProjectRepository
    
    // Подготавливает тестовое окружение и зависимости перед запуском тестов.
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatuleApi::class.java)
        
        repository = ProjectRepository(api)
    }
    
    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
    
    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `getProjects success returns project list`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestJsonResponses.projectsListJson)
        )
        
        // When
        // Когда
        val result = repository.getProjects()
        
        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val projects = result.getOrNull()
        assertNotNull(projects)
        assertEquals(1, projects?.size)
        assertEquals("Мой проект", projects?.first()?.title)
    }
    
    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `createProject success returns created project`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestJsonResponses.createdProjectJson)
        )
        
        // When
        // Когда
        val result = repository.createProject(
            title = "Новый проект",
            typeProject = "Новинки",
            userId = "user123",
            dateStart = "2026-02-01",
            dateEnd = "2026-06-30",
            gender = "Женский",
            descriptionSource = "Новое описание",
            category = "Новинки",
            image = null
        )
        
        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val project = result.getOrNull()
        assertNotNull(project)
        assertEquals("proj2", project?.id)
        assertEquals("Новый проект", project?.title)
    }
    
    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `getProjects failure returns error`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(TestJsonResponses.errorJson)
        )
        
        // When
        // Когда
        val result = repository.getProjects()
        
        // Then
        // Тогда
        assertTrue(result.isError)
        assertNotNull(result.errorMessageOrNull())
    }
}
