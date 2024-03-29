package com.example.foodyshop.adapter;

import static com.example.foodyshop.helper.Helper.PRICE_FORMAT;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.activity.MainActivity;
import com.example.foodyshop.model.ProductModel;

import java.text.MessageFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<ProductModel> mListProduct;
    private final boolean isLinearLayoutManager;
    private final IOnclickProductItem mIOnclickProductItem;

    private final int margin;
    private static int itemLength;

    public ProductAdapter(@NonNull Context context, List<ProductModel> mListProduct, boolean isLinearLayoutManager, IOnclickProductItem mIOnclickProductItem) {
        this.context = context;
        this.mListProduct = mListProduct;
        this.isLinearLayoutManager = isLinearLayoutManager;
        this.mIOnclickProductItem = mIOnclickProductItem;
        margin = (int) context.getResources().getDimension(R.dimen.space_view);
        itemLength = (MainActivity.WIDTH_DEVICE - (margin * (1 + MainActivity.TOTAL_ITEM_PRODUCT))) / MainActivity.TOTAL_ITEM_PRODUCT;
    }

    public void setListProduct(List<ProductModel> mListProduct) {
        this.mListProduct = mListProduct;
        notifyDataSetChanged();
    }

    public void addToListProduct(List<ProductModel> list) {
        if (mListProduct == null) {
            mListProduct = list;
            notifyDataSetChanged();
            return;
        }
        mListProduct.addAll(list);
        notifyItemRangeChanged(mListProduct.size() - 1, list.size());
    }

    public interface IOnclickProductItem {
        void onclickProductItem(ProductModel product);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = mListProduct.get(position);
        if (product == null) {
            return;
        }
        if (isLinearLayoutManager) {
            if (position == 0) {
                // margin left cho item đầu tiên
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.leftMargin = margin;
            }
        } else {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            // margin top cho item
            layoutParams.topMargin = margin;
            // resize độ dài cho img
            layoutParams.width = itemLength;
            holder.imgProduct.getLayoutParams().height = itemLength;
        }
        Glide.with(context).load(product.getImgDir())
                .placeholder(R.drawable.test_product_icon)
                .error(R.drawable.test_product_icon)
                .into(holder.imgProduct);
        holder.tvName.setText(product.getName());
        holder.tvPriceSale.setText(PRICE_FORMAT.format(product.getPriceSale()));
        if (product.getDiscount() != null) {
            holder.tvDiscount.setText(MessageFormat.format(context.getResources().getString(R.string.discount), product.getDiscount()));
            holder.tvPrice.setText(PRICE_FORMAT.format(product.getPrice()));
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.rlSale.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> mIOnclickProductItem.onclickProductItem(product));
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvName, tvPrice, tvPriceSale, tvDiscount;
        RelativeLayout rlSale;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPriceSale = itemView.findViewById(R.id.tv_price_sale);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            rlSale = itemView.findViewById(R.id.rl_sale);
        }
    }
}
