package com.mobilefirst.countdown.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class AppConstant {

    companion object{

        const val notificationId = 1001
        //const val totalDuration = 60*60*1000L // Adjust this value based on your total countdown duration
        const val totalDuration = 1*60*1000L // Adjust this value based on your total countdown duration
        var isNotify =true

        fun cancelAllNotification(context: Context)
        {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Cancel all notifications
            notificationManager.cancel(notificationId)

        }

        fun showNotification(context: Context, title: String, content: String) {

            AppLog.e("TimerNotificationReceiver showNotification  start")

            cancelAllNotification(context)

            try {

                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)


                val channelId = "default_channel_id"


                // Create a NotificationManager
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Create a NotificationChannel (required for API 26 and above)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Default Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                // Build the notification
                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Show the notification
                notificationManager.notify(notificationId, builder.build())

                AppLog.e("TimerNotificationReceiver showNotification  end")
            }
            catch (ex:Exception)
            {
                AppLog.e("TimerNotificationReceiver showNotification   Exception "+ex)
            }

        }
    }
}