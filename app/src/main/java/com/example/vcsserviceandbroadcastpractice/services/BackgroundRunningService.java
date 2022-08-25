package com.example.vcsserviceandbroadcastpractice.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
}