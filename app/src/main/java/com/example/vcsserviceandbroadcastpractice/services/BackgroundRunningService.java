package com.example.vcsserviceandbroadcastpractice.services;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.LOG_WRITING_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.SCREEN_ON_NOTIFICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.STARTUP_APPLICATION_FEATURE_ID;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BackgroundRunningService extends Service {
    public BackgroundRunningService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private void startTurnedOnFeature() {
        boolean state;

        if(featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                intentFilter.addDataScheme("package");
                registerReceiver(newPackageReceiver, intentFilter);
                Log.d("Feature Log","New Package Added Turned On!");
            }
        }

        if(featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(START_LOG_ACTION);
                intentFilter.addAction(STOP_LOG_ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(logNotificationBroadcastReceiver, intentFilter);

                Intent serviceIntent = new Intent(this, LogWritingService.class);
                ContextCompat.startForegroundService(this, serviceIntent);

                Log.d("Feature Log","Log Writing Turned On!");
            }
        }

        if(featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_ON);
                registerReceiver(screenOnBroadcastReceiver,intentFilter);
                Log.d("Feature Log","Screen On Message Box Turned On!");
            }
        }

        if(featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            if (state) {

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                intentFilter.addAction(Intent.ACTION_REBOOT);
                registerReceiver(bootCompletedReceiver,intentFilter);
                Log.d("Feature Log","Startup App Turned On!");
            }
        }
    }

    private void stopTurnedOffFeature() {
        boolean state;
        boolean canBackgroundRunning = false;
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        if(featureState.containsKey(RUNNING_IN_BACKGROUND_FEATURE_ID)) {
            canBackgroundRunning = featureState.get(RUNNING_IN_BACKGROUND_FEATURE_ID);
        }
        Log.d("Feature Log","Running in background: " + canBackgroundRunning);

        if(featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
                try {
                    unregisterReceiver(newPackageReceiver);
                    Log.d("Feature Log","New package added Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        if(featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
                try {
                    LocalBroadcastManager.getInstance(this).unregisterReceiver(logNotificationBroadcastReceiver);

                    Intent serviceIntent = new Intent(this, LogWritingService.class);
                    stopService(serviceIntent);
                    Log.d("Feature Log","Log writing Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        if(featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
                try {
                    unregisterReceiver(screenOnBroadcastReceiver);
                    Log.d("Feature Log","Screen on Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        if(featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            if (!state) {
                try {
                    unregisterReceiver(bootCompletedReceiver);
                    Log.d("Feature Log","startup app Turned Off!");
                } catch (IllegalArgumentException  e) {
                    e.printStackTrace();
                }
            }
        }
    }
}