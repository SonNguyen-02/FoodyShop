package com.example.foodyshop.fragment;

import static com.example.foodyshop.config.Const.KEY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.activity.MyAccountActivity;
import com.example.foodyshop.activity.SigninActivity;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.CustomerModel;

public class AccountFragment extends Fragment {

    private View view;
    private ImageView imgAvatar;
    private TextView tvCustomerName;
    private CardView cardNotification;
    private RelativeLayout rlMyAccount, rlNotification, rlSetting, rlHelpCenter, rlLogout, rlLogin;
    private boolean isCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        isCreate = true;
        initUi();
        initLogin();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCreate) {
            isCreate = false;
            return;
        }
        initLogin();
    }

    private void initUi() {
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        cardNotification = view.findViewById(R.id.card_notification);
        rlMyAccount = view.findViewById(R.id.rl_my_account);
        rlNotification = view.findViewById(R.id.rl_notification);
        rlSetting = view.findViewById(R.id.rl_setting);
        rlHelpCenter = view.findViewById(R.id.rl_help_center);
        rlLogout = view.findViewById(R.id.rl_logout);
        rlLogin = view.findViewById(R.id.rl_login);
    }

    private void initLogin() {
        if (Helper.isLogin(requireContext())) {
            rlLogin.setVisibility(View.GONE);
            rlLogout.setVisibility(View.VISIBLE);
            cardNotification.setVisibility(View.VISIBLE);

            CustomerModel mAccount = Helper.getCurrentAccount();

            Glide.with(requireContext()).load(mAccount.getImg())
                    .placeholder(R.drawable.placeholder_user)
                    .error(R.drawable.placeholder_user).into(imgAvatar);

            tvCustomerName.setText(mAccount.getName());

            rlMyAccount.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), MyAccountActivity.class);
                startActivity(intent);
            });

            rlNotification.setOnClickListener(view -> {

            });

            rlLogout.setOnClickListener(view -> {
                String message = "Bạn có chắc chắn muốn thoát?";
                ConfirmDialog.newInstance(requireActivity(), message, confirmDialog -> {
                    confirmDialog.dismiss();
                    Helper.logOut(getContext());
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }).show();
            });

        } else {
            rlLogin.setVisibility(View.VISIBLE);
            rlLogout.setVisibility(View.GONE);
            cardNotification.setVisibility(View.GONE);

            imgAvatar.setImageResource(R.drawable.placeholder_user);
            tvCustomerName.setText(R.string.guest_user);

            rlMyAccount.setOnClickListener(view -> {
                goToLogin();
            });

            rlLogin.setOnClickListener(view -> {
                goToLogin();
            });
        }

        rlSetting.setOnClickListener(view -> {

        });

        rlHelpCenter.setOnClickListener(view -> {

        });
    }

    private void goToLogin() {
        Intent intent = new Intent(requireContext(), SigninActivity.class);
        intent.putExtra(KEY_NEW_TASK, true);
        startActivity(intent);
    }
}
