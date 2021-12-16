package com.example.foodyshop.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodyshop.R;
import com.example.foodyshop.fragment.HomeFragment;
import com.example.foodyshop.fragment.OnboardingFragment;

public class OnboardingViewPagerAdapter extends FragmentStateAdapter {

    private final Context context;

    public OnboardingViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return OnboardingFragment.newInstance(
                    context.getString(R.string.title_onboarding_1),
                    context.getString(R.string.description_onboarding_1),
                    R.raw.lottie_splash_animation,
                    "#4CAF50");
        } else if (position == 1) {
            return OnboardingFragment.newInstance(
                    context.getString(R.string.title_onboarding_2),
                    context.getString(R.string.description_onboarding_2),
                    R.raw.lottie_messaging,
                    "#e37417");
        } else {
            return OnboardingFragment.newInstance(
                    context.getString(R.string.title_onboarding_3),
                    context.getString(R.string.description_onboarding_3),
                    R.raw.lottie_delivery_boy_bumpy_ride,
                    "#1b98d7");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
