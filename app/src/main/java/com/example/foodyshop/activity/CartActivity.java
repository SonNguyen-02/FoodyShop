package com.example.foodyshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.CartViewPagerAdapter;
import com.example.foodyshop.fragment.ProductInCartFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CartActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ImageView imgBack;
    private ViewPager2 mViewPager2;
    private TextView tvEdit;
    private boolean isEdit;
    private ProductInCartFragment productInCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        productInCartFragment = new ProductInCartFragment();

        initUi();

        imgBack.setOnClickListener(view -> finish());
        tvEdit.setOnClickListener(view -> {
            productInCartFragment.setBottomBox(isEdit);
            if (isEdit) {
                tvEdit.setText(R.string.confirm);
                isEdit = false;
            } else {
                tvEdit.setText(R.string.edit);
                isEdit = true;
            }
        });
        CartViewPagerAdapter adapter = new CartViewPagerAdapter(this, productInCartFragment);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setUserInputEnabled(false);

        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Tất cả");
            } else {
                tab.setText("Mua lần nữa");
            }
        }).attach();
    }

    private void initUi() {
        mTabLayout = findViewById(R.id.tab_layout_cart);
        mViewPager2 = findViewById(R.id.view_pager2_cart);
        imgBack = findViewById(R.id.img_back);
        tvEdit = findViewById(R.id.tv_edit);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public TextView getTvEdit() {
        return tvEdit;
    }
}