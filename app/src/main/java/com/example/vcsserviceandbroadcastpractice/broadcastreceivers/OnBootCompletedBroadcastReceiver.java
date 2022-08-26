package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;

public class OnBootCompletedBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "Device Boot Log";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "Device boot completed!");
        Intent startAppIntent = new Intent(context, MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startAppIntent);
    }
}
