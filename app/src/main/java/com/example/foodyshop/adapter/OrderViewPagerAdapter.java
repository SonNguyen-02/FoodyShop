package com.example.foodyshop.adapter;

import static com.example.foodyshop.fragment.OrderFragment.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodyshop.fragment.OrderTabFragment;
import com.example.foodyshop.model.OrderModel;

public class OrderViewPagerAdapter extends FragmentStateAdapter implements OrderTabFragment.IOnUpdatedOrder {

    private final OrderTabFragment tabCustomerConfirmed;
    private final OrderTabFragment tabOrderCancel;

    public OrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        tabCustomerConfirmed = new OrderTabFragment(TAB.CUS_CONFIRMED);
        tabOrderCancel = new OrderTabFragment(TAB.ORD_CANCEL);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new OrderTabFragment(TAB.AD_CONFIRMED, this);
            case 2:
                return new OrderTabFragment(TAB.WAIT_CUS_CONFIRM, this);
            case 3:
                return tabCustomerConfirmed;
            case 4:
                return new OrderTabFragment(TAB.SHIPPING);
            case 5:
                return new OrderTabFragment(TAB.ORD_DELIVERED);
            case 6:
                return tabOrderCancel;
            case 0:
            default:
                return new OrderTabFragment(TAB.WAIT_AD_CONFIRM, this);
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
