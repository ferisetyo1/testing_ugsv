package com.tencent.qcloud.ugckit.module.record;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.qcloud.ugckit.R;
import com.tencent.ugc.TXRecordCommon;

/**
 * 屏比，目前有三种（0.3；0.5；1; 2; 3）
 */
public class SpeedView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "SpeedView";
    private Activity mActivity;
    private TextView mTextSpeed;
    private ImageView mImageSpeedCurr;
    private TextView mTextSpeedFirst;
    private TextView mTextSpeedSecond;
    private TextView mTextSpeedThird;
    private TextView mTextSpeedFourth;
    private TextView mTextSpeedFifth;
    private ImageView mImageSpeedeMask;
    private RelativeLayout mLayoutSpeedSelect;
    private boolean mToggleSpeed;
    private int mFirstSpeed; // UI上三个位置的屏比分别对应哪个Icon
    private int mSecondSpeed;
    private int mThirdSpeed;
    private int mFourthSpeed;
    private int mFifthSpeed;
    private int mCurrentSpeed;
    private OnSpeedListener mOnSpeedListener;

    public SpeedView(Context context) {
        super(context);
        initViews();
    }

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mActivity = (Activity) getContext();
        inflate(mActivity, R.layout.ugckit_speed_view, this);

        mTextSpeed = (TextView) findViewById(R.id.tv_speed);
        mImageSpeedCurr = (ImageView) findViewById(R.id.iv_speed);
        mImageSpeedeMask = (ImageView) findViewById(R.id.iv_speed_mask);
        mTextSpeedFirst = (TextView) findViewById(R.id.iv_speed_first);
        mTextSpeedSecond = (TextView) findViewById(R.id.iv_speed_second);
        mTextSpeedThird = (TextView) findViewById(R.id.iv_speed_third);
        mTextSpeedFourth = (TextView) findViewById(R.id.iv_speed_fourth);
        mTextSpeedFifth = (TextView) findViewById(R.id.iv_speed_fifth);
        mLayoutSpeedSelect = (RelativeLayout) findViewById(R.id.layout_speed_select);

        mImageSpeedCurr.setOnClickListener(this);
        mTextSpeedFirst.setOnClickListener(this);
        mTextSpeedSecond.setOnClickListener(this);
        mTextSpeedThird.setOnClickListener(this);
        mTextSpeedFourth.setOnClickListener(this);
        mTextSpeedFifth.setOnClickListener(this);

        mFirstSpeed = TXRecordCommon.RECORD_SPEED_SLOWEST;
        mSecondSpeed = TXRecordCommon.RECORD_SPEED_SLOW;
        mThirdSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;
        mFourthSpeed = TXRecordCommon.RECORD_SPEED_FAST;
        mFifthSpeed = TXRecordCommon.RECORD_SPEED_FASTEST;
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.iv_speed) { // 点击当前屏比
            toggleSpeedAnim();
        } else if (id == R.id.iv_speed_first) { // 点击第一个位置的屏比
            toggleSpeedAnim();
            selectAnotherSpeed(mFirstSpeed);
        } else if (id == R.id.iv_speed_second) { // 点击第2个位置的屏比
            toggleSpeedAnim();
            selectAnotherSpeed(mSecondSpeed);
        } else if (id == R.id.iv_speed_third) { // 点击第3个位置的屏比
            toggleSpeedAnim();
            selectAnotherSpeed(mThirdSpeed);
        } else if (id == R.id.iv_speed_fourth) { // 点击第4个位置的屏比
            toggleSpeedAnim();
            selectAnotherSpeed(mFourthSpeed);
        } else if (id == R.id.iv_speed_fifth) { // 点击第4个位置的屏比
            toggleSpeedAnim();
            selectAnotherSpeed(mFifthSpeed);
        }
    }

    private void toggleSpeedAnim() {
        if (!mToggleSpeed) {
            showSpeedSelectAnim();
        } else {
            hideSpeedSelectAnim();
        }
        mToggleSpeed = !mToggleSpeed;
    }

    private void selectAnotherSpeed(int targetScale) {
        mCurrentSpeed = targetScale;

        mTextSpeedFirst.setBackgroundResource(R.drawable.normal_background_speed);
        mTextSpeedSecond.setBackgroundResource(R.drawable.normal_background_speed);
        mTextSpeedThird.setBackgroundResource(R.drawable.normal_background_speed);
        mTextSpeedFourth.setBackgroundResource(R.drawable.normal_background_speed);
        mTextSpeedFifth.setBackgroundResource(R.drawable.normal_background_speed);

        mTextSpeedFirst.setTextColor(Color.WHITE);
        mTextSpeedSecond.setTextColor(Color.WHITE);
        mTextSpeedThird.setTextColor(Color.WHITE);
        mTextSpeedFourth.setTextColor(Color.WHITE);
        mTextSpeedFifth.setTextColor(Color.WHITE);

        switch (mCurrentSpeed) {
            case TXRecordCommon.RECORD_SPEED_SLOWEST:
                if (mOnSpeedListener != null) {
                    mOnSpeedListener.onSpeedSelect(TXRecordCommon.RECORD_SPEED_SLOWEST);
                }
                mImageSpeedCurr.setImageResource(R.drawable.speed__03x);
                mTextSpeedFirst.setBackgroundResource(R.drawable.active_background_speed);
                mTextSpeedFirst.setTextColor(Color.BLACK);
                break;
            case TXRecordCommon.RECORD_SPEED_SLOW:
                if (mOnSpeedListener != null) {
                    mOnSpeedListener.onSpeedSelect(TXRecordCommon.RECORD_SPEED_SLOW);
                }
                mImageSpeedCurr.setImageResource(R.drawable.speed__05x);
                mTextSpeedSecond.setBackgroundResource(R.drawable.active_background_speed);
                mTextSpeedSecond.setTextColor(Color.BLACK);
                break;
            case TXRecordCommon.RECORD_SPEED_NORMAL:
                if (mOnSpeedListener != null) {
                    mOnSpeedListener.onSpeedSelect(TXRecordCommon.RECORD_SPEED_NORMAL);
                }
                mImageSpeedCurr.setImageResource(R.drawable.speed__1x);
                mTextSpeedThird.setBackgroundResource(R.drawable.active_background_speed);
                mTextSpeedThird.setTextColor(Color.BLACK);
                break;
            case TXRecordCommon.RECORD_SPEED_FAST:
                if (mOnSpeedListener != null) {
                    mOnSpeedListener.onSpeedSelect(TXRecordCommon.RECORD_SPEED_FAST);
                }
                mImageSpeedCurr.setImageResource(R.drawable.speed__2x);
                mTextSpeedFourth.setBackgroundResource(R.drawable.active_background_speed);
                mTextSpeedFourth.setTextColor(Color.BLACK);
                break;
            case TXRecordCommon.RECORD_SPEED_FASTEST:
                if (mOnSpeedListener != null) {
                    mOnSpeedListener.onSpeedSelect(TXRecordCommon.RECORD_SPEED_FASTEST);
                }
                mImageSpeedCurr.setImageResource(R.drawable.speed__3x);
                mTextSpeedFifth.setBackgroundResource(R.drawable.active_background_speed);
                mTextSpeedFifth.setTextColor(Color.BLACK);
                break;

        }
    }

    /**
     * 显示切换屏比动画
     */
    private void showSpeedSelectAnim() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mLayoutSpeedSelect, "translationX",
                2 * (getResources().getDimension(R.dimen.ugckit_aspect_divider) + getResources().getDimension(R.dimen.ugckit_aspect_width)), 0f);
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mLayoutSpeedSelect.setVisibility(View.VISIBLE);
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
    public void hideSpeedSelectAnim() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mLayoutSpeedSelect, "translationX", 0f,
                2 * (getResources().getDimension(R.dimen.ugckit_aspect_divider) + getResources().getDimension(R.dimen.ugckit_aspect_width)));
        showAnimator.setDuration(80);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mLayoutSpeedSelect.setVisibility(GONE);
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
    public void setOnSpeedListener(OnSpeedListener listener) {
        mOnSpeedListener = listener;
    }

    public void enableMask() {
        mImageSpeedeMask.setVisibility(View.VISIBLE);
        mImageSpeedCurr.setEnabled(false);
    }

    public void disableMask() {
        mImageSpeedeMask.setVisibility(View.GONE);
        mImageSpeedCurr.setEnabled(true);
    }

    public void setTextSize(int size) {
        mTextSpeed.setTextSize(size);
    }

    public void setTextColor(int color) {
        mTextSpeed.setTextColor(getResources().getColor(color));
    }

    public void setSpeed(int speed){
        selectAnotherSpeed(speed);
    }

    public interface OnSpeedListener {
        /**
         * 选择一种屏比
         *
         * @param currentSpeed 当前屏比
         */
        void onSpeedSelect(int currentSpeed);
    }
}
