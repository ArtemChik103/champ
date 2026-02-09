package com.example.lol

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache

// Инициализирует глобальные зависимости приложения при старте процесса.
class MatuleApplication : Application(), Configuration.Provider, ImageLoaderFactory {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(android.util.Log.INFO).build()

    // Создает и настраивает экземпляр загрузчика изображений для приложения.
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
                .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
                .diskCache {
                    DiskCache.Builder()
                            .directory(cacheDir.resolve("image_cache"))
                            .maxSizePercent(0.02)
                            .build()
                }
                .crossfade(true)
                .build()
    }

    // Инициализирует экран Activity и задает корневой Compose-контент.
    override fun onCreate() {
        super.onCreate()
    }
}
