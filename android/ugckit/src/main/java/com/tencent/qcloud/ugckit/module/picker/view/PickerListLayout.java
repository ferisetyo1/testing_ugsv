package com.tencent.qcloud.ugckit.module.picker.view;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.tencent.qcloud.ugckit.module.picker.data.ItemView;
import com.tencent.qcloud.ugckit.module.picker.data.TCVideoEditerListAdapter;
import com.tencent.qcloud.ugckit.module.picker.data.TCVideoFileInfo;
import com.tencent.qcloud.ugckit.R;

import java.util.ArrayList;

public class PickerListLayout extends RelativeLayout implements IPickerListLayout, ItemView.OnAddListener {
    private static final int                      VIDEO_SPAN_COUNT = 4;
    private              Activity                 mActivity;
    private              RecyclerView             mRecyclerView;
    private ProgressBar progressBar;
    private              TCVideoEditerListAdapter mAdapter;
    private              ItemView.OnAddListener   mOnAddListener;

    public PickerListLayout(Context context) {
        super(context);
        initViews();
    }

    public PickerListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PickerListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mActivity = (Activity) getContext();
        inflate(mActivity, R.layout.ugckit_picture_list_layout, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, VIDEO_SPAN_COUNT));

        mAdapter = new TCVideoEditerListAdapter(mActivity);
        mAdapter.setOnItemAddListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMultiplePick(true, false);
    }

    public void showProgressBar(){
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgressBar(){
        progressBar.setVisibility(GONE);
    }

    @Override
    public void pauseRequestBitmap() {
        Glide.with(mActivity).pauseRequests();
    }

    @Override
    public void resumeRequestBitmap() {
        Glide.with(mActivity).resumeRequests();
    }

    @Override
    public void updateItems(@NonNull ArrayList<TCVideoFileInfo> list) {
        mAdapter.addAll(list);
    }

    @Override
    public void onAdd(TCVideoFileInfo fileInfo) {
        mOnAddListener.onAdd(fileInfo);
    }

    @Override
    public void setOnItemAddListener(ItemView.OnAddListener listener) {
        mOnAddListener = listener;
    }

}
