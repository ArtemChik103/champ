package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lol.R
import com.example.lol.data.Product
import com.example.lol.ui.theme.AccentBlue

@Composable
fun ProductDetailsContent(product: Product, onAddToCart: () -> Unit, onClose: () -> Unit) {
    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Header
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
        ) {
            Text(
                    text = product.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,
                    lineHeight = 28.sp,
                    modifier = Modifier.weight(1f)
            )

            IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(Color(0xFFF5F5F9), CircleShape).size(24.dp)
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "Close",
                        tint = Color(0xFF7E7E9A),
                        modifier = Modifier.size(12.dp)
                )
            }
        }

        if (product.imageUrl.isNotBlank()) {
            AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    modifier =
                            Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description Section
        Text(
                text = "Описание",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF939396)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = product.description, fontSize = 15.sp, color = Color.Black, lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Consumption Section (matching screen4.png "Примерный расход")
        Text(text = "Примерный расход:", fontSize = 14.sp, color = Color(0xFF939396))
        Text(
                text = "80-90 г", // Static for now as per Figma, or could be linked to data
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                    text = "Добавить за ${product.price} ₽",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
            )
        }
    }
}
