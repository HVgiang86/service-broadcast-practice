package com.example.vcsserviceandbroadcastpractice.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.example.vcsserviceandbroadcastpractice.R;
import com.example.vcsserviceandbroadcastpractice.adapters.FeatureListAdapter;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.OnNewPackageInstalledBroadcastReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView featureListView = findViewById(R.id.feature_list_listview);

        FeatureListAdapter adapter = new FeatureListAdapter(this);
        featureListView.setAdapter(adapter);

        OnNewPackageInstalledBroadcastReceiver broadcastReceiver = new OnNewPackageInstalledBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);

        getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }
}