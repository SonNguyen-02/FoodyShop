package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.foodyshop.R;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.FeedbackModel;

import org.jetbrains.annotations.Contract;

public class FeedbackDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private EditText edtFeedback;
    private Button btnSendFeedback;
    private Button btnCancel;
    private IAddFeedback mIAddFeedback;
    private IEditFeedback mIEditFeedback;

    private FeedbackModel mFeedback;

    private FeedbackDialog(Activity activity, IAddFeedback mIAddFeedback) {
        this.activity = activity;
        this.mIAddFeedback = mIAddFeedback;
    }

    private FeedbackDialog(Activity activity, FeedbackModel mFeedback, IEditFeedback mIEditFeedback) {
        this.activity = activity;
        this.mFeedback = mFeedback;
        this.mIEditFeedback = mIEditFeedback;
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public static FeedbackDialog addFeedback(Activity activity, IAddFeedback mIAddFeedback) {
        return new FeedbackDialog(activity, mIAddFeedback);
    }

    @NonNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static FeedbackDialog editFeedback(Activity activity, FeedbackModel mFeedback, IEditFeedback mIEditFeedback) {
        return new FeedbackDialog(activity, mFeedback, mIEditFeedback);
    }

    public interface IAddFeedback {
        void onClickAddFeedback(FeedbackDialog mFbDialog, String content);
    }

    public interface IEditFeedback {
        void onClickEditFeedback(FeedbackDialog mFbDialog, String content);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_feedback, null);
        initUi(view);
        Helper.showKeyboard(activity);
        btnCancel.setOnClickListener(v -> dismiss());

        if (mFeedback == null) {
            btnSendFeedback.setOnClickListener(v -> {
                btnSendFeedback.setEnabled(false);
                new Handler().postDelayed(() -> btnSendFeedback.setEnabled(true), 1500);
                mIAddFeedback.onClickAddFeedback(this, edtFeedback.getText().toString().trim());
            });
        } else {
            edtFeedback.setText(mFeedback.getContent());
            edtFeedback.requestFocus(mFeedback.getContent().length());
            btnSendFeedback.setText(R.string.confirm);
            btnSendFeedback.setOnClickListener(v -> {
                btnSendFeedback.setEnabled(false);
                new Handler().postDelayed(() -> btnSendFeedback.setEnabled(true), 1500);
                mIEditFeedback.onClickEditFeedback(this, edtFeedback.getText().toString().trim());
            });
        }

        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void initUi(@NonNull View view) {
        edtFeedback = view.findViewById(R.id.edt_feedback);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSendFeedback = view.findViewById(R.id.btn_send_feedback);
    }

    public void dismiss() {
        if (dialog != null) {
            Helper.hideKeyboard(activity, edtFeedback);
            dialog.dismiss();
        }
    }

}
