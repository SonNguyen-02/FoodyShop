package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_PRODUCT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.DetailProductActivity;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.adapter.ProductAdapter;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements ProductAdapter.IOnclickProductItem, Callback<List<ProductModel>> {

    private View view;
    private ImageView imgBack, imgSearch;
    private EditText edtSearch;
    private RelativeLayout rlResult, rlNoHaveData, rlLoading;
    private TextView tvSearchInfo, tvShowAll, tvNoResult;
    private RecyclerView rcvProduct;

    private TopicModel mTopic;
    private CategoryModel mCategory;
    private final IOnClickShowALl mIOnClickShowALl;
    private String search;
    private long lastClickSearch;

    private SearchFragment(IOnClickShowALl mIOnClickShowALl) {
        this.mIOnClickShowALl = mIOnClickShowALl;
    }

    private SearchFragment(TopicModel mTopic, IOnClickShowALl mIOnClickShowALl) {
        this.mTopic = mTopic;
        this.mIOnClickShowALl = mIOnClickShowALl;
    }

    private SearchFragment(CategoryModel mCategory, IOnClickShowALl mIOnClickShowALl) {
        this.mCategory = mCategory;
        this.mIOnClickShowALl = mIOnClickShowALl;
    }

    public interface IOnClickShowALl {
        void onClickShowAll(String search);
    }

    @NonNull
    public static SearchFragment newInstance(IOnClickShowALl mIOnClickShowALl) {
        return new SearchFragment(mIOnClickShowALl);
    }

    @NonNull
    public static SearchFragment newInstance(TopicModel mTopic, IOnClickShowALl mIOnClickShowALl) {
        return new SearchFragment(mTopic, mIOnClickShowALl);
    }

    @NonNull
    public static SearchFragment newInstance(CategoryModel mCategory, IOnClickShowALl mIOnClickShowALl) {
        return new SearchFragment(mCategory, mIOnClickShowALl);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initUi();
        hideAll();
        if (mTopic != null) {
            edtSearch.setHint("Tìm kiếm trong " + mTopic.getName());
        }
        if (mCategory != null) {
            edtSearch.setHint("Tìm kiếm trong " + mCategory.getName());
        }
        edtSearch.requestFocus();
        Helper.showKeyboard(requireContext());
        imgBack.setOnClickListener(view -> {
            Helper.hideKeyboard(requireContext(), edtSearch);
            requireActivity().onBackPressed();
        });
        imgSearch.setOnClickListener(view -> {
            if (System.currentTimeMillis() - lastClickSearch < 1000) {
                return;
            }
            lastClickSearch = System.currentTimeMillis();
            String search = edtSearch.getText().toString().trim();
            if (search.isEmpty()) {
                edtSearch.requestFocus();
                ToastCustom.notice(requireContext(), "Hãy nhập để tìm kiếm!", ToastCustom.INFO).show();
            } else {
                if (this.search.equals(search)) {
                    return;
                }
                this.search = search;
                edtSearch.clearFocus();
                Helper.hideKeyboard(requireContext(), edtSearch);
                if (mTopic != null) {
                    searchProductInTopic();
                    return;
                }
                if (mCategory != null) {
                    searchProductInCategory();
                    return;
                }
                searchProductAll();
            }
        });
        return view;
    }

    private void initUi() {
        imgBack = view.findViewById(R.id.img_back);
        imgSearch = view.findViewById(R.id.img_search);
        edtSearch = view.findViewById(R.id.edt_search);
        rlResult = view.findViewById(R.id.rl_result);
        rlNoHaveData = view.findViewById(R.id.rl_no_have_data);
        rlLoading = view.findViewById(R.id.rl_loading);
        tvSearchInfo = view.findViewById(R.id.tv_search_info);
        tvShowAll = view.findViewById(R.id.tv_show_all);
        tvNoResult = view.findViewById(R.id.tv_no_result);
        rcvProduct = view.findViewById(R.id.rcv_product);
    }

    private void showLoading() {
        rlLoading.setVisibility(View.VISIBLE);
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.GONE);
    }

    private void hideAll() {
        rlLoading.setVisibility(View.GONE);
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.GONE);
    }

    private void showNoHaveData() {
        rlLoading.setVisibility(View.GONE);
        rlResult.setVisibility(View.GONE);
        rlNoHaveData.setVisibility(View.VISIBLE);
        tvNoResult.setText(MessageFormat.format(getString(R.string.no_result_for), search));
    }

    private void showResult() {
        rlLoading.setVisibility(View.GONE);
        rlResult.setVisibility(View.VISIBLE);
        rlNoHaveData.setVisibility(View.GONE);
        tvSearchInfo.setText(MessageFormat.format(getString(R.string.result_for), search));
    }

    private void searchProductInTopic() {
        showLoading();
        APIService.getService()
                .getAllProductInTopic(JWT.createToken(), mTopic.getId(), 1, false, 0, Integer.MAX_VALUE, search, "")
                .enqueue(this);
    }

    private void searchProductInCategory() {
        showLoading();
        APIService.getService()
                .getAllProductInCategory(JWT.createToken(), mCategory.getId(), 1, false, 0, Integer.MAX_VALUE, search, "")
                .enqueue(this);
    }

    private void searchProductAll() {
        showLoading();
        APIService.getService()
                .getSearchProduct(JWT.createToken(), 1, 0, Integer.MAX_VALUE, search, "")
                .enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
        if (getContext() == null) return;
        if (response.isSuccessful() && response.body() != null) {
            ProductAdapter mProductAdapter = new ProductAdapter(requireContext(), response.body(), false, SearchFragment.this);
            rcvProduct.setAdapter(mProductAdapter);
            rcvProduct.setFocusable(false);
            rcvProduct.setNestedScrollingEnabled(false);

            GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), MainActivity.TOTAL_ITEM_PRODUCT);
            rcvProduct.setLayoutManager(layoutManager);

            if (response.body().size() == 10) {
                tvShowAll.setVisibility(View.VISIBLE);
                tvShowAll.setOnClickListener(v -> mIOnClickShowALl.onClickShowAll(search));
            } else {
                tvShowAll.setVisibility(View.GONE);
            }
            if (!response.body().isEmpty()) {
                showResult();
                return;
            }
        }
        showNoHaveData();
    }


    @Override
    public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
        if (getContext() == null) return;
        hideAll();
        ToastCustom.notice(requireContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
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
