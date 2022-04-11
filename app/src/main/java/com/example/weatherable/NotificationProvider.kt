package com.example.weatherable

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color.BLUE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.*
import javax.inject.Inject


interface NotificationProvider {

    fun notification()

    @RequiresApi(Build.VERSION_CODES.O)
    class Base @Inject constructor(
        private val context: Application,
        private val rP: ResourceProvider
    ) : NotificationProvider {

        override fun notification() {
            createChannel
            manager.notify(123, Builder(context, rP.channel)
                .setContentTitle("      Work started")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_DEFAULT)
                .setCategory(CATEGORY_SERVICE)
                .build())

        }

        private val createChannel by lazy {
            NotificationChannel(rP.channel, rP.name, IMPORTANCE_DEFAULT)
                .apply {
                    lightColor = BLUE
                    lockscreenVisibility = VISIBILITY_PUBLIC
                    manager.createNotificationChannel(this)
                }
        }

        private val manager by lazy {
            (context.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?)!!
        }
    }
}