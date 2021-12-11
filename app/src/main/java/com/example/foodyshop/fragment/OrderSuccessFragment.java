package com.example.foodyshop.fragment;

import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderModel;

public class OrderSuccessFragment extends Fragment {

    private View view;
    private TextView tvOrderCode, tvFullname, tvPhone, tvAddress, tvNote, tvTotalMoney;
    private Button btnBack;

    private final OrderModel mOrder;

    public OrderSuccessFragment(OrderModel order) {
        mOrder = order;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_success, container, false);
        initUi();
        tvOrderCode.setText(mOrder.getOrderCode());
        tvFullname.setText(mOrder.getName());
        tvPhone.setText(mOrder.getPhone());
        tvAddress.setText(mOrder.getAddress());
        tvNote.setText(mOrder.getNote());
        tvTotalMoney.setText(PRICE_FORMAT.format(mOrder.getTotalMoney()));

        btnBack.setOnClickListener(this::onClickBack);

        return view;
    }

    private void onClickBack(View view) {
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }

    private void initUi() {
        tvOrderCode = view.findViewById(R.id.tv_order_code);
        tvFullname = view.findViewById(R.id.tv_fullname);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAddress = view.findViewById(R.id.tv_address);
        tvNote = view.findViewById(R.id.tv_note);
        tvTotalMoney = view.findViewById(R.id.tv_total_money);
        btnBack = view.findViewById(R.id.btn_back);
    }

}
