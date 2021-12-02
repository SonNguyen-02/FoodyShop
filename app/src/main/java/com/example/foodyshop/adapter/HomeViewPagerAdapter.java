package com.example.foodyshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodyshop.fragment.AccountFragment;
import com.example.foodyshop.fragment.HomeFragment;
import com.example.foodyshop.fragment.NotificationFragment;
import com.example.foodyshop.fragment.OrderFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new OrderFragment();
            case 2:
                return new NotificationFragment();
            case 3:
                return new AccountFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
