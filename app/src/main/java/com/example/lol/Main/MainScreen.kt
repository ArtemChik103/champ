package com.example.lol.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lol.Cart.CartViewModel
import com.example.lol.Catalogue.CatalogueViewModel
import com.example.lol.R
import com.example.lol.components.AppTextField
import com.example.lol.components.ProductCard
import com.example.lol.components.ProductDetailsContent
import com.example.lol.data.Product
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.Roboto
import com.example.lol.ui.theme.TextMedium
import com.example.lol.ui.theme.Title2ExtraBold
import com.example.lol.ui.theme.Title3Semibold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
        navController: NavController,
        viewModel: CatalogueViewModel,
        cartViewModel: CartViewModel
) {
    val products by viewModel.products.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var searchQuery by remember { mutableStateOf("") }

    // Баннеры берём из полного списка, чтобы они не зависели от фильтров.
    val bannerProducts = remember(allProducts) { allProducts.take(2) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Поиск в шапке.
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            AppTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.filterProducts(it)
                    },
                    placeholder = "Поиск",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                                painter = painterResource(id = R.drawable.icon_search),
                                contentDescription = "Search",
                                tint = Color(0xFF939396)
                        )
                    }
            )
        }

        LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Блок баннеров.
            item {
                Text(
                        text = "Акции и новости",
                        style = Title3Semibold,
                        color = Color(0xFF939396),
                        modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bannerProducts.size) { index ->
                        BannerCard(
                                index = index,
                                product = bannerProducts.getOrNull(index),
                                onClick = {
                                    bannerProducts.getOrNull(index)?.let { product ->
                                        selectedProduct = product
                                        showSheet = true
                                    }
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Блок категорий.
            item {
                Text(
                        text = "Каталог описаний",
                        style = Title3Semibold,
                        color = Color(0xFF939396),
                        modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val currentCategory by viewModel.selectedCategory.collectAsState()

                LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val categories =
                            listOf(
                                    "Все",
                                    "Популярные",
                                    "Новинки",
                                    "Мужское",
                                    "Женское",
                                    "Аксессуары"
                            )
                    items(categories) { category ->
                        CategoryChip(
                                text = category,
                                isSelected = category == currentCategory,
                                onClick = { viewModel.setCategory(category) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Блок товаров.
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
                        isInCart = isInCart,
                        modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
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

@Composable
fun BannerCard(index: Int, product: Product?, onClick: () -> Unit) {
    val imageRes =
            if (index == 0) R.drawable.zemri9vo71oremovebgpreview1
            else R.drawable.ditylc26zviremovebgpreview1
    val gradient =
            if (index == 0) {
                Brush.linearGradient(colors = listOf(Color(0xFF97D9F0), Color(0xFF92E9D4)))
            } else {
                Brush.linearGradient(colors = listOf(Color(0xFF76B3FF), Color(0xFFCDE3FF)))
            }

    Box(
            modifier =
                    Modifier.width(270.dp)
                            .height(152.dp)
                            .background(gradient, shape = RoundedCornerShape(12.dp))
                            .clickable(onClick = onClick)
    ) {
        // Слой изображения.
        androidx.compose.foundation.Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                modifier =
                        Modifier.align(Alignment.CenterEnd)
                                .size(150.dp)
                                .padding(end = 8.dp)
        )

        // Текстовый слой.
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                    text =
                            (product?.title
                                            ?: if (index == 0) "Шорты\nВторник"
                                            else "Рубашка\nВоскресенье")
                                    .replace(" ", "\n"),
                    style = Title2ExtraBold.copy(fontFamily = Roboto, lineHeight = 24.sp),
                    color = Color.White,
                    modifier = Modifier.width(170.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                    text = "${product?.price ?: if (index == 0) 4000 else 8000} ₽",
                    style = Title2ExtraBold.copy(fontFamily = Roboto),
                    color = Color.White
            )
        }
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
            modifier =
                    Modifier.height(48.dp)
                            .background(
                                    color =
                                            if (isSelected) AccentBlue
                                            else Color(0xFFF5F5F9),
                                    shape = RoundedCornerShape(10.dp)
                            )
                            .clickable(onClick = onClick)
                            .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = text,
                color =
                        if (isSelected) Color.White
                        else Color(0xFF7E7E9A),
                style = TextMedium
        )
    }
}
