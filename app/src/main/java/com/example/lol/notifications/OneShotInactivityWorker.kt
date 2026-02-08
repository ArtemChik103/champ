package com.example.lol.notifications

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lol.authorization.SessionManager

class OneShotInactivityWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): ListenableWorker.Result {
        val sessionManager = SessionManager(applicationContext)
        if (sessionManager.isOneShotInactivityNotificationSent()) {
            return ListenableWorker.Result.success()
        }

        InactivityNotificationHelper.showNotification(
            context = applicationContext,
            notificationId = InactivityNotificationConfig.ONE_SHOT_NOTIFICATION_ID,
            title = "Вернитесь в приложение",
            message = "Мы сохранили ваш прогресс. Продолжите с места, где остановились."
        )
        sessionManager.markOneShotInactivityNotificationSent()

        return ListenableWorker.Result.success()
    }
}