package com.example.foodyshop.adapter;

import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.model.OrderDetailModel;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrderDetailExpandAdapter extends RecyclerView.Adapter<OrderDetailExpandAdapter.OrderDetailViewHolder> {

    private final Context context;
    private final List<OrderDetailModel> mListOrderDetail;
    private long timeOrderCreated;
    private boolean isDelivered;
    private IOnClickCallback mIOnClickCallback;

    public OrderDetailExpandAdapter(Context context, List<OrderDetailModel> mListOrderDetail) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
    }

    public OrderDetailExpandAdapter(Context context, List<OrderDetailModel> mListOrderDetail, long timeOrderCreated, IOnClickCallback mIOnClickCallback) {
        this.context = context;
        this.mListOrderDetail = mListOrderDetail;
        this.timeOrderCreated = timeOrderCreated;
        this.mIOnClickCallback = mIOnClickCallback;
        this.isDelivered = true;
    }

    public interface IOnClickCallback {
        void onClickShowAddFeedbackDialog(int position, OrderDetailModel orderDetail);

        void onClickSeeFeedback(int position, OrderDetailModel orderDetail);
    }

    public List<OrderDetailModel> getListOrderDetail() {
        return mListOrderDetail;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_expand, parent, false);
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
        holder.tvPriceSale.setText(PRICE_FORMAT.format(orderDetail.getPriceSale()));
        holder.tvPriceSale2.setText(PRICE_FORMAT.format(orderDetail.getPriceSale()));
        holder.tvAmount.setText(String.valueOf(orderDetail.getAmount()));
        holder.tvTotalMoney.setText(PRICE_FORMAT.format((long) orderDetail.getPriceSale() * orderDetail.getAmount()));

        if (orderDetail.getDiscount() != null) {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(MessageFormat.format("Giảm " + context.getResources().getString(R.string.discount), orderDetail.getDiscount()));
            holder.tvPrice.setText(PRICE_FORMAT.format(orderDetail.getPrice()));
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvDiscount.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        }

        if (isDelivered) {
            holder.rlFeedbackBox.setVisibility(View.VISIBLE);
            holder.hiddenFeedbackBox();
            if (orderDetail.getFeedback() == null) {
                if (System.currentTimeMillis() - timeOrderCreated < TimeUnit.DAYS.toMillis(20)) {
                    holder.btnShowAddFb.setVisibility(View.VISIBLE);
                    holder.btnShowAddFb.setOnClickListener(v -> mIOnClickCallback.onClickShowAddFeedbackDialog(holder.getAdapterPosition(), orderDetail));
                } else {
                    holder.tvNoHaveFb.setVisibility(View.VISIBLE);
                }
            } else {
                holder.btnSeeFb.setVisibility(View.VISIBLE);
                holder.btnSeeFb.setOnClickListener(v -> mIOnClickCallback.onClickSeeFeedback(holder.getAdapterPosition(), orderDetail));
            }
        } else {
            holder.rlFeedbackBox.setVisibility(View.GONE);
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

        RelativeLayout rlFeedbackBox;
        Button btnShowAddFb, btnSeeFb;
        ImageView imgProduct;
        TextView tvName, tvPrice, tvPriceSale, tvPriceSale2, tvDiscount, tvAmount, tvTotalMoney, tvNoHaveFb;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            rlFeedbackBox = itemView.findViewById(R.id.rl_feedback_box);
            btnShowAddFb = itemView.findViewById(R.id.btn_show_add_feedback);
            btnSeeFb = itemView.findViewById(R.id.btn_see_feedback);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPriceSale = itemView.findViewById(R.id.tv_price_sale);
            tvPriceSale2 = itemView.findViewById(R.id.tv_price_sale_2);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTotalMoney = itemView.findViewById(R.id.tv_total_money);
            tvNoHaveFb = itemView.findViewById(R.id.tv_no_have_feedback);
            imgProduct = itemView.findViewById(R.id.img_product);
        }

        void hiddenFeedbackBox() {
            btnShowAddFb.setVisibility(View.GONE);
            btnSeeFb.setVisibility(View.GONE);
            tvNoHaveFb.setVisibility(View.GONE);
        }

    }
}
