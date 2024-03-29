package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_ACTION;
import static com.example.foodyshop.config.Const.KEY_ORDER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.CartActivity;
import com.example.foodyshop.activity.OrderDetailActivity;
import com.example.foodyshop.adapter.OrderAdapter;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.OrderModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTabFragment extends Fragment implements OrderAdapter.IInteractOrder {

    private final OrderFragment.TAB tab;

    private View view;
    private SwipeRefreshLayout swrlNoHaveData, swrlOrder;
    private RelativeLayout rlLoading;
    private RecyclerView rcvOrder;
    private OrderAdapter mOrderAdapter;
    private boolean isCreate;
    private long lastTimeInPage;
    private int lastUserId;


    private int mOrderPosition;
    private final OrderFragment.IPageRefreshStatus mIPageRefreshStatus;
    private IOnUpdatedOrder mIOnUpdatedOrder;

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null && intent.getExtras() != null && mOrderAdapter != null) {
                Bundle bundle = intent.getExtras();
                int action = bundle.getInt(KEY_ACTION);
                OrderModel order = (OrderModel) bundle.getSerializable(KEY_ORDER);
                if (action == OrderDetailActivity.ACTION_CONFIRM) {
                    mIOnUpdatedOrder.onConfirmSuccess(order);
                    removeOrder(mOrderPosition);
                }
                if (action == OrderDetailActivity.ACTION_CANCEL) {
                    mIOnUpdatedOrder.onCancelSuccess(order);
                    removeOrder(mOrderPosition);
                }
            }
        }
    });

    public OrderTabFragment(OrderFragment.TAB tab, OrderFragment.IPageRefreshStatus mIPageRefreshStatus) {
        this.tab = tab;
        this.mIPageRefreshStatus = mIPageRefreshStatus;
    }

    public OrderTabFragment(OrderFragment.TAB tab, OrderFragment.IPageRefreshStatus mIPageRefreshStatus, IOnUpdatedOrder mIOnUpdatedOrder) {
        this.tab = tab;
        this.mIPageRefreshStatus = mIPageRefreshStatus;
        this.mIOnUpdatedOrder = mIOnUpdatedOrder;
    }

    public interface IOnUpdatedOrder {

        void onCancelSuccess(OrderModel orderModel);

        void onConfirmSuccess(OrderModel orderModel);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_tab, container, false);
        lastUserId = Helper.getCurrentAccount().getId();
        isCreate = true;
        mOrderPosition = -1;
        initUi();
        initDataOrder(false);
        swrlNoHaveData.setOnRefreshListener(() -> initDataOrder(true));
        swrlNoHaveData.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorTextAccent));
        swrlOrder.setOnRefreshListener(() -> initDataOrder(true));
        swrlOrder.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorTextAccent));

        rcvOrder.setOnTouchListener(new View.OnTouchListener() {
            private float preX = 0f;
            private float preY = 0f;

            @Override
            public boolean onTouch(View view, MotionEvent e) {
                int x_BUFFER = 10;
                int y_BUFFER = 10;
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("ddd", "onInterceptTouchEvent Y: " + Math.abs(e.getY() - preY) + " |X: " + Math.abs(e.getX() - preX));
                        if (Math.abs(e.getY() - preY) > y_BUFFER) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                        } else if (Math.abs(e.getX() - preX) > Math.abs(e.getY() - preY) && Math.abs(e.getX() - preX) > x_BUFFER) {
                            view.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                }
                preX = e.getX();
                preY = e.getY();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCreate) {
            mIPageRefreshStatus.setPageRefreshStatus(tab.getIndex(), false);
            isCreate = false;
            return;
        }
        if (lastUserId > 0 && Helper.getCurrentAccount() != null && lastUserId != Helper.getCurrentAccount().getId()) {
            showTapEmpty();
            lastUserId = Helper.getCurrentAccount().getId();
            mIPageRefreshStatus.setPageRefreshStatus(tab.getIndex(), false);
            initDataOrder(false);
            return;
        }

        if (mIPageRefreshStatus.getPageRefreshStatus(tab.getIndex())) {
            mIPageRefreshStatus.setPageRefreshStatus(tab.getIndex(), false);
            if (System.currentTimeMillis() - lastTimeInPage > TimeUnit.SECONDS.toMillis(30)) {
                initDataOrder(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        lastTimeInPage = System.currentTimeMillis();
    }

    private void initUi() {
        swrlOrder = view.findViewById(R.id.swrl_order);
        swrlNoHaveData = view.findViewById(R.id.swrl_no_have_data);
        rlLoading = view.findViewById(R.id.rl_loading);
        rcvOrder = view.findViewById(R.id.rcv_order);
    }

    private void initDataOrder(boolean inBackground) {
        if (getContext() == null) {
            return;
        }
        long timeStartCall = System.currentTimeMillis();
        if (!inBackground) {
            rlLoading.setVisibility(View.VISIBLE);
            swrlOrder.setVisibility(View.GONE);
            swrlNoHaveData.setVisibility(View.GONE);
        }
        if (tab != null) {
            APIService.getService().getListOrder(Helper.getTokenLogin(requireContext()), tab.getRequestStatus()).enqueue(new Callback<List<OrderModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<OrderModel>> call, @NonNull Response<List<OrderModel>> response) {
                    if (getContext() == null) {
                        return;
                    }
                    if (swrlNoHaveData.isRefreshing() || swrlOrder.isRefreshing()) {
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (System.currentTimeMillis() - timeStartCall > TimeUnit.SECONDS.toMillis(2)) {
                                    swrlNoHaveData.setRefreshing(false);
                                    swrlOrder.setRefreshing(false);
                                    initRespond(response);
                                } else {
                                    handler.postDelayed(this, 300);
                                }
                            }
                        });
                    } else {
                        initRespond(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<OrderModel>> call, @NonNull Throwable t) {
                    if (getContext() == null) {
                        return;
                    }
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }

                private void initRespond(@NonNull Response<List<OrderModel>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        if (!inBackground) {
                            rlLoading.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_fade_out));
                            swrlNoHaveData.setVisibility(View.GONE);
                            new Handler().postDelayed(() -> {
                                rlLoading.setVisibility(View.GONE);
                                swrlOrder.setVisibility(View.VISIBLE);
                                swrlOrder.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_fade_in));
                            }, 200);
                        } else {
                            swrlNoHaveData.setVisibility(View.GONE);
                            swrlOrder.setVisibility(View.VISIBLE);
                            swrlOrder.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_fade_in));
                        }
                        mOrderAdapter = new OrderAdapter(requireContext(), response.body(), OrderTabFragment.this);
                        rcvOrder.setAdapter(mOrderAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                        rcvOrder.setLayoutManager(layoutManager);
                    } else {
                        showTapEmpty();
                    }
                }
            });
        }
    }

    private void showTapEmpty() {
        swrlOrder.setVisibility(View.GONE);
        swrlNoHaveData.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClickTitle(int position, OrderModel order) {
        mOrderPosition = position;
        Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER, order);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
    }

    @Override
    public void onClickBuyAgain(OrderModel order) {
        Helper.buyAgain(requireContext(), order);
        Intent intent = new Intent(requireContext(), CartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCancelOrder(int position, @NonNull OrderModel order) {
        String message = "Nhấn xác nhận để hủy đơn hàng " + order.getOrderCode();
        ConfirmDialog.newInstance(requireActivity(), message, confirmDialog -> {
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
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                            removeOrder(position);
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
        }).show();
    }

    @Override
    public void onClickConfirmOrder(int position, @NonNull OrderModel order) {
        String message = "Xác nhận giao hàng với giá ship " + Helper.PRICE_FORMAT.format(order.getShipPrice());
        ConfirmDialog.newInstance(requireActivity(), message, confirmDialog -> {
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
                            ToastCustom.notice(requireContext(), res.getMsg(), ToastCustom.SUCCESS).show();
                            removeOrder(position);
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
        }).show();
    }

    private void removeOrder(int position) {
        if (mOrderAdapter != null && position >= 0) {
            mOrderAdapter.getListOrder().remove(position);
            mOrderAdapter.notifyItemRemoved(position);
            if (mOrderAdapter.getItemCount() == 0) {
                showTapEmpty();
            }
        }
    }

    public void onUpdatedOrder(OrderModel order) {
        if (mOrderAdapter != null && order != null) {
            mOrderAdapter.getListOrder().add(0, order);
            mOrderAdapter.notifyItemInserted(0);
        } else if (view != null) {
            initUi();
            swrlNoHaveData.setVisibility(View.GONE);
            rlLoading.setVisibility(View.GONE);
            swrlOrder.setVisibility(View.VISIBLE);
            List<OrderModel> mListOrder = new ArrayList<>();
            mListOrder.add(order);
            mOrderAdapter = new OrderAdapter(requireContext(), mListOrder, OrderTabFragment.this);
            rcvOrder.setAdapter(mOrderAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            rcvOrder.setLayoutManager(layoutManager);
        }
    }

}
