package com.example.foodyshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.DetailCategoryActivity;
import com.example.foodyshop.activity.DetailTopicActivity;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.CategoryModel;
import com.example.foodyshop.model.TopicModel;

public class SearchFragment extends Fragment {

    private TopicModel mTopic;
    private CategoryModel mCategory;
    private DetailTopicActivity mDetailTopicActivity;
    private DetailCategoryActivity mDetailCategoryActivity;

    private View view;
    private ImageView imgBack;
    private EditText edtSearch;
    private MainActivity mMainActivity;

    public SearchFragment() {
    }

    public SearchFragment(TopicModel mTopic) {
        this.mTopic = mTopic;
    }

    public SearchFragment(CategoryModel mCategory) {
        this.mCategory = mCategory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) requireActivity();
        }
        if (requireActivity() instanceof DetailTopicActivity) {
            mDetailTopicActivity = (DetailTopicActivity) requireActivity();
        }
        if (requireActivity() instanceof DetailCategoryActivity) {
            mDetailCategoryActivity = (DetailCategoryActivity) requireActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initUi();
        if (mTopic != null) {
            edtSearch.setHint("Tìm kiếm trong " + mTopic.getName());
        }
        if (mCategory != null) {
            edtSearch.setHint("Tìm kiếm trong " + mCategory.getName());
        }
        edtSearch.requestFocus();
        Helper.showKeyboard(requireContext());
        if (mMainActivity != null) {
            imgBack.setOnClickListener(view -> {
                Helper.hideKeyboard(requireContext(), edtSearch);
                mMainActivity.removeFragmentFromMainLayout();
            });
        }
        if (mDetailTopicActivity != null) {
            imgBack.setOnClickListener(view -> {
                Helper.hideKeyboard(requireContext(), edtSearch);
                mDetailTopicActivity.removeFragmentFromMainLayout();
            });
        }
        if (mDetailCategoryActivity != null) {
            imgBack.setOnClickListener(view -> {
                Helper.hideKeyboard(requireContext(), edtSearch);
                mDetailCategoryActivity.removeFragmentFromMainLayout();
            });
        }

        return view;
    }

    private void initUi() {
        imgBack = view.findViewById(R.id.img_back);
        edtSearch = view.findViewById(R.id.edt_search);
    }


}
