package com.example.vcsserviceandbroadcastpractice.services;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.NOTIFICATION_CHANNEL_ID;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.activities.MainActivity;

//Because dynamic registered Broadcast Receivers keep running if only Application Context stays alive
// So this Service has only one mission that keeps application context stay alive
//Application context keep Our Broadcast Receiver that dynamic registered in code stay running
public class BackgroundRunningService extends Service {
    public BackgroundRunningService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Always on service", "Always on service startCommand() called!");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}