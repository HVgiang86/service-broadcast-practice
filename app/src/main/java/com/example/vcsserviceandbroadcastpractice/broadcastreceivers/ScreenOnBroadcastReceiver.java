package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.activities.MessageBoxActivity;


//When user turned on screen, this Broadcast Receiver receive an system Broadcast
//Start Message Box Activity to display an message box/alert dialog
public class ScreenOnBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Screen On Log";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Screen on triggered!");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(LOG_TAG, "Screen turned on!");

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
