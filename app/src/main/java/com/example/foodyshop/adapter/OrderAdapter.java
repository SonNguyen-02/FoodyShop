package com.example.foodyshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.OrderModel;

import java.text.MessageFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<OrderModel> mListOrder;
    private final IInteractOrder mInteractOrder;

    public OrderAdapter(Context context, List<OrderModel> mListOrder, IInteractOrder mInteractOrder) {
        this.context = context;
        this.mListOrder = mListOrder;
        this.mInteractOrder = mInteractOrder;
    }

    public interface IInteractOrder {
        void onClickTitle(int position, OrderModel order);

        void onClickBuyAgain(OrderModel order);

        void onClickCancelOrder(int position, OrderModel order);

        void onClickConfirmOrder(int position, OrderModel order);
    }

    public List<OrderModel> getListOrder() {
        return mListOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        holder.tvOrderCode.setText(MessageFormat.format(context.getString(R.string.code), order.getOrderCode()));
        holder.tvTotalProduct.setText(MessageFormat.format(context.getString(R.string.total_product), order.getTotalProduct()));
        holder.tvTotalMoney.setText(Helper.PRICE_FORMAT.format(order.getTotalMoney()));

        int status = order.getStatus();
        boolean collapseMax = status == -1 || status == -2 || status == 5;
        OrderDetailCollapseAdapter dtAdapter = new OrderDetailCollapseAdapter(context, order.getOrderDetails(), collapseMax);
        if (collapseMax) {
            dtAdapter.setIOnClickSeeMore(() -> mInteractOrder.onClickTitle(holder.getAdapterPosition(), order));
        }

        holder.rcvOrderDetail.setAdapter(dtAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.rcvOrderDetail.setLayoutManager(layoutManager);
        holder.rcvOrderDetail.setFocusable(false);
        holder.rcvOrderDetail.setNestedScrollingEnabled(false);
//        holder.rcvOrderDetail.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                rv.getParent().requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//            }
//        });

        holder.rlTitleOrder.setOnClickListener(view -> mInteractOrder.onClickTitle(holder.getAdapterPosition(), order));

        switch (status) {
            case 0:
                holder.tvStatus.setText("Chờ shop xác nhận");
                holder.tvShipPrice.setText("Đang cập nhập");
                holder.showCancelConfirmBox();
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnCancel.setOnClickListener(view -> mInteractOrder.onClickCancelOrder(holder.getAdapterPosition(), order));
                break;
            case 1:
                holder.tvStatus.setText("Đã được xác nhận");
                if (order.getShipPrice() != 0) {
                    holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                } else {
                    holder.tvShipPrice.setText("Đang cập nhập");
                }
                holder.showCancelConfirmBox();
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnCancel.setOnClickListener(view -> mInteractOrder.onClickCancelOrder(holder.getAdapterPosition(), order));
                break;
            case 2:
                holder.tvStatus.setText("Chờ bạn xác nhận");
                holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                holder.showCancelConfirmBox();
                holder.btnConfirm.setVisibility(View.VISIBLE);
                holder.btnCancel.setOnClickListener(view -> mInteractOrder.onClickCancelOrder(holder.getAdapterPosition(), order));
                holder.btnConfirm.setOnClickListener(view -> mInteractOrder.onClickConfirmOrder(holder.getAdapterPosition(), order));
                break;
            case 3:
                holder.tvStatus.setText("Đang xuất kho");
                holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                holder.hideAllBox();
                break;
            case 4:
                holder.tvStatus.setText("Đang giao hàng");
                holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                holder.hideAllBox();
                break;
            case -1:
                holder.tvStatus.setText("Đã hủy");
                if (order.getShipPrice() != 0) {
                    holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                } else {
                    holder.tvShipPrice.setText("Chưa cập nhập");
                }
                holder.showBuyAgainBox();
                holder.tvMixMessage.setVisibility(View.VISIBLE);
                holder.tvMixMessage.setText("Hủy bởi shop");
                holder.btnBuyAgain.setOnClickListener(view -> mInteractOrder.onClickBuyAgain(order));
                break;
            case -2:
                holder.tvStatus.setText("Đã hủy");
                if (order.getShipPrice() != 0) {
                    holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                } else {
                    holder.tvShipPrice.setText("Chưa cập nhập");
                }
                holder.showBuyAgainBox();
                holder.tvMixMessage.setVisibility(View.VISIBLE);
                holder.tvMixMessage.setText("Hủy bởi bạn");
                holder.btnBuyAgain.setOnClickListener(view -> mInteractOrder.onClickBuyAgain(order));
                break;
            case 5:
                holder.tvStatus.setText("Đã giao");
                holder.tvShipPrice.setText(Helper.PRICE_FORMAT.format(order.getShipPrice()));
                holder.showBuyAgainBox();
                holder.tvMixMessage.setVisibility(View.GONE);
                holder.btnBuyAgain.setOnClickListener(view -> mInteractOrder.onClickBuyAgain(order));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rcvOrderDetail;
        TextView tvOrderCode, tvStatus, tvShipPrice, tvTotalProduct, tvTotalMoney, tvMixMessage;
        RelativeLayout rlTitleOrder, rlBuyAgain;
        LinearLayout llCancelConfOrder;
        Button btnBuyAgain, btnCancel, btnConfirm;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            rcvOrderDetail = itemView.findViewById(R.id.rcv_order_detail);
            tvOrderCode = itemView.findViewById(R.id.tv_order_code);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvShipPrice = itemView.findViewById(R.id.tv_ship_price);
            tvTotalProduct = itemView.findViewById(R.id.tv_total_product);
            tvTotalMoney = itemView.findViewById(R.id.tv_total_money);
            tvMixMessage = itemView.findViewById(R.id.tv_mix_message);
            rlTitleOrder = itemView.findViewById(R.id.rl_title_order);
            rlBuyAgain = itemView.findViewById(R.id.rl_buy_again);
            llCancelConfOrder = itemView.findViewById(R.id.ll_cancel_conf_order);
            btnBuyAgain = itemView.findViewById(R.id.btn_buy_again);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
        }

        public void showBuyAgainBox() {
            rlBuyAgain.setVisibility(View.VISIBLE);
            llCancelConfOrder.setVisibility(View.GONE);
        }

        public void showCancelConfirmBox() {
            rlBuyAgain.setVisibility(View.GONE);
            llCancelConfOrder.setVisibility(View.VISIBLE);
        }

        public void hideAllBox() {
            rlBuyAgain.setVisibility(View.GONE);
            llCancelConfOrder.setVisibility(View.GONE);
        }
    }
}
