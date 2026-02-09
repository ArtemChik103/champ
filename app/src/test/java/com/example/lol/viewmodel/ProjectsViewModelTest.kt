
package com.example.lol.viewmodel

import com.example.lol.data.fake.FakeProjectRepository
import com.example.lol.data.network.NetworkResult
import com.example.lol.testdata.TestJsonResponses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit-тесты для логики проектов с использованием FakeProjectRepository.
 */
// Содержит набор тестов для проверки поведения соответствующего модуля.
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectsViewModelTest {
    
    private lateinit var fakeRepository: FakeProjectRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    // Подготавливает тестовое окружение и зависимости перед запуском тестов.
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProjectRepository()
    }
    
    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `loadProjects success returns project list`() = runTest {
        // Given
        // Дано
        val projects = listOf(
            TestJsonResponses.createTestProjectApi(id = "proj1", title = "Проект 1"),
            TestJsonResponses.createTestProjectApi(id = "proj2", title = "Проект 2")
        )
        fakeRepository.setGetProjectsSuccess(projects)
        
        // When
        // Когда
        val result = fakeRepository.getProjects()
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        assertEquals(1, fakeRepository.getProjectsCallCount)
    }
    
    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `createProject success adds new project`() = runTest {
        // Given
        // Дано
        val newProject = TestJsonResponses.createTestProjectApi(
            id = "projNew",
            title = "Новый проект"
        )
        fakeRepository.setCreateProjectSuccess(newProject)
        
        // When
        // Когда
        val result = fakeRepository.createProject(
            title = "Новый проект",
            typeProject = "Новинки",
            userId = "user123",
            dateStart = "2026-01-01",
            dateEnd = "2026-12-31",
            gender = "Мужской",
            descriptionSource = "Описание",
            category = "Популярные",
            image = null
        )
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Success)
        assertEquals("projNew", (result as NetworkResult.Success).data.id)
        assertEquals("Новый проект", fakeRepository.lastCreatedTitle)
        assertEquals("Популярные", fakeRepository.lastCreatedCategory)
    }
    
    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `loadProjects failure returns error`() = runTest {
        // Given
        // Дано
        fakeRepository.setGetProjectsError("Network error", null)
        
        // When
        // Когда
        val result = fakeRepository.getProjects()
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Error)
        assertEquals("Network error", (result as NetworkResult.Error).message)
    }
}
