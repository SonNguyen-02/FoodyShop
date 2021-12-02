package com.example.foodyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.service.APIService;
import com.example.foodyshop.service.DataService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCategoryActivity extends AppCompatActivity {

    private CategoryModel mCategory;

    private ImageView imgBack;
    private RelativeLayout rlFilter;
    private TextView tvSearch;

    private NestedScrollView scrollView;
    private ShimmerFrameLayout sflSaleProduct, sflProduct;
    private RelativeLayout rlSaleProduct, rlProduct;
    private RecyclerView rcvSaleProduct, rcvProduct;
    private RelativeLayout rlLoading;

    private ProductAdapter mProductAdapter;
    // paging
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);
        initUi();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalPage = 2;
            currentPage = 1;
            mCategory = (CategoryModel) bundle.getSerializable(Const.KEY_CATEGORY);
            imgBack.setOnClickListener(view -> finish());

            tvSearch.setOnClickListener(view -> addFragmentToMainLayout(new SearchFragment(mCategory), SearchFragment.class.getName()));
            tvSearch.setText(mCategory.getName());

            sflSaleProduct.startShimmer();
            sflProduct.startShimmer();

            initTotalPage();
            initSaleProductData();
            initProductData();
        }
    }

    private void initTotalPage() {
        DataService service = APIService.getService();
        Call<Integer> callbackTotalPage = service.getTotalPageProductInCategory(JWT.createToken(), mCategory.getId(), false);
        callbackTotalPage.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalPage = response.body();
                    if (totalPage == 1) {
                        isLastPage = true;
                    } else {
                        new Handler().postDelayed(() -> rlLoading.setVisibility(View.VISIBLE), 200);
                    }
                } else {
                    totalPage = 1;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
            }
        });
    }

    private void initSaleProductData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvSaleProduct.setLayoutManager(layoutManager);

        DataService service = APIService.getService();
        Call<List<ProductModel>> callback = service.getAllProductInCategory(JWT.createToken(), mCategory.getId(), 1, true);
        callback.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    showUi();
                    ProductAdapter adapter = new ProductAdapter(getApplicationContext(), response.body(), true);
                    rcvSaleProduct.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void initProductData() {
        DataService service = APIService.getService();
        Call<List<ProductModel>> callback = service.getAllProductInCategory(JWT.createToken(), mCategory.getId(), 1, false);
        callback.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    mProductAdapter = new ProductAdapter(getApplicationContext(), response.body(), false);
                    rcvProduct.setAdapter(mProductAdapter);
                    rcvProduct.setFocusable(false);
                    rcvProduct.setNestedScrollingEnabled(false);

                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProduct.setLayoutManager(layoutManager);

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
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void loadNextPage() {
        rlLoading.setVisibility(View.VISIBLE);
        DataService service = APIService.getService();
        Call<List<ProductModel>> callback = service.getAllProductInCategory(JWT.createToken(), mCategory.getId(), currentPage, false);
        callback.enqueue(new Callback<List<ProductModel>>() {
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

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        rlFilter = findViewById(R.id.rl_filter);
        tvSearch = findViewById(R.id.tv_search);

        scrollView = findViewById(R.id.nested_scroll);
        sflSaleProduct = findViewById(R.id.sfl_sale_product);
        sflProduct = findViewById(R.id.sfl_product);
        rlSaleProduct = findViewById(R.id.rl_sale_product);
        rlProduct = findViewById(R.id.rl_product);

        rcvSaleProduct = findViewById(R.id.rcv_sale_product);
        rcvProduct = findViewById(R.id.rcv_product);

        rlLoading = findViewById(R.id.rl_loading);
    }

    private void showUi() {
        sflSaleProduct.stopShimmer();
        sflSaleProduct.setVisibility(View.GONE);
        rlSaleProduct.setVisibility(View.VISIBLE);
        sflProduct.stopShimmer();
        sflProduct.setVisibility(View.GONE);
        rlProduct.setVisibility(View.VISIBLE);
    }

    private void addFragmentToMainLayout(Fragment fragment, String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_left_out, R.anim.anim_right_in, R.anim.anim_fade_out);
        transaction.add(R.id.fl_main_layout, fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public void removeFragmentFromMainLayout() {
        getSupportFragmentManager().popBackStack();
    }
}