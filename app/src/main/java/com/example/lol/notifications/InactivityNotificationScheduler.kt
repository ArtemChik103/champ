package com.example.lol.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lol.authorization.SessionManager
import java.util.concurrent.TimeUnit

object InactivityNotificationScheduler {

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

    fun cancel(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(InactivityNotificationConfig.REPEATING_WORK_NAME)
        workManager.cancelUniqueWork(InactivityNotificationConfig.ONE_SHOT_WORK_NAME)
    }
}