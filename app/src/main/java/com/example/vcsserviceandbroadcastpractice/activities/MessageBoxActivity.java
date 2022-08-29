package com.example.vcsserviceandbroadcastpractice.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MessageBoxActivity extends AppCompatActivity {
    private AlertDialog alertDialog;

    //This activity use Alert Theme that does not have an layout and display only a alertdialog
    //This activity has only one alertdialog that appear when user turn on the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Screen On")
                .setMessage("Màn hình vừa được bật sáng!");

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        alertDialog.dismiss();
        super.onDestroy();
    }
}