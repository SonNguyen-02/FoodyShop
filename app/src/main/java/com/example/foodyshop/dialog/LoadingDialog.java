package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.foodyshop.R;

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private final String message;

    public LoadingDialog(Activity activity) {
        this(activity, null);
    }

    public LoadingDialog(Activity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
        if(message != null && !message.trim().isEmpty()){
            TextView tvMessLoad = view.findViewById(R.id.tv_mess_loading);
            tvMessLoad.setText(message);
        }
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
