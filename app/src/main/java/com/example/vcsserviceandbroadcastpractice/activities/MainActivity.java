package com.example.vcsserviceandbroadcastpractice.activities;

import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.START_LOG_ACTION;
import static com.example.vcsserviceandbroadcastpractice.services.LogWritingService.STOP_LOG_ACTION;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.adapters.FeatureListAdapter;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.LogNotificationBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.BootCompletedBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.NewPackageInstalledBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.ScreenOnBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.services.LogWritingService;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Every feature is  assign to each ID
    //That id help distinguish each feature to set title in list view or set on/off state to feature
    public static final String STARTUP_APPLICATION_FEATURE_ID = "com.example.vcsserviceandbroadcastpractice.STARTUP_APPLICATION";
    public static final String PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID = "com.example.vcsserviceandbroadcastpractice.PACKAGE_INSTALL_NOTIFICATION";
    public static final String SCREEN_ON_NOTIFICATION_FEATURE_ID = "com.example.vcsserviceandbroadcastpractice.SCREEN_ON_NOTIFICATION";
    public static final String RUNNING_IN_BACKGROUND_FEATURE_ID = "com.example.vcsserviceandbroadcastpractice.RUNNING_IN_BACKGROUND";
    public static final String LOG_WRITING_FEATURE_ID = "com.example.vcsserviceandbroadcastpractice.LOG_WRITING";

    //This notification id, used to show notification in device that use Android 8 and higher
    public static final String NOTIFICATION_CHANNEL_ID = "My Channel ID";

    //Settings about feature on/off is stored in a SharedPreferences file.
    //sharedPreferences file contains string key/boolean value pairs that's feature and its state
    private static final String PREFS_FILE = "MySettingsFile";

    //This Map<String, Boolean> used to store feature's on/off state
    //Key is feature's ID and Value is true/false
    private final Map<String, Boolean> featureState = new HashMap<>();

    //instance of Broadcast Receiver used for features
    private final ScreenOnBroadcastReceiver screenOnBroadcastReceiver = new ScreenOnBroadcastReceiver();
    private final NewPackageInstalledBroadcastReceiver newPackageReceiver = new NewPackageInstalledBroadcastReceiver();
    private final LogNotificationBroadcastReceiver logNotificationBroadcastReceiver = new LogNotificationBroadcastReceiver();
    private final BootCompletedBroadcastReceiver bootCompletedReceiver = new BootCompletedBroadcastReceiver();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get sharedPreferences that saved on/off state of features
        sharedPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        if (sharedPreferences == null)
            Log.d("Shared Preferences Tag", "saved file unavailable!");

        //Get state of each feature from sharedPreferences
        getSavedSettings(sharedPreferences);

        //Display list of feature and switch button to turn on or turn off
        ListView featureListView = findViewById(R.id.feature_list_listview);
        FeatureListAdapter adapter = new FeatureListAdapter(featureState, this);
        featureListView.setAdapter(adapter);

        //Create Notification Channel to display channel
        //API level 29 and higher required it for notification to work correctly
        createNotificationChannel();

        //start all feature that has on state
        startTurnedOnFeature();
    }

    @Override
    protected void onStop() {
        saveSettings(sharedPreferences);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //before destroy activity, turn off features
        stopTurnedOffFeature();
        super.onDestroy();
    }

    public void applySettings(View view) {
        try {
            startTurnedOnFeature();
            stopTurnedOffFeature();
            Toast.makeText(this, "Successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Fail to apply settings!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSavedSettings(SharedPreferences sharedPreferences) {
        boolean state;
        state = sharedPreferences.getBoolean(RUNNING_IN_BACKGROUND_FEATURE_ID, false);
        featureState.put(RUNNING_IN_BACKGROUND_FEATURE_ID, state);

        state = sharedPreferences.getBoolean(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID, false);
        featureState.put(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID, state);

        state = sharedPreferences.getBoolean(LOG_WRITING_FEATURE_ID, false);
        featureState.put(LOG_WRITING_FEATURE_ID, state);

        state = sharedPreferences.getBoolean(SCREEN_ON_NOTIFICATION_FEATURE_ID, false);
        featureState.put(SCREEN_ON_NOTIFICATION_FEATURE_ID, state);

        state = sharedPreferences.getBoolean(STARTUP_APPLICATION_FEATURE_ID, false);
        featureState.put(STARTUP_APPLICATION_FEATURE_ID, state);
    }

    private void saveSettings(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean state;

        if (featureState.containsKey(RUNNING_IN_BACKGROUND_FEATURE_ID)) {
            state = featureState.get(RUNNING_IN_BACKGROUND_FEATURE_ID);
            editor.putBoolean(RUNNING_IN_BACKGROUND_FEATURE_ID, state);
            Log.d("Shared Preferences Tag", "Running in background saved: " + state);
        }

        if (featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            editor.putBoolean(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID, state);
            Log.d("Shared Preferences Tag", "pakage added saved: " + state);
        }

        if (featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            editor.putBoolean(LOG_WRITING_FEATURE_ID, state);
            Log.d("Shared Preferences Tag", "log writing saved: " + state);
        }

        if (featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            editor.putBoolean(SCREEN_ON_NOTIFICATION_FEATURE_ID, state);
            Log.d("Shared Preferences Tag", "screen on saved: " + state);
        }

        if (featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            editor.putBoolean(STARTUP_APPLICATION_FEATURE_ID, state);
            Log.d("Shared Preferences Tag", "startup app saved: " + state);
        }

        editor.apply();
        Log.d("Shared Preferences Tag", "saved file unavailable!");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startTurnedOnFeature() {
        boolean state;

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

        if (featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (state) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_ON);
                registerReceiver(screenOnBroadcastReceiver, intentFilter);
                Log.d("Feature Log", "Screen On Message Box Turned On!");
            }
        }

        if (featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            if (state) {

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                intentFilter.addAction(Intent.ACTION_REBOOT);
                registerReceiver(bootCompletedReceiver, intentFilter);
                Log.d("Feature Log", "Startup App Turned On!");
            }
        }
    }

    private void stopTurnedOffFeature() {
        boolean state;
        boolean canBackgroundRunning = false;
        if (featureState.containsKey(RUNNING_IN_BACKGROUND_FEATURE_ID)) {
            canBackgroundRunning = featureState.get(RUNNING_IN_BACKGROUND_FEATURE_ID);
        }
        Log.d("Feature Log", "Running in background: " + canBackgroundRunning);

        if (featureState.containsKey(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
                try {
                    unregisterReceiver(newPackageReceiver);
                    Log.d("Feature Log", "New package added Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        if (featureState.containsKey(LOG_WRITING_FEATURE_ID)) {
            state = featureState.get(LOG_WRITING_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
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

        if (featureState.containsKey(SCREEN_ON_NOTIFICATION_FEATURE_ID)) {
            state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
            if (!(state && canBackgroundRunning)) {
                try {
                    unregisterReceiver(screenOnBroadcastReceiver);
                    Log.d("Feature Log", "Screen on Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        if (featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            if (!state) {
                try {
                    unregisterReceiver(bootCompletedReceiver);
                    Log.d("Feature Log", "startup app Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}