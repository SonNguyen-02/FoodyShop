package com.example.foodyshop.dialog;

import static com.example.foodyshop.config.Const.KEY_PRODUCT;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;
import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodyshop.R;
import com.example.foodyshop.model.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddToCardBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ProductModel mProduct;
    private ImageView imgProduct, imgMinus, imgPlus;
    private TextView tvProductName, tvPrice, tvPriceSale;
    private EditText edtAmount;
    private Button btnAddToCart;
    private final IOnClickAddToCart mIOnClickAddToCart;

    public AddToCardBottomSheetDialogFragment(IOnClickAddToCart mIOnClickAddToCart) {
        this.mIOnClickAddToCart = mIOnClickAddToCart;
    }

    public interface IOnClickAddToCart {
        void onClick(int amount);
    }

    @NonNull
    public static AddToCardBottomSheetDialogFragment newInstant(ProductModel product, IOnClickAddToCart mIOnClickAddToCart) {
        AddToCardBottomSheetDialogFragment addToCardBottomSheetDialogFragment = new AddToCardBottomSheetDialogFragment(mIOnClickAddToCart);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        addToCardBottomSheetDialogFragment.setArguments(bundle);
        return addToCardBottomSheetDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            mProduct = (ProductModel) bundleReceive.getSerializable(KEY_PRODUCT);
        }
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_cart, null);
        bottomSheetDialog.setContentView(view);
        initUi(view);
        setDataProduct();

        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(Objects.requireNonNull(bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        return bottomSheetDialog;
    }

    private void initUi(@NonNull View view) {
        imgProduct = view.findViewById(R.id.img_product);
        imgPlus = view.findViewById(R.id.img_plus);
        imgMinus = view.findViewById(R.id.img_minus);
        tvProductName = view.findViewById(R.id.tv_product_name);
        tvPrice = view.findViewById(R.id.tv_price);
        tvPriceSale = view.findViewById(R.id.tv_price_sale);
        edtAmount = view.findViewById(R.id.edt_amount);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);
    }

    private void setDataProduct() {
        if (mProduct == null) {
            return;
        }
//            Glide.with(this).load(mProduct.getImg()).placeholder(R.drawable.placeholder_img).into(imgProduct);
        imgProduct.setImageResource(R.drawable.test_product_icon);
        tvProductName.setText(mProduct.getName());
        tvPriceSale.setText(PRICE_FORMAT.format(mProduct.getPriceSale()));
        if (mProduct.getDiscount() != null) {
            tvPrice.setText(PRICE_FORMAT.format(mProduct.getPrice()));
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvPrice.setVisibility(View.GONE);
        }

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String regex = "^[1-9][0-9]{0,2}$";
                new Handler().postDelayed(() -> {
                    if (charSequence.toString().matches(regex)) {
                        Log.e("ddd", "beforeTextChanged OK: " + charSequence);
                        int amount = Integer.parseInt(charSequence.toString());
                        if (amount == 1) {
                            imgMinus.setEnabled(false);
                            imgMinus.setAlpha(0.5f);
                            imgPlus.setAlpha(1f);
                            imgPlus.setEnabled(true);
                        } else if (amount == 999) {
                            imgMinus.setEnabled(true);
                            imgMinus.setAlpha(1f);
                            imgPlus.setAlpha(0.5f);
                            imgPlus.setEnabled(false);
                        } else {
                            imgMinus.setEnabled(true);
                            imgPlus.setEnabled(true);
                            imgMinus.setAlpha(1f);
                            imgPlus.setAlpha(1f);
                        }
                    } else {
                        if (charSequence.toString().trim().length() != 0) {
                            edtAmount.setText("1");
                            edtAmount.requestFocus(1);
                            Log.e("ddd", "beforeTextChanged FA: " + charSequence);
                        } else {
                            imgMinus.setAlpha(0.5f);
                            imgMinus.setEnabled(false);
                        }
                    }
                }, 100);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        imgMinus.setOnClickListener(view -> {
            int amount = Integer.parseInt(edtAmount.getText().toString());
            edtAmount.setText(String.valueOf(--amount));
        });

        imgPlus.setOnClickListener(view -> {
            int amount;
            if (edtAmount.getText().toString().trim().isEmpty()) {
                amount = 0;
            } else {
                amount = Integer.parseInt(edtAmount.getText().toString());
            }
            edtAmount.setText(String.valueOf(++amount));
        });

        btnAddToCart.setOnClickListener(view -> {
            btnAddToCart.setEnabled(false);
            new Handler().postDelayed(() -> btnAddToCart.setEnabled(true), 1000);
            if (!edtAmount.getText().toString().trim().isEmpty()) {
                addToCart(Integer.parseInt(edtAmount.getText().toString()));
            } else {
                edtAmount.requestFocus();
                ToastCustom.notice(getContext(), "Hãy nhập số lượng", ToastCustom.INFO).show();
            }
        });
    }

    private void addToCart(int amount) {
        if (mProduct != null) {
            setCancelable(false);
            ToastCustom.loading(requireContext(), 1000).show();
            new Handler().postDelayed(() -> {
                mIOnClickAddToCart.onClick(amount);
                ToastCustom.notice(requireContext(), "Thêm thành công!", ToastCustom.SUCCESS).show();
                dismiss();
            }, 1000);
        }
    }
}
