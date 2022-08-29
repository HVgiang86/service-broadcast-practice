package com.example.vcsserviceandbroadcastpractice.services;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.NOTIFICATION_CHANNEL_ID;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.LogNotificationBroadcastReceiver;

public class LogWritingService extends Service {
    private Handler handler;

    private boolean allowToRun = false;

    private final String LOG_TAG = "Log Writing Tag";
    public static final String START_LOG_ACTION = "com.example.vcsserviceandbroadcastpractice.services.START_LOG_ACTION";
    public static final String STOP_LOG_ACTION = "com.example.vcsserviceandbroadcastpractice.services.STOP_LOG_ACTION";
    public static final String STOP_LOG_SERVICE_ACTION = "com.example.vcsserviceandbroadcastpractice.services.STOP_LOG_SERVICE_ACTION";
    public static final String REQUIRED_KEY = "com.example.vcsserviceandbroadcastpractice.services.LOG_SERVICE_REQUIRED_KEY";


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (allowToRun) {
                Log.d(LOG_TAG, "This log will be sent to logcat every 5 seconds. Current time in seconds: " + System.currentTimeMillis()/1000);
                handler.postDelayed(runnable, 5000);
            }
        }
    };


    public LogWritingService() {

    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG,"Log Service onCreate() called!");
        HandlerThread thread = new HandlerThread("LogWritingServiceStart", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper looper = thread.getLooper();
        handler = new Handler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getStringExtra(REQUIRED_KEY) != null) {
            if (intent.getStringExtra(REQUIRED_KEY).equals(STOP_LOG_ACTION))
                allowToRun = false;
            else
                allowToRun = true;
        }
        else {
            allowToRun = true;
        }

        if (allowToRun) {
            handler.removeCallbacks(runnable);
            handler.post(runnable);
        }


        Log.d(LOG_TAG, "Service start command called");

        Intent startIntent = new Intent(this, LogNotificationBroadcastReceiver.class);
        startIntent.setAction(START_LOG_ACTION);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(this, 0, startIntent, 0);

        Intent stopIntent = new Intent(this, LogNotificationBroadcastReceiver.class);
        stopIntent.setAction(STOP_LOG_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0,stopIntent, 0);

        Intent contentIntent = new Intent(this, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Logcat Writer")
                .setContentText("I will write a log to logcat every 5s!")
                .setTicker("Logcat writer")
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSilent(true)
                .setContentIntent(contentPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(R.drawable.ic_play_icon,"Start",startPendingIntent)
                .addAction(R.drawable.ic_stop_icon,"Stop",stopPendingIntent)
                .setChannelId(NOTIFICATION_CHANNEL_ID);

        Notification notification = builder.build();
        Log.d(LOG_TAG, "Notification created");
        startForeground(101, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy called!");
        allowToRun = false;
        stopForeground(true);
        stopSelf();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}