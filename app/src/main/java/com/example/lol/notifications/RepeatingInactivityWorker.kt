package com.example.lol.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class RepeatingInactivityWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): ListenableWorker.Result {
        InactivityNotificationHelper.showNotification(
            context = applicationContext,
            notificationId = InactivityNotificationConfig.REPEATING_NOTIFICATION_ID,
            title = "Вы давно не заходили",
            message = "Откройте приложение, чтобы продолжить работу над проектами."
        )

        scheduleNextTick()
        return ListenableWorker.Result.success()
    }

    private fun scheduleNextTick() {
        val nextRequest =
            OneTimeWorkRequestBuilder<RepeatingInactivityWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                InactivityNotificationConfig.REPEATING_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                nextRequest
            )
    }
}