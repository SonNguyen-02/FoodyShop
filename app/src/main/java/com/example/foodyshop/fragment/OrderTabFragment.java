package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_ORDER;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.CartActivity;
import com.example.foodyshop.activity.OrderDetailActivity;
import com.example.foodyshop.adapter.OrderAdapter;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.OrderModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTabFragment extends Fragment implements OrderAdapter.IInteractOrder {

    private final OrderFragment.TAB tab;

    private View view;
    private LinearLayout llNoHaveData, llOrder;
    private RelativeLayout rlLoading;
    private RecyclerView rcvOrder;
    private OrderAdapter mOrderAdapter;
    private int lastUserId;
    private IOnUpdatedOrder mIOnUpdatedOrder;

    public OrderTabFragment(OrderFragment.TAB tab) {
        this.tab = tab;
    }

    public OrderTabFragment(OrderFragment.TAB tab, IOnUpdatedOrder mIOnUpdatedOrder) {
        this.tab = tab;
        this.mIOnUpdatedOrder = mIOnUpdatedOrder;
    }

    public interface IOnUpdatedOrder {
        void onCancelSuccess(OrderModel orderModel);

        void onConfirmSuccess(OrderModel orderModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_tab, container, false);
        lastUserId = Helper.getUserInfo(requireContext()).getId();
        initUi();
        initDataOrder();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastUserId > 0 && Helper.getUserInfo(requireContext()) != null && lastUserId != Helper.getUserInfo(requireContext()).getId()) {
            showTapEmpty();
            lastUserId = Helper.getUserInfo(requireContext()).getId();
            initDataOrder();
        }
    }

    private void initUi() {
        llNoHaveData = view.findViewById(R.id.ll_no_have_data);
        llOrder = view.findViewById(R.id.ll_order);
        rlLoading = view.findViewById(R.id.rl_loading);
        rcvOrder = view.findViewById(R.id.rcv_order);
    }

    private void initDataOrder() {
        rlLoading.setVisibility(View.VISIBLE);
        llOrder.setVisibility(View.GONE);
        llNoHaveData.setVisibility(View.GONE);
        if (tab != null) {
            APIService.getService().getListOrder(Helper.getTokenLogin(requireContext()), tab.getRequestStatus()).enqueue(new Callback<List<OrderModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<OrderModel>> call, @NonNull Response<List<OrderModel>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        rlLoading.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_fade_out));
                        llNoHaveData.setVisibility(View.GONE);
                        new Handler().postDelayed(() -> {
                            rlLoading.setVisibility(View.GONE);
                            llOrder.setVisibility(View.VISIBLE);
                            llOrder.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_fade_in));
                        }, 200);
                        mOrderAdapter = new OrderAdapter(requireContext(), response.body(), OrderTabFragment.this);
                        rcvOrder.setAdapter(mOrderAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                        rcvOrder.setLayoutManager(layoutManager);
                    } else {
                        showTapEmpty();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<OrderModel>> call, @NonNull Throwable t) {
                    showTapEmpty();
                }
            });
        }
    }

    private void showTapEmpty() {
        llOrder.setVisibility(View.GONE);
        llNoHaveData.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClickTitle(OrderModel order) {
        Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER, order);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClickBuyAgain(OrderModel order) {
        Helper.buyAgain(requireContext(), order);
        Intent intent = new Intent(requireContext(), CartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCancelOrder(@NonNull OrderModel order) {
        String message = "Nhấn xác nhận để hủy đơn hàng";
        ConfirmDialog dialog = new ConfirmDialog(requireActivity(), message, confirmDialog -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
            loadingDialog.show();
            APIService.getService().cancelOrder(Helper.getTokenLogin(requireContext()), order.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Respond res = response.body();
                        if (res.isSuccess()) {
                            order.setStatus(-2);
                            mIOnUpdatedOrder.onCancelSuccess(order);
                            int pos = mOrderAdapter.getListOrder().indexOf(order);
                            mOrderAdapter.getListOrder().remove(order);
                            mOrderAdapter.notifyItemRemoved(pos);
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                            if (mOrderAdapter.getItemCount() == 0) {
                                showTapEmpty();
                            }
                        } else {
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.ERROR).show();
                        }
                    } else {
                        ToastCustom.notice(requireContext(), "Hủy đơn hàng thất bại. Vui lòng thử lại sau", ToastCustom.WARNING).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            });
        });
        dialog.show();
    }

    @Override
    public void onClickConfirmOrder(@NonNull OrderModel order) {
        String message = "Xác nhận giao hàng với giá ship " + Helper.PRICE_FORMAT.format(order.getShipPrice());
        ConfirmDialog dialog = new ConfirmDialog(requireActivity(), message, confirmDialog -> {
            confirmDialog.dismiss();
            LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
            loadingDialog.show();
            APIService.getService().confirmBuy(Helper.getTokenLogin(requireContext()), order.getId()).enqueue(new Callback<Respond>() {
                @Override
                public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Respond res = response.body();
                        if (res.isSuccess()) {
                            order.setStatus(3);
                            mIOnUpdatedOrder.onConfirmSuccess(order);
                            int pos = mOrderAdapter.getListOrder().indexOf(order);
                            mOrderAdapter.getListOrder().remove(order);
                            mOrderAdapter.notifyItemRemoved(pos);
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                            if (mOrderAdapter.getItemCount() == 0) {
                                showTapEmpty();
                            }
                        } else {
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.ERROR).show();
                        }
                    } else {
                        ToastCustom.notice(requireContext(), "Xác nhận thất bại. Vui lòng thử lại sau", ToastCustom.WARNING).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            });
        });
        dialog.show();
    }

    public void onUpdatedOrder(OrderModel orderModel) {
        if (mOrderAdapter != null && orderModel != null) {
            mOrderAdapter.getListOrder().add(0, orderModel);
            mOrderAdapter.notifyItemInserted(0);
        }
    }

}
