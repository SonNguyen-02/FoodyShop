package com.example.foodyshop.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodyshop.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Contract;

public class ShowOrChangeAvatarBottomSheet extends BottomSheetDialogFragment {

    private final IOnClickItem mIOnClickItem;
    private LinearLayout llShowAvatar, llChangeAvatar, llClose;

    private ShowOrChangeAvatarBottomSheet(IOnClickItem mIOnClickAddToCart) {
        this.mIOnClickItem = mIOnClickAddToCart;
    }

    public interface IOnClickItem {
        void onClickShowAvatar(ShowOrChangeAvatarBottomSheet bottomSheet);

        void onClickChangeAvatar(ShowOrChangeAvatarBottomSheet bottomSheet);
    }

    @NonNull
    @Contract("_ -> new")
    public static ShowOrChangeAvatarBottomSheet newInstant(IOnClickItem mIOnClickItem) {
        return new ShowOrChangeAvatarBottomSheet(mIOnClickItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_show_or_change_avatar, null);
        bottomSheetDialog.setContentView(view);

        initUi(view);
        llShowAvatar.setOnClickListener(v -> mIOnClickItem.onClickShowAvatar(this));
        llChangeAvatar.setOnClickListener(v -> mIOnClickItem.onClickChangeAvatar(this));
        llClose.setOnClickListener(v -> dismiss());

        return bottomSheetDialog;
    }

    private void initUi(@NonNull View view) {
        llShowAvatar = view.findViewById(R.id.ll_show_avatar);
        llChangeAvatar = view.findViewById(R.id.ll_change_avatar);
        llClose = view.findViewById(R.id.ll_close);
    }

}
