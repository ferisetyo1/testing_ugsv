package com.tencent.qcloud.ugckit.module.effect.bubble;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.ugckit.R;


import java.lang.ref.WeakReference;
import java.util.List;

public class BubbleAdapter extends RecyclerView.Adapter<BubbleAdapter.BubbleViewHolder> implements View.OnClickListener {

    private List<NewBubbleInfo> mBubbles;
    private WeakReference<RecyclerView> mRecyclerView;

    public BubbleAdapter(List<NewBubbleInfo> bubbles) {
        mBubbles = bubbles;
    }

    private int selection = 0;

    public void setSelection(int selection) {
        int last=this.selection;
        this.selection = selection;
        notifyItemChanged(last);
        notifyItemChanged(selection);
    }

    @NonNull
    @Override
    public BubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        return new BubbleViewHolder(View.inflate(parent.getContext(), R.layout.ugckit_item_bubble_img, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BubbleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        //glide 加在asset目录资源图片需要拼接
        Bitmap iconBitmap = mBubbles.get(position).buildIconBitmap(holder.itemView.getContext());
        Glide.with(holder.itemView.getContext()).asBitmap().load(iconBitmap).into(holder.ivBubble);
        holder.ivSelected.setVisibility(position==selection?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return mBubbles.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            RecyclerView recyclerView = mRecyclerView.get();
            if (recyclerView != null) {
                int position = recyclerView.getChildAdapterPosition(v);
                int lastPosition=selection;
                selection = position;
                mListener.onItemClick(v, position);
                notifyItemChanged(lastPosition);
                notifyItemChanged(position);
            }
        }
    }

    public static class BubbleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBubble;
        ImageView ivSelected;

        public BubbleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBubble = (ImageView) itemView.findViewById(R.id.bubble_iv_img);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
