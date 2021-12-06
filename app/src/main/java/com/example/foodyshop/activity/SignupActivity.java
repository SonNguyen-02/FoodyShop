package com.example.foodyshop.activity;

import static com.example.foodyshop.activity.EnterOtpActivity.ACTION_SIGN_UP;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.AuthSuccessDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText edtFullName, edtPhone, edtPassword, edtConfPassword;

    private ImageView imgCheck;
    private Button btnSignup;
    private boolean isValidPhone;

    private String mFullName;
    private String mPhoneNumber;
    private String mPassword;

    private FirebaseAuth mAuth;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initToolbar();
        initUi();

        mAuth = FirebaseAuth.getInstance();
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

        btnSignup.setOnClickListener(this::onClickSignUp);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sign up");
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void initUi() {
        ccp = findViewById(R.id.country_code_picker);
        edtFullName = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        imgCheck = findViewById(R.id.img_check);
        edtPassword = findViewById(R.id.edt_password);
        edtConfPassword = findViewById(R.id.edt_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
    }

    private void onClickSignUp(View v) {
        btnSignup.setEnabled(false);
        new Handler().postDelayed(() -> {
            btnSignup.setEnabled(true);
        }, TOAST_DEFAULT);
        String fullName = edtFullName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confPassword = edtConfPassword.getText().toString().trim();

        // validate full name
        if (fullName.isEmpty()) {
            edtFullName.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập tên của bạn!", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }
        // validate sdt
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập số điện thoại", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }
        if (!isValidPhone) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(getApplicationContext(), "Số điện thoại không đúng định dạng", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }

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
            ToastCustom.notice(this, "Mật khẩu không giống", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }

        mFullName = fullName;
        mPhoneNumber = ccp.getFullNumberWithPlus();
        mPassword = password;
        // prepare call api
        dialog = new LoadingDialog(this, "Đang kiểm tra ...");
        dialog.show();

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put(Const.KEY_PHONE, mPhoneNumber);

        String token = JWT.createToken(payloadMap, System.currentTimeMillis() + 60 * 1000);
        APIService.getService().checkAccountExists(token).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        dialog.setMessage("Đang gửi OPT ...");
                        verifyPhoneNumber(mPhoneNumber);
                    } else {
                        dialog.dismiss();
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR, TOAST_DEFAULT).show();
                    }
                } else {
                    dialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Có lỗi sảy ra. Vui lòng thử lại!", ToastCustom.ERROR, TOAST_DEFAULT).show();
                    Log.e("ddd", "onResponse: sever error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.WARNING, TOAST_DEFAULT).show();
            }
        });
    }

    private void verifyPhoneNumber(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            // OnVerificationStateChangedCallbacks

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                dialog.dismiss();
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                dialog.dismiss();
                                ToastCustom.notice(SignupActivity.this, "Verification False", ToastCustom.ERROR, TOAST_DEFAULT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                dialog.dismiss();
                                super.onCodeSent(verificationId, forceResendingToken);
                                goToEnterOtpActivity(phoneNumber, verificationId);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("ddd", "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        // Update UI
                        signUp();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.e("ddd", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            ToastCustom.notice(this, "The verification code entered was invalid.", ToastCustom.ERROR, TOAST_DEFAULT).show();
                        }
                    }
                });
    }

    private void goToEnterOtpActivity(String phoneNumber, String verificationId) {
        Intent intent = new Intent(this, EnterOtpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Const.KEY_ACTION, ACTION_SIGN_UP);
        bundle.putString(Const.KEY_NAME, mFullName);
        bundle.putString(Const.KEY_PHONE_CODE, ccp.getSelectedCountryCodeWithPlus());
        bundle.putString(Const.KEY_PHONE, phoneNumber);
        bundle.putString(Const.KEY_PASSWORD, mPassword);
        bundle.putString(Const.KEY_VERIFICATION_ID, verificationId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void signUp() {
        LoadingDialog dialog = new LoadingDialog(this, "Đang tải ...");
        dialog.show();
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put(Const.KEY_NAME, mFullName);
        payloadMap.put(Const.KEY_PHONE, mPhoneNumber);
        payloadMap.put(Const.KEY_PASSWORD, mPassword);
        String token = JWT.createToken(payloadMap, System.currentTimeMillis() + 60 * 1000);
        APIService.getService().register(token).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        String mess = "Đăng kí tài khoản thành công!";
                        showDialogSuccess(R.drawable.register_success, mess);
                    } else {
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR, TOAST_DEFAULT).show();
                    }
                } else {
                    Log.e("ddd", "onResponse: sever error");
                    ToastCustom.notice(getApplicationContext(), "Có lỗi sảy ra. Vui lòng thử lại!", ToastCustom.ERROR, TOAST_DEFAULT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT).show();
            }
        });
    }

    private void showDialogSuccess(int imgRes, String mess) {
        AuthSuccessDialog successDialog = new AuthSuccessDialog(this, imgRes, mess, () -> {
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        successDialog.show();
    }
}