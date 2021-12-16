package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_FROM_CART;
import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.LIMIT_TIME_EDIT_DEL_FEEDBACK;
import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.OrderDetailActivity;
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

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends Fragment {

    private View view;
    private ImageView imgBack, imgProduct, imgAvatar;
    private TextView tvTimer, tvProductName, tvPrice, tvPriceSale, tvCustomerName, tvCreated, tvContent;
    private RelativeLayout rlProduct;
    private Button btnDelete, btnEdit;
    private LinearLayout llBottomEdit;

    private OrderDetailActivity mOrderDetailActivity;
    private final int position;
    private final OrderDetailModel orderDetail;

    private long minutes;
    private long lastClickItem;

    public FeedbackFragment(int position, OrderDetailModel orderDetail) {
        this.position = position;
        this.orderDetail = orderDetail;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOrderDetailActivity = (OrderDetailActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        initUi();
        if (orderDetail.getFeedback() == null) {
            mOrderDetailActivity.onBackPressed();
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        imgBack.setOnClickListener(v -> mOrderDetailActivity.onBackPressed());
        // init box product
        rlProduct.setOnClickListener(this::onClickProduct);
        Glide.with(this).load(orderDetail.getImg())
                .placeholder(R.drawable.test_product_icon)
                .error(R.drawable.test_product_icon)
                .into(imgProduct);
        tvProductName.setText(orderDetail.getName());
        tvPriceSale.setText(PRICE_FORMAT.format(orderDetail.getPriceSale()));
        if (orderDetail.getDiscount() != null) {
            tvPrice.setVisibility(View.VISIBLE);
            tvPrice.setText(PRICE_FORMAT.format(orderDetail.getPrice()));
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvPrice.setVisibility(View.GONE);
        }

        // init box feedback
        tvCustomerName.setText(Helper.getCurrentAccount().getName());
        Glide.with(this).load(Helper.getCurrentAccount().getImg())
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.placeholder_user)
                .into(imgAvatar);
        SimpleDateFormat formatTo = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("vi_VN"));
        long date = Helper.parseDate(orderDetail.getFeedback().getCreated());
        tvCreated.setText(formatTo.format(new Date(date)));
        tvContent.setText(orderDetail.getFeedback().getContent());

        long timePass = System.currentTimeMillis() - orderDetail.getFeedback().getTime();
        if (timePass >= LIMIT_TIME_EDIT_DEL_FEEDBACK) {
            tvTimer.setVisibility(View.GONE);
            llBottomEdit.setVisibility(View.GONE);
        } else {
            tvTimer.setVisibility(View.VISIBLE);
            llBottomEdit.setVisibility(View.VISIBLE);
            long timeLeft = LIMIT_TIME_EDIT_DEL_FEEDBACK - timePass;
            minutes = timeLeft / TimeUnit.MINUTES.toMillis(1);
            long seconds = (timeLeft - minutes * TimeUnit.MINUTES.toMillis(1)) / TimeUnit.SECONDS.toMillis(1);
            minutes = seconds > 0 ? minutes + 1 : minutes;
            tvTimer.setText(MessageFormat.format(getString(R.string.feedback_timer), minutes));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getContext() == null) {
                        return;
                    }
                    if (minutes - 1 > 0) {
                        tvTimer.setText(MessageFormat.format(getString(R.string.feedback_timer), --minutes));
                        handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1));
                    } else {
                        tvTimer.setText(getString(R.string.feedback_timer_finish));
                        llBottomEdit.setVisibility(View.GONE);
                    }
                }
            }, TimeUnit.SECONDS.toMillis(seconds));

            btnEdit.setOnClickListener(this::onClickEdit);
            btnDelete.setOnClickListener(this::onClickDeleteFeedback);
        }

        return view;
    }

    private void initUi() {
        imgBack = view.findViewById(R.id.img_back);
        imgProduct = view.findViewById(R.id.img_product);
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvTimer = view.findViewById(R.id.tv_timer);
        tvProductName = view.findViewById(R.id.tv_product_name);
        tvPrice = view.findViewById(R.id.tv_price);
        tvPriceSale = view.findViewById(R.id.tv_price_sale);
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvCreated = view.findViewById(R.id.tv_created);
        tvContent = view.findViewById(R.id.tv_content);
        rlProduct = view.findViewById(R.id.rl_product);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnEdit = view.findViewById(R.id.btn_edit);
        llBottomEdit = view.findViewById(R.id.ll_bottom_edit);
    }

    private void onClickEdit(View view) {
        FeedbackModel feedback = orderDetail.getFeedback();
        FeedbackDialog.editFeedback(mOrderDetailActivity, feedback, (mFbDialog, content) -> {
            if (content.isEmpty()) {
                ToastCustom.notice(requireContext(), "Hãy nhập đánh giá", ToastCustom.WARNING).show();
            } else {
                mFbDialog.dismiss();
                LoadingDialog loadingDialog = new LoadingDialog(mOrderDetailActivity);
                loadingDialog.show();
                String token = Helper.getTokenLogin(requireContext());
                APIService.getService().editFeedback(token, feedback.getId(), content).enqueue(new Callback<Respond>() {
                    @Override
                    public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                        loadingDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            feedback.setContent(content);
                            tvContent.setText(content);
                            mOrderDetailActivity.updateItemOrderDetail(position, feedback);
                            ToastCustom.notice(requireContext(), "Thay đổi thành công!", ToastCustom.SUCCESS).show();
                        } else {
                            ToastCustom.notice(requireContext(), "Thay đổi thất bại! Vui lòng thử lại sau", ToastCustom.ERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                        loadingDialog.dismiss();
                        ToastCustom.notice(requireContext(), "Thay đổi thất bại! Vui lòng thử lại sau", ToastCustom.ERROR).show();
                    }
                });
            }
        }).show();
    }

    public void onClickDeleteFeedback(View view) {
        FeedbackModel feedback = orderDetail.getFeedback();
        String message = "Bạn có muốn xóa nhận xét này?";
        ConfirmDialog.newInstance(mOrderDetailActivity, message, (confirmDialog) -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(mOrderDetailActivity);
            loadingDialog.show();
            String token = Helper.getTokenLogin(requireContext());
            APIService.getService().deleteFeedback(token, feedback.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        llBottomEdit.setVisibility(View.GONE);
                        mOrderDetailActivity.updateItemOrderDetail(position, null);
                        mOrderDetailActivity.onBackPressed();
                        ToastCustom.notice(requireContext(), "Xóa thành công!", ToastCustom.SUCCESS).show();
                    } else {
                        ToastCustom.notice(requireContext(), "Xóa thất bại! Vui lòng thử lại sau", ToastCustom.ERROR).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(requireContext(), "Xóa thất bại! Vui lòng thử lại sau", ToastCustom.ERROR).show();
                }
            });
        }).show();
    }

    private void onClickProduct(View view) {
        if (System.currentTimeMillis() - lastClickItem < 1500) {
            return;
        }
        lastClickItem = System.currentTimeMillis();
        ToastCustom load = ToastCustom.loading(requireContext(), 0);
        load.show();
        APIService.getService().getDetailProduct(JWT.createToken(), orderDetail.getProductId()).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                load.cancel();
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(requireContext(), DetailProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_PRODUCT, response.body());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối!", ToastCustom.ERROR).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
                load.cancel();
                ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối!", ToastCustom.ERROR).show();
            }
        });

    }
}
