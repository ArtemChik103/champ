package com.example.lol.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lol.authorization.SessionManager
import java.util.concurrent.TimeUnit

// Управляет постановкой и отменой фоновых задач.
object InactivityNotificationScheduler {

    /**
     * Планирует запуск фоновой задачи по заданной политике выполнения.
     *
     * @param context Контекст Android для доступа к системным сервисам и ресурсам.
     * @param sessionManager Менеджер сессии с пользовательскими настройками и флагами уведомлений.
     */
    fun schedule(context: Context, sessionManager: SessionManager) {
        if (!sessionManager.isNotificationsEnabled()) return

        val repeatingRequest =
            OneTimeWorkRequestBuilder<RepeatingInactivityWorker>()
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                InactivityNotificationConfig.REPEATING_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                repeatingRequest
            )

        if (!sessionManager.isOneShotInactivityNotificationSent()) {
            val oneShotRequest =
                OneTimeWorkRequestBuilder<OneShotInactivityWorker>()
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    InactivityNotificationConfig.ONE_SHOT_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    oneShotRequest
                )
        }
    }

    /**
     * Отменяет ранее запланированные фоновые задачи.
     *
     * @param context Контекст Android для доступа к системным сервисам и ресурсам.
     */
    fun cancel(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(InactivityNotificationConfig.REPEATING_WORK_NAME)
        workManager.cancelUniqueWork(InactivityNotificationConfig.ONE_SHOT_WORK_NAME)
    }
}