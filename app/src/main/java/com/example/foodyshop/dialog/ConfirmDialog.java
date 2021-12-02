package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodyshop.R;

public class ConfirmDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private final String message;
    private final IOnClickConfirm mIOnClickConfirm;

    public ConfirmDialog(Activity activity, String message, IOnClickConfirm mIOnClickConfirm) {
        this.activity = activity;
        this.message = message;
        this.mIOnClickConfirm = mIOnClickConfirm;
    }

    public interface IOnClickConfirm {
        void onClick(ConfirmDialog confirmDialog);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Button btnCancel, btnOk;
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnOk = view.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(v -> dismiss());
        btnOk.setOnClickListener(v -> mIOnClickConfirm.onClick(this));

        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
