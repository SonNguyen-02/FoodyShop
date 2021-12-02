package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.foodyshop.R;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.FeedbackModel;

public class FeedbackDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private EditText edtFeedback;
    private Button btnSendFeedback;
    private Button btnCancel;
    private final IOnClickSend mIOnClickSend;

    private FeedbackModel mFeedback;

    public FeedbackDialog(Activity activity) {
        this(activity, null);
    }

    public FeedbackDialog(Activity activity, FeedbackModel mFeedback) {
        this.activity = activity;
        this.mFeedback = mFeedback;
        this.mIOnClickSend = (IOnClickSend) activity;
    }

    public interface IOnClickSend {
        void onClickAddFeedback(String content);

        void onClickEditFeedback(FeedbackModel feedbackModel, String content);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_feedback, null);
        initUi(view);
        Helper.showKeyboard(activity);
        btnCancel.setOnClickListener(v -> dismiss());

        if (mFeedback == null) {
            btnSendFeedback.setOnClickListener(v -> mIOnClickSend.onClickAddFeedback(edtFeedback.getText().toString().trim()));
        } else {
            edtFeedback.setText(mFeedback.getContent());
            btnSendFeedback.setText(R.string.confirm);
            btnSendFeedback.setOnClickListener(v -> mIOnClickSend.onClickEditFeedback(mFeedback, edtFeedback.getText().toString().trim()));
        }


        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    private void initUi(@NonNull View view) {
        edtFeedback = view.findViewById(R.id.edt_feedback);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSendFeedback = view.findViewById(R.id.btn_send_feedback);
    }

    public void dismiss() {
        Helper.hideKeyboard(activity, edtFeedback);
        dialog.dismiss();
    }

    public void focusEdt() {
        edtFeedback.requestFocus();
    }
}
