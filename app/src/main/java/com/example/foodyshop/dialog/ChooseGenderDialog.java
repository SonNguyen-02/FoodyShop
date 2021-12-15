package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.foodyshop.R;
import com.example.foodyshop.model.CustomerModel;

import org.jetbrains.annotations.Contract;

public class ChooseGenderDialog {

    private final Activity activity;
    private AlertDialog dialog;

    private final IOnChooseItem mIOnChooseItem;

    private ChooseGenderDialog(Activity activity, IOnChooseItem mIOnChooseItem) {
        this.activity = activity;
        this.mIOnChooseItem = mIOnChooseItem;
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public static ChooseGenderDialog newInstance(Activity activity, IOnChooseItem mIOnChooseItem) {
        return new ChooseGenderDialog(activity, mIOnChooseItem);
    }

    public interface IOnChooseItem {
        void onChoose(ChooseGenderDialog dialog, int typeChoose);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_choose_gender, null);

        TextView tvMale = view.findViewById(R.id.tv_male);
        TextView tvFemale = view.findViewById(R.id.tv_female);
        TextView tvOther = view.findViewById(R.id.tv_other);

        tvMale.setOnClickListener(v -> mIOnChooseItem.onChoose(this, CustomerModel.MALE));
        tvFemale.setOnClickListener(v -> mIOnChooseItem.onChoose(this, CustomerModel.FEMALE));
        tvOther.setOnClickListener(v -> mIOnChooseItem.onChoose(this, CustomerModel.OTHER));

        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
