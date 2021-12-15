package com.example.foodyshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyshop.R;
import com.example.foodyshop.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<CategoryModel> mListCategory;
    private final IOnclickCategoryItem mIOnclickCategoryItem;
    private int lastCheckPos;

    public CategoryAdapter(IOnclickCategoryItem listener, List<CategoryModel> mListCategory) {
        lastCheckPos = 0;
        this.mIOnclickCategoryItem = listener;
        this.mListCategory = mListCategory;
    }

    public interface IOnclickCategoryItem {
        void onclickCategoryItem(CategoryModel category);
    }

    public CategoryModel getCurrentItem() {
        if (lastCheckPos >= 0 && mListCategory != null && lastCheckPos < mListCategory.size()) {
            return mListCategory.get(lastCheckPos);
        }
        return null;
    }

    public int getLastCheckPos() {
        return lastCheckPos;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = mListCategory.get(position);
        if (category == null) {
            return;
        }
        if (category.isChecked()) {
            holder.itemView.setBackgroundResource(R.drawable.custom_bg_white_stroke_green_corn_4);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.ripple_white_stroke_blue_corn_4);
        }
        holder.tvName.setText(category.getName());
        holder.itemView.setOnClickListener(view -> {
            if (lastCheckPos != holder.getAdapterPosition()) {
                mListCategory.get(lastCheckPos).setChecked(false);
                lastCheckPos = holder.getAdapterPosition();
                mListCategory.get(lastCheckPos).setChecked(true);
                notifyDataSetChanged();
                mIOnclickCategoryItem.onclickCategoryItem(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null) {
            return mListCategory.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }
    }
}
