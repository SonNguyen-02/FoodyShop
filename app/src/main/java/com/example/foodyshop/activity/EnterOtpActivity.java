package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_PHONE;
import static com.example.foodyshop.config.Const.KEY_PHONE_CODE;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

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
import com.example.foodyshop.dialog.AuthSuccessDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOtpActivity extends AppCompatActivity {

    public static final int ACTION_FORGOT_PASSWORD = 0;
    public static final int ACTION_SIGN_UP = 1;

    private TextView tvPhone, tvResendOtp;
    private EditText edtCode1, edtCode2, edtCode3, edtCode4, edtCode5, edtCode6;
    private EditText currentFocus;
    private Button btnResendOtp, btnVerify;

    private FirebaseAuth mAuth;

    private int mAction;

    private String mToken;
    private String mFullName;
    private String mPhoneNumber;
    private String mPhoneCode;
    private String mPassword;

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
        initSendOtp();
        btnVerify.setOnClickListener(v -> verifyCode());
        btnResendOtp.setOnClickListener(v -> {
            btnResendOtp.setEnabled(false);
            resendOtp();
        });
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAction = bundle.getInt(Const.KEY_ACTION);

            mPhoneCode = bundle.getString(Const.KEY_PHONE_CODE);
            mPhoneNumber = bundle.getString(Const.KEY_PHONE);
            mVerificationId = bundle.getString(Const.KEY_VERIFICATION_ID);
            if (mAction == ACTION_SIGN_UP) {
                mFullName = bundle.getString(Const.KEY_NAME);
                mPassword = bundle.getString(Const.KEY_PASSWORD);
            }
            if (mAction == ACTION_FORGOT_PASSWORD) {
                mToken = bundle.getString(Const.KEY_TOKEN);
            }
            tvPhone.setText(mPhoneNumber.replace(mPhoneCode, "(" + mPhoneCode + ") "));
        }
    }

    private void initToolbar() {
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
            if (i == 0) {
                edt.addTextChangedListener(new GenericTextWatcher(null, edt));
            } else {
                edt.addTextChangedListener(new GenericTextWatcher(listEdt[i - 1], edt));
            }
            if (i != listEdt.length - 1) {
                edt.setOnKeyListener(new GenericKeyEvent(listEdt[i + 1]));
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
                    btnResendOtp.setEnabled(true);
                    return;
                }
                tvResendOtp.setText(MessageFormat.format("Resend OTP ({0}s)", timer--));
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void onFocusChange(View view, boolean b) {
        if (b) {
            currentFocus = (EditText) view;
        }
    }

    static class GenericKeyEvent implements View.OnKeyListener {

        private final EditText nextView;

        public GenericKeyEvent(EditText nextView) {
            this.nextView = nextView;
        }

        @Override
        public boolean onKey(View view, int i, @NonNull KeyEvent keyEvent) {
            EditText currentView = (EditText) view;
            int action = keyEvent.getAction();
            int code = keyEvent.getKeyCode();
//            Log.e("ddd", "onKey: Action " + action + " code: " + code);
            if (action == KeyEvent.ACTION_DOWN && code >= KeyEvent.KEYCODE_0 && code <= KeyEvent.KEYCODE_9) {
                if (currentView.getText().toString().trim().isEmpty()) {
                    currentView.setText(String.valueOf(code - 7));
                } else {
                    if (nextView.getText().toString().trim().isEmpty()) {
                        nextView.setText(String.valueOf(code - 7));
                    }
                }
                nextView.requestFocus(nextView.getText().length());
                return true;
            }
            return false;
        }
    }

    static class GenericTextWatcher implements TextWatcher {

        private boolean isDelete;
        private final EditText prevView;
        private final EditText currentView;

        public GenericTextWatcher(EditText prevView, EditText currentView) {
            this.prevView = prevView;
            this.currentView = currentView;
        }

        @Override
        public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString();
            if (!s.isEmpty()) {
                new Handler().postDelayed(() -> {
                    if (!s.matches("\\d")) {
                        isDelete = true;
                        currentView.setText("");
                    }
                }, 100);
            } else {
                if (!isDelete && prevView != null) {
                    prevView.requestFocus(prevView.getText().length());
                } else {
                    isDelete = false;
                }
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
            ToastCustom.notice(this, "Vui lòng nhập mã hợp lệ", ToastCustom.ERROR).show();
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
        String message = "Đang gửi lại OTP . . .";
        LoadingDialog dialog = new LoadingDialog(this, message);
        dialog.show();
        new Handler().postDelayed(() -> {
            initSendOtp();
            dialog.dismiss();
        }, 2000);

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
                                Toast.makeText(EnterOtpActivity.this, "Verification False", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                                mForceResendingToken = forceResendingToken;
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("ddd", "signInWithCredential:success");

                        //FirebaseUser user = task.getResult().getUser();
                        // Update UI
                        onVerifySuccess();
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

    private void onVerifySuccess() {
        if (mAction == ACTION_FORGOT_PASSWORD) {
            Intent intent = new Intent(this, EnterPasswordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Const.KEY_TOKEN, mToken);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (mAction == ACTION_SIGN_UP) {
            signUp();
        }
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
                        AuthSuccessDialog successDialog = new AuthSuccessDialog(EnterOtpActivity.this, R.drawable.register_success, mess, () -> {
                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(KEY_PHONE, mPhoneNumber);
                            bundle.putString(KEY_PHONE_CODE, mPhoneCode);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                        successDialog.show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                    }
                } else {
                    Log.e("ddd", "onResponse: sever error");
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.ERROR).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                Log.e("ddd", "onFailure: ", t);
                dialog.dismiss();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.ERROR).show();
            }
        });
    }


}