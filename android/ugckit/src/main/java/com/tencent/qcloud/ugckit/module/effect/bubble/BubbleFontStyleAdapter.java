package com.tencent.qcloud.ugckit.module.effect.bubble;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.ugckit.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class BubbleFontStyleAdapter extends RecyclerView.Adapter<BubbleFontStyleAdapter.BubbleViewHolder> implements View.OnClickListener {

    private List<BubbleFontStyle> mFontStyles;
    private WeakReference<RecyclerView> mRecyclerView;
    private int selected = 0;

    public BubbleFontStyleAdapter(List<BubbleFontStyle> bubbles) {
        mFontStyles = bubbles;
    }

    @NonNull
    @Override
    public BubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRecyclerView == null)
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        return new BubbleViewHolder(View.inflate(parent.getContext(), R.layout.ugckit_bubble_font_style_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BubbleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.setData(mFontStyles.get(position));
        holder.ivSelected.setVisibility(selected == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mFontStyles.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            RecyclerView recyclerView = mRecyclerView.get();
            if (recyclerView != null) {
                int position = recyclerView.getChildAdapterPosition(v);
                selected=position;
                mListener.onFontClick(v, position);
                notifyDataSetChanged();
            }
        }
    }

    public static class BubbleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelected;
        TextView mText;

        public BubbleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
            mText = (TextView) itemView.findViewById(R.id.tv_font_text);
        }

        public void setData(BubbleFontStyle model) {
            mText.setText(model.getName());
            mText.setTypeface(ResourcesCompat.getFont(itemView.getContext(), model.getRes()));
        }
    }

    private OnFontClickListener mListener;

    public void setOnFontClickListener(OnFontClickListener listener) {
        mListener = listener;
    }

    public interface OnFontClickListener {
        void onFontClick(View view, int position);
    }

}
