package com.example.foodyshop.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.activity.MyAccountActivity;
import com.example.foodyshop.helper.Helper;

public class ShowAvatarFragment extends Fragment {

    private static final String KEY_URI = "key_uri";

    private ShowAvatarFragment() {
    }

    @NonNull
    public static ShowAvatarFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_URI, uri);
        ShowAvatarFragment fragment = new ShowAvatarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_avatar, container, false);
        LinearLayout llOverlay = view.findViewById(R.id.ll_overlay);
        ImageView imgAvatar = view.findViewById(R.id.img_avatar);
        Bundle bundle = getArguments();
        Uri uri = bundle != null ? bundle.getParcelable(KEY_URI) : null;
        if (uri != null) {
            imgAvatar.setImageURI(uri);
        } else {
            if (Helper.isLogin(requireContext())) {
                Glide.with(requireContext()).load(Helper.getCurrentAccount().getImg())
                        .placeholder(R.drawable.placeholder_user)
                        .error(R.drawable.placeholder_user).into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.placeholder_user);
            }
        }
        llOverlay.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        return view;
    }
}
