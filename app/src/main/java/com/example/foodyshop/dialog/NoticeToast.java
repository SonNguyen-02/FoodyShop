package com.example.foodyshop.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodyshop.R;

public class NoticeToast {

    private final Context context;
    private final String message;
    private final boolean isSuccess;
    private final Toast toast;

    public NoticeToast(Context context, String message, boolean isSuccess) {
        this.context = context;
        this.message = message;
        this.isSuccess = isSuccess;
        toast = new Toast(context);
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_notice, null);
        ImageView imgIcon = view.findViewById(R.id.img_icon);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        if(isSuccess){
            imgIcon.setImageResource(R.drawable.ic_valid);
        }else{
            imgIcon.setImageResource(R.drawable.ic_invalid);
        }
        tvMessage.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(view);
        toast.show();
    }

}
