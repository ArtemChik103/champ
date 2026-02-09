package com.example.lol.storybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lol.ui.theme.LolTheme

// Управляет жизненным циклом экрана и инициализацией пользовательского интерфейса.
class StorybookActivity : ComponentActivity() {
    /**
     * Инициализирует экран Activity и задает корневой Compose-контент.
     *
     * @param savedInstanceState Сохраненное состояние Activity для восстановления после пересоздания.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LolTheme { StorybookScreen() } }
    }
}
