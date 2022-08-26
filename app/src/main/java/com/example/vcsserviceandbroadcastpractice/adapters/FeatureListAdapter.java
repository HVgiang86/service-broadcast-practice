package com.example.vcsserviceandbroadcastpractice.adapters;

import static com.example.vcsserviceandbroadcastpractice.activities.MainActivity.*;

import android.app.Activity;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcsserviceandbroadcastpractice.R;

import java.util.Map;


public class FeatureListAdapter extends BaseAdapter {

    private final String[] featureIdList = {STARTUP_APPLICATION_FEATURE_ID,
                                            PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID,
                                            SCREEN_ON_NOTIFICATION_FEATURE_ID,
                                            RUNNING_IN_BACKGROUND_FEATURE_ID,
                                            LOG_WRITING_FEATURE_ID};

    private final Map<String, Boolean> featureState;

    private final Activity activity;

    public FeatureListAdapter(Map<String, Boolean> featureState, Activity activity) {
        this.featureState = featureState;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return featureIdList.length;
    }

    @Override
    public String getItem(int position) {
        return featureIdList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.feature_list_item, parent, false);

        TextView textView = convertView.findViewById(R.id.feature_tv);
        SwitchCompat switchButton = convertView.findViewById(R.id.feature_sw);

        String featureId = featureIdList[position];
        switchButton.setOnCheckedChangeListener(new OnFeatureCheckedChangeListener(featureId));
        setFeatureNameTextView(textView, featureId);

        boolean state = featureState.get(featureId);
        switchButton.setChecked(state);

        return convertView;
    }

    private void setFeatureNameTextView(TextView textView, String featureId) {
        int resource = 0;
        switch (featureId) {
            case STARTUP_APPLICATION_FEATURE_ID:
                resource = R.string.startup_app;
                break;
            case PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID:
                resource = R.string.package_install;
                break;
            case SCREEN_ON_NOTIFICATION_FEATURE_ID:
                resource = R.string.screen_on;
                break;
            case RUNNING_IN_BACKGROUND_FEATURE_ID:
                resource = R.string.running_in_background;
                break;
            case LOG_WRITING_FEATURE_ID:
                resource = R.string.log_writing;
                break;
            default:
                break;
        }
        textView.setText(resource);
    }



    //Listener For Feature's Switch Button state changing
    class OnFeatureCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        String featureId;

        public OnFeatureCheckedChangeListener(String featureId) {
            this.featureId = featureId;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            featureState.remove(featureId);
            featureState.put(featureId, isChecked);
        }
    }
}
