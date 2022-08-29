package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;


//This Broadcast Receiver receive broadcast when device booted successfully
//Open an activity and Notification from here
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Device Boot Log";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "Device boot completed!");

        try {
            // If android 10 or higher
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);

        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage() + "");
        }

    }

}
