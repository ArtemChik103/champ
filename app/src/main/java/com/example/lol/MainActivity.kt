package com.example.lol

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.lol.Cart.CartViewModelFactory
import com.example.lol.Catalogue.CatalogueScreen
import com.example.lol.Catalogue.CatalogueViewModel
import com.example.lol.Catalogue.CatalogueViewModelFactory
import com.example.lol.Main.MainScreen
import com.example.lol.Profile.MyOrdersScreen
import com.example.lol.Profile.OrdersViewModelFactory
import com.example.lol.Profile.ProfileScreen
import com.example.lol.Projects.CreateProjectScreen
import com.example.lol.Projects.ProjectsScreen
import com.example.lol.Projects.ProjectsViewModel
import com.example.lol.Projects.ProjectsViewModelFactory
import com.example.lol.authorization.CreatePasswordScreen
import com.example.lol.authorization.CreatePinScreen
import com.example.lol.authorization.PinCodeScreen
import com.example.lol.authorization.SignInScreen
import com.example.lol.authorization.SignUpScreen
import com.example.lol.authorization.SplashScreen
import com.example.lol.data.network.RetrofitInstance
import com.example.lol.data.network.TokenManager
import com.example.lol.data.repository.CartRepository
import com.example.lol.data.repository.OrderRepository
import com.example.lol.data.repository.ProductRepositoryApi
import com.example.lol.data.repository.ProjectRepository
import com.example.lol.domain.usecase.cart.AddToCartUseCase
import com.example.lol.domain.usecase.cart.UpdateCartItemUseCase
import com.example.lol.domain.usecase.product.GetProductsUseCase
import com.example.lol.domain.usecase.product.SearchProductsUseCase
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.LolTheme

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                // Инициализация TokenManager и настройка RetrofitInstance
                val tokenManager = TokenManager(this)
                RetrofitInstance.setTokenProvider { tokenManager.getToken() }

                enableEdgeToEdge()
                setContent {
                        LolTheme {
                                Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                ) {
                                        val navController = rememberNavController()

                                        NavHost(
                                                navController = navController,
                                                startDestination = "Splash"
                                        ) {
                                                // Splash - fade out only
                                                composable(
                                                        route = "Splash",
                                                        exitTransition = {
                                                                fadeOut(animationSpec = tween(400))
                                                        }
                                                ) { SplashScreen(navController = navController) }
                                                // SignIn - fade in, slide for navigation
                                                composable(
                                                        route = "SignIn",
                                                        enterTransition = {
                                                                fadeIn(animationSpec = tween(300))
                                                        },
                                                        exitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popEnterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popExitTransition = {
                                                                fadeOut(animationSpec = tween(300))
                                                        }
                                                ) { SignInScreen(navController = navController) }
                                                // PinCode - fade in (from splash)
                                                composable(
                                                        route = "PinCode",
                                                        enterTransition = {
                                                                fadeIn(animationSpec = tween(300))
                                                        },
                                                        exitTransition = {
                                                                fadeOut(
                                                                        animationSpec = tween(300)
                                                                ) + scaleOut(targetScale = 0.9f)
                                                        },
                                                        popEnterTransition = {
                                                                fadeIn(animationSpec = tween(300))
                                                        },
                                                        popExitTransition = {
                                                                fadeOut(animationSpec = tween(300))
                                                        }
                                                ) { PinCodeScreen(navController = navController) }
                                                // SignUp - slide horizontal
                                                composable(
                                                        route = "SignUp",
                                                        enterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        exitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popEnterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popExitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        }
                                                ) { SignUpScreen(navController = navController) }
                                                // CreatePassword - slide horizontal
                                                composable(
                                                        route = "CreatePassword",
                                                        enterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        exitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popEnterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popExitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        }
                                                ) {
                                                        CreatePasswordScreen(
                                                                navController = navController
                                                        )
                                                }
                                                // CreatePin - slide horizontal, fade+scale out to
                                                // Home
                                                composable(
                                                        route = "CreatePin",
                                                        enterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        exitTransition = {
                                                                fadeOut(
                                                                        animationSpec = tween(300)
                                                                ) + scaleOut(targetScale = 0.9f)
                                                        },
                                                        popEnterTransition = {
                                                                slideInHorizontally(
                                                                        initialOffsetX = { -it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        },
                                                        popExitTransition = {
                                                                slideOutHorizontally(
                                                                        targetOffsetX = { it },
                                                                        animationSpec =
                                                                                tween(
                                                                                        300,
                                                                                        easing =
                                                                                                FastOutSlowInEasing
                                                                                )
                                                                )
                                                        }
                                                ) { CreatePinScreen(navController = navController) }
                                                // Home - fade+scale in
                                                composable(
                                                        route = "Home",
                                                        enterTransition = {
                                                                fadeIn(animationSpec = tween(400)) +
                                                                        scaleIn(
                                                                                initialScale = 0.95f
                                                                        )
                                                        }
                                                ) {
                                                        MainContainer(
                                                                application = application,
                                                                rootNavController = navController,
                                                                tokenManager = tokenManager
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }

        override fun onResume() {
                super.onResume()
                // Cancel notification when app is open
                androidx.work.WorkManager.getInstance(this).cancelUniqueWork("inactivity_work")
        }

        override fun onStop() {
                super.onStop()
                // Schedule notification on app close/background only if enabled
                val sessionManager = com.example.lol.authorization.SessionManager(this)
                if (sessionManager.isNotificationsEnabled()) {
                        val workRequest =
                                androidx.work.OneTimeWorkRequest.Builder(
                                                com.example.lol.notifications
                                                                .InactivityWorker::class
                                                        .java
                                        )
                                        .setInitialDelay(1, java.util.concurrent.TimeUnit.MINUTES)
                                        .build()

                        androidx.work.WorkManager.getInstance(this)
                                .enqueueUniqueWork(
                                        "inactivity_work",
                                        androidx.work.ExistingWorkPolicy.REPLACE,
                                        workRequest
                                )
                }
        }
}

@Composable
fun MainContainer(
        application: Application,
        rootNavController: NavController,
        tokenManager: TokenManager
) {
        val navController = rememberNavController()
        val context = LocalContext.current
        val sessionManager = remember { com.example.lol.authorization.SessionManager(context) }

        // Создание API репозиториев
        val api = RetrofitInstance.api
        val productRepositoryApi = remember { ProductRepositoryApi(api) }
        val cartRepository = remember { CartRepository(api) }
        val projectRepository = remember { ProjectRepository(api) }
        val orderRepository = remember { OrderRepository(api) }

        // Создание UseCases
        val getProductsUseCase = remember { GetProductsUseCase(productRepositoryApi) }
        val searchProductsUseCase = remember { SearchProductsUseCase(productRepositoryApi) }
        val addToCartUseCase = remember { AddToCartUseCase(cartRepository) }
        val updateCartItemUseCase = remember { UpdateCartItemUseCase(cartRepository) }

        // Создание ViewModels с UseCases (Domain Layer integration)
        val catalogueViewModel: CatalogueViewModel =
                viewModel(
                        factory =
                                CatalogueViewModelFactory(
                                        application,
                                        getProductsUseCase,
                                        searchProductsUseCase
                                )
                )
        val cartViewModel: CartViewModel =
                viewModel(
                        factory =
                                CartViewModelFactory(
                                        addToCartUseCase,
                                        updateCartItemUseCase,
                                        tokenManager
                                )
                )
        val projectsViewModel: ProjectsViewModel =
                viewModel(
                        factory =
                                ProjectsViewModelFactory(
                                        application,
                                        projectRepository,
                                        tokenManager
                                )
                )
        val ordersViewModel: com.example.lol.Profile.OrdersViewModel =
                viewModel(
                        factory = OrdersViewModelFactory(application, orderRepository, tokenManager)
                )

        LaunchedEffect(navController) {
                navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        backStackEntry.destination.route?.let { route ->
                                sessionManager.saveLastRoute(route)
                        }
                }
        }
        var hasNavigatedToLastRoute by rememberSaveable { mutableStateOf(false) }
        val lastRoute = remember { sessionManager.getLastRoute() }

        LaunchedEffect(Unit) {
                if (!hasNavigatedToLastRoute && lastRoute != "Main") {
                        hasNavigatedToLastRoute = true
                        try {
                                navController.navigate(lastRoute) {
                                        popUpTo("Main") { inclusive = false }
                                        launchSingleTop = true
                                }
                        } catch (e: Exception) {
                                // Если маршрут недействителен, остаёмся на Main
                        }
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
                        NavHost(navController = navController, startDestination = "Main") {
                                // Main - fade+scale for tab switching
                                composable(
                                        route = "Main",
                                        enterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        exitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        },
                                        popEnterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        popExitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        }
                                ) {
                                        MainScreen(
                                                navController = navController,
                                                viewModel = catalogueViewModel,
                                                cartViewModel = cartViewModel
                                        )
                                }
                                // Catalogue - fade+scale for tab switching
                                composable(
                                        route = "Catalogue",
                                        enterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        exitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        },
                                        popEnterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        popExitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        }
                                ) {
                                        CatalogueScreen(
                                                navController = navController,
                                                viewModel = catalogueViewModel,
                                                cartViewModel = cartViewModel
                                        )
                                }
                                // Cart - slide up/down (modal style)
                                composable(
                                        route = "Cart",
                                        enterTransition = {
                                                slideInVertically(
                                                        initialOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeIn(animationSpec = tween(300))
                                        },
                                        exitTransition = {
                                                slideOutVertically(
                                                        targetOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeOut(animationSpec = tween(300))
                                        },
                                        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
                                        popExitTransition = {
                                                slideOutVertically(
                                                        targetOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeOut(animationSpec = tween(300))
                                        }
                                ) {
                                        CartScreen(
                                                navController = navController,
                                                viewModel = cartViewModel,
                                                ordersViewModel = ordersViewModel
                                        )
                                }
                                // Profile - fade+scale for tab switching
                                composable(
                                        route = "Profile",
                                        enterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        exitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        },
                                        popEnterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        popExitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        }
                                ) {
                                        ProfileScreen(
                                                navController = navController,
                                                rootNavController = rootNavController
                                        )
                                }
                                // MyOrders - slide horizontal (hierarchy navigation)
                                composable(
                                        route = "MyOrders",
                                        enterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        exitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popEnterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popExitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        }
                                ) {
                                        MyOrdersScreen(
                                                navController = navController,
                                                viewModel = ordersViewModel
                                        )
                                }
                                // OrderDetails - slide horizontal (hierarchy navigation)
                                composable(
                                        route = "OrderDetails/{orderId}",
                                        arguments =
                                                listOf(
                                                        navArgument("orderId") {
                                                                type = NavType.StringType
                                                        }
                                                ),
                                        enterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        exitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popEnterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popExitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        }
                                ) { backStackEntry ->
                                        val orderId =
                                                backStackEntry.arguments?.getString("orderId")
                                                        ?: return@composable
                                        com.example.lol.Profile.OrderDetailsScreen(
                                                navController = navController,
                                                viewModel = ordersViewModel,
                                                orderId = orderId
                                        )
                                }
                                // Projects - fade+scale for tab switching
                                composable(
                                        route = "Projects",
                                        enterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        exitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        },
                                        popEnterTransition = {
                                                fadeIn(animationSpec = tween(200)) +
                                                        scaleIn(initialScale = 0.96f)
                                        },
                                        popExitTransition = {
                                                fadeOut(animationSpec = tween(200)) +
                                                        scaleOut(targetScale = 0.96f)
                                        }
                                ) {
                                        ProjectsScreen(
                                                navController = navController,
                                                viewModel = projectsViewModel
                                        )
                                }
                                // CreateProject - slide up/down (modal style)
                                composable(
                                        route = "CreateProject",
                                        enterTransition = {
                                                slideInVertically(
                                                        initialOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeIn(animationSpec = tween(300))
                                        },
                                        exitTransition = {
                                                slideOutVertically(
                                                        targetOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeOut(animationSpec = tween(300))
                                        },
                                        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
                                        popExitTransition = {
                                                slideOutVertically(
                                                        targetOffsetY = { fullHeight ->
                                                                fullHeight
                                                        },
                                                        animationSpec = tween(300)
                                                ) + fadeOut(animationSpec = tween(300))
                                        }
                                ) {
                                        CreateProjectScreen(
                                                navController = navController,
                                                viewModel = projectsViewModel
                                        )
                                }
                                // ProjectDetails - slide horizontal (hierarchy navigation)
                                composable(
                                        route = "ProjectDetails/{projectId}",
                                        arguments =
                                                listOf(
                                                        navArgument("projectId") {
                                                                type = NavType.StringType
                                                        }
                                                ),
                                        enterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        exitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popEnterTransition = {
                                                slideInHorizontally(
                                                        initialOffsetX = { -it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        },
                                        popExitTransition = {
                                                slideOutHorizontally(
                                                        targetOffsetX = { it },
                                                        animationSpec =
                                                                tween(
                                                                        300,
                                                                        easing = FastOutSlowInEasing
                                                                )
                                                )
                                        }
                                ) { backStackEntry ->
                                        val projectId =
                                                backStackEntry.arguments?.getString("projectId")
                                                        ?: return@composable
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
                                        elevation =
                                                0.5.dp, // box-shadow: 0px -0.5px 0px rgba(160, 160,
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
                                                popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                }
                                                launchSingleTop = true
                                        }
                                }
                        )
                }
        }
}

data class BottomNavItem(val route: String, val title: String, val iconRes: Int)
