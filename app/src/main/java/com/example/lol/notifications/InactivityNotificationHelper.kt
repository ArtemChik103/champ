package com.example.lol.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.lol.MainActivity
import com.example.lol.R

// Содержит вспомогательные операции для работы с уведомлениями и системными API.
object InactivityNotificationHelper {

    /**
     * Создает и отображает системное уведомление с заданным заголовком и текстом.
     *
     * @param context Контекст Android для доступа к системным сервисам и ресурсам.
     * @param notificationId Идентификатор уведомления для показа или обновления.
     * @param title Заголовок, который отображается в интерфейсе.
     * @param message Текст сообщения, отображаемый пользователю.
     */
    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        ensureChannel(notificationManager)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(context, InactivityNotificationConfig.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(notificationId, notification)
    }

    /**
     * Создает канал уведомлений на Android O+ перед показом уведомления.
     *
     * @param notificationManager Системный менеджер уведомлений для каналов и показа.
     */
    private fun ensureChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel =
            NotificationChannel(
                InactivityNotificationConfig.CHANNEL_ID,
                InactivityNotificationConfig.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        notificationManager.createNotificationChannel(channel)
    }
}