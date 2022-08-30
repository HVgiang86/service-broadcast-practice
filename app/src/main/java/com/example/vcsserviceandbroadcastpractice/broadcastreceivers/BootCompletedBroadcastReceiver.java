package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.STARTUP_APPLICATION_FEATURE_ID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;


//This Broadcast Receiver receive broadcast when device booted successfully
//Open an activity and Notification from here
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Device Boot Log";
    private static final String PREFS_FILE = "MySettingsFile";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_REBOOT)) {
            Log.d(LOG_TAG, "Device boot completed!");
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                boolean state = sharedPreferences.getBoolean(STARTUP_APPLICATION_FEATURE_ID, false);

                if (state) {
                    Intent launchIntent = new Intent(context, MainActivity.class);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent);
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage() + "");
            }
        }
    }

}
