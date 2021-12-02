package com.example.foodyshop.adapter;

import android.os.Handler;
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
    private View currentCategory;

    public CategoryAdapter(IOnclickCategoryItem listener, List<CategoryModel> mListCategory) {
        this.mIOnclickCategoryItem = listener;
        this.mListCategory = mListCategory;
    }

    public interface IOnclickCategoryItem{
        void onclickCategoryItem(CategoryModel category);
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
        if(currentCategory == null || currentCategory == holder.itemView){
            currentCategory = holder.itemView;
            holder.itemView.setBackgroundResource(R.drawable.custom_bg_item_category_active);
        }
        holder.tvName.setText(category.getName());
        holder.itemView.setOnClickListener(view -> {
            if(currentCategory != holder.itemView){
                new Handler().postDelayed(() -> {
                    currentCategory.setBackgroundResource(R.drawable.custom_bg_item_category);
                    currentCategory = holder.itemView;
                    holder.itemView.setBackgroundResource(R.drawable.custom_bg_item_category_active);
                }, 350);
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
