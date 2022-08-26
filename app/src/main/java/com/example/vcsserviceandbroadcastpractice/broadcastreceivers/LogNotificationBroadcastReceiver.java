package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.REQUIRED_KEY;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_SERVICE_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.vcsserviceandbroadcastpractice.services.LogWritingService;

public class LogNotificationBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Log Writing Tag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "BROADCAST RECEIVED!");
        Intent serviceIntent = new Intent(context.getApplicationContext(), LogWritingService.class);
        if (intent.getAction().equals(START_LOG_ACTION)) {
            Log.d(LOG_TAG, "ACTION START RECEIVED");
            serviceIntent.putExtra(REQUIRED_KEY, START_LOG_ACTION);
            context.startService(serviceIntent);
        }
        else if (intent.getAction().equals(STOP_LOG_ACTION)) {
            Log.d(LOG_TAG, "ACTION STOP RECEIVED");
            serviceIntent.putExtra(REQUIRED_KEY, STOP_LOG_ACTION);
            context.startService(serviceIntent);
        }
        else if (intent.getAction().equals(STOP_LOG_SERVICE_ACTION)) {
            Log.d(LOG_TAG, "ACTION STOP SERVICE RECEIVED");
            context.stopService(serviceIntent);
        }
    }
}
