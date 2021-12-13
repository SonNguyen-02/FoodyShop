package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_ACTION;
import static com.example.foodyshop.config.Const.KEY_ORDER;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;
import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;
import static com.example.foodyshop.helper.Helper.parseDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.OrderDetailExpandAdapter;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.FeedbackDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.fragment.FeedbackFragment;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.FeedbackModel;
import com.example.foodyshop.model.OrderDetailModel;
import com.example.foodyshop.model.OrderModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailExpandAdapter.IOnClickCallback {

    public static final int ACTION_NONE = 0;
    public static final int ACTION_CANCEL = 1;
    public static final int ACTION_CONFIRM = 2;

    private OrderModel mOrder;

    private ImageView imgBack;
    private NestedScrollView scrollView;
    private RelativeLayout rlMainPage, rlLoading;

    private TextView tvFullname, tvPhone, tvAddress, tvNote, tvDateCreate, tvOrderCode, tvShipPrice, tvTotalProduct, tvTotalMoney;
    private Button btnBuyAgain, btnCancel, btnConfirm;
    private LinearLayout llBuyAgain, llCancelConfOrder;

    private RecyclerView rcvOrderDetail;
    private OrderDetailExpandAdapter mOrderDetailExpandAdapter;

    private int action = ACTION_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initUi();
            mOrder = (OrderModel) bundle.getSerializable(KEY_ORDER);
            if (mOrder == null) {
                finish();
            }
            initPage();
            if (mOrder.getStatus() == 5) {
                startLoading();
                initFeedbackData();
            } else {
                stopLoading();
                mOrderDetailExpandAdapter = new OrderDetailExpandAdapter(getApplicationContext(), mOrder.getOrderDetails());
                rcvOrderDetail.setAdapter(mOrderDetailExpandAdapter);
                rcvOrderDetail.setFocusable(false);
                rcvOrderDetail.setNestedScrollingEnabled(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rcvOrderDetail.setLayoutManager(layoutManager);
            }
        } else {
            finish();
        }
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        scrollView = findViewById(R.id.nested_scroll);
        rlMainPage = findViewById(R.id.rl_main_page);
        rlLoading = findViewById(R.id.rl_loading);

        tvFullname = findViewById(R.id.tv_fullname);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvNote = findViewById(R.id.tv_note);
        tvDateCreate = findViewById(R.id.tv_date_create);
        tvOrderCode = findViewById(R.id.tv_order_code);
        tvShipPrice = findViewById(R.id.tv_ship_price);
        tvTotalProduct = findViewById(R.id.tv_total_product);
        tvTotalMoney = findViewById(R.id.tv_total_money);

        btnBuyAgain = findViewById(R.id.btn_buy_again);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);

        llBuyAgain = findViewById(R.id.ll_buy_again);
        llCancelConfOrder = findViewById(R.id.ll_cancel_conf_order);

        rcvOrderDetail = findViewById(R.id.rcv_order_detail);
    }

    private void initPage() {
        tvFullname.setText(mOrder.getName());
        tvPhone.setText(mOrder.getPhone());
        tvAddress.setText(mOrder.getAddress());
        tvNote.setText(mOrder.getNote());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm: dd/MM/yyyy");
        tvDateCreate.setText(dateFormat.format(new Date(parseDate(mOrder.getCreated()))));
        tvOrderCode.setText(mOrder.getOrderCode());
        tvTotalProduct.setText(MessageFormat.format("Tổng " + getString(R.string.total_product), mOrder.getTotalProduct()));
        tvTotalMoney.setText(PRICE_FORMAT.format(mOrder.getTotalMoney()));
        int status = mOrder.getStatus();
        switch (status) {
            case 0:
                tvShipPrice.setText("Đang cập nhập");
                showCancelConfirmBox();
                btnConfirm.setVisibility(View.GONE);
                break;
            case 1:
                if (mOrder.getShipPrice() != 0) {
                    tvShipPrice.setText(Helper.PRICE_FORMAT.format(mOrder.getShipPrice()));
                } else {
                    tvShipPrice.setText("Đang cập nhập");
                }
                showCancelConfirmBox();
                btnConfirm.setVisibility(View.GONE);
                break;
            case 2:
                tvShipPrice.setText(Helper.PRICE_FORMAT.format(mOrder.getShipPrice()));
                showCancelConfirmBox();
                btnConfirm.setVisibility(View.VISIBLE);
                break;
            case 3:
            case 4:
            case 5:
                tvShipPrice.setText(Helper.PRICE_FORMAT.format(mOrder.getShipPrice()));
                showBuyAgainBox();
                break;
            case -1:
            case -2:
                if (mOrder.getShipPrice() != 0) {
                    tvShipPrice.setText(Helper.PRICE_FORMAT.format(mOrder.getShipPrice()));
                } else {
                    tvShipPrice.setText("Chưa cập nhập");
                }
                showBuyAgainBox();
                break;
        }
        imgBack.setOnClickListener(view -> onBackPressed());
        btnBuyAgain.setOnClickListener(this::onClickBuyAgain);
        btnCancel.setOnClickListener(view -> cancelOrder());
        btnConfirm.setOnClickListener(view -> confirmOrder());
    }

    private void onClickBuyAgain(View view) {
        Helper.buyAgain(getApplicationContext(), mOrder);
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    public void showBuyAgainBox() {
        llBuyAgain.setVisibility(View.VISIBLE);
        llCancelConfOrder.setVisibility(View.GONE);
    }

    public void showCancelConfirmBox() {
        llBuyAgain.setVisibility(View.GONE);
        llCancelConfOrder.setVisibility(View.VISIBLE);
    }

    private void startLoading() {
        rlLoading.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    private void stopLoading() {
        rlLoading.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void initFeedbackData() {
        Gson gson = new Gson();
        APIService.getService().getFeedbackInOrder(Helper.getTokenLogin(getApplicationContext()), gson.toJson(mOrder)).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderModel> call, @NonNull Response<OrderModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mOrder.setOrderDetails(response.body().getOrderDetails());
                    mOrderDetailExpandAdapter = new OrderDetailExpandAdapter(getApplicationContext(), mOrder.getOrderDetails(), mOrder.getTime(), OrderDetailActivity.this);
                    rcvOrderDetail.setAdapter(mOrderDetailExpandAdapter);
                    rcvOrderDetail.setFocusable(false);
                    rcvOrderDetail.setNestedScrollingEnabled(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rcvOrderDetail.setLayoutManager(layoutManager);
                    stopLoading();
                } else {
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderModel> call, @NonNull Throwable t) {
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT).show();
            }
        });
    }

    public void cancelOrder() {
        String message = "Nhấn xác nhận để hủy đơn hàng " + mOrder.getOrderCode();
        ConfirmDialog dialog = new ConfirmDialog(this, message, confirmDialog -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            APIService.getService().cancelOrder(Helper.getTokenLogin(getApplicationContext()), mOrder.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Respond res = response.body();
                        if (res.isSuccess()) {
                            action = ACTION_CANCEL;
                            mOrder.setStatus(-2);
                            showBuyAgainBox();
                            ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                        } else {
                            ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                        }
                    } else {
                        ToastCustom.notice(getApplicationContext(), "Hủy đơn hàng thất bại. Vui lòng thử lại sau", ToastCustom.WARNING).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            });
        });
        dialog.show();
    }

    public void confirmOrder() {
        String message = "Xác nhận giao hàng với giá ship " + Helper.PRICE_FORMAT.format(mOrder.getShipPrice());
        ConfirmDialog dialog = new ConfirmDialog(this, message, confirmDialog -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            APIService.getService().confirmBuy(Helper.getTokenLogin(getApplicationContext()), mOrder.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Respond res = response.body();
                        if (res.isSuccess()) {
                            action = ACTION_CONFIRM;
                            mOrder.setStatus(3);
                            showBuyAgainBox();
                            ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                        } else {
                            ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                        }
                    } else {
                        ToastCustom.notice(getApplicationContext(), "Xác nhận thất bại. Vui lòng thử lại sau", ToastCustom.WARNING).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            });
        });
        dialog.show();
    }

    @Override
    public void onClickShowAddFeedbackDialog(int position, OrderDetailModel orderDetail) {
        FeedbackDialog.addFeedback(this, (mFbDialog, content) -> {
            if (content.isEmpty()) {
                ToastCustom.notice(getApplicationContext(), "Hãy nhập đánh giá", ToastCustom.WARNING).show();
            } else {
                mFbDialog.dismiss();
                LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                String token = Helper.getTokenLogin(getApplicationContext());
                FeedbackModel feedback = new FeedbackModel();
                feedback.setCustomerId(Helper.getCurrentAccount().getId());
                feedback.setProductId(orderDetail.getProductId());
                feedback.setOrderDetailId(orderDetail.getId());
                feedback.setContent(content);
                Gson gson = new Gson();
                String feedbackJson = gson.toJson(feedback);
                APIService.getService().addFeedback(token, feedbackJson).enqueue(new Callback<FeedbackModel>() {
                    @Override
                    public void onResponse(@NonNull Call<FeedbackModel> call, @NonNull Response<FeedbackModel> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            orderDetail.setFeedback(response.body());
                            mOrderDetailExpandAdapter.notifyItemChanged(position);
                            onClickSeeFeedback(position, orderDetail);
                            ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thành công!", ToastCustom.SUCCESS).show();
                        } else {
                            ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thất bại", ToastCustom.ERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FeedbackModel> call, @NonNull Throwable t) {
                        loadingDialog.dismiss();
                        ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thất bại", ToastCustom.ERROR).show();
                    }
                });
            }
        }).show();
    }

    @Override
    public void onClickSeeFeedback(int position, OrderDetailModel orderDetail) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_out);
        transaction.add(R.id.frame_root, new FeedbackFragment(position, orderDetail));
        transaction.addToBackStack(FeedbackFragment.class.getSimpleName());
        transaction.commit();
        new Handler().postDelayed(() -> rlMainPage.setVisibility(View.GONE), 300);
    }

    public void updateItemOrderDetail(int position, FeedbackModel feedback) {
        mOrderDetailExpandAdapter.getListOrderDetail().get(position).setFeedback(feedback);
        mOrderDetailExpandAdapter.notifyItemChanged(position);
    }

    @Override
    public void onBackPressed() {
        if (action != ACTION_NONE) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_ACTION, action);
            bundle.putSerializable(KEY_ORDER, mOrder);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                rlMainPage.setVisibility(View.VISIBLE);
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

}