package com.example.vcsserviceandbroadcastpractice.adapters;

import android.app.Activity;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcsserviceandbroadcastpractice.R;


public class FeatureListAdapter extends BaseAdapter {
    public static final String STARTUP_APPLICATION_FEATURE_ID           = "com.example.vcsserviceandbroadcastpractice.STARTUP_APPLICATION";
    public static final String PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID  = "com.example.vcsserviceandbroadcastpractice.PACKAGE_INSTALL_NOTIFICATION";
    public static final String SCREEN_ON_NOTIFICATION_FEATURE_ID        = "com.example.vcsserviceandbroadcastpractice.SCREEN_ON_NOTIFICATION";
    public static final String RUNNING_IN_BACKGROUND_FEATURE_ID         = "com.example.vcsserviceandbroadcastpractice.RUNNING_IN_BACKGROUND";
    public static final String LOG_WRITING_FEATURE_ID                   = "com.example.vcsserviceandbroadcastpractice.LOG_WRITING";

    private final String[] featureIdList = {STARTUP_APPLICATION_FEATURE_ID,
                                            PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID,
                                            SCREEN_ON_NOTIFICATION_FEATURE_ID,
                                            RUNNING_IN_BACKGROUND_FEATURE_ID,
                                            LOG_WRITING_FEATURE_ID};

    private final Activity activity;

    public FeatureListAdapter(Activity activity) {
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

        switchButton.setOnCheckedChangeListener(new OnFeatureCheckedChangeListener(featureIdList[position]));
        setFeatureNameTextView(textView, featureIdList[position]);

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
            switch (featureId) {
                case STARTUP_APPLICATION_FEATURE_ID:
                    Toast.makeText(activity, "Start_up checked change!", Toast.LENGTH_SHORT).show();
                    break;
                case PACKAGE_INSTALL_NOTIFICATION_FEATURE_ID:
                    Toast.makeText(activity, "package installation checked change!", Toast.LENGTH_SHORT).show();
                    break;
                case SCREEN_ON_NOTIFICATION_FEATURE_ID:
                    Toast.makeText(activity, "screen on checked change!", Toast.LENGTH_SHORT).show();
                    break;
                case RUNNING_IN_BACKGROUND_FEATURE_ID:
                    Toast.makeText(activity, "running in background checked change!", Toast.LENGTH_SHORT).show();
                    break;
                case LOG_WRITING_FEATURE_ID:
                    Toast.makeText(activity, "log writing checked change!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(activity, "Wrong chosen!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
