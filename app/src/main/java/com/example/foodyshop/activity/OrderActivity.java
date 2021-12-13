package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_IS_CHECK_ALL;
import static com.example.foodyshop.config.Const.KEY_PHONE_CODE;
import static com.example.foodyshop.config.Const.KEY_TOTAL_MONEY;
import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.ProductOrderAdapter;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.fragment.OrderSuccessFragment;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.model.OrderDetailModel;
import com.example.foodyshop.model.OrderModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private RelativeLayout rlMainPage;
    private ImageView imgBack, imgCheck;
    private CountryCodePicker ccp;
    private EditText edtFullName, edtPhone, edtAddress, edtNote;
    private RecyclerView rcvProductOrder;
    private TextView tvTotalMoney;
    private Button btnOrder;

    private List<OrderDetailModel> mListOrderDetailChoose;
    private int mTotalMoney;
    private boolean isValidPhone;
    private boolean isSendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        CustomerModel mCustomer = Helper.getCurrentAccount();
        if (mCustomer == null) {
            finish();
            return;
        }

        initUi();
        initListProduct();

        imgBack.setOnClickListener(view -> finish());

        edtFullName.setText(mCustomer.getName());

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

        String phoneCode = Helper.getPrefVal(getApplicationContext(), KEY_PHONE_CODE);
        ccp.setCountryForPhoneCode(Integer.parseInt(phoneCode.replaceAll("\\D", "")));
        edtPhone.setText(mCustomer.getPhone().replace(phoneCode, ""));

        edtAddress.setText(mCustomer.getAddress());

        btnOrder.setOnClickListener(this::onClickOrder);
    }

    private void initUi() {
        rlMainPage = findViewById(R.id.rl_main_page);
        imgBack = findViewById(R.id.img_back);
        imgCheck = findViewById(R.id.img_check);
        ccp = findViewById(R.id.country_code_picker);
        edtFullName = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        edtNote = findViewById(R.id.edt_note);
        rcvProductOrder = findViewById(R.id.rcv_product_order);
        tvTotalMoney = findViewById(R.id.tv_total_money);
        btnOrder = findViewById(R.id.btn_order);
    }

    private void initListProduct() {

        mTotalMoney = getIntent().getIntExtra(KEY_TOTAL_MONEY, 0);
        tvTotalMoney.setText(PRICE_FORMAT.format(mTotalMoney));

        boolean isCheckAll = getIntent().getBooleanExtra(KEY_IS_CHECK_ALL, false);
        if (isCheckAll) {
            mListOrderDetailChoose = Helper.getAllProductInCart(getApplicationContext());
        } else {
            mListOrderDetailChoose = new ArrayList<>();
            for (OrderDetailModel ord : Helper.getAllProductInCart(getApplicationContext())) {
                if (ord.isChecked()) {
                    mListOrderDetailChoose.add(ord);
                }
            }
        }

        ProductOrderAdapter adapter = new ProductOrderAdapter(getApplicationContext(), mListOrderDetailChoose);
        rcvProductOrder.setAdapter(adapter);
        rcvProductOrder.setFocusable(false);
        rcvProductOrder.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rcvProductOrder.setLayoutManager(layoutManager);
    }

    private void onClickOrder(View view) {
        String name = edtFullName.getText().toString().trim();
        String phone = ccp.getFullNumberWithPlus();
        String address = edtAddress.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        if (name.isEmpty()) {
            edtFullName.requestFocus();
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập tên của bạn!", ToastCustom.WARNING).show();
            Helper.showKeyboard(getApplicationContext());
            return;
        }

        if (phone.isEmpty()) {
            edtPhone.requestFocus();
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập số điện thoại!", ToastCustom.WARNING).show();
            Helper.showKeyboard(getApplicationContext());
            return;
        }

        if (!isValidPhone) {
            edtPhone.requestFocus();
            ToastCustom.notice(getApplicationContext(), "Số điện thoại không hợp lệ.", ToastCustom.ERROR).show();
            Helper.showKeyboard(getApplicationContext());
            return;
        }

        if (address.isEmpty()) {
            edtAddress.requestFocus();
            ToastCustom.notice(getApplicationContext(), "Vui lòng nhập địa chỉ nhận hàng!", ToastCustom.WARNING).show();
            Helper.showKeyboard(getApplicationContext());
            return;
        }

        ConfirmDialog cfDialog = new ConfirmDialog(this, "Xác nhận đặt hàng?", confirmDialog -> {
            confirmDialog.dismiss();
            OrderModel order = new OrderModel();
            order.setName(name);
            order.setPhone(phone);
            order.setAddress(address);
            order.setNote(note);
            order.setTotalMoney(mTotalMoney);
            order.setOrderDetails(mListOrderDetailChoose);
            sendOrder(order);
        });
        cfDialog.show();
    }

    private void sendOrder(@NonNull OrderModel order) {

        Gson gson = new Gson();
        String orderJson = gson.toJson(order);

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.show();
        APIService.getService().sendOrder(Helper.getTokenLogin(getApplicationContext()), orderJson).enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Respond res = response.body();
                    if (res.isSuccess()) {
                        isSendOrder = true;
                        order.setOrderCode(res.getMsg());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_root, new OrderSuccessFragment(order));
                        transaction.commit();
                        rlMainPage.setVisibility(View.GONE);
                    } else {
                        ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                    }
                } else {
                    ToastCustom.notice(getApplicationContext(), "Đã có lỗi sảy ra! Vui lòng thử lại.", ToastCustom.ERROR).show();
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
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (isSendOrder) {
            setResult(RESULT_OK);
        }
        finish();
    }
}