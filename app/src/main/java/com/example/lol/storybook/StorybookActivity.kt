package com.example.lol.storybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lol.ui.theme.LolTheme

class StorybookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LolTheme { StorybookScreen() } }
    }
}
