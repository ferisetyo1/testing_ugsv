package com.tencent.qcloud.ugckit.module.effect.bubble;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tencent.qcloud.ugckit.module.effect.BaseRecyclerAdapter;
import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.component.bubbleview.BubbleViewParams;


import java.io.IOException;
import java.util.List;

public class AddBubbleAdapter extends BaseRecyclerAdapter<AddBubbleAdapter.AddPasterViewHolder> {
    public static final int TYPE_FOOTER = 0;  // 带有Footer的
    public static final int TYPE_NORMAL = 1;  // 真实数据

    private Context mContext;
    private View mFooterView;
    private int mCurrentSelectedPos = -1;
    private int mPasterTextSize;
    private int mPasterTextColor;
    private int mCoverIcon;
    private List<BubbleViewParams> mBubbleInfoList;

    public AddBubbleAdapter(List<BubbleViewParams> bubbleInfoList, Context context) {
        mBubbleInfoList = bubbleInfoList;
        mContext = context;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(0);
    }

    public void setCurrentSelectedPos(int pos) {
        int tPos = mCurrentSelectedPos;
        mCurrentSelectedPos = pos;
        this.notifyItemChanged(tPos);
        this.notifyItemChanged(mCurrentSelectedPos);
    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public void onBindVH(@NonNull AddPasterViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        try{
            BubbleViewParams bubbleViewParams = mBubbleInfoList.get(position-1);

            if (bubbleViewParams != null) {
                if (bubbleViewParams.iconBitmap != null)
                    holder.ivAddPaster.setImageBitmap(bubbleViewParams.iconBitmap);
                if (mPasterTextSize != 0) {
                    holder.tvAddPasterText.setTextSize(mPasterTextSize);
                }
                if (mPasterTextColor != 0) {
                    holder.tvAddPasterText.setTextColor(mContext.getResources().getColor(mPasterTextColor));
                }
                holder.tvAddPasterText.setText(TextUtils.isEmpty(bubbleViewParams.text) ? "" : bubbleViewParams.text);
            }else{
                holder.tvAddPasterText.setText("params null");
            }
            if (mCoverIcon != 0) {
                holder.ivAddPasterTint.setImageResource(mCoverIcon);
            }
            if (mCurrentSelectedPos == position) {
                holder.ivAddPasterTint.setVisibility(View.VISIBLE);
            } else {
                holder.ivAddPasterTint.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.getStackTraceString(e.fillInStackTrace());
        }
    }

    @NonNull
    @Override
    public AddPasterViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new AddPasterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ugckit_item_add, parent, false));
        }
        return new AddPasterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ugckit_item_add_paster, parent, false));
    }

    @Override
    public int getItemCount() {
        if (mFooterView != null) {
            return mBubbleInfoList.size() + 1;
        }
        return mBubbleInfoList.size();
    }

    public void setPasterTextSize(int textSize) {
        mPasterTextSize = textSize;
    }

    public void setPasterTextColor(int textColor) {
        mPasterTextColor = textColor;
    }

    public void setCoverIconResouce(int icon) {
        mCoverIcon = icon;
    }

    public class AddPasterViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAddPaster;
        ImageView ivAddPasterTint;
        TextView tvAddPasterText;

        public AddPasterViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView == mFooterView) {
                return;
            }
            ivAddPaster = (ImageView) itemView.findViewById(R.id.add_paster_image);
            ivAddPasterTint = (ImageView) itemView.findViewById(R.id.add_paster_tint);
            tvAddPasterText = (TextView) itemView.findViewById(R.id.add_paster_tv_name);
            tvAddPasterText.setSingleLine(true);
            tvAddPasterText.setEllipsize(TextUtils.TruncateAt.END);
            tvAddPasterText.setMaxEms(4);
        }
    }
}
