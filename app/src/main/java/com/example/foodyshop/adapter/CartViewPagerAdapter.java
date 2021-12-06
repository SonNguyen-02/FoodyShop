package com.example.foodyshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodyshop.fragment.BuyAgainFragment;
import com.example.foodyshop.fragment.ProductInCartFragment;

public class CartViewPagerAdapter extends FragmentStateAdapter {

    private final ProductInCartFragment productInCartFragment;

    public CartViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductInCartFragment productInCartFragment) {
        super(fragmentActivity);
        this.productInCartFragment = productInCartFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return productInCartFragment;
        }
        return new BuyAgainFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
