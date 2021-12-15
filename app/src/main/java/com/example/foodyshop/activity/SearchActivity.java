package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.KEY_SEARCH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.CategoryAdapter;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.Filter;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.service.APIService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.IOnclickProductItem {

    private ImageView imgBack;
    private TextView tvSearch;
    private RelativeLayout rlFilter, rlFilterContainer, rlFilterContent;
    private RadioButton rbAsc, rbNormal, rbDesc;
    private Spinner mSpinner;
    private Button btnApply, btnClear;

    private NestedScrollView scrollView;
    private ShimmerFrameLayout sflProduct;
    private RelativeLayout rlResult, rlNoHaveData, rlLoading;
    private TextView tvSearchInfo, tvNoResult;
    private RecyclerView rcvProduct;

    private String search;
    private ProductAdapter mProductAdapter;

    private static final Filter mFilter = new Filter();
    // paging
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initUi();
            this.search = bundle.getString(KEY_SEARCH);
            imgBack.setOnClickListener(view -> finish());

            tvSearch.setOnClickListener(view -> {
                addFragmentToMainLayout(SearchFragment.newInstance(search -> {
                    this.search = search;
                    tvSearch.setText(search);
                    reloadProduct();
                    removeFragmentFromMainLayout();
                }), SearchFragment.class.getSimpleName());
            });
            tvSearch.setText(search);

            initFilterContainer();
            initTotalPage();
        } else {
            finish();
        }
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        tvSearch = findViewById(R.id.tv_search);
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
        sflProduct = findViewById(R.id.sfl_product);
        rlResult = findViewById(R.id.rl_result);
        rlNoHaveData = findViewById(R.id.rl_no_have_data);
        rlLoading = findViewById(R.id.rl_loading);
        tvSearchInfo = findViewById(R.id.tv_search_info);
        tvNoResult = findViewById(R.id.tv_no_result);
        rcvProduct = findViewById(R.id.rcv_product);
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
        currentPage = 1;
        totalPage = 1;
        isLastPage = false;
        isLoading = false;
        initTotalPage();
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

    private void showLoading() {
        sflProduct.setVisibility(View.VISIBLE);
        sflProduct.startShimmer();
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.GONE);
    }

    private void hideAll() {
        sflProduct.stopShimmer();
        sflProduct.setVisibility(View.GONE);
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.GONE);
    }

    private void showNoHaveData() {
        sflProduct.stopShimmer();
        sflProduct.setVisibility(View.GONE);
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.VISIBLE);
        tvNoResult.setText(MessageFormat.format(getString(R.string.no_result_for), search));
    }

    private void showResult() {
        sflProduct.stopShimmer();
        sflProduct.setVisibility(View.GONE);
        rlResult.setVisibility(View.VISIBLE);
        rlNoHaveData.setVisibility(View.GONE);
        tvSearchInfo.setText(MessageFormat.format(getString(R.string.result_for), search));
    }

    private void initTotalPage() {
        showLoading();
        APIService.getService().getTotalPageSearchProduct(JWT.createToken(), mFilter.getMinPrice(), mFilter.getMaxPrice(), search).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalPage = response.body();
                    if (totalPage == 0) {
                        isLastPage = true;
                        showNoHaveData();
                    } else if (totalPage == 1) {
                        isLastPage = true;
                    } else {
                        new Handler().postDelayed(() -> rlLoading.setVisibility(View.VISIBLE), 200);
                    }
                    initProductData();
                } else {
                    hideAll();
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                hideAll();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });
    }

    private void initProductData() {
        APIService.getService().getSearchProduct(JWT.createToken(), 1, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showResult();
                    mProductAdapter = new ProductAdapter(getApplicationContext(), response.body(), false, SearchActivity.this);
                    rcvProduct.setAdapter(mProductAdapter);
                    rcvProduct.setFocusable(false);
                    rcvProduct.setNestedScrollingEnabled(false);

                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProduct.setLayoutManager(layoutManager);

                    if (isLastPage) return;
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
                } else {
                    hideAll();
                    ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                hideAll();
                ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });
    }

    private void loadNextPage() {
        rlLoading.setVisibility(View.VISIBLE);
        APIService.getService().getSearchProduct(JWT.createToken(), currentPage, mFilter.getMinPrice(), mFilter.getMaxPrice(), search, mFilter.getOrderPrice()).enqueue(new Callback<List<ProductModel>>() {
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
    public void onclickProductItem(ProductModel product) {
        Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        intent.putExtras(bundle);
        startActivity(intent);
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