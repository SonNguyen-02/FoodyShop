package com.example.foodyshop.adapter;

import static com.example.foodyshop.fragment.OrderFragment.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodyshop.fragment.OrderFragment;
import com.example.foodyshop.fragment.OrderTabFragment;
import com.example.foodyshop.model.OrderModel;

public class OrderViewPagerAdapter extends FragmentStateAdapter implements OrderTabFragment.IOnUpdatedOrder {

    private final OrderTabFragment tabCustomerConfirmed;
    private final OrderTabFragment tabOrderCancel;
    private final IPageRefreshStatus mIPageRefreshStatus;

    public OrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, IPageRefreshStatus mIPageRefreshStatus) {
        super(fragmentActivity);
        this.mIPageRefreshStatus = mIPageRefreshStatus;
        tabCustomerConfirmed = new OrderTabFragment(TAB.CUS_CONFIRMED, mIPageRefreshStatus);
        tabOrderCancel = new OrderTabFragment(TAB.ORD_CANCEL, mIPageRefreshStatus);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new OrderTabFragment(TAB.AD_ACCEPT, mIPageRefreshStatus, this);
            case 2:
                return new OrderTabFragment(TAB.WAIT_CUS_CONFIRM, mIPageRefreshStatus, this);
            case 3:
                return tabCustomerConfirmed;
            case 4:
                return new OrderTabFragment(TAB.SHIPPING, mIPageRefreshStatus);
            case 5:
                return new OrderTabFragment(TAB.ORD_DELIVERED, mIPageRefreshStatus);
            case 6:
                return tabOrderCancel;
            case 0:
            default:
                return new OrderTabFragment(TAB.WAIT_AD_CONFIRM, mIPageRefreshStatus, this);
        }
    }

    @Override
    public void onCancelSuccess(OrderModel orderModel) {
        tabOrderCancel.onUpdatedOrder(orderModel);
    }

    @Override
    public void onConfirmSuccess(OrderModel orderModel) {
        tabCustomerConfirmed.onUpdatedOrder(orderModel);
    }

    @Override
    public int getItemCount() {
        return 7;
    }


}
