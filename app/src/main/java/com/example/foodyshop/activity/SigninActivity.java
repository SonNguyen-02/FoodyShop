package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_NEW_TASK;
import static com.example.foodyshop.config.Const.KEY_PHONE;
import static com.example.foodyshop.config.Const.KEY_PHONE_CODE;
import static com.example.foodyshop.config.Const.KEY_SIGN_IN_OK;
import static com.example.foodyshop.config.Const.KEY_TOTAL_MONEY;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText edtPhone, edtPassword;
    private ImageView imgCheck;
    private Button btnSignin, btnSignup, btnForgotPassword;
    private boolean isValidPhone;
    private boolean isNewTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        isNewTask = getIntent().getBooleanExtra(KEY_NEW_TASK, false);

        initUi();
        // attach CarrierNumber editText to CCP
        ccp.registerCarrierNumberEditText(edtPhone);

        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> {
            isValidPhone = isValidNumber;
            if (isValidNumber) {
                imgCheck.setImageResource(R.drawable.ic_valid);
            } else {
                imgCheck.setImageResource(R.drawable.ic_invalid);
            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().trim();
                if (input.isEmpty()) {
                    imgCheck.setVisibility(View.GONE);
                } else {
                    imgCheck.setVisibility(View.VISIBLE);
                }
            }
        });
        btnSignin.setOnClickListener(v -> login());
        btnSignup.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignupActivity.class)));
        btnForgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String mPhoneCode = bundle.getString(KEY_PHONE_CODE);
            String mPhoneNumber = bundle.getString(KEY_PHONE);
            if (mPhoneCode != null && mPhoneNumber != null) {
                ccp.setCountryForPhoneCode(Integer.parseInt(mPhoneCode.replaceAll("\\D", "")));
                edtPhone.setText(mPhoneNumber.replace(mPhoneCode, ""));
            }
        }
    }

    private void initUi() {
        ccp = findViewById(R.id.country_code_picker);
        edtPhone = findViewById(R.id.edt_phone);

        edtPassword = findViewById(R.id.edt_password);
        imgCheck = findViewById(R.id.img_check);

        btnSignin = findViewById(R.id.btn_signin);
        btnSignup = findViewById(R.id.btn_signup);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
    }

    private void login() {
        btnSignin.setEnabled(false);
        new Handler().postDelayed(() -> {
            btnSignin.setEnabled(true);
        }, TOAST_DEFAULT);
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(this, "Vui lòng nhập số điện thoại", ToastCustom.WARNING).show();
            return;
        }
        if (!isValidPhone) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(this, "Vui lòng nhập đúng số điện thoại", ToastCustom.WARNING).show();
            return;
        }
        if (Helper.isInvalidPassword(getApplicationContext(), edtPassword, false)) {
            return;
        }
        final LoadingDialog dialog = new LoadingDialog(this);
        dialog.show();

        String phone = ccp.getFullNumberWithPlus();
        String password = edtPassword.getText().toString().trim();

        Map<String, Object> mMap = new HashMap<>();
        mMap.put(Const.KEY_PHONE, phone);
        mMap.put(Const.KEY_PASSWORD, password);

        String tokenSend = JWT.createToken(mMap, System.currentTimeMillis() + 60 * 1000);
        APIService.getService().login(tokenSend).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        // lưu token mới khi đăng nhập thành công
                        SharedPreferences.Editor editor = Helper.getSharedPreferences(getApplicationContext()).edit();
                        editor.putString(Const.KEY_TOKEN_LOGIN, res.getMsg());
                        editor.putString(Const.KEY_PHONE_CODE, ccp.getSelectedCountryCodeWithPlus());
                        editor.apply();
                        Helper.saveUserInfo(getApplicationContext(), res.getMsg(), isSuccessful -> {
                            dialog.dismiss();
                            if (isSuccessful) {
                                if (isNewTask) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent();
                                    intent.putExtra(KEY_SIGN_IN_OK, true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            } else {
                                ToastCustom.notice(getApplicationContext(), "Đã xảy ra lỗi. Vui lòng thử lại!", ToastCustom.WARNING).show();
                            }
                        });
                    } else {
                        // Show lỗi khi đăng nhập false
                        dialog.dismiss();
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                        Log.e("ddd", "onResponse: " + res.getMsg());
                    }
                } else {
                    dialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Có lỗi. Vui lòng thử lại sau!", ToastCustom.ERROR).show();
                    Log.e("ddd", "onResponse: " + "server error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                Log.e("ddd", "onFailure: ");
            }
        });

    }

}