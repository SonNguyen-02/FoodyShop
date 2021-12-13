package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodyshop.R;

public class AuthSuccessDialog {

    private AlertDialog dialog;
    private final Activity activity;

    private final int imgRes;
    private final String message;
    private final IOnClickOk mIOnClickOk;

    public AuthSuccessDialog(Activity activity, int imgRes, String message, IOnClickOk mIOnClickOk) {
        this.activity = activity;
        this.imgRes = imgRes;
        this.message = message;
        this.mIOnClickOk = mIOnClickOk;
    }

    public interface IOnClickOk {
        void onClick();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_auth_success, null);
        ImageView imgIcon = view.findViewById(R.id.img_icon);
        TextView tvMess = view.findViewById(R.id.tv_message);
        Button btnOk = view.findViewById(R.id.btn_confirm);

        imgIcon.setImageResource(imgRes);
        tvMess.setText(message);
        btnOk.setOnClickListener(v -> {
            dismiss();
            mIOnClickOk.onClick();
        });

        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
