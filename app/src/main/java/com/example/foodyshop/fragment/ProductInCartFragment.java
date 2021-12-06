package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_FROM_CART;
import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.CartActivity;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.adapter.OrderDetailAdapter;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.OrderDetailModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.service.APIService;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInCartFragment extends Fragment implements OrderDetailAdapter.IInteractItem {

    private View view;
    private LinearLayout llEmptyCart, llBottomOrder;
    private NestedScrollView mNestedScrollView;
    private RecyclerView rcvOrderDetail;
    private CheckBox cbAll;
    private TextView tvTotalMoney;
    private Button btnBuy, btnDelete;

    private OrderDetailAdapter adapter;
    private List<OrderDetailModel> mListOrderDetail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_in_cart, container, false);
        mListOrderDetail = Helper.getAllOrderDetailCart(requireContext());
        initUI();
        if (mListOrderDetail.isEmpty()) {
            mNestedScrollView.setVisibility(View.GONE);
            llEmptyCart.setVisibility(View.VISIBLE);
        } else {
            ((CartActivity) requireActivity()).getTvEdit().setVisibility(View.VISIBLE);
            mNestedScrollView.setVisibility(View.VISIBLE);
            llEmptyCart.setVisibility(View.GONE);
        }

        initBottomBox(0, 0);
        adapter = new OrderDetailAdapter(requireContext(), mListOrderDetail, this);
        rcvOrderDetail.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(layoutManager);

        cbAll.setOnClickListener(view -> {
            boolean check = cbAll.isChecked();
            adapter.setCheck(check);
            initBottomBox();
        });

        btnDelete.setOnClickListener(this::onClickDelete);
        btnBuy.setOnClickListener(this::onClickBuy);

        return view;
    }


    private void initUI() {
        llEmptyCart = view.findViewById(R.id.ll_empty_cart);
        llBottomOrder = view.findViewById(R.id.ll_bottom_order);
        mNestedScrollView = view.findViewById(R.id.nested_scroll);
        rcvOrderDetail = view.findViewById(R.id.rcv_order_detail);
        cbAll = view.findViewById(R.id.cb_all);
        tvTotalMoney = view.findViewById(R.id.tv_total_money);
        btnBuy = view.findViewById(R.id.btn_buy);
        btnDelete = view.findViewById(R.id.btn_delete);
    }

    public void setBottomBox(boolean isEdit) {
        if (isEdit) {
            llBottomOrder.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            llBottomOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void initBottomBox() {
        int totalMoney = 0;
        int totalProduct = 0;
        for (OrderDetailModel ord : mListOrderDetail) {
            if (ord.isChecked()) {
                totalProduct++;
                totalMoney += ord.getPriceSale() * ord.getNumber();
            }
        }
        initBottomBox(totalMoney, totalProduct);
    }

    private void initBottomBox(int totalMoney, int totalProduct) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        btnBuy.setEnabled(totalMoney != 0);
        btnBuy.setText(MessageFormat.format(requireContext().getResources().getString(R.string.total_buy), totalProduct));
        tvTotalMoney.setText(format.format(totalMoney));
    }

    private void onClickDelete(@NonNull View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), TOAST_DEFAULT);
        if (mListOrderDetail.isEmpty()) {
            ToastCustom.notice(requireContext(), "Giỏ hàng trống!", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return;
        }
        if (cbAll.isChecked()) {
            ConfirmDialog dialog = new ConfirmDialog(requireActivity(), "Bạn có muốn xóa sạch giỏ hàng?", confirmDialog -> {
                Helper.clearCart(requireContext());
                mListOrderDetail = new ArrayList<>();
                adapter.setmListOrderDetail(mListOrderDetail);
                confirmDialog.dismiss();
                ToastCustom.notice(requireContext(), "Đã xóa sạch giỏ hàng.", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
                rcvOrderDetail.setVisibility(View.GONE);
                cbAll.setChecked(false);
                initBottomBox(0, 0);
            });
            dialog.show();
            return;
        }
        boolean hasCheck = false;
        List<OrderDetailModel> tmlList = new ArrayList<>();
        for (OrderDetailModel ord : mListOrderDetail) {
            if (ord.isChecked()) {
                tmlList.add(ord);
                Helper.removeOrderDetailFromCart(requireContext(), ord);
                hasCheck = true;
            }
        }
        if (!hasCheck) {
            ToastCustom.notice(requireContext(), "Hãy chọn một sản phẩm.", ToastCustom.INFO, TOAST_DEFAULT).show();
        } else {
            ToastCustom.notice(requireContext(), "Đã xóa " + tmlList.size() + " sản phẩm.", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
            mListOrderDetail.removeAll(tmlList);
            adapter.setmListOrderDetail(mListOrderDetail);
            initBottomBox();
        }
    }

    private void onClickBuy(@NonNull View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), TOAST_DEFAULT);
        if (Helper.isLogin(requireContext())) {
            // result launcher
            ToastCustom.notice(requireContext(), "Login OK", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
        } else {
            Intent intent = new Intent(requireContext(), SigninActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClickItem(@NonNull OrderDetailModel orderDetail) {
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
                    bundle.putBoolean(KEY_FROM_CART, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối!", ToastCustom.ERROR, TOAST_DEFAULT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
                load.cancel();
                ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối!", ToastCustom.ERROR, TOAST_DEFAULT).show();
            }
        });
    }

    @Override
    public void onCheckBoxChange(boolean checked) {
        if (checked) {
            boolean isCheckAll = true;
            int totalMoney = 0;
            int totalProduct = 0;
            for (OrderDetailModel ord : mListOrderDetail) {
                if (isCheckAll && !ord.isChecked()) {
                    isCheckAll = false;
                }
                if (ord.isChecked()) {
                    totalProduct++;
                    totalMoney += ord.getPriceSale() * ord.getNumber();
                }
            }
            initBottomBox(totalMoney, totalProduct);
            if (isCheckAll) {
                cbAll.setChecked(true);
            }
        } else {
            cbAll.setChecked(false);
            initBottomBox();
        }
    }

    @Override
    public void onAmountNumberChange(@NonNull OrderDetailModel orderDetail, int amountNumber) {
        orderDetail.setNumber(amountNumber);
        Helper.addOrChangeAmountOrderDetail(requireContext(), orderDetail, false);
        if (orderDetail.isChecked()) {
            initBottomBox();
        }
    }

    @Override
    public void onDeleteItem(OrderDetailModel orderDetail) {
        Helper.removeOrderDetailFromCart(requireContext(), orderDetail);
        initBottomBox();
    }

}
