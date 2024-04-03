package com.tencent.qcloud.ugckit.module.record;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tencent.qcloud.ugckit.R;
import com.tencent.ugc.TXRecordCommon;

/**
 * 屏比，目前有三种（1:1；3:4；4:3; 9:16; 16:9）
 */
public class AspectView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "AspectView";
    private Activity mActivity;
    private TextView mTextAspect;
    private ImageView mImageAspectCurr;
    private ImageView mImageAspectFirst;
    private ImageView mImageAspectSecond;
    private ImageView mImageAspectThird;
    private ImageView mImageAspectFourth;
    private ImageView mImageAspectFifth;
    private ImageView mImageAspecteMask;
    private RelativeLayout mLayoutAspectSelect;
    private boolean mToggleAspect;
    private int mFirstAspect; // UI上三个位置的屏比分别对应哪个Icon
    private int mSecondAspect;
    private int mThirdAspect;
    private int mFourthAspect;
    private int mFifthAspect;
    private int mCurrentAspect;
    private OnAspectListener mOnAspectListener;

    public AspectView(Context context) {
        super(context);
        initViews();
    }

    public AspectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public AspectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mActivity = (Activity) getContext();
        inflate(mActivity, R.layout.ugckit_aspect_view, this);

        mTextAspect = (TextView) findViewById(R.id.tv_aspect);
        mImageAspectCurr = (ImageView) findViewById(R.id.iv_aspect);
        mImageAspecteMask = (ImageView) findViewById(R.id.iv_aspect_mask);
        mImageAspectFirst = (ImageView) findViewById(R.id.iv_aspect_first);
        mImageAspectSecond = (ImageView) findViewById(R.id.iv_aspect_second);
        mImageAspectThird = (ImageView) findViewById(R.id.iv_aspect_third);
        mImageAspectFourth = (ImageView) findViewById(R.id.iv_aspect_fourth);
        mImageAspectFifth = (ImageView) findViewById(R.id.iv_aspect_fifth);
        mLayoutAspectSelect = (RelativeLayout) findViewById(R.id.layout_aspect_select);

        mImageAspectCurr.setOnClickListener(this);
        mImageAspectFirst.setOnClickListener(this);
        mImageAspectSecond.setOnClickListener(this);
        mImageAspectThird.setOnClickListener(this);
        mImageAspectFourth.setOnClickListener(this);
        mImageAspectFifth.setOnClickListener(this);

        mFirstAspect = TXRecordCommon.VIDEO_ASPECT_RATIO_1_1;
        mSecondAspect = TXRecordCommon.VIDEO_ASPECT_RATIO_3_4;
        mThirdAspect = TXRecordCommon.VIDEO_ASPECT_RATIO_16_9;
        mFourthAspect = TXRecordCommon.VIDEO_ASPECT_RATIO_9_16;
        mFifthAspect = TXRecordCommon.VIDEO_ASPECT_RATIO_4_3;
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.iv_aspect) { // 点击当前屏比
            toggleAspectAnim();
        } else if (id == R.id.iv_aspect_first) { // 点击第一个位置的屏比
            toggleAspectAnim();
            selectAnotherAspect(mFirstAspect);
        } else if (id == R.id.iv_aspect_second) { // 点击第2个位置的屏比
            toggleAspectAnim();
            selectAnotherAspect(mSecondAspect);
        } else if (id == R.id.iv_aspect_third) { // 点击第3个位置的屏比
            toggleAspectAnim();
            selectAnotherAspect(mThirdAspect);
        } else if (id == R.id.iv_aspect_fourth) { // 点击第4个位置的屏比
            toggleAspectAnim();
            selectAnotherAspect(mFourthAspect);
        }else if (id == R.id.iv_aspect_fifth) { // 点击第4个位置的屏比
            toggleAspectAnim();
            selectAnotherAspect(mFifthAspect);
        }
    }

    private void toggleAspectAnim() {
        if (!mToggleAspect) {
            showAspectSelectAnim();
        } else {
            hideAspectSelectAnim();
        }
        mToggleAspect = !mToggleAspect;
    }

    private void selectAnotherAspect(int targetScale) {
        mCurrentAspect = targetScale;

        mImageAspectFirst.setBackgroundResource(android.R.color.transparent);
        mImageAspectSecond.setBackgroundResource(android.R.color.transparent);
        mImageAspectThird.setBackgroundResource(android.R.color.transparent);
        mImageAspectFourth.setBackgroundResource(android.R.color.transparent);
        mImageAspectFifth.setBackgroundResource(android.R.color.transparent);

        switch (mCurrentAspect) {
            case TXRecordCommon.VIDEO_ASPECT_RATIO_9_16:
                if (mOnAspectListener != null) {
                    mOnAspectListener.onAspectSelect(TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);
                }
                mImageAspectCurr.setImageResource(R.drawable.menu_ic_ratio_9_16);
                mImageAspectFourth.setBackgroundResource(R.drawable.active_border);
                break;
            case TXRecordCommon.VIDEO_ASPECT_RATIO_3_4:
                if (mOnAspectListener != null) {
                    mOnAspectListener.onAspectSelect(TXRecordCommon.VIDEO_ASPECT_RATIO_3_4);
                }
                mImageAspectCurr.setImageResource(R.drawable.menu_ic_ratio_3_4);
                mImageAspectSecond.setBackgroundResource(R.drawable.active_border);
                break;
            case TXRecordCommon.VIDEO_ASPECT_RATIO_1_1:
                if (mOnAspectListener != null) {
                    mOnAspectListener.onAspectSelect(TXRecordCommon.VIDEO_ASPECT_RATIO_1_1);
                }
                mImageAspectCurr.setImageResource(R.drawable.menu_ic_ratio_1_1);
                mImageAspectFirst.setBackgroundResource(R.drawable.active_border);
                break;
            case TXRecordCommon.VIDEO_ASPECT_RATIO_4_3:
                if (mOnAspectListener != null) {
                    mOnAspectListener.onAspectSelect(TXRecordCommon.VIDEO_ASPECT_RATIO_4_3);
                }
                mImageAspectCurr.setImageResource(R.drawable.menu_ic_ratio_4_3);
                mImageAspectFifth.setBackgroundResource(R.drawable.active_border);
                break;
            case TXRecordCommon.VIDEO_ASPECT_RATIO_16_9:
                if (mOnAspectListener != null) {
                    mOnAspectListener.onAspectSelect(TXRecordCommon.VIDEO_ASPECT_RATIO_16_9);
                }
                mImageAspectCurr.setImageResource(R.drawable.menu_ic_ratio_16_9);
                mImageAspectThird.setBackgroundResource(R.drawable.active_border);
                break;
        }
    }

    /**
     * 显示切换屏比动画
     */
    private void showAspectSelectAnim() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mLayoutAspectSelect, "translationX",
                2 * (getResources().getDimension(R.dimen.ugckit_aspect_divider) + getResources().getDimension(R.dimen.ugckit_aspect_width)), 0f);
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mLayoutAspectSelect.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        showAnimator.start();
    }

    /**
     * 隐藏切换屏比动画
     */
    public void hideAspectSelectAnim() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mLayoutAspectSelect, "translationX", 0f,
                2 * (getResources().getDimension(R.dimen.ugckit_aspect_divider) + getResources().getDimension(R.dimen.ugckit_aspect_width)));
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mLayoutAspectSelect.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        showAnimator.start();
    }

    /**
     * 设置切换"屏比"监听器
     *
     * @param listener
     */
    public void setOnAspectListener(OnAspectListener listener) {
        mOnAspectListener = listener;
    }

    public void enableMask() {
        mImageAspecteMask.setVisibility(View.VISIBLE);
        mImageAspectCurr.setEnabled(false);
    }

    public void disableMask() {
        mImageAspecteMask.setVisibility(View.GONE);
        mImageAspectCurr.setEnabled(true);
    }

    public void setTextSize(int size) {
        mTextAspect.setTextSize(size);
    }

    public void setTextColor(int color) {
        mTextAspect.setTextColor(getResources().getColor(color));
    }

    public void setIconList(int[] iconList) {
        Log.d(TAG, "iconList size:" + iconList.length);

        mImageAspectCurr.setImageResource(iconList[0]);
        mImageAspectFirst.setImageResource(iconList[1]);
        mImageAspectSecond.setImageResource(iconList[2]);
    }

    public void setAspect(int aspectRatio) {
        selectAnotherAspect(aspectRatio);
    }

    public interface OnAspectListener {
        /**
         * 选择一种屏比
         *
         * @param currentAspect 当前屏比
         */
        void onAspectSelect(int currentAspect);
    }
}
