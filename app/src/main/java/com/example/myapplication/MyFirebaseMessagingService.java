package com.example.myapplication;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle the incoming message
        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload
            Log.d("FCM", "Message data payload: " + remoteMessage.getData());
            // You can extract custom data from the payload
            String customData = remoteMessage.getData().get("key");
        }

        if (remoteMessage.getNotification() != null) {
            // Handle the notification payload
            Log.d("FCM", "Message notification body: " + remoteMessage.getNotification().getBody());

            // Display a notification to the user
            showNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default_channel_id", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel_id")
                .setContentTitle("FCM Notification")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher) // Replace with your app's icon
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(0, notification);
    }
}
