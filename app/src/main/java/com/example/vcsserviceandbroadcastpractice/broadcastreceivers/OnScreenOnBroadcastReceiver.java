package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.vcsserviceandbroadcastpractice.activities.MessageBoxActivity;

public class OnScreenOnBroadcastReceiver extends BroadcastReceiver {
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
            context.startActivity(openMessageBoxIntent);
        }

    }
}
