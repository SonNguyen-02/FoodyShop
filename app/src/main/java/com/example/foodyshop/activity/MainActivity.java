package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_TOPIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.adapter.HomeViewPagerAdapter;
import com.example.foodyshop.adapter.TopicAdapter;
import com.example.foodyshop.fragment.TopicFragment;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.TopicModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TopicAdapter.IOnclickTopicItem {

    public static final int TOTAL_ITEM_PRODUCT = 2;
    public static int HEIGHT_DEVICE;
    public static int WIDTH_DEVICE;


    private ImageView imgMenu, imgSearch, imgCart;
    private TextView tvTitle;
    private RelativeLayout rlSearch;
    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    private TopicFragment mTopicFragment;

//    private boolean isClickBottomNav;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init account if login
        if (Helper.isLogin(getApplicationContext())) {
            Helper.currentAccount = Helper.getUserInfo(getApplicationContext());
            Log.e("ddd", "onCreate: " + Helper.currentAccount);
        }

        initUi();
        mTopicFragment = new TopicFragment();
        mBottomNavigationView.setOnItemSelectedListener(this::onItemSelected);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setUserInputEnabled(false);
//        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if(isClickBottomNav){
//                    isClickBottomNav = false;
//                    return;
//                }
//                setSearchBarVisibility(position == 0);
//                switch (position) {
//                    case 0:
//                        mBottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
//                        break;
//                    case 1:
//                        mBottomNavigationView.getMenu().findItem(R.id.menu_order).setChecked(true);
//                        tvTitle.setText(R.string.menu_order);
//                        break;
//                    case 2:
//                        mBottomNavigationView.getMenu().findItem(R.id.menu_notification).setChecked(true);
//                        tvTitle.setText(R.string.menu_notification);
//                        break;
//                    case 3:
//                        mBottomNavigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
//                        tvTitle.setText(R.string.menu_account);
//                        break;
//                }
//            }
//        });

        rlSearch.setOnClickListener(view -> addFragmentToMainLayout(new SearchFragment(), SearchFragment.class.getName()));
        imgSearch.setOnClickListener(view -> addFragmentToMainLayout(new SearchFragment(), SearchFragment.class.getName()));
        imgMenu.setOnClickListener(view -> addFragmentToMainLayout(mTopicFragment, TopicFragment.class.getName()));
        imgCart.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
        });

        Intent intent = new Intent(this, EnterOtpActivity.class);
        startActivity(intent);

    }

    private void initUi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        HEIGHT_DEVICE = metrics.heightPixels;
        WIDTH_DEVICE = metrics.widthPixels;
        imgMenu = findViewById(R.id.img_menu_category);
        imgSearch = findViewById(R.id.img_search);
        imgCart = findViewById(R.id.img_cart);
        tvTitle = findViewById(R.id.tv_title);
        rlSearch = findViewById(R.id.rl_search);
        mViewPager2 = findViewById(R.id.view_pager2_main);
        mBottomNavigationView = findViewById(R.id.menu_nav);
    }

    private void addFragmentToMainLayout(Fragment fragment, String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_left_out, R.anim.anim_right_in, R.anim.anim_fade_out);
        transaction.add(R.id.fl_main_layout, fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public void removeFragmentFromMainLayout() {
        getSupportFragmentManager().popBackStack();
    }

    private void setSearchBarVisibility(boolean show) {
        if (show) {
            tvTitle.setVisibility(View.GONE);
            imgSearch.setVisibility(View.GONE);
            rlSearch.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.VISIBLE);
            rlSearch.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != mBottomNavigationView.getSelectedItemId()) {
//            isClickBottomNav = true;
            setSearchBarVisibility(id == R.id.menu_home);
            switch (id) {
                case R.id.menu_home:
                    mViewPager2.setCurrentItem(0);
                    break;
                case R.id.menu_order:
                    mViewPager2.setCurrentItem(1);
                    tvTitle.setText(R.string.menu_order);
                    break;
                case R.id.menu_notification:
                    mViewPager2.setCurrentItem(2);
                    tvTitle.setText(R.string.menu_notification);
                    break;
                case R.id.menu_account:
                    mViewPager2.setCurrentItem(3);
                    tvTitle.setText(R.string.menu_account);
                    break;
            }
        }
        return true;
    }

    @Override
    public void onclickTopicItem(TopicModel topic) {
        Intent intent = new Intent(this, DetailTopicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TOPIC, topic);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}