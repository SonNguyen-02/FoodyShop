package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_FROM_CART;
import static com.example.foodyshop.config.Const.KEY_IS_CHECK_ALL;
import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.KEY_SIGN_IN_OK;
import static com.example.foodyshop.config.Const.KEY_TOTAL_MONEY;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.CartActivity;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.OrderActivity;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.adapter.ProductCartAdapter;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CustomerModel;
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

public class ProductInCartFragment extends Fragment implements ProductCartAdapter.IInteractItem {

    private View view;
    private LinearLayout llEmptyCart, llClearEmptyCart, llBottomOrder, llBottomEdit;
    private RelativeLayout rlCart;
    private RecyclerView rcvProductCart;
    private CheckBox cbAll;
    private TextView tvTotalMoney;
    private Button btnBuy, btnDelete, btnCancel;

    private long lastClickItem;
    private ProductCartAdapter adapter;
    private List<OrderDetailModel> mListOrderDetail;
    private int mTotalMoney;

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                if (intent.getBooleanExtra(KEY_SIGN_IN_OK, false)) {
                    btnBuy.callOnClick();
                    return;
                }
            }
            List<OrderDetailModel> tmpList = new ArrayList<>();
            for (OrderDetailModel ord : mListOrderDetail) {
                if (ord.isChecked()) {
                    tmpList.add(ord);
                }
            }
            mListOrderDetail.removeAll(tmpList);
            Helper.saveCart(requireContext(), mListOrderDetail);
            initViewAfterDelete();
            initBottomBox();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_in_cart, container, false);
        mListOrderDetail = Helper.getAllProductInCart(requireContext());
        initUI();
        if (mListOrderDetail.isEmpty()) {
            ((CartActivity) requireActivity()).getBtnEdit().setVisibility(View.GONE);
            rlCart.setVisibility(View.GONE);
            llEmptyCart.setVisibility(View.VISIBLE);
        } else {
            ((CartActivity) requireActivity()).getBtnEdit().setVisibility(View.VISIBLE);
            rlCart.setVisibility(View.VISIBLE);
            llEmptyCart.setVisibility(View.GONE);
        }

        initBottomBox();
        initCbAll();
        adapter = new ProductCartAdapter(requireContext(), mListOrderDetail, this);
        rcvProductCart.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvProductCart.setLayoutManager(layoutManager);

        cbAll.setOnClickListener(view -> {
            boolean check = cbAll.isChecked();
            adapter.setCheck(check);
            Helper.saveCart(requireContext(), mListOrderDetail);
            initBottomBox();
        });

        btnBuy.setOnClickListener(this::onClickBuy);
        btnDelete.setOnClickListener(this::onClickDelete);
        btnCancel.setOnClickListener(this::onClickCancel);
        return view;
    }

    private void initUI() {
        rlCart = view.findViewById(R.id.rl_cart);
        llEmptyCart = view.findViewById(R.id.ll_empty_cart);
        llClearEmptyCart = view.findViewById(R.id.ll_clear_empty_cart);
        llBottomOrder = view.findViewById(R.id.ll_bottom_order);
        llBottomEdit = view.findViewById(R.id.ll_bottom_edit);
        rcvProductCart = view.findViewById(R.id.rcv_product_cart);
        cbAll = view.findViewById(R.id.cb_all);
        tvTotalMoney = view.findViewById(R.id.tv_total_money);
        btnBuy = view.findViewById(R.id.btn_buy);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnCancel = view.findViewById(R.id.btn_cancel);
    }

    public void setBottomBox(boolean isEdit) {
        if (isEdit) {
            llBottomOrder.setVisibility(View.GONE);
            llBottomEdit.setVisibility(View.VISIBLE);
        } else {
            llBottomOrder.setVisibility(View.VISIBLE);
            llBottomEdit.setVisibility(View.GONE);
        }
    }

    private void initBottomBox() {
        int totalMoney = 0;
        int totalProduct = 0;
        for (OrderDetailModel ord : mListOrderDetail) {
            if (ord.isChecked()) {
                totalProduct++;
                totalMoney += ord.getPriceSale() * ord.getAmount();
            }
        }
        initBottomBox(totalMoney, totalProduct);
    }

    private void initBottomBox(int totalMoney, int totalProduct) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        btnBuy.setText(MessageFormat.format(requireContext().getResources().getString(R.string.total_buy), totalProduct));
        tvTotalMoney.setText(format.format(totalMoney));
        mTotalMoney = totalMoney;
    }

    private void initCbAll() {
        if (mListOrderDetail != null && !mListOrderDetail.isEmpty()) {
            boolean isCheckAll = true;
            for (OrderDetailModel ord : mListOrderDetail) {
                if (!ord.isChecked()) {
                    isCheckAll = false;
                    break;
                }
            }
            cbAll.setChecked(isCheckAll);
        } else {
            cbAll.setChecked(false);
        }
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
                rcvProductCart.setVisibility(View.GONE);
                cbAll.setChecked(false);
                initViewAfterDelete();
                initBottomBox(0, 0);
                confirmDialog.dismiss();
                ToastCustom.notice(requireContext(), "Đã xóa sạch giỏ hàng.", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
            });
            dialog.show();
        } else {
            boolean hasCheck = false;
            List<OrderDetailModel> tmlList = new ArrayList<>();
            for (OrderDetailModel ord : mListOrderDetail) {
                if (ord.isChecked()) {
                    tmlList.add(ord);
                    hasCheck = true;
                }
            }
            if (!hasCheck) {
                ToastCustom.notice(requireContext(), "Hãy chọn một sản phẩm.", ToastCustom.INFO, TOAST_DEFAULT).show();
            } else {
                ToastCustom.notice(requireContext(), "Đã xóa " + tmlList.size() + " sản phẩm.", ToastCustom.SUCCESS, TOAST_DEFAULT).show();
                mListOrderDetail.removeAll(tmlList);
                adapter.setmListOrderDetail(mListOrderDetail);
                Helper.saveCart(requireContext(), mListOrderDetail);
                initViewAfterDelete();
                initBottomBox();
            }
        }
    }

    private void initViewAfterDelete() {
        if (mListOrderDetail.isEmpty()) {
            ((CartActivity) requireActivity()).getBtnEdit().setVisibility(View.GONE);
            llEmptyCart.setVisibility(View.GONE);
            llClearEmptyCart.setVisibility(View.VISIBLE);
            rlCart.setVisibility(View.GONE);
        }
    }

    public void reLoadCart() {
        if (adapter != null) {
            ((CartActivity) requireActivity()).getBtnEdit().setVisibility(View.VISIBLE);
            llEmptyCart.setVisibility(View.GONE);
            llClearEmptyCart.setVisibility(View.GONE);
            rlCart.setVisibility(View.VISIBLE);
            mListOrderDetail = Helper.getAllProductInCart(requireContext());
            adapter.setmListOrderDetail(mListOrderDetail);
        }
    }

    public boolean isEmptyCart() {
        return mListOrderDetail == null || mListOrderDetail.isEmpty();
    }

    private void onClickCancel(View view) {
        ((CartActivity) requireActivity()).getBtnEdit().callOnClick();
    }

    private void onClickBuy(@NonNull View view) {
        view.setEnabled(false);
        new Handler().postDelayed(() -> view.setEnabled(true), TOAST_DEFAULT);
        if (mTotalMoney == 0) {
            ToastCustom.notice(requireContext(), "Hãy chọn một sản phẩm.", ToastCustom.INFO, TOAST_DEFAULT).show();
            return;
        }
        if (Helper.isLogin(requireContext())) {
            // result launcher
            if (Helper.getCurrentAccount() != null) {
                Intent intent = new Intent(requireContext(), OrderActivity.class);
                intent.putExtra(KEY_IS_CHECK_ALL, cbAll.isChecked());
                intent.putExtra(KEY_TOTAL_MONEY, mTotalMoney);
                mActivityResultLauncher.launch(intent);
            } else {
                LoadingDialog dialog = new LoadingDialog(requireActivity());
                dialog.show();
                APIService.getService().getUserInfo(Helper.getTokenLogin(requireContext())).enqueue(new Callback<CustomerModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Helper.saveUserInfo(requireContext(), response.body());
                            btnBuy.callOnClick();
                        } else {
                            goToSignIn();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        goToSignIn();
                    }
                });
            }
        } else {
            goToSignIn();
        }
    }

    private void goToSignIn() {
        Intent intent = new Intent(requireContext(), SigninActivity.class);
        mActivityResultLauncher.launch(intent);
    }

    @Override
    public void onClickItem(@NonNull OrderDetailModel orderDetail) {
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
            initCbAll();
        } else {
            cbAll.setChecked(false);
        }
        Helper.saveCart(requireContext(), mListOrderDetail);
        initBottomBox();
    }

    @Override
    public void onAmountNumberChange(@NonNull OrderDetailModel orderDetail, int amount) {
        orderDetail.setAmount(amount);
        Helper.saveCart(requireContext(), mListOrderDetail);
        if (orderDetail.isChecked()) {
            initBottomBox();
        }
    }

    @Override
    public void onDeleteItem() {
        initViewAfterDelete();
        Helper.saveCart(requireContext(), mListOrderDetail);
        initBottomBox();
    }

}
