package com.example.foodyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtCurrentPass, edtNewPass, edtReNewpass;
    private TextView tvErrCurrentPass, tvErrNewPass, tvErrReNewpass;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        if (!Helper.isLogin(this)) {
            finish();
        }

        initUi();

        imgBack.setOnClickListener(v -> finish());

        edtCurrentPass.setOnFocusChangeListener(new GenericOnFocusChangeListener(edtCurrentPass, tvErrCurrentPass));
        edtNewPass.setOnFocusChangeListener(new GenericOnFocusChangeListener(edtNewPass, tvErrNewPass));
        edtReNewpass.setOnFocusChangeListener(new GenericOnFocusChangeListener(edtReNewpass, tvErrReNewpass));

        edtCurrentPass.addTextChangedListener(new GenericTextWatcher(tvErrCurrentPass));
        edtNewPass.addTextChangedListener(new GenericTextWatcher(tvErrNewPass));
        edtReNewpass.addTextChangedListener(new GenericTextWatcher(tvErrReNewpass));

        btnChangePassword.setOnClickListener(this::onChangePassword);

    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        edtCurrentPass = findViewById(R.id.edt_current_pass);
        edtNewPass = findViewById(R.id.edt_new_pass);
        edtReNewpass = findViewById(R.id.edt_renew_pass);
        tvErrCurrentPass = findViewById(R.id.tv_err_current_pass);
        tvErrNewPass = findViewById(R.id.tv_err_new_pass);
        tvErrReNewpass = findViewById(R.id.tv_err_renew_pass);
        btnChangePassword = findViewById(R.id.btn_change_password);
    }

    private void onChangePassword(View view) {

        if (isInvalidPassword(edtCurrentPass, tvErrCurrentPass)) {
            edtCurrentPass.requestFocus();
            return;
        }
        if (isInvalidPassword(edtNewPass, tvErrNewPass)) {
            edtNewPass.requestFocus();
            return;
        }
        if (isInvalidPassword(edtReNewpass, tvErrReNewpass)) {
            edtReNewpass.requestFocus();
            return;
        }
        String currentPass = edtCurrentPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String renewPass = edtReNewpass.getText().toString().trim();

        if (!newPass.equals(renewPass)) {
            edtReNewpass.requestFocus();
            edtReNewpass.selectAll();
            tvErrReNewpass.setText("Mật khẩu nhập lại không giống");
            return;
        }

        if (isKeyboardOpen(getCurrentFocus())) {
            Helper.hideKeyboard(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.show();

        APIService.getService().changePassword(Helper.getTokenLogin(this), currentPass, newPass).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        edtCurrentPass.setText("");
                        edtNewPass.setText("");
                        edtReNewpass.setText("");
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                    }
                } else {
                    ToastCustom.notice(getApplicationContext(), "Đã sảy ra lỗi. Vui lòng thử lại!", ToastCustom.WARNING).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });


    }

    private static boolean isInvalidPassword(@NonNull EditText editText, TextView textView) {
        String s = editText.getText().toString().trim();
        if (s.isEmpty()) {
            textView.setText("Vui lòng nhập trường này");
            return true;
        }
        if (s.length() < 8) {
            textView.setText("Mật khẩu tối thiểu 8 kí tự");
            return true;
        }

        if (!s.matches(Const.PASSWORD_REGEX)) {
            textView.setText("Mật khẩu cần có ít nhất 1 chữ và 1 số");
            return true;
        }
        return false;
    }

    static class GenericTextWatcher implements TextWatcher {

        private final TextView tvErr;

        public GenericTextWatcher(TextView tvErr) {
            this.tvErr = tvErr;
        }

        @Override
        public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
            tvErr.setText("");
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    static class GenericOnFocusChangeListener implements View.OnFocusChangeListener {

        private final EditText mEditText;
        private final TextView tvErr;

        public GenericOnFocusChangeListener(EditText editText, TextView tvErr) {
            mEditText = editText;
            this.tvErr = tvErr;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                isInvalidPassword(mEditText, tvErr);
            }
        }
    }

    private boolean isKeyboardOpen(@NonNull View view) {
        Rect visibleBounds = new Rect();
        view.getRootView().getWindowVisibleDisplayFrame(visibleBounds);
        int heightDiff = view.getRootView().getHeight() - visibleBounds.height();
        int marginOfError = Math.round(convertDpToPx(50F));
        return heightDiff > marginOfError;
    }

    private float convertDpToPx(Float dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

}