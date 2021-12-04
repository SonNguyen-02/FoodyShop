package com.example.foodyshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodyshop.R;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.helper.Helper;

public class AccountFragment extends Fragment {

    private View view;
    private TextView tvCustomerName;
    private Button btnSignIn, btnSignOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initUi();
        initLogin();
        Log.e("ddd", "onCreateView: account");
        btnSignOut.setOnClickListener(view -> {
            Helper.logOut(getContext());
            initLogin();
        });
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SigninActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void initLogin() {
        if (Helper.currentAccount != null) {
            String s = "Hello " + Helper.currentAccount.getName();
            tvCustomerName.setText(s);
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            tvCustomerName.setText("");
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }

    private void initUi() {
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        btnSignIn = view.findViewById(R.id.btn_signin);
        btnSignOut = view.findViewById(R.id.btn_signup);
    }
}
