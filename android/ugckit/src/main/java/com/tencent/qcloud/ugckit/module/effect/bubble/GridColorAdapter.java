package com.tencent.qcloud.ugckit.module.effect.bubble;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.ugckit.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class GridColorAdapter extends RecyclerView.Adapter<GridColorAdapter.BubbleViewHolder> implements View.OnClickListener {

    private List<Integer> colors;
    private WeakReference<RecyclerView> mRecyclerView;
    private int selected = 0;

    public GridColorAdapter(List<Integer> color) {
        colors = color;
    }

    public void setSelected(int selected) {
        int last=selected;
        this.selected = selected;
        notifyItemChanged(last);
        notifyItemChanged(selected);
    }

    @NonNull
    @Override
    public BubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        return new BubbleViewHolder(View.inflate(parent.getContext(), R.layout.ugckit_bubble_color_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BubbleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        Bitmap target = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        paint.setColor(colors.get(position));
        paint.setAntiAlias(true);
        canvas.drawCircle(100f / 2, 100f / 2, 100f / 2, paint);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);

        if (position == selected) {
            paint.setColor(Color.RED);
            canvas.drawCircle(100f / 2, 100f / 2, 100f / 2, paint);
        } else {
            paint.setColor(Color.parseColor("#D1D1D1"));
            canvas.drawCircle(100f / 2, 100f / 2, 100f / 2, paint);
        }
        Glide.with(holder.itemView.getContext()).asBitmap().load(target).into(holder.ivColor);

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            RecyclerView recyclerView = mRecyclerView.get();
            if (recyclerView != null) {
                int position = recyclerView.getChildAdapterPosition(v);
                int last=selected;
                selected = position;
                mListener.onColorClick(v, position);
                notifyItemChanged(last);
                notifyItemChanged(position);
            }
        }
    }

    public static class BubbleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivColor;

        public BubbleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivColor = (ImageView) itemView.findViewById(R.id.iv_color);

        }

    }

    private OnColorClickListener mListener;

    public void setOnColorClickListener(OnColorClickListener listener) {
        mListener = listener;
    }

    public interface OnColorClickListener {
        void onColorClick(View view, int position);
    }

}
