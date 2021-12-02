package com.example.foodyshop.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodyshop.R;

import org.jetbrains.annotations.Contract;

public class ToastCustom {

    private final Context context;
    private final String message;
    private boolean isSuccess;
    private final Toast toast;
    private boolean isLoading;
    private final int duration;

    private ToastCustom(Context context, String message, boolean isSuccess, int duration) {
        this.context = context;
        this.message = message;
        this.isSuccess = isSuccess;
        this.duration = duration;
        toast = new Toast(context);
    }

    private ToastCustom(Context context, String message, int duration) {
        isLoading = true;
        this.context = context;
        this.message = message;
        this.duration = duration;
        toast = new Toast(context);
    }

    @NonNull
    @org.jetbrains.annotations.Contract("_, _, _, _ -> new")
    public static ToastCustom notice(Context context, String message, boolean isSuccess, int duration) {
        return new ToastCustom(context, message, isSuccess, duration);
    }

    @NonNull
    @Contract("_, _, _ -> new")
    public static ToastCustom loading(Context context, String message, int duration) {
        return new ToastCustom(context, message, duration);
    }

    @NonNull
    public static ToastCustom loading(Context context, int duration) {
        return loading(context, null, duration);
    }

    public void show() {
        View view;
        if (isLoading) {
            view = LayoutInflater.from(context).inflate(R.layout.toast_loading, null);
            if (message != null && !message.trim().isEmpty()) {
                TextView tvMessage = view.findViewById(R.id.tv_message);
                tvMessage.setText(message);
            }
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.toast_notice, null);
            ImageView imgIcon = view.findViewById(R.id.img_icon);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            if (isSuccess) {
                imgIcon.setImageResource(R.drawable.ic_valid);
            } else {
                imgIcon.setImageResource(R.drawable.ic_invalid);
            }
            tvMessage.setText(message);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(view);
        toast.show();
        new Handler().postDelayed(toast::cancel, duration);
    }

}
