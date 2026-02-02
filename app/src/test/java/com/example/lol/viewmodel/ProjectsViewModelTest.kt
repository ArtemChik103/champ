
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
@OptIn(ExperimentalCoroutinesApi::class)
class ProjectsViewModelTest {
    
    private lateinit var fakeRepository: FakeProjectRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProjectRepository()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadProjects success returns project list`() = runTest {
        // Given
        val projects = listOf(
            TestJsonResponses.createTestProjectApi(id = "proj1", title = "Проект 1"),
            TestJsonResponses.createTestProjectApi(id = "proj2", title = "Проект 2")
        )
        fakeRepository.setGetProjectsSuccess(projects)
        
        // When
        val result = fakeRepository.getProjects()
        
        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        assertEquals(1, fakeRepository.getProjectsCallCount)
    }
    
    @Test
    fun `createProject success adds new project`() = runTest {
        // Given
        val newProject = TestJsonResponses.createTestProjectApi(
            id = "projNew",
            title = "Новый проект"
        )
        fakeRepository.setCreateProjectSuccess(newProject)
        
        // When
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
        assertTrue(result is NetworkResult.Success)
        assertEquals("projNew", (result as NetworkResult.Success).data.id)
        assertEquals("Новый проект", fakeRepository.lastCreatedTitle)
        assertEquals("Популярные", fakeRepository.lastCreatedCategory)
    }
    
    @Test
    fun `loadProjects failure returns error`() = runTest {
        // Given
        fakeRepository.setGetProjectsError("Network error", null)
        
        // When
        val result = fakeRepository.getProjects()
        
        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals("Network error", (result as NetworkResult.Error).message)
    }
}
