package com.example.vcsserviceandbroadcastpractice.activities;

import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.adapters.FeatureListAdapter;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.LogNotificationBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.OnNewPackageInstalledBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.services.LogWritingService;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "My Channel ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView featureListView = findViewById(R.id.feature_list_listview);

        FeatureListAdapter adapter = new FeatureListAdapter(this);
        featureListView.setAdapter(adapter);

        createNotificationChannel();

        OnNewPackageInstalledBroadcastReceiver broadcastReceiver = new OnNewPackageInstalledBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);

        LogNotificationBroadcastReceiver notificationBroadcastReceiver = new LogNotificationBroadcastReceiver();
        IntentFilter logIntentFilter = new IntentFilter();
        logIntentFilter.addAction(START_LOG_ACTION);
        logIntentFilter.addAction(STOP_LOG_ACTION);
        registerReceiver(notificationBroadcastReceiver, logIntentFilter);

        Intent serviceIntent = new Intent(this, LogWritingService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}