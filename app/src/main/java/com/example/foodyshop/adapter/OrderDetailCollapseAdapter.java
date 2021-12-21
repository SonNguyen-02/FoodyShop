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

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderDetailModel;

import java.text.MessageFormat;
import java.util.List;

public class OrderDetailCollapseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEE_MORE = 1;
    public static final int MAX_ITEM_SHOW = 2;

    private final Context context;
    private final List<OrderDetailModel> mListOrderDetail;
    private final boolean collapseMax;
    private IOnClickSeeMore mIOnClickSeeMore;

    public OrderDetailCollapseAdapter(Context context, List<OrderDetailModel> mListOrderDetail, boolean collapseMax) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
        this.collapseMax = collapseMax;
    }

    public interface IOnClickSeeMore {
        void onClick();
    }

    public void setIOnClickSeeMore(IOnClickSeeMore mIOnClickSeeMore) {
        this.mIOnClickSeeMore = mIOnClickSeeMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_collapse, parent, false);
            return new OrderDetailViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_collapse_see_more, parent, false);
            return new ItemSeeMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            OrderDetailViewHolder orderDetailViewHolder = (OrderDetailViewHolder) holder;
            OrderDetailModel orderDetail = mListOrderDetail.get(position);
            if (orderDetail == null) {
                return;
            }
            Glide.with(context).load(orderDetail.getImg())
                    .placeholder(R.drawable.test_product_icon)
                    .error(R.drawable.test_product_icon)
                    .into(orderDetailViewHolder.imgProduct);
            orderDetailViewHolder.tvName.setText(orderDetail.getName());
            orderDetailViewHolder.tvAmount.setText("x" + orderDetail.getAmount());
            orderDetailViewHolder.tvPriceSale.setText(PRICE_FORMAT.format(orderDetail.getPriceSale()));
            if (orderDetail.getDiscount() != null) {
                orderDetailViewHolder.tvPrice.setText(PRICE_FORMAT.format(orderDetail.getPrice()));
                orderDetailViewHolder.tvPrice.setPaintFlags(orderDetailViewHolder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                orderDetailViewHolder.tvPrice.setVisibility(View.GONE);
            }
        } else {
            ItemSeeMoreViewHolder seeMoreViewHolder = (ItemSeeMoreViewHolder) holder;
            seeMoreViewHolder.tvSeeMore.setText(MessageFormat.format(context.getString(R.string.see_more_product), mListOrderDetail.size() - MAX_ITEM_SHOW));
            seeMoreViewHolder.tvSeeMore.setOnClickListener(view -> {
                mIOnClickSeeMore.onClick();
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mListOrderDetail != null) {
            if (collapseMax && mListOrderDetail.size() > MAX_ITEM_SHOW) {
                return MAX_ITEM_SHOW + 1;
            }
            return mListOrderDetail.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (collapseMax && mListOrderDetail != null && mListOrderDetail.size() > MAX_ITEM_SHOW && position == getItemCount() - 1) {
            return TYPE_SEE_MORE;
        }
        return TYPE_ITEM;
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

    public static class ItemSeeMoreViewHolder extends RecyclerView.ViewHolder {

        TextView tvSeeMore;

        public ItemSeeMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeeMore = itemView.findViewById(R.id.tv_see_more);
        }
    }
}
