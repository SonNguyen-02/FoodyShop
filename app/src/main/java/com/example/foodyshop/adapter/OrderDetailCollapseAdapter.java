package com.example.foodyshop.adapter;

import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderDetailModel;

import java.util.List;

public class OrderDetailCollapseAdapter extends RecyclerView.Adapter<OrderDetailCollapseAdapter.OrderDetailViewHolder> {

    private final Context context;
    private final List<OrderDetailModel> mListOrderDetail;

    public OrderDetailCollapseAdapter(Context context, List<OrderDetailModel> mListOrderDetail) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_collapse, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailModel orderDetail = mListOrderDetail.get(position);
        if (orderDetail == null) {
            return;
        }

        holder.imgProduct.setImageResource(R.drawable.test_product_icon);
        holder.tvName.setText(orderDetail.getName());
        holder.tvAmount.setText("x" + orderDetail.getAmount());
        holder.tvPriceSale.setText(PRICE_FORMAT.format(orderDetail.getPriceSale()));
        if (orderDetail.getDiscount() != null) {
            holder.tvPrice.setText(PRICE_FORMAT.format(orderDetail.getPrice()));
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mListOrderDetail != null) {
            return mListOrderDetail.size();
        }
        return 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvName, tvAmount, tvPrice, tvPriceSale;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPriceSale = itemView.findViewById(R.id.tv_price_sale);
        }
    }
}
