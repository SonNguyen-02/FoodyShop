package com.example.foodyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.helper.Helper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtpActivity extends AppCompatActivity {

    private TextView tvPhone, tvResendOtp;
    private EditText edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6;
    private View currentFocus;
    private Button btnResendOtp, btnVerify;

    private FirebaseAuth mAuth;

    private int mTypeChangePassword;

    private String mToken;
    private String mFullname;

    private String mPhoneCode;
    private String mPhoneNumber;
    private String mVerificationId;

    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        initToolbar();
        initUI();
        mAuth = FirebaseAuth.getInstance();
        getDataIntent();
        setUpOTPInputs();
        tvPhone.setText(mPhoneNumber.replace(mPhoneCode, "(" + mPhoneCode + ") "));
        initSendOtp();
        btnVerify.setOnClickListener(v -> verifyCode());
        btnResendOtp.setOnClickListener(v -> resendOtp());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTypeChangePassword = bundle.getInt(Const.KEY_TYPE_CHANGE_PASSWORD);
            mPhoneCode = bundle.getString(Const.KEY_PHONE_CODE);
            mPhoneNumber = bundle.getString(Const.KEY_PHONE_NUMBER);
            mVerificationId = bundle.getString(Const.KEY_VERIFICATION_ID);
            if (mTypeChangePassword == EnterPasswordActivity.TYPE_FORGOT_PASSWORD) {
                mToken = bundle.getString(Const.KEY_TOKEN);
            }
            if (mTypeChangePassword == EnterPasswordActivity.TYPE_SIGN_UP) {
                mFullname = bundle.getString(Const.KEY_FULL_NAME);
            }
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Enter OTP");
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }
    private void initUI() {
        tvPhone = findViewById(R.id.tv_phone);
        tvResendOtp = findViewById(R.id.tv_resend_otp);
        edtCode1 = findViewById(R.id.edt_code1);
        edtCode2 = findViewById(R.id.edt_code2);
        edtCode3 = findViewById(R.id.edt_code3);
        edtCode4 = findViewById(R.id.edt_code4);
        edtCode5 = findViewById(R.id.edt_code5);
        edtCode6 = findViewById(R.id.edt_code6);
        currentFocus = edtCode1;
        btnResendOtp = findViewById(R.id.btn_resend_otp);
        btnVerify = findViewById(R.id.btn_verify);
    }

    private void setUpOTPInputs() {
        EditText[] listEdt = new EditText[]{edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6};
        int i = 0;
        for (EditText edt : listEdt) {
            edt.setOnFocusChangeListener(this::onFocusChange);
            if (i + 1 != listEdt.length) {
                edt.addTextChangedListener(new GenericTextWatcher(listEdt[i + 1]));
            }
            if (i - 1 >= 0) {
                edt.setOnKeyListener(new GenericKeyEvent(edt, listEdt[i - 1]));
            }
            i++;
        }
    }

    private void initSendOtp() {
        tvResendOtp.setVisibility(View.VISIBLE);
        btnResendOtp.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            int timer = 60;

            @Override
            public void run() {
                if (timer == 0) {
                    tvResendOtp.setVisibility(View.GONE);
                    btnResendOtp.setVisibility(View.VISIBLE);
                    return;
                }
                tvResendOtp.setText("Resend OTP (" + timer-- + "s)");
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void onFocusChange(View view, boolean b) {
        if (b) {
            currentFocus = view;
        }
    }

    static class GenericKeyEvent implements View.OnKeyListener {

        private final EditText currentView, prevView;

        public GenericKeyEvent(EditText currentView, EditText prevView) {
            this.currentView = currentView;
            this.prevView = prevView;
        }

        @Override
        public boolean onKey(View view, int i, @NonNull KeyEvent keyEvent) {
            int action = keyEvent.getAction();
            int code = keyEvent.getKeyCode();
            if (action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_DEL) {
                if (currentView.getText().toString().trim().isEmpty()) {
                    prevView.setText("");
                    prevView.requestFocus();
                } else {
                    currentView.setText("");
                }
                return true;
            }
            return false;
        }
    }

    static class GenericTextWatcher implements TextWatcher {

        private final EditText nextView;

        public GenericTextWatcher(EditText nextView) {
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
            if (!charSequence.toString().trim().isEmpty()) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void verifyCode() {
        Helper.hideKeyboard(this, currentFocus);
        if (isEmpty(edtCode1)
                || isEmpty(edtCode2)
                || isEmpty(edtCode3)
                || isEmpty(edtCode4)
                || isEmpty(edtCode5)
                || isEmpty(edtCode6)) {
            Toast.makeText(this, "Vui lòng nhập mã hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String otp = edtCode1.getText().toString() +
                edtCode2.getText().toString() +
                edtCode3.getText().toString() +
                edtCode4.getText().toString() +
                edtCode5.getText().toString() +
                edtCode6.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private boolean isEmpty(@NonNull EditText edt) {
        return edt.getText().toString().trim().isEmpty();
    }

    private void resendOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            // OnVerificationStateChangedCallbacks
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                initSendOtp();
                                Toast.makeText(EnterOtpActivity.this, "Verification False", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                initSendOtp();
                                mVerificationId = verificationId;
                                mForceResendingToken = forceResendingToken;
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


    private void goToEnterPasswordActivity() {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Const.KEY_TYPE_CHANGE_PASSWORD, mTypeChangePassword);
        if(mTypeChangePassword == EnterPasswordActivity.TYPE_FORGOT_PASSWORD){
            bundle.putString(Const.KEY_TOKEN, mToken);
        }
        if(mTypeChangePassword == EnterPasswordActivity.TYPE_SIGN_UP){
            bundle.putString(Const.KEY_FULL_NAME, mFullname);
            bundle.putString(Const.KEY_PHONE_NUMBER, mPhoneNumber);
        }

        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}