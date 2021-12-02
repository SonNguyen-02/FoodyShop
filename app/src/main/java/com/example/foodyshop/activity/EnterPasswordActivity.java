package com.example.foodyshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.AuthSuccessDialog;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPasswordActivity extends AppCompatActivity {

    public static final int TYPE_FORGOT_PASSWORD = 0;
    public static final int TYPE_SIGN_UP = 1;

    private int mTypeChangePassword;
    private String mToken;
    private String mFullName;
    private String mPhoneNumber;

    private EditText edtPassword, edtConfPassword;
    private Button btnConfirm;
    private TextView tvTerms;
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
            mTypeChangePassword = bundle.getInt(Const.KEY_TYPE_CHANGE_PASSWORD);
            if (mTypeChangePassword == TYPE_FORGOT_PASSWORD) {
                mToken = bundle.getString(Const.KEY_TOKEN);
                btnConfirm.setText(R.string.button_change_password);
                tvTerms.setVisibility(View.GONE);
            }
            if (mTypeChangePassword == TYPE_SIGN_UP) {
                btnConfirm.setText(R.string.button_sign_up);
                mFullName = bundle.getString(Const.KEY_FULL_NAME);
                mPhoneNumber = bundle.getString(Const.KEY_PHONE_NUMBER);
            }
        }
    }

    private void initUI() {
        edtPassword = findViewById(R.id.edt_password);
        edtConfPassword = findViewById(R.id.edt_confirm_password);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvTerms = findViewById(R.id.tv_terms);
    }

    private void onClickBtnConfirm(View v) {
        String password = edtPassword.getText().toString().trim();
        String confPassword = edtConfPassword.getText().toString().trim();
        if (password.isEmpty()) {
            edtPassword.requestFocus();
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.matches(Const.PASSWORD_REGEX)) {
            edtPassword.requestFocus();
            Toast.makeText(this, "Mật khẩu tối thiểu 8 kí tự, có ít nhất 1 chữ và 1 số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confPassword.isEmpty()) {
            edtConfPassword.requestFocus();
            Toast.makeText(this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!confPassword.matches(Const.PASSWORD_REGEX)) {
            edtConfPassword.requestFocus();
            Toast.makeText(this, "Mật khẩu tối thiểu 8 kí tự, có ít nhất 1 chữ và 1 số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confPassword)) {
            edtConfPassword.requestFocus();
            edtConfPassword.selectAll();
            Toast.makeText(this, "Mật khẩu không giống", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mTypeChangePassword == TYPE_FORGOT_PASSWORD) {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            APIService.getService().changePasswordOTP(mToken, password).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(Call<Respond> call, Response<Respond> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Respond res = response.body();
                        if (res.isSuccess()) {
                            String mess = "Đổi mật khẩu thành công!";
                            showDialogSuccess(R.drawable.resetpw_success, mess);
                        }else{
                            Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Có lỗi sảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        Log.e("ddd", "onResponse: sever error");
                    }
                }

                @Override
                public void onFailure(Call<Respond> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }
        if (mTypeChangePassword == TYPE_SIGN_UP) {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put(Const.KEY_NAME, mFullName);
            payloadMap.put(Const.KEY_PHONE, mPhoneNumber);
            payloadMap.put(Const.KEY_PASSWORD, password);
            String token = JWT.createToken(payloadMap, System.currentTimeMillis() + 60 * 1000);
            APIService.getService().register(token).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(Call<Respond> call, Response<Respond> response) {
                    dialog.dismiss();
                    if(response.isSuccessful() && response.body() != null){
                        Respond res = response.body();
                        if (res.isSuccess()){
                            String mess = "Đăng kí tài khoản thành công!";
                            showDialogSuccess(R.drawable.register_success, mess);
                        }else{
                            Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.e("ddd", "onResponse: sever error");
                        Toast.makeText(getApplicationContext(), "Có lỗi sảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Respond> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void showDialogSuccess(int imgRes, String mess){
        AuthSuccessDialog successDialog = new AuthSuccessDialog(EnterPasswordActivity.this, imgRes, mess, () -> {
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        successDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isBackPress) {
            isBackPress = false;
            super.onBackPressed();
        } else {
            isBackPress = true;
            Toast.makeText(this, "Nhấn back thêm lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
    }
}