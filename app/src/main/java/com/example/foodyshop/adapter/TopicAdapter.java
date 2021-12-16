package com.example.foodyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.model.TopicModel;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    public enum ORIENTATION {
        VERTICAL, HORIZONTAL
    }

    private final Context context;
    private List<TopicModel> topicModelList;
    private final ORIENTATION mOrientation;
    private final IOnclickTopicItem mIOnclickTopicItem;

    public TopicAdapter(Context context, ORIENTATION mOrientation) {
        this.context = context;
        this.mOrientation = mOrientation;
        this.mIOnclickTopicItem = (IOnclickTopicItem) context;
    }

    public interface IOnclickTopicItem {
        void onclickTopicItem(TopicModel topic);
    }

    public void setTopicModelList(List<TopicModel> topicModelList) {
        this.topicModelList = topicModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mOrientation == ORIENTATION.VERTICAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_ver, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_hoz, parent, false);
        }
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicModel topicModel = topicModelList.get(position);
        if (topicModel == null) {
            return;
        }
        holder.tvName.setText(topicModel.getName());
        holder.rlItem.setOnClickListener(view -> mIOnclickTopicItem.onclickTopicItem(topicModel));
        Glide.with(context).load(topicModel.getImg()).placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img).into(holder.imgTopic);
    }

    @Override
    public int getItemCount() {
        if (topicModelList != null) {
            return topicModelList.size();
        }
        return 0;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItem;
        TextView tvName;
        ImageView imgTopic;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_item);
            tvName = itemView.findViewById(R.id.tv_topic_name);
            imgTopic = itemView.findViewById(R.id.img_topic);
        }
    }
}
