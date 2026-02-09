package com.example.lol.Catalogue

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.Cart.CartViewModel
import com.example.lol.R
import com.example.lol.components.AppChip
import com.example.lol.components.AppTextField
import com.example.lol.components.ProductCard
import com.example.lol.components.ProductDetailsContent
import com.example.lol.data.Product
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.HeadlineMedium
import com.example.lol.ui.theme.TextMedium

/**
 * Отрисовывает экран и связывает пользовательские действия с состоянием UI.
 *
 * @param navController Контроллер навигации для переходов между экранами и возврата по стеку.
 * @param viewModel ViewModel экрана с состоянием, событиями и бизнес-логикой.
 * @param cartViewModel ViewModel корзины для синхронизации количества и состава товаров.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueScreen(
        navController: NavController,
        viewModel: CatalogueViewModel,
        cartViewModel: CartViewModel
) {
    val products by viewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Search & Header
        // Поиск и заголовок
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            // Header with Search and Cart icon
            // Заголовок с поиском и иконкой корзины
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.filterProducts(it)
                        },
                        placeholder = "Искать описания",
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                    painter = painterResource(id = R.drawable.icon_search),
                                    contentDescription = "Search",
                                    tint = Color(0xFF939396)
                            )
                        }
                )

                // Profile icon
                // Иконка профиля
                Image(
                        painter = painterResource(id = R.drawable.user_icon_catalogue),
                        contentDescription = "Профиль",
                        contentScale = ContentScale.Crop,
                        modifier =
                                Modifier.size(32.dp).clip(CircleShape).clickable {
                                    navController.navigate("Profile") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            val currentCategory by viewModel.selectedCategory.collectAsState()

            // Categories Chips
            // Чипы категорий
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val categories =
                        listOf("Все", "Популярные", "Новинки", "Мужское", "Женское", "Аксессуары")
                items(categories) { category ->
                    AppChip(
                            text = category,
                            isSelected = category == currentCategory,
                            onClick = { viewModel.setCategory(category) }
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            val cartItems by cartViewModel.cartItems.collectAsState()

            LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(products) { product ->
                    val isInCart = cartItems.any { it.product.id == product.id }
                    ProductCard(
                            product = product,
                            onClick = {
                                selectedProduct = product
                                showSheet = true
                            },
                            onAddToCart = { cartViewModel.addToCart(product) },
                            onRemoveFromCart = { cartViewModel.removeFromCart(product) },
                            isInCart = isInCart
                    )
                }

                if (cartItems.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            if (cartItems.isNotEmpty()) {
                val totalPrice = cartViewModel.calculateTotal()
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(20.dp)
                                        .shadow(10.dp, RoundedCornerShape(12.dp))
                                        .background(AccentBlue, RoundedCornerShape(12.dp))
                                        .clickable { navController.navigate("Cart") }
                                        .padding(vertical = 16.dp, horizontal = 20.dp)
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                    painter = painterResource(id = R.drawable.icon_shopping_cart),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "В корзину", color = Color.White, style = HeadlineMedium)
                        }
                        Text(text = "$totalPrice ₽", color = Color.White, style = HeadlineMedium)
                    }
                }
            }
        }

        if (showSheet && selectedProduct != null) {
            ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    containerColor = Color.White,
                    dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFC4C4C4)) }
            ) {
                ProductDetailsContent(
                        product = selectedProduct!!,
                        onAddToCart = {
                            cartViewModel.addToCart(selectedProduct!!)
                            showSheet = false
                        },
                        onClose = { showSheet = false }
                )
            }
        }
    }
}

