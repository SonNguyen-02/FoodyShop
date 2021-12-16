package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_SEARCH;
import static com.example.foodyshop.config.Const.KEY_TOPIC;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

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
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.fragment.TopicFragment;
import com.example.foodyshop.fragment.SearchFragment;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.helper.JWT;
import com.example.foodyshop.model.TopicModel;
import com.example.foodyshop.service.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TopicAdapter.IOnclickTopicItem, SearchFragment.IOnClickShowALl {

    public static final int TOTAL_ITEM_PRODUCT = 2;
    public static int HEIGHT_DEVICE;
    public static int WIDTH_DEVICE;

    private ImageView imgMenu, imgSearch;
    private TextView tvTitle, tvIndicator;
    private RelativeLayout rlSearch, rlCart;
    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    private TopicFragment mTopicFragment;

    private List<TopicModel> mListTopic;

    private long lastAddFragment;
    private boolean isBackPress;
    private boolean isInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Helper.isLogin(getApplicationContext())) {
            Helper.setCurrentAccount(Helper.getUserInfo(getApplicationContext()));
        }
        isInit = true;
        initUi();
        initListTopic();
        mTopicFragment = new TopicFragment();
        mBottomNavigationView.setOnItemSelectedListener(this::onItemSelected);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setUserInputEnabled(false);

        rlSearch.setOnClickListener(view -> addFragmentToMainLayout(SearchFragment.newInstance(this), SearchFragment.class.getName()));
        imgSearch.setOnClickListener(view -> addFragmentToMainLayout(SearchFragment.newInstance(this), SearchFragment.class.getName()));
        imgMenu.setOnClickListener(view -> addFragmentToMainLayout(mTopicFragment, TopicFragment.class.getName()));
        rlCart.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
        });
        initRlCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackPress = false;
        if (isInit) {
            isInit = false;
            return;
        }
        initRlCart();
    }

    private void initUi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        HEIGHT_DEVICE = metrics.heightPixels;
        WIDTH_DEVICE = metrics.widthPixels;
        imgMenu = findViewById(R.id.img_menu_category);
        imgSearch = findViewById(R.id.img_search);
        tvTitle = findViewById(R.id.tv_title);
        tvIndicator = findViewById(R.id.tv_indicator);
        rlSearch = findViewById(R.id.rl_search);
        rlCart = findViewById(R.id.rl_cart);
        mViewPager2 = findViewById(R.id.view_pager2_main);
        mBottomNavigationView = findViewById(R.id.menu_nav);
    }

    private void initRlCart() {
        int totalCart = Helper.getTotalProductInCart(getApplicationContext());
        if (totalCart > 0) {
            tvIndicator.setVisibility(View.VISIBLE);
            tvIndicator.setText(String.valueOf(totalCart));
        } else {
            tvIndicator.setVisibility(View.GONE);
        }
    }

    private void addFragmentToMainLayout(Fragment fragment, String name) {
        if (System.currentTimeMillis() - lastAddFragment < 1000) {
            return;
        }
        lastAddFragment = System.currentTimeMillis();
        isBackPress = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_left_out, R.anim.anim_right_in, R.anim.anim_fade_out);
        transaction.add(R.id.fl_main_layout, fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public void removeFragmentFromMainLayout() {
        getSupportFragmentManager().popBackStack();
    }

    public List<TopicModel> getListTopic() {
        return mListTopic;
    }

    private void initListTopic() {
        APIService.getService().getAllTopic(JWT.createToken()).enqueue(new Callback<List<TopicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopicModel>> call, @NonNull Response<List<TopicModel>> response) {
                if (response.isSuccessful()) {
                    mListTopic = response.body();
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
            isBackPress = false;
            setSearchBarVisibility(id == R.id.menu_home);
            switch (id) {
                case R.id.menu_home:
                    mViewPager2.setCurrentItem(0, false);
                    break;
                case R.id.menu_favorite:
                    mViewPager2.setCurrentItem(1, false);
                    tvTitle.setText(R.string.menu_favorite);
                    break;
                case R.id.menu_order:
                    mViewPager2.setCurrentItem(2, false);
                    tvTitle.setText(R.string.menu_order);
                    break;
                case R.id.menu_account:
                    mViewPager2.setCurrentItem(3, false);
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

    @Override
    public void onClickShowAll(String search) {
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SEARCH, search);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isBackPress = false;
            removeFragmentFromMainLayout();
            return;
        }
        if (isBackPress) {
            isBackPress = false;
            super.onBackPressed();
        } else {
            ToastCustom.notice(this, "Nhấn back thêm lần nữa để thoát", ToastCustom.NONE, TOAST_DEFAULT).show();
            isBackPress = true;
        }
    }
}