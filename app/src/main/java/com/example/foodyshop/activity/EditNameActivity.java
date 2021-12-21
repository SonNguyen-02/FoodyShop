package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_DATA_USER;
import static com.example.foodyshop.config.Const.KEY_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodyshop.R;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.CustomerModel;

public class EditNameActivity extends AppCompatActivity {

    private String fullName;
    private EditText edtFullname;
    private long lastClickSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        ImageView imgBack = findViewById(R.id.img_back);
        TextView tvSave = findViewById(R.id.tv_save);
        edtFullname = findViewById(R.id.edt_fullname);
        fullName = getIntent().getStringExtra(KEY_NAME);
        if (fullName != null) {
            edtFullname.setText(fullName);
            edtFullname.requestFocus(fullName.length());
            Helper.showKeyboard(this);
        }
        tvSave.setOnClickListener(v -> {
            if (System.currentTimeMillis() - lastClickSave < 1000) {
                return;
            }
            lastClickSave = System.currentTimeMillis();
            String name = edtFullname.getText().toString().trim();
            if (name.isEmpty()) {
                ToastCustom.notice(getApplicationContext(), "Tên không được bỏ trống!", ToastCustom.WARNING).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(KEY_NAME, name);
            setResult(RESULT_OK, intent);
            if (isKeyboardOpen(edtFullname)) {
                Helper.hideKeyboard(getApplicationContext(), edtFullname);
            }
            finish();
        });
        imgBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        Helper.hideKeyboard(this, edtFullname);
        String name = edtFullname.getText().toString().trim();
        if (!name.equals(fullName)) {
            ConfirmDialog.newInstance(this, "Hủy thay đổi?", confirmDialog -> {
                confirmDialog.dismiss();
                super.onBackPressed();
            }).show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isKeyboardOpen(@NonNull View view) {
        Rect visibleBounds = new Rect();
        view.getRootView().getWindowVisibleDisplayFrame(visibleBounds);
        int heightDiff = view.getRootView().getHeight() - visibleBounds.height();
        int marginOfError = Math.round(convertDpToPx(50F));
        return heightDiff > marginOfError;
    }

    private float convertDpToPx(Float dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}