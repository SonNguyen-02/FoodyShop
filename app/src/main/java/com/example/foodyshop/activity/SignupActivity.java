package com.example.foodyshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
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
    private EditText edtFullName, edtPhone;
    private ImageView imgCheck;
    private Button btnNext;
    private boolean isValidPhone;

    private String fullName;
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

        btnNext.setOnClickListener(this::onClickBtnNext);
    }

    private void initToolbar(){
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
        btnNext = findViewById(R.id.button_next);
    }

    private void onClickBtnNext(View v){
        fullName = edtFullName.getText().toString().trim();

        if(fullName.isEmpty()){
            edtFullName.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPhone){
            edtPhone.requestFocus();
            Helper.showKeyboard(getApplicationContext());
            Toast.makeText(this, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // prepare call api
        dialog = new LoadingDialog(this, "Đang kiểm tra ...");
        dialog.show();

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put(Const.KEY_PHONE, ccp.getFullNumberWithPlus());

        String token = JWT.createToken(payloadMap, System.currentTimeMillis() + 60 * 1000);
        APIService.getService().checkAccountExists(token).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {
                if (response.isSuccessful() && response.body() != null){
                    Respond res = response.body();
                    if(res.isSuccess()){
                        verifyPhoneNumber(ccp.getFullNumberWithPlus());
                    }else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    Log.e("ddd", "onResponse: sever error");
                }
            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                dialog.dismiss();
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
                                Toast.makeText(SignupActivity.this, "Verification False", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                dialog.dismiss();
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
        bundle.putInt(Const.KEY_TYPE_CHANGE_PASSWORD, EnterPasswordActivity.TYPE_SIGN_UP);
        bundle.putString(Const.KEY_FULL_NAME, fullName);
        bundle.putString(Const.KEY_PHONE_CODE, ccp.getSelectedCountryCodeWithPlus());
        bundle.putString(Const.KEY_PHONE_NUMBER, phoneNumber);
        bundle.putString(Const.KEY_VERIFICATION_ID, verificationId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goToEnterPasswordActivity() {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Const.KEY_TYPE_CHANGE_PASSWORD, EnterPasswordActivity.TYPE_SIGN_UP);
        bundle.putString(Const.KEY_FULL_NAME, fullName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}