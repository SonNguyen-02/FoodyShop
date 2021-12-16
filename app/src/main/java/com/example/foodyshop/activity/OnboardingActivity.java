package com.example.foodyshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.OnboardingViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jaeger.library.StatusBarUtil;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private TextView textSkip;
    private TextView textEnd;
    private ImageButton btnNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        initUi();

        mViewPager.setAdapter(new OnboardingViewPagerAdapter(this, this));
        mViewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(mTabLayout, mViewPager, (t, p) -> {
        }).attach();

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 2) {
                    btnNextStep.setVisibility(View.GONE);
                    textEnd.setVisibility(View.VISIBLE);
                    textSkip.setVisibility(View.GONE);
                } else {
                    btnNextStep.setVisibility(View.VISIBLE);
                    textEnd.setVisibility(View.GONE);
                    textSkip.setVisibility(View.VISIBLE);
                }
            }
        });

        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);

        textSkip.setOnClickListener(this::goToMainActivity);
        textEnd.setOnClickListener(this::goToMainActivity);

        ImageButton btnNextStep = findViewById(R.id.btn_next_step);
        btnNextStep.setOnClickListener(view -> {
            if (mViewPager.getCurrentItem() > mViewPager.getChildCount()) {
                goToMainActivity(view);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });
    }

    private void initUi() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.pageIndicator);
        textSkip = findViewById(R.id.text_skip);
        textEnd = findViewById(R.id.text_end);
        btnNextStep = findViewById(R.id.btn_next_step);
    }

    private void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}