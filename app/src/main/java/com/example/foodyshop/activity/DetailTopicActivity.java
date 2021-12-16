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
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.CategoryAdapter;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.Filter;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;
import com.example.foodyshop.service.DataService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTopicActivity extends AppCompatActivity implements CategoryAdapter.IOnclickCategoryItem, ProductAdapter.IOnclickProductItem, SearchFragment.IOnClickShowALl {

    private TopicModel mTopic;

    private ImageView imgBack, imgClearEdt;
    private TextView tvSearch;
    private RelativeLayout rlFilter, rlFilterContainer, rlFilterContent;
    private RadioButton rbAsc, rbNormal, rbDesc;
    private Spinner mSpinner;
    private Button btnApply, btnClear;

    private NestedScrollView scrollView;
    private ShimmerFrameLayout sflSaleProductCategory, sflProduct;
    private LinearLayout llSaleProductCategory;
    private RelativeLayout rlProduct, rlSaleProduct;
    private RecyclerView rcvSaleProduct, rcvCategory, rcvProduct;
    private RelativeLayout rlLoading, rlNoHaveData;

    private String search;

    private ProductAdapter mProductAdapter;
    private CategoryAdapter mCategoryAdapter;
    private static final Filter mFilter = new Filter();
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

            tvSearch.setOnClickListener(view -> {
                if (mCategoryAdapter != null && mCategoryAdapter.getLastCheckPos() != 0) {
                    addFragmentToMainLayout(SearchFragment.newInstance(mCategoryAdapter.getCurrentItem(), this), SearchFragment.class.getName());
                } else {
                    addFragmentToMainLayout(SearchFragment.newInstance(mTopic, this), SearchFragment.class.getName());
                }
            });
            tvSearch.setText(mTopic.getName());

            imgClearEdt.setOnClickListener(view -> {
                this.search = "";
                reloadProduct();
                CategoryModel category = mCategoryAdapter.getCurrentItem();
                if (category.getId() == -1) {
                    tvSearch.setText(mTopic.getName());
                } else {
                    tvSearch.setText(category.getName());
                }
                view.setVisibility(View.GONE);
            });

            initFilterContainer();

            sflSaleProductCategory.startShimmer();
            sflProduct.startShimmer();
            initTotalPage(true);
            initSaleProductData();
            initCategoryData();
            initProductData(true);
        } else {
            finish();
        }
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        tvSearch = findViewById(R.id.tv_search);
        imgClearEdt = findViewById(R.id.img_clear_edt);
        rlFilter = findViewById(R.id.rl_filter);

        rlFilterContainer = findViewById(R.id.rl_filter_container);
        rlFilterContent = findViewById(R.id.rl_filter_content);
        rbAsc = findViewById(R.id.rb_asc);
        rbNormal = findViewById(R.id.rb_normal);
        rbDesc = findViewById(R.id.rb_desc);
        mSpinner = findViewById(R.id.spinner);
        btnApply = findViewById(R.id.btn_apply);
        btnClear = findViewById(R.id.btn_clear);

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

    private void initFilterContainer() {
        rlFilter.setOnClickListener(view -> {
            if (rlFilterContainer.getVisibility() == View.VISIBLE) {
                hideFilterContainer();
            } else {
                showFilterContainer();
            }
        });
        rlFilterContainer.setOnClickListener(view -> hideFilterContainer());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_price, R.id.tv_item, mFilter.getItemsPriceRange());
        mSpinner.setAdapter(adapter);

        if (mFilter.getSortEnum() == Filter.SORT.ASC) {
            rbAsc.setChecked(true);
        }
        if (mFilter.getSortEnum() == Filter.SORT.NOR) {
            rbNormal.setChecked(true);
        }
        if (mFilter.getSortEnum() == Filter.SORT.DESC) {
            rbDesc.setChecked(true);
        }
        mSpinner.setSelection(mFilter.getIndex());

        btnApply.setOnClickListener(view -> {
            boolean isChange = false;
            if (mFilter.getIndex() != mSpinner.getSelectedItemPosition()) {
                mFilter.setPriceRange(mSpinner.getSelectedItemPosition());
                isChange = true;
            }
            if ((mFilter.getSortEnum() == Filter.SORT.ASC && !rbAsc.isChecked())
                    || (mFilter.getSortEnum() == Filter.SORT.NOR && !rbNormal.isChecked())
                    || (mFilter.getSortEnum() == Filter.SORT.DESC && !rbDesc.isChecked())) {
                isChange = true;
            }
            if (isChange) {
                hideFilterContainer();
                if (rbAsc.isChecked()) {
                    mFilter.setSortEnum(Filter.SORT.ASC);
                }
                if (rbNormal.isChecked()) {
                    mFilter.setSortEnum(Filter.SORT.NOR);
                }
                if (rbDesc.isChecked()) {
                    mFilter.setSortEnum(Filter.SORT.DESC);
                }
                reloadProduct();
            }
        });
        btnClear.setOnClickListener(view -> {
            mSpinner.setSelection(0);
            rbNormal.setChecked(true);
        });
    }

    private void reloadProduct() {
        CategoryModel category = mCategoryAdapter.getCurrentItem();
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
        setVisibilityProduct(false);
        if (category.getId() == -1) {
            initTotalPage(false);
        } else {
            initTotalPage(category);
        }
    }

    private void showFilterContainer() {
        rlFilterContainer.setEnabled(true);
        rlFilterContainer.setVisibility(View.VISIBLE);
        rlFilterContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_fade_in));
        rlFilterContent.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_right_in));
    }

    private void hideFilterContainer() {
        rlFilterContainer.setEnabled(false);
        rlFilterContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_fade_out));
        rlFilterContent.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_left_out));
        rlFilterContainer.setVisibility(View.GONE);
    }

    private void initSaleProductData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvSaleProduct.setLayoutManager(layoutManager);

        DataService service = APIService.getService();
        Call<List<ProductModel>> callback = service.getAllProductInTopic(JWT.createToken(), mTopic.getId(), 1, true, 0, Integer.MAX_VALUE, search, "nor");
        callback.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                showUi();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ProductAdapter adapter = new ProductAdapter(getApplicationContext(), response.body(), true, DetailTopicActivity.this);
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
        CategoryModel mCategory = new CategoryModel(-1, mTopic.getId(), "Tất cả");
        mCategory.setChecked(true);
        mTopic.getCategories().add(0, mCategory);
        mCategoryAdapter = new CategoryAdapter(this, mTopic.getCategories());
        rcvCategory.setAdapter(mCategoryAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        rcvCategory.setLayoutManager(layoutManager);
    }

    private final ViewTreeObserver.OnScrollChangedListener scrollListener = () -> {
        if (isLoading || isLastPage) {
            return;
        }
        if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
            //scroll view is at bottom
            Log.e("ddd", "loadMoreItem: ");
            isLoading = true;
            currentPage++;
            CategoryModel category = mCategoryAdapter.getCurrentItem();
            if (category.getId() == -1) {
                loadNextPage();
            } else {
                loadNextPage(category);
            }
        }
    };

    // All category
    private void initProductData(boolean isFirst) {
        APIService.getService().getAllProductInTopic(JWT.createToken(), mTopic.getId(), 1, false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
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
                    mProductAdapter = new ProductAdapter(getApplicationContext(), response.body(), false, DetailTopicActivity.this);
                    rcvProduct.setAdapter(mProductAdapter);
                    rcvProduct.setFocusable(false);
                    rcvProduct.setNestedScrollingEnabled(false);

                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProduct.setLayoutManager(layoutManager);

                    // set on scroll botom load more data
                    scrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
                } else {
                    scrollView.getViewTreeObserver().removeOnScrollChangedListener(scrollListener);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void initTotalPage(boolean isFirst) {
        rlNoHaveData.setVisibility(View.GONE);
        APIService.getService().getTotalPageProductInTopic(JWT.createToken(), mTopic.getId(), false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalAllPage = totalPage = response.body();
                    if (totalPage == 0) {
                        isLastPage = true;
                        rlNoHaveData.setVisibility(View.VISIBLE);
                        rlLoading.setVisibility(View.GONE);
                    } else if (totalPage == 1) {
                        isLastPage = true;
                        rlLoading.setVisibility(View.GONE);
                    } else {
                        new Handler().postDelayed(() -> rlLoading.setVisibility(View.VISIBLE), 200);
                    }
                    if (!isFirst) {
                        initProductData(false);
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
        APIService.getService().getAllProductInTopic(JWT.createToken(), mTopic.getId(), currentPage, false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
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
        APIService.getService().getAllProductInCategory(JWT.createToken(), mCategory.getId(), 1, false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (sflProduct.isShimmerStarted()) {
                        setVisibilityProduct(true);
                    }
                    mProductAdapter = new ProductAdapter(getApplicationContext(), response.body(), false, DetailTopicActivity.this);
                    rcvProduct.setAdapter(mProductAdapter);
                    rcvProduct.setFocusable(false);
                    rcvProduct.setNestedScrollingEnabled(false);

                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProduct.setLayoutManager(layoutManager);

                    // set on scroll botom load more data
                    scrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
                } else {
                    scrollView.getViewTreeObserver().removeOnScrollChangedListener(scrollListener);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
            }
        });
    }

    private void initTotalPage(@NonNull CategoryModel mCategory) {
        rlNoHaveData.setVisibility(View.GONE);
        APIService.getService().getTotalPageProductInCategory(JWT.createToken(), mCategory.getId(), false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search).enqueue(new Callback<Integer>() {
            final int currentPoint = currentPointLoad;

            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (currentPoint == currentPointLoad) {
                        totalPage = response.body();
                        Log.e("ddd", "onResponse: TotalPage: " + totalPage);
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
        APIService.getService().getAllProductInCategory(JWT.createToken(), mCategory.getId(), currentPage, false, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
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
            tvSearch.setText(mTopic.getName());
            initProductData(false);
        } else {
            tvSearch.setText(category.getName());
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

    @Override
    public void onClickShowAll(String search) {
        mFilter.clear();
        tvSearch.setText(search);
        this.search = search;
        imgClearEdt.setVisibility(View.VISIBLE);

        reloadProduct();
        removeFragmentFromMainLayout();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            removeFragmentFromMainLayout();
            return;
        }
        if (rlFilterContainer.getVisibility() == View.VISIBLE) {
            hideFilterContainer();
            return;
        }
        super.onBackPressed();
    }
}