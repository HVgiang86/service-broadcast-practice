package com.example.vcsserviceandbroadcastpractice;

import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.OnBootCompletedBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.OnNewPackageInstalledBroadcastReceiver;
import com.example.vcsserviceandbroadcastpractice.broadcastreceivers.OnScreenOnBroadcastReceiver;

public class FeatureManager {
    private final static FeatureManager INSTANCE = new FeatureManager();

    private OnBootCompletedBroadcastReceiver onBootCompletedBroadcastReceiver;
    private OnNewPackageInstalledBroadcastReceiver onNewPackageInstalledBroadcastReceiver;
    private OnScreenOnBroadcastReceiver onScreenOnBroadcastReceiver;

    private FeatureManager() {
    }

    public static FeatureManager getInstance() {
        return INSTANCE;
    }

    public void turnOnFeature(String featureId) {

    }

    public void turnOffFeature(String featureId) {

    }

    public void turnOnAllAutomaticFeature() {

    }

    public void setAllFeatureToOff() {

    }

    public void turnOffAllFeature() {

    }


}
