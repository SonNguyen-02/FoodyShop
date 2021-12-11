package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_FROM_BUY_AGAIN;
import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.KEY_RELOAD_CART;
import static com.example.foodyshop.config.Const.KEY_SELECT_TAP_ALL;
import static com.example.foodyshop.config.Const.KEY_SIGN_IN_OK;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.CartActivity;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.DetailTopicActivity;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.service.APIService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyAgainFragment extends Fragment implements ProductAdapter.IOnclickProductItem {

    private View view;
    private NestedScrollView scrollView;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private RelativeLayout rlProduct, rlLoading;
    private LinearLayout llOopsLogin, llDataBoughtProduct, llNoHaveData;
    private RecyclerView rcvProductBought;
    private Button btnGotoLogin;

    private ProductAdapter mProductAdapter;
    // paging
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage;
    private int currentPage;

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                if (intent.getBooleanExtra(KEY_SIGN_IN_OK, false)) {
                    initTotalPageProduct();
                    return;
                }
                if (intent.getBooleanExtra(KEY_RELOAD_CART, false)) {
                    ((CartActivity) requireActivity()).reLoadCart(intent.getBooleanExtra(KEY_SELECT_TAP_ALL, false));
                }
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_again, container, false);

        initUi();

        if (Helper.isLogin(requireContext())) {
            initTotalPageProduct();
        } else {
            btnGotoLogin.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), SigninActivity.class);
                mActivityResultLauncher.launch(intent);
            });
        }
        return view;
    }

    private void initUi() {
        scrollView = view.findViewById(R.id.nested_scroll);
        mShimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        rlProduct = view.findViewById(R.id.rl_product);
        rlLoading = view.findViewById(R.id.rl_loading);
        llOopsLogin = view.findViewById(R.id.ll_oops_login);
        llDataBoughtProduct = view.findViewById(R.id.ll_data_bought_product);
        llNoHaveData = view.findViewById(R.id.ll_no_have_data);
        rcvProductBought = view.findViewById(R.id.rcv_product_bought);
        btnGotoLogin = view.findViewById(R.id.btn_go_to_login);
    }

    private void initTotalPageProduct() {
        startShimmer();
        APIService.getService().getTotalPageBoughtProduct(Helper.getTokenLogin(requireContext())).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalPage = response.body();
                    if (totalPage == 0) {
                        stopShimmer();
                        isLastPage = true;
                        llNoHaveData.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (totalPage == 1) {
                        isLastPage = true;
                    }
                    initProductData();
                } else {
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });
    }

    private void initProductData() {
        APIService.getService().getBoughtProduct(Helper.getTokenLogin(requireContext()), 1).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stopShimmer();

                    mProductAdapter = new ProductAdapter(requireContext(), response.body(), false, BuyAgainFragment.this);
                    rcvProductBought.setAdapter(mProductAdapter);
                    rcvProductBought.setFocusable(false);
                    rcvProductBought.setNestedScrollingEnabled(false);
                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProductBought.setLayoutManager(layoutManager);

                    if (totalPage > 1) {
                        rlLoading.setVisibility(View.VISIBLE);
                        // set on scroll botom load more data
                        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                            if (isLoading || isLastPage) {
                                return;
                            }
                            if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                                //scroll view is at bottom
                                Log.e("ddd", "loadMoreItem: ");
                                isLoading = true;
                                currentPage++;
                                loadNextPage();
                            }
                        });
                    }
                } else {
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO, TOAST_DEFAULT);
            }
        });
    }

    private void loadNextPage() {
        APIService.getService().getBoughtProduct(Helper.getTokenLogin(requireContext()), currentPage).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mProductAdapter.addToListProduct(response.body());
                    isLoading = false;
                    if (currentPage >= totalPage) {
                        rlLoading.setVisibility(View.GONE);
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void startShimmer() {
        llOopsLogin.setVisibility(View.GONE);
        llDataBoughtProduct.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.startShimmer();
        rlLoading.setVisibility(View.GONE);
        llNoHaveData.setVisibility(View.GONE);
        rlProduct.setVisibility(View.GONE);
    }

    private void stopShimmer() {
        mShimmerFrameLayout.stopShimmer();
        mShimmerFrameLayout.setVisibility(View.GONE);
        rlProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void onclickProductItem(ProductModel product) {
        Intent intent = new Intent(requireContext(), DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        bundle.putBoolean(KEY_FROM_BUY_AGAIN, true);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
    }
}
