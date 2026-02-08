package com.example.lol.Profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.lol.R // Assuming R is available, or use Icons
import com.example.lol.authorization.SessionManager
import com.example.lol.ui.theme.HeadlineRegular
import com.example.lol.ui.theme.TextMedium
import com.example.lol.ui.theme.Title1Bold
import com.example.lol.ui.theme.Title3Semibold

@Composable
fun ProfileScreen(navController: NavController, rootNavController: NavController) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val sessionManager = remember { SessionManager(context) }
        val showNotifications = remember { mutableStateOf(sessionManager.isNotificationsEnabled()) }

        val notificationsPermissionLauncher =
                rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                                showNotifications.value = isGranted
                                sessionManager.setNotificationsEnabled(isGranted)
                        }
                )

        Column(
                modifier =
                        Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp)
        ) {
                // User Info Section
                Column(
                        modifier = Modifier.padding(top = 76.dp) // From 1.css top: 76px
                ) {
                        Text(
                                text = sessionManager.getUserName() ?: "Эдуард",
                                style = Title1Bold,
                                color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = sessionManager.getEmail() ?: "afersfsr@dsfsr.ru",
                                style = HeadlineRegular,
                                color = Color(0xFF939396)
                        )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Menu Items
                ProfileMenuItem(iconRes = R.drawable.order, title = "Мои заказы") {
                        navController.navigate("MyOrders")
                }

                ProfileMenuItem(
                        iconRes = R.drawable.settings, // Gear icon as per screen1.png
                        title = "Уведомления",
                        isSwitch = true,
                        checked = showNotifications.value,
                        onCheckedChange = { isChecked ->
                                if (isChecked) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                val permissionCheckResult =
                                                        ContextCompat.checkSelfPermission(
                                                                context,
                                                                Manifest.permission
                                                                        .POST_NOTIFICATIONS
                                                        )
                                                if (permissionCheckResult ==
                                                                PackageManager.PERMISSION_GRANTED
                                                ) {
                                                        showNotifications.value = true
                                                        sessionManager.setNotificationsEnabled(true)
                                                } else {
                                                        notificationsPermissionLauncher.launch(
                                                                Manifest.permission
                                                                        .POST_NOTIFICATIONS
                                                        )
                                                }
                                        } else {
                                                showNotifications.value = true
                                                sessionManager.setNotificationsEnabled(true)
                                        }
                                } else {
                                        showNotifications.value = false
                                        sessionManager.setNotificationsEnabled(false)
                                        WorkManager.getInstance(context)
                                                .cancelUniqueWork("inactivity_work")
                                }
                        }
                ) {}

                Spacer(modifier = Modifier.height(176.dp))

                // Footer (Policies & Logout)
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        Text(
                                text = "Политика конфиденциальности",
                                color = Color(0xFF939396),
                                style = TextMedium,
                                modifier =
                                        Modifier.clickable {
                                                val browserIntent =
                                                        Intent(
                                                                Intent.ACTION_VIEW,
                                                                Uri.parse(
                                                                        "https://example.com/privacy_policy.pdf"
                                                                )
                                                        )
                                                context.startActivity(browserIntent)
                                        }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                                text = "Пользовательское соглашение",
                                color = Color(0xFF939396),
                                style = TextMedium,
                                modifier =
                                        Modifier.clickable {
                                                val browserIntent =
                                                        Intent(
                                                                Intent.ACTION_VIEW,
                                                                Uri.parse(
                                                                        "https://example.com/user_agreement.pdf"
                                                                )
                                                        )
                                                context.startActivity(browserIntent)
                                        }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                                text = "Выйти",
                                color = Color(0xFFFD3535),
                                style = TextMedium,
                                modifier =
                                        Modifier.clickable {
                                                sessionManager.clearSession()
                                                rootNavController.navigate("SignIn") {
                                                        popUpTo(0) { inclusive = true }
                                                }
                                        }
                        )
                }
        }
}

@Composable
fun ProfileMenuItem(
        iconRes: Int,
        title: String,
        isSwitch: Boolean = false,
        checked: Boolean = false,
        onCheckedChange: (Boolean) -> Unit = {},
        onClick: () -> Unit
) {
        Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Box(
                        modifier =
                                Modifier.size(32.dp)
                                        .background(Color(0xFFF5F5F9), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                ) {
                        Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                        )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                        text = title,
                        style = Title3Semibold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                )
                if (isSwitch) {
                        Switch(
                                checked = checked,
                                onCheckedChange = onCheckedChange,
                                colors =
                                        SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = Color(0xFF1A6FEE),
                                                uncheckedThumbColor = Color.White,
                                                uncheckedTrackColor = Color(0xFFD1D1D1),
                                                uncheckedBorderColor = Color.Transparent
                                        )
                        )
                } else {
                        // No chevron in screen1.png for "Мои заказы", but usually it's there.
                        // Design shows no chevron for the first item either.
                        // I'll leave it as is for now or remove if strictly following PNG.
                        // PNG shows NO chevron for "Мои заказы".
                }
        }
}
