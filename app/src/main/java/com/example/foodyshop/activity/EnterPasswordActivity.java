package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.AuthSuccessDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPasswordActivity extends AppCompatActivity {

    private String mToken;

    private EditText edtPassword, edtConfPassword;
    private Button btnConfirm;
    private boolean isBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        initUI();
        getDataIntent();
        btnConfirm.setOnClickListener(this::onClickBtnConfirm);
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mToken = bundle.getString(Const.KEY_TOKEN);
        }
    }

    private void initUI() {
        edtPassword = findViewById(R.id.edt_password);
        edtConfPassword = findViewById(R.id.edt_confirm_password);
        btnConfirm = findViewById(R.id.btn_confirm);
    }

    private void onClickBtnConfirm(View v) {
        String password = edtPassword.getText().toString().trim();
        String confPassword = edtConfPassword.getText().toString().trim();

        // validate password
        if (Helper.isInvalidPassword(getApplicationContext(), edtPassword, false)) {
            return;
        }
        if (Helper.isInvalidPassword(getApplicationContext(), edtConfPassword, true)) {
            return;
        }
        if (!password.equals(confPassword)) {
            edtConfPassword.requestFocus();
            edtConfPassword.selectAll();
            ToastCustom.notice(this, "Mật khẩu không giống", ToastCustom.WARNING).show();
            return;
        }

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.show();
        APIService.getService().changePasswordOTP(mToken, password).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        String mess = "Đổi mật khẩu thành công!";
                        AuthSuccessDialog successDialog = new AuthSuccessDialog(EnterPasswordActivity.this, R.drawable.resetpw_success, mess, () -> {
                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                        successDialog.show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                    }
                } else {
                    ToastCustom.notice(getApplicationContext(), "Có lỗi sảy ra. Vui lòng thử lại!", ToastCustom.ERROR).show();
                    Log.e("ddd", "onResponse: sever error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isBackPress) {
            isBackPress = false;
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            ToastCustom.notice(this, "Nhấn back thêm lần nữa để thoát", ToastCustom.NONE).show();
            isBackPress = true;
        }
    }
}