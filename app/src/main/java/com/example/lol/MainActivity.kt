package com.example.lol

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lol.Cart.CartScreen
import com.example.lol.Cart.CartViewModel
import com.example.lol.Catalogue.CatalogueScreen
import com.example.lol.Catalogue.CatalogueViewModel
import com.example.lol.Main.MainScreen
import com.example.lol.Profile.MyOrdersScreen
import com.example.lol.Profile.ProfileScreen
import com.example.lol.Projects.CreateProjectScreen
import com.example.lol.Projects.ProjectsScreen
import com.example.lol.Projects.ProjectsViewModel
import com.example.lol.authorization.CreatePasswordScreen
import com.example.lol.authorization.CreatePinScreen
import com.example.lol.authorization.EmailCodeScreen
import com.example.lol.authorization.PinCodeScreen
import com.example.lol.authorization.SignInScreen
import com.example.lol.authorization.SignUpScreen
import com.example.lol.authorization.SplashScreen
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.LolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LolTheme {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "Splash") {
                        composable("Splash") { SplashScreen(navController = navController) }
                        composable("SignIn") { SignInScreen(navController = navController) }
                        composable("PinCode") { PinCodeScreen(navController = navController) }
                        composable("SignUp") { SignUpScreen(navController = navController) }
                        composable("EmailCode") { EmailCodeScreen(navController = navController) }
                        composable("CreatePassword") {
                            CreatePasswordScreen(navController = navController)
                        }
                        composable("CreatePin") { CreatePinScreen(navController = navController) }
                        composable("Home") {
                            MainContainer(
                                    application = application,
                                    rootNavController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContainer(application: Application, rootNavController: NavController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { com.example.lol.authorization.SessionManager(context) }
    val catalogueViewModel: CatalogueViewModel = viewModel { CatalogueViewModel(application) }
    val cartViewModel: CartViewModel = viewModel { CartViewModel() }
    val projectsViewModel: ProjectsViewModel = viewModel { ProjectsViewModel(application) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            backStackEntry.destination.route?.let { route -> sessionManager.saveLastRoute(route) }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar =
            currentRoute !in listOf("Cart", "ProductDetails/{productId}", "CreateProject")

    Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                    navController = navController,
                    startDestination = sessionManager.getLastRoute()
            ) {
                composable("Main") {
                    MainScreen(
                            navController = navController,
                            viewModel = catalogueViewModel,
                            cartViewModel = cartViewModel
                    )
                }
                composable("Catalogue") {
                    CatalogueScreen(
                            navController = navController,
                            viewModel = catalogueViewModel,
                            cartViewModel = cartViewModel
                    )
                }
                composable("Cart") {
                    CartScreen(navController = navController, viewModel = cartViewModel)
                }
                composable("Profile") {
                    ProfileScreen(
                            navController = navController,
                            rootNavController = rootNavController
                    )
                }
                composable("MyOrders") { MyOrdersScreen(navController = navController) }
                composable("Projects") {
                    ProjectsScreen(navController = navController, viewModel = projectsViewModel)
                }
                composable("CreateProject") {
                    CreateProjectScreen(
                            navController = navController,
                            viewModel = projectsViewModel
                    )
                }
                composable(
                        route = "ProjectDetails/{projectId}",
                        arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val projectId =
                            backStackEntry.arguments?.getString("projectId") ?: return@composable
                    com.example.lol.Projects.ProjectDetailsScreen(
                            navController = navController,
                            viewModel = projectsViewModel,
                            projectId = projectId
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items =
            listOf(
                    BottomNavItem("Main", "Главная", R.drawable.analizy),
                    BottomNavItem("Catalogue", "Каталог", R.drawable.rezultaty),
                    BottomNavItem("Projects", "Проекты", R.drawable.podderzhka),
                    BottomNavItem("Profile", "Профиль", R.drawable.polzovatel)
            )

    NavigationBar(
            modifier =
                    Modifier.height(88.dp)
                            .shadow(
                                    elevation = 0.5.dp, // box-shadow: 0px -0.5px 0px rgba(160, 160,
                                    // 160, 0.3)
                                    spotColor = Color(0x4DA0A0A0)
                            ),
            containerColor = Color.White,
            contentColor = AccentBlue,
            tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                    icon = {
                        Icon(
                                painterResource(id = item.iconRes),
                                contentDescription = item.title,
                                modifier = Modifier.size(32.dp)
                        )
                    }, // From 1.css item height
                    label = {
                        Text(
                                item.title,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal
                        )
                    },
                    selected = currentRoute == item.route,
                    colors =
                            NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentBlue,
                                    selectedTextColor = AccentBlue,
                                    indicatorColor = Color.Transparent,
                                    unselectedIconColor =
                                            Color(0xFFB8C1CC), // Icons color from 1.css
                                    unselectedTextColor = Color(0xFFB8C1CC)
                            ),
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            )
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val iconRes: Int)
