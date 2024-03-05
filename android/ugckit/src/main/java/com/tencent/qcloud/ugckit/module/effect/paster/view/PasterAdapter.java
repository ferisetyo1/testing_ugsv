package com.tencent.qcloud.ugckit.module.effect.paster.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import com.tencent.qcloud.ugckit.module.effect.paster.IPasterPannel;
import com.tencent.qcloud.ugckit.module.effect.paster.TCPasterInfo;
import com.tencent.qcloud.ugckit.R;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PasterAdapter extends RecyclerView.Adapter<PasterAdapter.PasterViewHolder> implements View.OnClickListener {

    @Nullable
    private List<TCPasterInfo>                mPasterInfoList;
    private WeakReference<RecyclerView>       mRecyclerView;
    private IPasterPannel.OnItemClickListener mOnItemClickListener;

    public PasterAdapter(@Nullable List<TCPasterInfo> pasterInfoList) {
        if (pasterInfoList == null) {
            mPasterInfoList = new ArrayList<TCPasterInfo>();
        } else {
            mPasterInfoList = pasterInfoList;
        }
    }

    @NonNull
    @Override
    public PasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRecyclerView == null) {
            mRecyclerView = new WeakReference<RecyclerView>((RecyclerView) parent);
        }
        return new PasterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ugckit_layout_paster_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PasterViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        TCPasterInfo tcPasterInfo = mPasterInfoList.get(position);
        if (tcPasterInfo.getPasterType()==PasterView.TYPE_CHILD_VIEW_ANIMATED_PASTER){
            Context context = holder.itemView.getContext();
            Glide.with(context).load(tcPasterInfo.bitmapEmote(context)).into(holder.ivPasterEmoticon);
            holder.ivPasterEmoticon.setVisibility(View.VISIBLE);
        }else{
            holder.ivPaster.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(tcPasterInfo.getIconPath()).into(holder.ivPaster);
        }
    }

    @Override
    public int getItemCount() {
        return mPasterInfoList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener == null) {
            return;
        }
        RecyclerView recyclerView = mRecyclerView.get();
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(view);
            mOnItemClickListener.onItemClick(mPasterInfoList.get(position), position);
        }
    }

    class PasterViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPaster;
        ImageView ivPasterEmoticon;

        public PasterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPaster = (ImageView) itemView.findViewById(R.id.iv_paster);
            ivPasterEmoticon = (ImageView) itemView.findViewById(R.id.iv_paster_emoticon);
        }
    }

    public void setOnItemClickListener(IPasterPannel.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
