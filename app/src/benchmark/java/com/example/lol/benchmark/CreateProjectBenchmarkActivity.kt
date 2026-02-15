package com.example.lol.benchmark

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lol.Projects.CreateProjectScreen
import com.example.lol.Projects.ProjectsViewModel
import com.example.lol.Projects.ProjectsViewModelFactory
import com.example.lol.data.network.TokenManager
import com.example.lol.ui.theme.LolTheme

class CreateProjectBenchmarkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val benchmarkTokenManager = TokenManager(this)
        benchmarkTokenManager.saveUserId("benchmark-user-id")

        setContent {
            LolTheme { CreateProjectBenchmarkRoot(application, benchmarkTokenManager) }
        }
    }
}

@Composable
private fun CreateProjectBenchmarkRoot(application: Application, tokenManager: TokenManager) {
    val navController = rememberNavController()
    val repository = BenchmarkProjectRepository()
    val projectsViewModel: ProjectsViewModel =
            viewModel(
                    factory =
                            ProjectsViewModelFactory(
                                    application = application,
                                    projectRepository = repository,
                                    tokenManager = tokenManager
                            )
            )

    NavHost(navController = navController, startDestination = "CreateProject") {
        composable("CreateProject") {
            CreateProjectScreen(navController = navController, viewModel = projectsViewModel)
        }
        composable("Projects") {
            Box(
                    modifier =
                            Modifier.fillMaxSize().semantics {
                                contentDescription = "benchmark_submit_complete"
                            },
                    contentAlignment = Alignment.Center
            ) { Text("Benchmark Projects") }
        }
    }
}
