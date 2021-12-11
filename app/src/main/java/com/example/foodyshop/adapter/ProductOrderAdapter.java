package com.example.foodyshop.adapter;

import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderDetailModel;

import java.text.MessageFormat;
import java.util.List;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {

    private final Context context;
    private final List<OrderDetailModel> mListOrderDetail;

    public ProductOrderAdapter(Context context, List<OrderDetailModel> mListOrderDetail) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
    }

    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order, parent, false);
        return new ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        OrderDetailModel orderDetail = mListOrderDetail.get(position);
        if (orderDetail == null) {
            return;
        }
        holder.imgProduct.setImageResource(R.drawable.test_product_icon);
        holder.tvName.setText(orderDetail.getName());
        holder.tvPrice.setText(PRICE_FORMAT.format(orderDetail.getPrice()));
        holder.tvAmount.setText(String.valueOf(orderDetail.getAmount()));
        int totalMoney = orderDetail.getPrice() * orderDetail.getAmount();
        if (orderDetail.getDiscount() != null) {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(MessageFormat.format("Giảm " + context.getResources().getString(R.string.discount), orderDetail.getDiscount()));
            int totalSale = totalMoney * orderDetail.getDiscount() / 100;
            holder.tvTotalMoney.setText(PRICE_FORMAT.format(totalMoney));
            holder.tvTotalSale.setText(PRICE_FORMAT.format(totalSale));
            holder.tvIntoMoney.setText(PRICE_FORMAT.format(totalMoney - totalSale));
        } else {
            holder.tvDiscount.setVisibility(View.GONE);
            holder.llTotalMoney.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            holder.tvIntoMoney.setText(PRICE_FORMAT.format(totalMoney));
        }
    }

    @Override
    public int getItemCount() {
        if (mListOrderDetail != null) {
            return mListOrderDetail.size();
        }
        return 0;
    }

    public static class ProductOrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        LinearLayout llTotalMoney;
        View divider;
        TextView tvName, tvDiscount, tvPrice, tvAmount, tvTotalMoney, tvTotalSale, tvIntoMoney;

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            llTotalMoney = itemView.findViewById(R.id.ll_total_money);
            divider = itemView.findViewById(R.id.divider);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTotalMoney = itemView.findViewById(R.id.tv_total_money);
            tvTotalSale = itemView.findViewById(R.id.tv_total_sale);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvIntoMoney = itemView.findViewById(R.id.tv_into_money);
        }
    }
}
