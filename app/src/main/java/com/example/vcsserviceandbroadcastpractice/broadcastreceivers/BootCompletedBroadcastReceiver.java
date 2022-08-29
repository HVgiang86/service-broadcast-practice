package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;
import com.example.vcsserviceandbroadcastpractice.services.ApplicationAlwaysOnService;


//This Broadcast Receiver receive broadcast when device booted successfully
//Open an activity and Notification from here
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Device Boot Log";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "Device boot completed!");

        try {

            // If android 10 or higher
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            {
                Intent serviceIntent= new Intent(context, ApplicationAlwaysOnService.class);
                context.startForegroundService(serviceIntent);
            }
            else
            {
                // If lower than Android 10, we use the normal method ever.
                Intent activity = new Intent(context, MainActivity.class);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activity);
            }

        } catch (Exception e)
        {
            Log.d(LOG_TAG,e.getMessage()+"");
        }

    }

}
