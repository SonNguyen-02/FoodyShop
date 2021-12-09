package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_FROM_BUY_AGAIN;
import static com.example.foodyshop.config.Const.KEY_FROM_CART;
import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.KEY_RELOAD_CART;
import static com.example.foodyshop.config.Const.KEY_SELECT_TAP_ALL;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.FeedbackAdapter;
import com.example.foodyshop.dialog.AddToCardBottomSheetDialogFragment;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.FeedbackDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.FeedbackModel;
import com.example.foodyshop.model.OrderDetailModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity implements FeedbackDialog.IOnClickSend, FeedbackAdapter.IOnClickCallback {

    private ProductModel mProduct;
    private FeedbackDialog fDialog;

    private ImageView imgBack, imgCart, imgProduct;
    private TextView tvIndicator, tvProductName, tvPrice, tvPriceSale, tvDiscount, tvTotalFeedback;
    private ExpandableTextView tvDescription;
    private Button btnAddFeedback;

    private RelativeLayout rlSale, rlCart;
    private LinearLayout llBottomBar, llAddToCart, llBuyNow;

    private RecyclerView rcvFeedback;
    private FeedbackAdapter mFeedbackAdapter;
    // paging
    private boolean isInitTotalPage;
    private int totalFeedback;
    private int totalPage;
    private int currentPage;

    private boolean isFromBuyAgain;
    private boolean isInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initUi();
            mProduct = (ProductModel) bundle.getSerializable(KEY_PRODUCT);
            boolean isFromCart = bundle.getBoolean(KEY_FROM_CART);
            isFromBuyAgain = bundle.getBoolean(KEY_FROM_BUY_AGAIN);
            if (isFromCart) {
                imgBack.setOnClickListener(v -> finish());
                rlCart.setVisibility(View.GONE);
                llBottomBar.setVisibility(View.GONE);
            } else {
                if (isFromBuyAgain) {
                    imgBack.setOnClickListener(v -> backToBuyAgain());
                    rlCart.setVisibility(View.GONE);
                } else {
                    imgBack.setOnClickListener(v -> finish());
                    rlCart.setOnClickListener(v -> gotoCart());
                }

                llAddToCart.setOnClickListener(v -> {
                    AddToCardBottomSheetDialogFragment sheetDialogFragment = AddToCardBottomSheetDialogFragment.newInstant(mProduct);
                    sheetDialogFragment.show(getSupportFragmentManager(), sheetDialogFragment.getTag());
                });

                llBuyNow.setOnClickListener(v -> {
                    ToastCustom.loading(this, 800).show();
                    new Handler().postDelayed(() -> {
                        OrderDetailModel orderDetail = new OrderDetailModel(mProduct, 1);
                        Helper.addProductToCart(getApplicationContext(), orderDetail);
                        if (isFromBuyAgain) {
                            backToCart();
                        } else {
                            gotoCart();
                        }
                    }, 800);
                });
            }

            // init feedback
            totalPage = currentPage = 1;
            initTotalPage();
            initFeedbackData();
            btnAddFeedback.setOnClickListener(v -> {
                if (Helper.getCurrentAccount() == null) {
                    Intent intent = new Intent(DetailProductActivity.this, SigninActivity.class);
                    startActivity(intent);
                    return;
                }
                fDialog = new FeedbackDialog(this);
                fDialog.show();
            });
            // init product
//            Glide.with(this).load(mProduct.getImg()).placeholder(R.drawable.placeholder_img).into(imgProduct);
            imgProduct.setImageResource(R.drawable.test_product_detail);
            tvProductName.setText(mProduct.getName());
            tvDescription.setText(mProduct.getDescription());

            tvPriceSale.setText(format.format(mProduct.getPriceSale()));
            if (mProduct.getDiscount() != null) {
                tvDiscount.setText(MessageFormat.format(getResources().getString(R.string.discount), mProduct.getDiscount()));
                tvPrice.setText(format.format(mProduct.getPrice()));
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                rlSale.setVisibility(View.GONE);
                tvPrice.setVisibility(View.GONE);
            }
            initRlCart();

        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit) {
            isInit = false;
            return;
        }
        initRlCart();
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        imgCart = findViewById(R.id.img_cart);
        imgProduct = findViewById(R.id.img_product);
        tvIndicator = findViewById(R.id.tv_indicator);
        tvProductName = findViewById(R.id.tv_product_name);
        tvPrice = findViewById(R.id.tv_price);
        tvPriceSale = findViewById(R.id.tv_price_sale);
        tvDiscount = findViewById(R.id.tv_discount);
        tvDescription = findViewById(R.id.tv_description);
        tvTotalFeedback = findViewById(R.id.tv_total_feedback);
        btnAddFeedback = findViewById(R.id.btn_add_feedback);
        rlSale = findViewById(R.id.rl_sale);
        rlCart = findViewById(R.id.rl_cart);

        llBottomBar = findViewById(R.id.ll_bottom_bar);
        llAddToCart = findViewById(R.id.ll_add_to_cart);
        llBuyNow = findViewById(R.id.ll_buy_now);
        rcvFeedback = findViewById(R.id.rcv_feedback);
    }

    private void initRlCart() {
        int totalCart = Helper.getTotalProductInCart(getApplicationContext());
        if (totalCart > 0) {
            tvIndicator.setVisibility(View.VISIBLE);
            tvIndicator.setText(String.valueOf(totalCart));
            imgCart.setTranslationX(-3f);
        } else {
            tvIndicator.setVisibility(View.GONE);
            imgCart.setTranslationX(0);
        }
    }

    private void initTotalPage() {
        tvTotalFeedback.setVisibility(View.GONE);
        APIService.getService().getTotalFeedbackFeedbackInProduct(JWT.createToken(), mProduct.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String[] arr = response.body().split("\\|");
                    if (!arr[0].equals("0")) {
                        totalFeedback = Integer.parseInt(arr[0]);
                        totalPage = Integer.parseInt(arr[1]);
                        tvTotalFeedback.setVisibility(View.VISIBLE);
                        tvTotalFeedback.setText(MessageFormat.format(getResources().getString(R.string.total_feedback), totalFeedback));
                        isInitTotalPage = true;
                        return;
                    }
                }
                initDone();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("ddd", "onFailure: error");
                initDone();
            }

            private void initDone() {
                totalPage = 0;
                tvTotalFeedback.setVisibility(View.VISIBLE);
                tvTotalFeedback.setText(R.string.no_have_feedback);
                isInitTotalPage = true;
            }
        });
    }

    private void initFeedbackData() {
        APIService.getService().getAllFeedbackInProduct(JWT.createToken(), mProduct.getId(), 0).enqueue(new Callback<List<FeedbackModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedbackModel>> call, @NonNull Response<List<FeedbackModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mFeedbackAdapter = new FeedbackAdapter(DetailProductActivity.this, response.body());
                    rcvFeedback.setAdapter(mFeedbackAdapter);
                    rcvFeedback.setFocusable(false);
                    rcvFeedback.setNestedScrollingEnabled(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    rcvFeedback.setLayoutManager(layoutManager);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isInitTotalPage) {
                                mFeedbackAdapter.setTotalFeedback(totalFeedback);
                                if (totalPage > 1) {
                                    mFeedbackAdapter.addFooterLoading();
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    }, 100);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedbackModel>> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onClickAddFeedback(@NonNull String content) {
        if (content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Hãy nhập đánh giá.", Toast.LENGTH_SHORT).show();
            fDialog.focusEdt();
        } else {
            fDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            String token = Helper.getTokenLogin(getApplicationContext());
            FeedbackModel feedback = new FeedbackModel();
            feedback.setCustomerId(Helper.getCurrentAccount().getId());
            feedback.setProductId(mProduct.getId());
            feedback.setContent(content);
            Gson gson = new Gson();
            String feedbackJson = gson.toJson(feedback);
            APIService.getService().addFeedback(token, feedbackJson).enqueue(new Callback<FeedbackModel>() {
                @Override
                public void onResponse(@NonNull Call<FeedbackModel> call, @NonNull Response<FeedbackModel> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful()) {
                        tvTotalFeedback.setText(MessageFormat.format(getResources().getString(R.string.total_feedback), ++totalFeedback));
                        mFeedbackAdapter.addFirst(response.body());
                        ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thành công!", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thất bại", ToastCustom.ERROR, TOAST_DEFAULT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FeedbackModel> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Thêm đánh giá thất bại", ToastCustom.ERROR, TOAST_DEFAULT).show();
                }
            });
        }
    }

    @Override
    public void onClickEditFeedback(FeedbackModel feedback, @NonNull String content) {
        if (content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Hãy nhập đánh giá.", Toast.LENGTH_SHORT).show();
            fDialog.focusEdt();
        } else {
            fDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            String token = Helper.getTokenLogin(getApplicationContext());
            APIService.getService().editFeedback(token, feedback.getId(), content).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        feedback.setContent(content);
                        mFeedbackAdapter.reloadFeedback(feedback);
                        ToastCustom.notice(getApplicationContext(), "Thay đổi thành công!", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), "Thay đổi thất bại! Vui lòng thử lại sau", ToastCustom.ERROR, TOAST_DEFAULT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Thay đổi thất bại! Vui lòng thử lại sau", ToastCustom.ERROR, TOAST_DEFAULT).show();
                }
            });
        }
    }

    @Override
    public void onClickLoadMore() {
        currentPage++;
        APIService.getService().getAllFeedbackInProduct(JWT.createToken(), mProduct.getId(), mFeedbackAdapter.getSize()).enqueue(new Callback<List<FeedbackModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedbackModel>> call, @NonNull Response<List<FeedbackModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mFeedbackAdapter.removeFooterLoading();
                    mFeedbackAdapter.addToListFeedback(response.body());
                    if (currentPage < totalPage) {
                        mFeedbackAdapter.addFooterLoading();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FeedbackModel>> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onClickEditFeedback(@NonNull FeedbackModel feedback) {
        fDialog = new FeedbackDialog(this, feedback);
        fDialog.show();
    }

    @Override
    public void onClickDeleteFeedback(@NonNull FeedbackModel feedback) {
        String message = "Bạn có muốn xóa nhận xét này?";
        new ConfirmDialog(this, message, (confirmDialog) -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(DetailProductActivity.this);
            loadingDialog.show();
            String token = Helper.getTokenLogin(getApplicationContext());
            APIService.getService().deleteFeedback(token, feedback.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        if (--totalFeedback > 0) {
                            tvTotalFeedback.setText(MessageFormat.format(getResources().getString(R.string.total_feedback), totalFeedback));
                        } else {
                            tvTotalFeedback.setText(R.string.no_have_feedback);
                        }
                        mFeedbackAdapter.removeFeedback(feedback);
                        ToastCustom.notice(getApplicationContext(), "Xóa thành công!", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
                    } else {
                        ToastCustom.notice(getApplicationContext(), "Xóa thất bại! Vui lòng thử lại sau", ToastCustom.ERROR, TOAST_DEFAULT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(getApplicationContext(), "Xóa thất bại! Vui lòng thử lại sau", ToastCustom.ERROR, TOAST_DEFAULT).show();
                }
            });
        }).show();
    }

    private void gotoCart() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    private void backToCart() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RELOAD_CART, true);
        intent.putExtra(KEY_SELECT_TAP_ALL, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void backToBuyAgain() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RELOAD_CART, true);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (isFromBuyAgain) {
            backToBuyAgain();
        } else {
            super.onBackPressed();
        }
    }
}