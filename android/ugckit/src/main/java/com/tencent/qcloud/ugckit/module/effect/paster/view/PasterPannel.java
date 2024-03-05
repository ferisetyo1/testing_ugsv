package com.tencent.qcloud.ugckit.module.effect.paster.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.tencent.qcloud.ugckit.module.effect.paster.IPasterPannel;
import com.tencent.qcloud.ugckit.module.effect.paster.TCPasterInfo;
import com.tencent.qcloud.ugckit.utils.UIAttributeUtil;
import com.tencent.qcloud.ugckit.R;


import java.util.List;

/**
 * 贴纸面板
 */
public class PasterPannel extends LinearLayout implements IPasterPannel, View.OnClickListener {
    private Context                            mContext;
    private int                                mCurrentTab;
    private TabLayout tabs;
    private RecyclerView                       mRecyclerView;
    private PasterAdapter                      mPasterAdapter;
    private ImageView                          mImageSure;
    private IPasterPannel.OnTabChangedListener mOnTabChangedListener;
    private IPasterPannel.OnAddClickListener   mOnAddClickListener;
    private IPasterPannel.OnItemClickListener  mOnItemClickListener;

    public PasterPannel(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PasterPannel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasterPannel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.ugckit_layout_paster_select, this);
        tabs=findViewById(R.id.tabs_paster);

        mRecyclerView = (RecyclerView) findViewById(R.id.paster_recycler_view);
        mImageSure = (ImageView) findViewById(R.id.paster_btn_done);
        mImageSure.setOnClickListener(this);

        mCurrentTab = TAB_PASTER;

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    mOnTabChangedListener.onTabChanged(TAB_PASTER);
                }else{
                    mOnTabChangedListener.onTabChanged(TAB_ANIMATED_PASTER);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void setPasterInfoList(List<TCPasterInfo> pasterInfoList,int spanCount) {
        mPasterAdapter = new PasterAdapter(pasterInfoList);
        mPasterAdapter.setOnItemClickListener(mOnItemClickListener);

        GridLayoutManager manager = new GridLayoutManager(mContext, spanCount);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mPasterAdapter);
    }

    @Override
    public void onClick(@NonNull View view) {
        int i = view.getId();
        if (i == R.id.paster_btn_done) {
            exitAnimator();
        }
    }

    @Override
    public void show() {
        this.post(new Runnable() {
            @Override
            public void run() {
                enterAnimator();
            }
        });
    }

    @Override
    public void dismiss() {
        this.post(new Runnable() {
            @Override
            public void run() {
                exitAnimator();
            }
        });
    }

    private void enterAnimator() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationY", this.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.play(translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                PasterPannel.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void exitAnimator() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationY", 0,
                this.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.play(translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PasterPannel.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    @Override
    public void setOnTabChangedListener(IPasterPannel.OnTabChangedListener listener) {
        mOnTabChangedListener = listener;
    }

    @Override
    public void setOnItemClickListener(IPasterPannel.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void setOnAddClickListener(IPasterPannel.OnAddClickListener listener) {
        mOnAddClickListener = listener;
    }

    public void setCancelIconResource(int resid) {
        mImageSure.setImageResource(resid);
    }

    @Override
    public int getCurrentTab() {
        return mCurrentTab;
    }
}
