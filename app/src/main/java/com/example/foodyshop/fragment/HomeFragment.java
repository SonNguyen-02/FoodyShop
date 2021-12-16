package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.adapter.TopicAdapter;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ProductAdapter.IOnclickProductItem {

    private View view;
    private NestedScrollView scrollView;
    private ShimmerFrameLayout sflHome;
    private LinearLayout llHome;
    private RecyclerView rcvTopic, rcvProductPopular, rcvProductSuggest;
    private RelativeLayout rlPopular, rlSuggest, rlLoading;

    private MainActivity mMainActivity;
    private List<TopicModel> mListTopic;
    private TopicAdapter topicAdapter;
    private ProductAdapter mSuggestProductAdapter;

    private boolean isLoadTopicSuccess, isLoadPrdPopularSuccess;

    // paging
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage;
    private int currentPage = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) requireActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUi();
        startShimmer();
        initTopicData();
        initProductPopularData();
        initTotalPageProductSuggest();

        return view;
    }

    private void initUi() {
        scrollView = view.findViewById(R.id.nested_scroll);
        sflHome = view.findViewById(R.id.sfl_home);
        llHome = view.findViewById(R.id.ll_home);
        rcvTopic = view.findViewById(R.id.rcv_topic);
        rcvProductPopular = view.findViewById(R.id.rcv_product_popular);
        rcvProductSuggest = view.findViewById(R.id.rcv_product_suggest);
        rlPopular = view.findViewById(R.id.rl_popular);
        rlSuggest = view.findViewById(R.id.rl_suggest);
        rlLoading = view.findViewById(R.id.rl_loading);
    }

    private void initTopicData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvTopic.setLayoutManager(layoutManager);
        topicAdapter = new TopicAdapter(requireContext(), TopicAdapter.ORIENTATION.HORIZONTAL);
        rcvTopic.setAdapter(topicAdapter);
        mListTopic = mMainActivity.getListTopic();
        if (mListTopic == null) {
            getTopicData();
        } else {
            topicAdapter.setTopicModelList(mListTopic);
            isLoadTopicSuccess = true;
        }
    }

    private void getTopicData() {
        APIService.getService().getAllTopic(JWT.createToken()).enqueue(new Callback<List<TopicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopicModel>> call, @NonNull Response<List<TopicModel>> response) {
                if (getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    mListTopic = response.body();
                    topicAdapter.setTopicModelList(mListTopic);
                    isLoadTopicSuccess = true;
                } else {
                    Log.e("ddd", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TopicModel>> call, @NonNull Throwable t) {
                Log.e("ddd", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initProductPopularData() {
        APIService.getService().getPopularProduct(JWT.createToken()).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        rlPopular.setVisibility(View.GONE);
                        isLoadPrdPopularSuccess = true;
                        return;
                    }
                    rlPopular.setVisibility(View.VISIBLE);
                    ProductAdapter adapter = new ProductAdapter(requireContext(), response.body(), false, HomeFragment.this);
                    rcvProductPopular.setAdapter(adapter);
                    rcvProductPopular.setFocusable(false);
                    rcvProductPopular.setNestedScrollingEnabled(false);
                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProductPopular.setLayoutManager(layoutManager);
                    isLoadPrdPopularSuccess = true;
                } else {
                    Log.e("ddd", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                Log.e("ddd", "onFailure: " + t.getMessage());
            }
        });

    }

    private void initTotalPageProductSuggest() {
        APIService.getService().getTotalPageSuggestProduct(JWT.createToken()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    totalPage = response.body();
                    if (totalPage == 0) {
                        isLastPage = true;
                        rlSuggest.setVisibility(View.GONE);
                        return;
                    }
                    if (totalPage == 1) {
                        isLastPage = true;
                    }
                    initProductSuggestData();
                } else {
                    Log.e("ddd", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.e("ddd", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initProductSuggestData() {
        APIService.getService().getSuggestProduct(JWT.createToken(), 1).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    mSuggestProductAdapter = new ProductAdapter(requireContext(), response.body(), false, HomeFragment.this);
                    rcvProductSuggest.setAdapter(mSuggestProductAdapter);
                    rcvProductSuggest.setFocusable(false);
                    rcvProductSuggest.setNestedScrollingEnabled(false);
                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), MainActivity.TOTAL_ITEM_PRODUCT);
                    rcvProductSuggest.setLayoutManager(layoutManager);

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
                    ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                if (getContext() == null) {
                    return;
                }
                ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
            }
        });
    }

    private void loadNextPage() {
        APIService.getService().getSuggestProduct(JWT.createToken(), currentPage).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    mSuggestProductAdapter.addToListProduct(response.body());
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
        llHome.setVisibility(View.GONE);
        sflHome.setVisibility(View.VISIBLE);
        if (!sflHome.isShimmerStarted()) {
            sflHome.startShimmer();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isLoadTopicSuccess || !isLoadPrdPopularSuccess) {
                    handler.postDelayed(this, 300);
                } else {
                    stopShimmer();
                }
            }
        }, 0);
    }

    private void stopShimmer() {
        if (sflHome.isShimmerStarted()) {
            sflHome.stopShimmer();
        }
        sflHome.setVisibility(View.GONE);
        llHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onclickProductItem(ProductModel product) {
        Intent intent = new Intent(requireContext(), DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
