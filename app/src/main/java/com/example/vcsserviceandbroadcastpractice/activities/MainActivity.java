package com.example.vcsserviceandbroadcastpractice.activities;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.adapters.FeatureListAdapter;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.BootCompletedBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.services.BackgroundRunningService;
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
    private SharedPreferences sharedPreferences;

    //This is request code to require System's draw on app permission
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;


    private final BootCompletedBroadcastReceiver bootCompletedReceiver = new BootCompletedBroadcastReceiver();

    //This Map<String, Boolean> used to store feature's on/off state
    //Key is feature's ID and Value is true/false
    private final Map<String, Boolean> featureState = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

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
            stopTurnedOffFeature();
            startTurnedOnFeature();
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

        //Turn on startup app feature
        if (featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            Log.d("Feature Log", "startup app: " + state);
            if (state) {

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                intentFilter.addAction(Intent.ACTION_REBOOT);
                registerReceiver(bootCompletedReceiver, intentFilter);
                Log.d("Feature Log", "Startup App Turned On!");
            }
        }

        //turn on service to start features
        Intent serviceIntent = new Intent(getBaseContext(), BackgroundRunningService.class);
        Bundle bundle = convertFeatureStateMapToBundle();
        serviceIntent.putExtras(bundle);
        startService(serviceIntent);
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.d("Feature Log", "Running in background turned on!");
    }

    private void stopTurnedOffFeature() {
        boolean state;
        boolean canBackgroundRunning = false;

        //Turn off startup app feature
        if (featureState.containsKey(STARTUP_APPLICATION_FEATURE_ID)) {
            state = featureState.get(STARTUP_APPLICATION_FEATURE_ID);
            Log.d("Feature Log", "startup app: " + state);
            if (!state) {
                try {
                    unregisterReceiver(bootCompletedReceiver);
                    Log.d("Feature Log", "startup app Turned Off!");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        //If running in background is turned off, stop all service
        if (featureState.containsKey(RUNNING_IN_BACKGROUND_FEATURE_ID)) {
            canBackgroundRunning = featureState.get(RUNNING_IN_BACKGROUND_FEATURE_ID);
            Log.d("Feature Log", "Running in background: " + canBackgroundRunning);
            if (!canBackgroundRunning) {
                Log.d("Feature Log", "Running in background turned off!");
                Intent serviceIntent = new Intent(getBaseContext(), BackgroundRunningService.class);
                stopService(serviceIntent);
            }
        }


    }

    private Bundle convertFeatureStateMapToBundle() {
        Bundle bundle = new Bundle();
        boolean state;

        state = featureState.get(SCREEN_ON_NOTIFICATION_FEATURE_ID);
        bundle.putBoolean(SCREEN_ON_NOTIFICATION_FEATURE_ID, state);

        state = featureState.get(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID);
        bundle.putBoolean(PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID,state);

        state = featureState.get(LOG_WRITING_FEATURE_ID);
        bundle.putBoolean(LOG_WRITING_FEATURE_ID,state);

        return bundle;
    }

    //This method will require System's draw on app permission
    //It is necessary for device running on api 29+ to start activity from a broadcast receiver when boot
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                checkPermission();
            }
        }
    }
}