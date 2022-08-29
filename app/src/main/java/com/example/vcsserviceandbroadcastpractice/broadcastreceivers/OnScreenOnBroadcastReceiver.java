package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.activities.MessageBoxActivity;

public class OnScreenOnBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Screen On Log";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Screen on triggered!");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(LOG_TAG, "Screen turned on!");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("Screen on")
                    .setContentText("You just turned on your screen!")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setSilent(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setChannelId(CHANNEL_ID);

            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(102,notification);

            Intent openMessageBoxIntent = new Intent(context, MessageBoxActivity.class);
            openMessageBoxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            openMessageBoxIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            openMessageBoxIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            try {
                context.startActivity(openMessageBoxIntent);
            } catch (IllegalArgumentException e) {
                Log.d(LOG_TAG, "Screen turned on but cant start activity!");
                e.printStackTrace();
            }

        }

    }
}
