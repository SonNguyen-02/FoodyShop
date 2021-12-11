package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_ORDER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderModel;

public class OrderDetailActivity extends AppCompatActivity {

    private OrderModel mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mOrder = (OrderModel) bundle.getSerializable(KEY_ORDER);
            Log.e("ddd", "onCreate: " + mOrder.getName());
        } else {
            finish();
        }
    }
}