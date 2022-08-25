package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.vcsserviceandbroadcastpractice.services.LogWritingService;

public class LogNotificationBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Log Writing Tag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BROADCAST RECEIVED!");
        Intent serviceIntent = new Intent(context.getApplicationContext(), LogWritingService.class);
        if (intent.getAction().equals(START_LOG_ACTION)) {
            Log.d(LOG_TAG, "ACTION START RECEIVED");
            ContextCompat.startForegroundService(context, serviceIntent);
        }
        else if (intent.getAction().equals(STOP_LOG_ACTION)) {
            Log.d(LOG_TAG, "ACTION STOP RECEIVED");
            context.stopService(serviceIntent);
        }
    }
}
