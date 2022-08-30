package com.example.vcsserviceandbroadcastpractice.services;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.LOG_WRITING_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.NOTIFICATION_CHANNEL_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.RUNNING_IN_BACKGROUND_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.SCREEN_ON_NOTIFICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.STARTUP_APPLICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.BootCompletedBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.LogNotificationBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.NewPackageInstalledBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.ScreenOnBroadcastReceiver;

import java.util.HashMap;
import java.util.Map;

//Because dynamic registered Broadcast Receivers keep running if only Application Context stays alive
// So this Service has only one mission that keeps application context stay alive
//Application context keep Our Broadcast Receiver that dynamic registered in code stay running
public class BackgroundRunningService extends Service {
    private final Map<String, Boolean> featureState = new HashMap<>();

    //instance of Broadcast Receiver used for features
    private final ScreenOnBroadcastReceiver screenOnBroadcastReceiver = new ScreenOnBroadcastReceiver();
    private final NewPackageInstalledBroadcastReceiver newPackageReceiver = new NewPackageInstalledBroadcastReceiver();
    private final LogNotificationBroadcastReceiver logNotificationBroadcastReceiver = new LogNotificationBroadcastReceiver();

    private final String LOG_TAG = "Log Writing Tag";

    public BackgroundRunningService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Always on service startCommand() called!");

        if (intent!= null) {
            Bundle bundle = intent.getExtras();
            getFeatureStateMapFromBundle(bundle);
        }

        //restart all
        stopTurnedOffFeature();
        startTurnedOnFeature();

        //Display a notification to run service while application is closed
        Intent contentIntent = new Intent(this, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("App is running in background!")
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSilent(true)
                .setContentIntent(contentPendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setChannelId(NOTIFICATION_CHANNEL_ID);

        Notification notification = builder.build();
        Log.d(LOG_TAG, "Notification created");
        startForeground(105, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d("Always on service", "Always on service onDestroy() called!");
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private void getFeatureStateMapFromBundle(Bundle bundle) {
        boolean state;

        state = bundle.getBoolean(SCREEN_ON_NOTIFICATION_FEATURE_ID);
        featureState.remove(SCREEN_ON_NOTIFICATION_FEATURE_ID);
        featureState.put(SCREEN_ON_NOTIFICATION_FEATURE_ID,state);

        state = bundle.getBoolean(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
        featureState.remove(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
        featureState.put(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID,state);

        state = bundle.getBoolean(LOG_WRITING_FEATURE_ID);
        featureState.remove(LOG_WRITING_FEATURE_ID);
        featureState.put(LOG_WRITING_FEATURE_ID,state);
    }

    private void stopTurnedOffFeature() {
        boolean state;
        boolean canBackgroundRunning = false;

        //Turn off broadcast receiver when new package added
        if (featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            if (!state) {
                try {
                    unregisterReceiver(newPackageReceiver);
                    Log.d("Feature Log", "New package added Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        //Turn off Log writing service and broadcast receiver
        if (featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            if (!state) {
                try {
                    LocalBroadcastManager.getInstance(this).unregisterReceiver(logNotificationBroadcastReceiver);

                    Intent serviceIntent = new Intent(this, LogWritingService.class);
                    stopService(serviceIntent);
                    Log.d("Feature Log", "Log writing Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        //Turn off screen on broadcast receiver
        if (featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (!state) {
                try {
                    unregisterReceiver(screenOnBroadcastReceiver);
                    Log.d("Feature Log", "Screen on Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void startTurnedOnFeature() {
        boolean state;

        //Turn on new package installed broadcast receiver
        if (featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                intentFilter.addDataScheme("package");
                registerReceiver(newPackageReceiver, intentFilter);
                Log.d("Feature Log", "New Package Added Turned On!");
            }
        }

        //Turn on Log writing service and broadcast receiver
        if (featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(START_LOG_ACTION);
                intentFilter.addAction(STOP_LOG_ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(logNotificationBroadcastReceiver, intentFilter);

                Intent serviceIntent = new Intent(this, LogWritingService.class);
                ContextCompat.startForegroundService(this, serviceIntent);

                Log.d("Feature Log", "Log Writing Turned On!");
            }
        }

        //Turn on screen on broadcast receiver
        if (featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_ON);
                registerReceiver(screenOnBroadcastReceiver, intentFilter);
                Log.d("Feature Log", "Screen On Message Box Turned On!");
            }
        }


    }
}