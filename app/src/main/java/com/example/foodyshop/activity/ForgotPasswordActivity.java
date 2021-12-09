package com.example.foodyshop.activity;

import static com.example.foodyshop.activity.EnterOtpActivity.ACTION_FORGOT_PASSWORD;
import static com.example.foodyshop.config.Const.KEY_PHONE;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText edtPhone;
    private ImageView imgCheck;
    private Button btnSendOtp;
    private boolean isValidPhone;

    private FirebaseAuth mAuth;
    private String token;

    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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

        btnSendOtp.setOnClickListener(v -> checkPhoneUser());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot password");
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void initUi() {
        ccp = findViewById(R.id.country_code_picker);
        edtPhone = findViewById(R.id.edt_phone);
        imgCheck = findViewById(R.id.img_check);
        btnSendOtp = findViewById(R.id.btn_send_otp);
    }

    private void checkPhoneUser() {
        btnSendOtp.setEnabled(false);
        new Handler().postDelayed(() -> {
            btnSendOtp.setEnabled(true);
        }, TOAST_DEFAULT);
        // validate
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập số điện thoại", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }
        if (!isValidPhone) {
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            ToastCustom.notice(getApplicationContext(), "Số điện thoại không đúng định dạng", ToastCustom.ERROR, TOAST_DEFAULT).show();
            return;
        }
        Helper.hideKeyboard(getApplicationContext(), edtPhone);
        // prepare call api
        dialog = new LoadingDialog(this, "Đang kiểm tra ...");
        dialog.show();
        // call api & check phone is register ?
        Map<String, Object> payloadMap = new HashMap<>();
        // get phone number
        payloadMap.put(KEY_PHONE, ccp.getFullNumberWithPlus());
        // create token to call api
        String tokenSend = JWT.createToken(payloadMap, System.currentTimeMillis() + 60 * 1000);
        APIService.getService().forgotPassword(tokenSend).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        // verify phone
                        token = res.getMsg();
                        dialog.setMessage("Đang gửi OPT ...");
                        verifyPhoneNumber(ccp.getFullNumberWithPlus());
                    } else {
                        dialog.dismiss();
                        ToastCustom.notice(ForgotPasswordActivity.this, res.getMsg(), ToastCustom.ERROR, TOAST_DEFAULT).show();
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
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT).show();
                Log.e("ddd", "onFailure: sever error");
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
                                Toast.makeText(ForgotPasswordActivity.this, "Verification False", Toast.LENGTH_SHORT).show();
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
                        goToEnterPasswordActivity();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.e("ddd", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(this, "The verification code entered was invalid.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToEnterOtpActivity(String phoneNumber, String verificationId) {
        Intent intent = new Intent(this, EnterOtpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Const.KEY_ACTION, ACTION_FORGOT_PASSWORD);
        bundle.putString(Const.KEY_TOKEN, token);
        bundle.putString(Const.KEY_PHONE_CODE, ccp.getSelectedCountryCodeWithPlus());
        bundle.putString(KEY_PHONE, phoneNumber);
        bundle.putString(Const.KEY_VERIFICATION_ID, verificationId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goToEnterPasswordActivity() {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Const.KEY_ACTION, ACTION_FORGOT_PASSWORD);
        bundle.putString(Const.KEY_TOKEN, token);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}