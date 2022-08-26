package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;


import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.vcsserviceandbroadcastpractice.R;

public class OnNewPackageInstalledBroadcastReceiver extends BroadcastReceiver {
    private final String LOG_TAG = "New_Package_Tag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG,"New package added action triggered");
        if (!intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            Log.d(LOG_TAG,"Action not match");
            return;
        }


        String pkgName = intent.getData().getSchemeSpecificPart();
        PackageManager pm = context.getPackageManager();
        String appLabel;

        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(pkgName, 0);
            appLabel = pm.getApplicationLabel(appInfo).toString();
            Log.d(LOG_TAG,appLabel + " has just installed");
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG_TAG,"Name not found exception");
            return;
        }

        Intent launchIntent = pm.getLaunchIntentForPackage(pkgName);
        PendingIntent launchPendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("New application!")
                .setContentText(appLabel + " just has been installed!")
                .setTicker("New application installed")
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setOngoing(false)
                .setSilent(true)
                .setContentIntent(launchPendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_ID);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(100,notification);
        Log.d(LOG_TAG,"Notification shown");
    }
}
