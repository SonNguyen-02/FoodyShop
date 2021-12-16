package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_SIGN_IN_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.adapter.OrderViewPagerAdapter;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.transformer.ZoomOutPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class OrderFragment extends Fragment {

    private View view;
    private LinearLayout llOopsLogin, llOrder;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private Button btnGotoLogin;
    private boolean isCreate;
    private int lastUserId;

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                if (intent.getBooleanExtra(KEY_SIGN_IN_OK, false)) {
                    isCreate = true;
                    showDataOrder();
                }
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        if (Helper.getCurrentAccount() != null) {
            lastUserId = Helper.getCurrentAccount().getId();
        }
        initUi();
        initPage();
        isCreate = true;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCreate) {
            isCreate = false;
            return;
        }
        initPage();
    }

    private void initPage() {
        if (Helper.isLogin(requireContext())) {
            showDataOrder();
        } else {
            llOopsLogin.setVisibility(View.VISIBLE);
            llOrder.setVisibility(View.GONE);
            btnGotoLogin.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), SigninActivity.class);
                mActivityResultLauncher.launch(intent);
            });
        }
    }

    private void initUi() {
        llOopsLogin = view.findViewById(R.id.ll_oops_login);
        llOrder = view.findViewById(R.id.ll_order);
        mTabLayout = view.findViewById(R.id.tab_layout_order);
        mViewPager2 = view.findViewById(R.id.view_pager2_order);
        btnGotoLogin = view.findViewById(R.id.btn_go_to_login);
    }

    private void showDataOrder() {
        llOopsLogin.setVisibility(View.GONE);
        llOrder.setVisibility(View.VISIBLE);
        if (mViewPager2.getAdapter() != null) {
            if (lastUserId > 0 && lastUserId != Helper.getCurrentAccount().getId()) {
                lastUserId = Helper.getCurrentAccount().getId();
                mViewPager2.setCurrentItem(0);
            }
            return;
        }

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(requireActivity());
        mViewPager2.setAdapter(adapter);
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(TAB.WAIT_AD_CONFIRM.tabName);
                    break;
                case 1:
                    tab.setText(TAB.AD_CONFIRMED.tabName);
                    break;
                case 2:
                    tab.setText(TAB.WAIT_CUS_CONFIRM.tabName);
                    break;
                case 3:
                    tab.setText(TAB.CUS_CONFIRMED.tabName);
                    break;
                case 4:
                    tab.setText(TAB.SHIPPING.tabName);
                    break;
                case 6:
                    tab.setText(TAB.ORD_CANCEL.tabName);
                    break;
                case 5:
                default:
                    tab.setText(TAB.ORD_DELIVERED.tabName);
                    break;
            }
        }).attach();
    }

    public enum TAB {
        WAIT_AD_CONFIRM(0, "Chờ xác nhận", "0"),
        AD_CONFIRMED(1, "Shop đã xác nhận", "1"),
        WAIT_CUS_CONFIRM(2, "Chờ xác nhận lại", "2"),
        CUS_CONFIRMED(3, "Bạn đã xác nhận", "3"),
        SHIPPING(4, "Đang giao", "4"),
        ORD_DELIVERED(5, "Đã giao", "5"),
        ORD_CANCEL(6, "Đã hủy", "-1, -2");

        private final int index;
        private final String tabName;
        private final String requestStatus;

        TAB(int index, String tabName, String requestStatus) {
            this.index = index;
            this.tabName = tabName;
            this.requestStatus = requestStatus;
        }

        public int getIndex() {
            return index;
        }

        public String getTabName() {
            return tabName;
        }

        public String getRequestStatus() {
            return requestStatus;
        }
    }

}
