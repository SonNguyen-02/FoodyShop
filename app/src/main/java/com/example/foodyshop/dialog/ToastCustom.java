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

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;

    private final Context context;
    private final String message;
    private int type;
    private final Toast toast;
    private boolean isLoading;
    private final int duration;

    private ToastCustom(Context context, String message, int type, int duration) {
        this.context = context;
        this.message = message;
        this.type = type;
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
    public static ToastCustom notice(Context context, String message, int type, int duration) {
        return new ToastCustom(context, message, type, duration);
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
            switch (type) {
                case SUCCESS:
                    imgIcon.setImageResource(R.drawable.ic_valid);
                    break;
                case ERROR:
                    imgIcon.setImageResource(R.drawable.ic_invalid);
                    break;
                case WARNING:
                    imgIcon.setImageResource(R.drawable.ic_warning);
                    break;
                case INFO:
                    imgIcon.setImageResource(R.drawable.ic_info);
                    break;
            }
            tvMessage.setText(message);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(view);
        toast.show();
        if (duration > 0) {
            new Handler().postDelayed(toast::cancel, duration);
        }
    }

    public void cancel() {
        toast.cancel();
    }

}
