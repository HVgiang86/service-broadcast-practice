package com.example.vcsserviceandbroadcastpractice.broadcastreceivers;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;

import com.example.vcsserviceandbroadcastpractice.R;

import java.util.List;

public class OnBootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get New Package Name
        String pkgName = intent.getData().getEncodedSchemeSpecificPart();
        PackageManager pm = context.getPackageManager();
        Drawable appIcon = AppCompatResources.getDrawable(context, R.drawable.ic_default_package_icon);
        String appName = "";

        //Get new installed package's information: icon & name
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        for (int i = 0; i < pkgAppsList.size(); ++i) {
            if (pkgAppsList.get(i).activityInfo.packageName.equals(pkgName)) {
                appIcon = pkgAppsList.get(i).loadIcon(pm);
                appName = pkgAppsList.get(i).loadLabel(pm).toString();
                break;
            }
        }

        //Create a notification that show name of new app. Can tap on notification to open new app
        Intent launchPackageIntent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchPackageIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(appName + " just has been installed! Tap on notification to open!")
                .setContentText("Sending Log")
                .setSmallIcon(R.drawable.ic_default_package_icon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker("service is running!")
                .setOngoing(true)
                .setAutoCancel(true);

        Notification notification = builder.build();

        //Display a notification about new package installed
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(100, notification);
    }
}
