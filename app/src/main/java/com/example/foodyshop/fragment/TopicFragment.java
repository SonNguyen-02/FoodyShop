package com.example.foodyshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.foodyshop.service.DataService;
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
        if(requireActivity() instanceof MainActivity){
            mMainActivity = (MainActivity) requireActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topic, container, false);
        initUi();
        rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_left_in));
        if(mListTopic == null){
            shimmerFrameLayout.startShimmer();
            getListTopic();
        }else{
            shimmerFrameLayout.setVisibility(View.GONE);
            rcvTopic.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvTopic.setLayoutManager(layoutManager);

        if(topicAdapter == null){
            topicAdapter = new TopicAdapter(requireContext());
        }
        topicAdapter.setTopicModelList(mListTopic);
        rcvTopic.setAdapter(topicAdapter);
        if(mMainActivity != null){
            imgBack.setOnClickListener(view-> {
                rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_right_out));
                mMainActivity.removeFragmentFromMainLayout();
            });
            overlay.setOnClickListener(view -> {
                rlNavBar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.anim_right_out));
                mMainActivity.removeFragmentFromMainLayout();
            });
        }
        return view;
    }

    private void initUi() {
        imgBack = view.findViewById(R.id.img_back);
        rlNavBar = view.findViewById(R.id.rl_nab_bar);
        rcvTopic = view.findViewById(R.id.rcv_topic);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        overlay = view.findViewById(R.id.view_overlay);
    }

    private void getListTopic() {
        DataService service= APIService.getService();
        Call<List<TopicModel>> callback = service.getAllTopic(JWT.createToken());
        callback.enqueue(new Callback<List<TopicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopicModel>> call, @NonNull Response<List<TopicModel>> response) {
                if(response.isSuccessful()){
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rcvTopic.setVisibility(View.VISIBLE);
                    mListTopic = response.body();
                    topicAdapter.setTopicModelList(mListTopic);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TopicModel>> call, @NonNull Throwable t) {
            }
        });
    }
}
