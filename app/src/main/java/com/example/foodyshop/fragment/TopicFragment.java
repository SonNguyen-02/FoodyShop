package com.example.foodyshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.adapter.TopicAdapter;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicFragment extends Fragment {

    private View view;
    private ImageView imgBack;
    private RelativeLayout rlNavBar;
    private RecyclerView rcvTopic;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View overlay;
    private MainActivity mMainActivity;

    private List<TopicModel> mListTopic;
    private TopicAdapter topicAdapter;

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
        view = inflater.inflate(R.layout.fragment_topic, container, false);
        initUi();
        rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_left_in));

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvTopic.setLayoutManager(layoutManager);

        if (topicAdapter == null) {
            topicAdapter = new TopicAdapter(requireContext(), TopicAdapter.ORIENTATION.VERTICAL);
        }
        rcvTopic.setAdapter(topicAdapter);

        if (mListTopic == null) {
            mListTopic = mMainActivity.getListTopic();
            shimmerFrameLayout.startShimmer();
            if (mListTopic == null) {
                initListTopic();
            } else {
                new Handler().postDelayed(() -> {
                    stopShimmer();
                    topicAdapter.setTopicModelList(mListTopic);
                }, 500);
            }
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            rcvTopic.setVisibility(View.VISIBLE);
            topicAdapter.setTopicModelList(mListTopic);
        }

        imgBack.setOnClickListener(view -> {
            rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_right_out));
            mMainActivity.removeFragmentFromMainLayout();
        });
        overlay.setOnClickListener(view -> {
            rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_right_out));
            mMainActivity.removeFragmentFromMainLayout();
        });
        return view;
    }

    private void initUi() {
        imgBack = view.findViewById(R.id.img_back);
        rlNavBar = view.findViewById(R.id.rl_nab_bar);
        rcvTopic = view.findViewById(R.id.rcv_topic);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        overlay = view.findViewById(R.id.view_overlay);
    }

    private void stopShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        rcvTopic.setVisibility(View.VISIBLE);
    }

    private void initListTopic() {
        APIService.getService().getAllTopic(JWT.createToken()).enqueue(new Callback<List<TopicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopicModel>> call, @NonNull Response<List<TopicModel>> response) {
                if (response.isSuccessful()) {
                    stopShimmer();
                    mListTopic = response.body();
                    topicAdapter.setTopicModelList(mListTopic);
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
}
