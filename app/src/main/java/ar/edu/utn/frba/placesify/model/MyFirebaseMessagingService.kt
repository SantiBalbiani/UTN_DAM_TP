package ar.edu.utn.frba.placesify.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import ar.edu.utn.frba.placesify.MainActivity
import ar.edu.utn.frba.placesify.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await
import java.lang.Math.random
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FIREBASE", "Mensaje recibido: ${remoteMessage.notification?.body}")
        remoteMessage.notification?.let { showNotification(it) }
/*
        if (remoteMessage.data.isNotEmpty()) {
            //Todo
            Log.d("FIREBASE", "Message data payload: ${remoteMessage.data}")
            showNotification(remoteMessage.notification!!)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FIREBASE", "Message Notification Body: ${it.body}")
            showNotification(remoteMessage.notification!!)
        }
 */
    }

    override fun onNewToken(newToken: String) {
        //MyPreferences.setFirebaseToken(this, newToken)
        Log.d("FIREBASE", "Refreshed token: $newToken")

        super.onNewToken(newToken)
    }

    private fun showNotification(notification: RemoteMessage.Notification) {

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val requestCode = 0

        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ico_placesify)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }

    companion object {

        private val CHANNEL_ID = "CHANNEL_1"
    }
}