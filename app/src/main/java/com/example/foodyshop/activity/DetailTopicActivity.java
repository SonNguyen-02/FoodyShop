package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.KEY_TOPIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.CategoryAdapter;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;
import com.example.foodyshop.service.DataService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTopicActivity extends AppCompatActivity implements CategoryAdapter.IOnclickCategoryItem, ProductAdapter.IOnclickProductItem {

    private TopicModel mTopic;

    private ImageView imgBack;
    private RelativeLayout rlFilter;
    private TextView tvSearch;

    private NestedScrollView scrollView;
    private ShimmerFrameLayout sflSaleProductCategory, sflProduct;
    private LinearLayout llSaleProductCategory;
    private RelativeLayout rlProduct, rlSaleProduct;
    private RecyclerView rcvSaleProduct, rcvCategory, rcvProduct;
    private RelativeLayout rlLoading, rlNoHaveData;

    private ProductAdapter mProductAdapter;
    // paging
    private boolean isLoading;
    private boolean isLastPage;
    private int totalAllPage;
    private int totalPage;
    private int currentPage;
    private int currentPointLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_topic);
        initUi();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalPage = 2;
            currentPage = 1;
            mTopic = (TopicModel) bundle.getSerializable(KEY_TOPIC);
            imgBack.setOnClickListener(view -> finish());

            tvSearch.setOnClickListener(view -> addFragmentToMainLayout(new SearchFragment(mTopic), SearchFragment.class.getName()));
            tvSearch.setText(mTopic.getName());
            rlFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                }
            });

            sflSaleProductCategory.startShimmer();
            sflProduct.startShimmer();
            initTotalPage();
            initSaleProductData();
            initCategoryData();
            initProductData(true);
        } else {
            finish();
        }
    }

    private void initSaleProductData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvSaleProduct.setLayoutManager(layoutManager);

        DataService service = APIService.getService();
        Call<List<ProductModel>> callback = service.getAllProductInTopic(JWT.createToken(), mTopic.getId(), 1, true);
        callback.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                showUi();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ProductAdapter adapter = new ProductAdapter(DetailTopicActivity.this, response.body(), true);
                    rcvSaleProduct.setAdapter(adapter);
                } else {
                    rlSaleProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void initCategoryData() {
        mTopic.getCategories().add(0, new CategoryModel(-1, mTopic.getId(), "Tất cả"));
        CategoryAdapter adapter = new CategoryAdapter(this, mTopic.getCategories());
        rcvCategory.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        rcvCategory.setLayoutManager(layoutManager);
    }

    // All category
    private void initProductData(boolean isFirst) {
        APIService.getService().getAllProductInTopic(JWT.createToken(), mTopic.getId(), 1, false).enqueue(new Callback<List<ProductModel>>() {
            final int currentPoint = currentPointLoad;

            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (currentPoint != currentPointLoad) {
                        return;
                    }
                    if (!isFirst && sflProduct.isShimmerStarted()) {
                        setVisibilityProduct(true);
                    }
                    if (totalAllPage > 1) {
                        rlLoading.setVisibility(View.VISIBLE);
                    }
                    mProductAdapter = new ProductAdapter(DetailTopicActivity.this, response.body(), false);
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

    private void initTotalPage() {
        rlNoHaveData.setVisibility(View.GONE);
        APIService.getService().getTotalPageProductInTopic(JWT.createToken(), mTopic.getId(), false).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalAllPage = totalPage = response.body();
                    if (totalPage == 0) {
                        rlNoHaveData.setVisibility(View.VISIBLE);
                    }
                    if (totalPage <= 1) {
                        isLastPage = true;
                        rlLoading.setVisibility(View.GONE);
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

    private void loadNextPage() {
        rlLoading.setVisibility(View.VISIBLE);
        APIService.getService().getAllProductInTopic(JWT.createToken(), mTopic.getId(), currentPage, false).enqueue(new Callback<List<ProductModel>>() {
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

    // a Category
    private void initProductData(@NonNull CategoryModel mCategory) {
        APIService.getService().getAllProductInCategory(JWT.createToken(), mCategory.getId(), 1, false).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (sflProduct.isShimmerStarted()) {
                        setVisibilityProduct(true);
                    }
                    mProductAdapter = new ProductAdapter(DetailTopicActivity.this, response.body(), false);
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
                            loadNextPage(mCategory);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void initTotalPage(@NonNull CategoryModel mCategory) {
        rlNoHaveData.setVisibility(View.GONE);
        APIService.getService().getTotalPageProductInCategory(JWT.createToken(), mCategory.getId(), false).enqueue(new Callback<Integer>() {
            final int currentPoint = currentPointLoad;

            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                Log.e("ddd", "onResponse: " + currentPoint + " : " + currentPointLoad);
                if (response.isSuccessful() && response.body() != null) {
                    if (currentPoint == currentPointLoad) {
                        totalPage = response.body();
                        if (totalPage == 0) {
                            isLastPage = true;
                            rlLoading.setVisibility(View.GONE);
                            rlNoHaveData.setVisibility(View.VISIBLE);
                            mProductAdapter.setListProduct(null);
                            setVisibilityProduct(true);
                            return;
                        }
                        if (totalPage == 1) {
                            isLastPage = true;
                            rlLoading.setVisibility(View.GONE);
                        } else {
                            new Handler().postDelayed(() -> rlLoading.setVisibility(View.VISIBLE), 200);
                        }
                        initProductData(mCategory);
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

    private void loadNextPage(@NonNull CategoryModel mCategory) {
        rlLoading.setVisibility(View.VISIBLE);
        APIService.getService().getAllProductInCategory(JWT.createToken(), mCategory.getId(), currentPage, false).enqueue(new Callback<List<ProductModel>>() {
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
        sflSaleProductCategory = findViewById(R.id.sfl_sale_product_category);
        sflProduct = findViewById(R.id.sfl_product);
        llSaleProductCategory = findViewById(R.id.ll_sale_product_category);
        rlProduct = findViewById(R.id.rl_product);
        rlSaleProduct = findViewById(R.id.rl_sale_product);

        rcvSaleProduct = findViewById(R.id.rcv_sale_product);
        rcvCategory = findViewById(R.id.rcv_category);
        rcvProduct = findViewById(R.id.rcv_product);

        rlLoading = findViewById(R.id.rl_loading);
        rlNoHaveData = findViewById(R.id.rl_no_have_data);
    }

    private void showUi() {
        sflSaleProductCategory.stopShimmer();
        sflSaleProductCategory.setVisibility(View.GONE);
        llSaleProductCategory.setVisibility(View.VISIBLE);
        setVisibilityProduct(true);
    }

    private void setVisibilityProduct(boolean isVisible) {
        if (isVisible) {
            sflProduct.stopShimmer();
            sflProduct.setVisibility(View.GONE);
            rlProduct.setVisibility(View.VISIBLE);
        } else {
            sflProduct.startShimmer();
            sflProduct.setVisibility(View.VISIBLE);
            rlProduct.setVisibility(View.GONE);
        }
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

    @Override
    public void onclickCategoryItem(@NonNull CategoryModel category) {
        currentPointLoad++;
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
        setVisibilityProduct(false);
        if (category.getId() == -1) {
            totalPage = totalAllPage;
            initProductData(false);
        } else {
            initTotalPage(category);
        }

    }

    @Override
    public void onclickProductItem(ProductModel product) {
        Intent intent = new Intent(this, DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}