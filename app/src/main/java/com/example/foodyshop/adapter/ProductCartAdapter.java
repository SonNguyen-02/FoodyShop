package com.example.foodyshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderDetailModel;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.OrderDetailViewHolder> {

    private final Context context;
    private List<OrderDetailModel> mListOrderDetail;
    private final ViewBinderHelper mViewBinderHelper = new ViewBinderHelper();
    private final NumberFormat format;
    private final IInteractItem mInteractItem;

    public ProductCartAdapter(Context context, List<OrderDetailModel> mListOrderDetail, IInteractItem mInteractItem) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
        this.mInteractItem = mInteractItem;
        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
    }

    public void setmListOrderDetail(List<OrderDetailModel> mListOrderDetail) {
        this.mListOrderDetail = mListOrderDetail;
        notifyDataSetChanged();
    }

    public void setCheck(boolean check) {
        for (OrderDetailModel ord : mListOrderDetail) {
            ord.setChecked(check);
        }
        notifyDataSetChanged();
    }

    public interface IInteractItem {
        void onClickItem(OrderDetailModel orderDetail);

        void onCheckBoxChange(boolean checked);

        void onAmountNumberChange(OrderDetailModel orderDetail, int amount);

        void onDeleteItem();
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailModel orderDetail = mListOrderDetail.get(position);
        if (orderDetail == null) {
            return;
        }
        mViewBinderHelper.bind(holder.mSwipeRevealLayout, String.valueOf(orderDetail.getId()));
        mViewBinderHelper.closeLayout(String.valueOf(orderDetail.getId()));
        holder.imgProduct.setImageResource(R.drawable.test_product_icon);
        holder.tvName.setText(orderDetail.getName());
        holder.tvPriceSale.setText(format.format(orderDetail.getPriceSale()));
        holder.edtAmount.setText(String.valueOf(orderDetail.getAmount()));
        holder.initAmount(orderDetail.getAmount());
        if (orderDetail.isChecked()) {
            if (!holder.cbItem.isChecked()) {
                holder.cbItem.setChecked(true);
            }
        } else {
            if (holder.cbItem.isChecked()) {
                holder.cbItem.setChecked(false);
            }
        }

        if (orderDetail.getDiscount() != null) {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(MessageFormat.format("Giảm " + context.getResources().getString(R.string.discount), orderDetail.getDiscount()));
            holder.tvPrice.setText(format.format(orderDetail.getPrice()));
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvDiscount.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        }

        holder.llItem.setOnClickListener(v -> {
            v.setEnabled(false);
            new Handler().postDelayed(() -> v.setEnabled(true), 1000);
            mInteractItem.onClickItem(orderDetail);
        });

        holder.cbItem.setOnClickListener(v -> {
            orderDetail.setChecked(holder.cbItem.isChecked());
            mInteractItem.onCheckBoxChange(holder.cbItem.isChecked());
        });

        TextWatcher textWatcher = new TextWatcher() {
            private boolean isNotMatch;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
                String regex = "^[1-9][0-9]{0,2}$";
                if (charSequence.toString().matches(regex)) {
                    holder.edtAmount.requestFocus(charSequence.length());
                    if (isNotMatch) {
                        isNotMatch = false;
                        return;
                    }
//                    Log.e("ddd", "beforeTextChanged OK: " + charSequence);
                    int amount = Integer.parseInt(charSequence.toString());
                    holder.initAmount(amount);
                    mInteractItem.onAmountNumberChange(orderDetail, amount);
                } else {
                    new Handler().postDelayed(() -> {
                        isNotMatch = true;
                        String s = charSequence.toString().replaceAll("\\D", "");
                        if (s.isEmpty() || charSequence.length() > 3) {
                            s = "1";
                        }
                        holder.edtAmount.setText(s);
                        int amount = Integer.parseInt(s);
                        holder.initAmount(amount);
                        mInteractItem.onAmountNumberChange(orderDetail, amount);
                    }, 100);
//                    Log.e("ddd", "beforeTextChanged FA: " + charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        holder.edtAmount.setOnFocusChangeListener((view, b) -> {
            if (b) {
                ((EditText) view).addTextChangedListener(textWatcher);
            } else {
                Log.e("ddd", "onBindViewHolder: remove");
                ((EditText) view).removeTextChangedListener(textWatcher);
            }
        });

        holder.imgMinus.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.edtAmount.getText().toString());
            holder.edtAmount.setText(String.valueOf(--amount));
            holder.initAmount(amount);
            mInteractItem.onAmountNumberChange(orderDetail, amount);
        });

        holder.imgPlus.setOnClickListener(view -> {
            int amount;
            if (holder.edtAmount.getText().toString().trim().isEmpty()) {
                amount = 0;
            } else {
                amount = Integer.parseInt(holder.edtAmount.getText().toString());
            }
            holder.edtAmount.setText(String.valueOf(++amount));
            holder.initAmount(amount);
            mInteractItem.onAmountNumberChange(orderDetail, amount);
        });

        holder.llItemDelete.setOnClickListener(view -> {
            mListOrderDetail.remove(orderDetail);
            mInteractItem.onDeleteItem();
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        if (mListOrderDetail != null) {
            return mListOrderDetail.size();
        }
        return 0;
    }


    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        SwipeRevealLayout mSwipeRevealLayout;
        LinearLayout llItemDelete, llItem;
        CheckBox cbItem;
        ImageView imgProduct, imgMinus, imgPlus;
        TextView tvName, tvPrice, tvPriceSale, tvDiscount;
        EditText edtAmount;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mSwipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            llItemDelete = itemView.findViewById(R.id.ll_item_delete);
            llItem = itemView.findViewById(R.id.ll_item);
            cbItem = itemView.findViewById(R.id.cb_item);
            imgProduct = itemView.findViewById(R.id.img_product);
            imgMinus = itemView.findViewById(R.id.img_minus);
            imgPlus = itemView.findViewById(R.id.img_plus);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPriceSale = itemView.findViewById(R.id.tv_price_sale);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            edtAmount = itemView.findViewById(R.id.edt_amount);
        }

        public void initAmount(int amount) {
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
        }
    }

}
