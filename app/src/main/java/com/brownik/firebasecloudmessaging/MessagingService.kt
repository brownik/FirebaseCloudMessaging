package com.brownik.firebasecloudmessaging

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    private val channelId = "501"
    private val channelName = "MessagingService"
    private val notificationId = 502
    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private var rootInstance = FirebaseDatabase.getInstance()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("qwe123", "onNewToken: $token")

        sendToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val from = remoteMessage.from
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val requestId = remoteMessage.data["requestId"]?.let { Integer.parseInt(it) }
        Log.d("qwe123", "from: $from, title: $title, body: $body, id: $requestId")

        val notificationBuilder = showNotification(title, body)
        NotificationManagerCompat.from(this).notify(notificationId, notificationBuilder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun showNotification(title: String?, body: String?): NotificationCompat.Builder {
        val customLayout = RemoteViews(packageName, R.layout.custom_push)

        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationManager.getNotificationChannel(channelId) == null) {
                    val channel = NotificationChannel(channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT)
                    notificationManager.createNotificationChannel(channel)
                }
                NotificationCompat.Builder(applicationContext, channelId)
            } else {
                NotificationCompat.Builder(applicationContext)
            }

        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.basic_img)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setChannelId(channelId)
            .setCustomContentView(customLayout)

        Log.d("qwe123", "title: $title, body: $body")
        return builder
    }

    private fun sendToken(token: String?){
        val path = rootInstance.reference.child("id")
        path.setValue(token)
    }
}