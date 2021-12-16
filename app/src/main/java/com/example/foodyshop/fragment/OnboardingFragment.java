package com.example.foodyshop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodyshop.R;

public class OnboardingFragment extends Fragment {

    private String title;
    private String description;
    private String backgroundColor;
    private int imageResource;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    @NonNull
    public static OnboardingFragment newInstance(String title, String description, int imageResource, String backgroundColor) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, description);
        args.putInt(ARG_PARAM3, imageResource);
        args.putString(ARG_PARAM4, backgroundColor);
        OnboardingFragment fragment = new OnboardingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = requireArguments().getString(ARG_PARAM1);
        description = requireArguments().getString(ARG_PARAM2);
        imageResource = requireArguments().getInt(ARG_PARAM3);
        backgroundColor = requireArguments().getString(ARG_PARAM4);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_onboarding, container, false);
        Log.e("ddd", "onCreateView: " + title);
        TextView tvTitle = rootLayout.findViewById(R.id.text_onboarding_title);
        TextView tvDescription = rootLayout.findViewById(R.id.text_onboarding_description);
        LottieAnimationView image = rootLayout.findViewById(R.id.image_onboarding);
        RelativeLayout layout = rootLayout.findViewById(R.id.layout_container);
        View mFakeStatusBar = rootLayout.findViewById(R.id.fake_statusbar_view);
        tvTitle.setText(title);
        tvDescription.setText(description);
        image.setAnimation(imageResource);
        layout.setBackgroundColor(Color.parseColor(backgroundColor));
        mFakeStatusBar.setBackgroundColor(Color.parseColor(backgroundColor));

        return rootLayout;
    }
}
