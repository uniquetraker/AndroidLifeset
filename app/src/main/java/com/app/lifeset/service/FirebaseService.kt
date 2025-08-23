package com.app.lifeset.service

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.app.lifeset.R
import com.app.lifeset.activity.HomeActivity
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("refreshToken", "=$token")
        PrefManager(this).setvalue(StaticData.fcmToken, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val json = JSONObject()
        val isForeground = !isAppForeground()
        handleNow(json, message.notification, isForeground)

    }

    private fun handleNow(
        json: JSONObject,
        notification: RemoteMessage.Notification?,
        isForeground: Boolean,
    ) {
        try {
            var title = notification?.title ?: ""
            var message = notification?.body ?: ""


            if (json.has("title")) {
                title = json.getString("title")
            }
            if (json.has("body")) {
                val body = json.getString("body")
                try {
                    val bodyJson = JSONObject(body)
                    if (bodyJson.has("message")) {
                        message = bodyJson.getString("message")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val intent =
                Intent(this, HomeActivity::class.java)

            sendNotification(title, message, intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun sendNotification(
        title: String, message: String, intent: Intent
    ) {
        try {
            val notificationId = System.currentTimeMillis().toInt()
            val randomInt = System.currentTimeMillis().toInt()
            val pendingIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(
                        this, randomInt, intent,
                        PendingIntent.FLAG_MUTABLE,
                    )
                } else {
                    PendingIntent.getActivity(
                        this, randomInt, intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
                    )
                }
            val channelId = "0"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder =
                NotificationCompat.Builder(this, channelId)
                    .apply {
                        color = ResourcesCompat.getColor(resources, R.color.black, null)
                        setSmallIcon(R.drawable.ic_notificationlogo)
                    }
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                    .apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            priority = NotificationManagerCompat.IMPORTANCE_HIGH
                        }
                    }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun isAppForeground(): Boolean {
        val activityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in appProcess.pkgList) {
                    if (activeProcess == packageName) {
                        return false
                    }
                }
            }
        }
        return true
    }
}