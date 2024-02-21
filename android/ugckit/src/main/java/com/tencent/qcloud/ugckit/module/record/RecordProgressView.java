package com.tencent.qcloud.ugckit.module.record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.module.record.interfaces.IRecordProgressView;

import java.util.ArrayList;

/**
 * 录制进度条
 */
public class RecordProgressView extends View implements IRecordProgressView {
    private final String TAG = "RecordProgressView";

    private Paint mRecordPaint;
    private Paint mPendingPaint;
    private Paint mSpacePaint;
    @Nullable
    private Handler mHandler;
    private ArrayList<ClipInfo> mClipInfoList;
    private ClipInfo mCurClipInfo;
    private int mMaxDuration;
    private int mMinDuration;
    private int mLastTotalDuration;
    public int mNormalColor;           // 已经录制的视频进度条颜色
    public int mDeleteColor;           // 删除上一段选中的进度条颜色
    public int mBackgroundColor;       // 进度条背景颜色
    public int mSpaceColor;            // 多段录制间隔颜色
    private boolean isPending;
    private boolean isCursorShow = false;
    private boolean isInProgress = false;

    public RecordProgressView(Context context) {
        super(context);
        init();
    }

    public RecordProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRecordPaint = new Paint();
        mPendingPaint = new Paint();
        mSpacePaint = new Paint();

        mBackgroundColor = getResources().getColor(R.color.ugckit_white);
        mNormalColor = getResources().getColor(R.color.ugckit_record_progress);
        mDeleteColor = getResources().getColor(R.color.ugckit_record_progress_pending);
        mSpaceColor = getResources().getColor(R.color.ugckit_white);

        configurePaint(mRecordPaint, mNormalColor);
        configurePaint(mSpacePaint, mSpaceColor);
        mSpacePaint.setStrokeCap(Paint.Cap.SQUARE);
        configurePaint(mPendingPaint, mDeleteColor);

        mClipInfoList = new ArrayList<ClipInfo>();
        mCurClipInfo = new ClipInfo();
        isPending = false;

        mHandler = new Handler();
        startCursorBling();
    }

    private void configurePaint(Paint paint, int color) {
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12f);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint mBackground = new Paint();
        configurePaint(mBackground, mBackgroundColor);
//        canvas.drawLine(getWidth(),0f,0,getHeight(),mBackground);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 6;
        RectF rect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rect, 0, 360, false, mBackground);

        int lastTotalProgress = 0;
        float totalWidth = 270;
        int index = 0;
        for (ClipInfo clipInfo : mClipInfoList) {
            float sweep = (clipInfo.progress) / (float) mMaxDuration * 360;
            float newWidth = (lastTotalProgress + clipInfo.progress) / (float) mMaxDuration * 360;
            switch (clipInfo.clipType) {
                case ClipInfo.CLIP_TYPE_SPACE:
                    canvas.drawArc(rect, totalWidth - getResources().getDimension(R.dimen.ugckit_progress_divider), getResources().getDimension(R.dimen.ugckit_progress_divider), false, mSpacePaint);
                    break;
                case ClipInfo.CLIP_TYPE_PROGRESS:
                    canvas.drawArc(rect, totalWidth, sweep, false, mRecordPaint);
//                    newWidth = newWidth + 10;
//                    canvas.drawArc(rect, 270+newWidth+getResources().getDimension(R.dimen.ugckit_progress_divider), getResources().getDimension(R.dimen.ugckit_progress_divider), false, mSpacePaint);
                    break;
                case ClipInfo.CLIP_TYPE_PENDING:
                    canvas.drawArc(rect, totalWidth, sweep, false, mPendingPaint);
                    break;
            }
            lastTotalProgress += clipInfo.progress;
            totalWidth = 270 + newWidth;
            index++;
        }
//        Paint paint = new Paint();
//        paint.setTextSize(24);
//        paint.setColor(Color.WHITE);
//        canvas.drawText(totalWidth+"",centerX,centerY,paint);
        if (mCurClipInfo != null && mCurClipInfo.progress != 0) {
            canvas.drawArc(rect, totalWidth, mCurClipInfo.progress / (float) mMaxDuration * 360, false, mRecordPaint);
            totalWidth = totalWidth + mCurClipInfo.progress / (float) mMaxDuration * 360;
        }
        if (lastTotalProgress + mCurClipInfo.progress < mMinDuration) {
            canvas.drawArc(rect, mMinDuration / (float) mMaxDuration * 360,
                    mMinDuration / (float) mMaxDuration * 360 + getResources().getDimension(R.dimen.ugckit_progress_min_pos), false, mSpacePaint);
        }
//        if (isCursorShow || isInProgress) {
//            canvas.drawArc(rect,totalWidth, totalWidth + getResources().getDimension(R.dimen.ugckit_progress_cursor), false, mSpacePaint);
//        }
    }

    @Override
    public void setNormalColor(@ColorInt int color) {
        mNormalColor = color;
        mRecordPaint.setColor(mNormalColor);
    }

    @Override
    public void setDeleteColor(@ColorInt int color) {
        mDeleteColor = color;
        mPendingPaint.setColor(mDeleteColor);
    }

    @Override
    public void setSpaceColor(@ColorInt int color) {
        mSpaceColor = color;
        mSpacePaint.setColor(mSpaceColor);
    }

    private class ClipInfo {
        public static final int CLIP_TYPE_PROGRESS = 1;
        public static final int CLIP_TYPE_PENDING = 2;
        public static final int CLIP_TYPE_SPACE = 3;

        public int progress;
        public int clipType;
    }

    @Override
    public void setMaxDuration(int maxDuration) {
        this.mMaxDuration = maxDuration;
    }

    @Override
    public void setMinDuration(int minDuration) {
        this.mMinDuration = minDuration;
    }

    @Override
    public void setProgress(int progress) {
        isInProgress = true;
        stopCursorBling();
        if (isPending) {
            for (ClipInfo clipInfo : mClipInfoList) {
                if (clipInfo.clipType == ClipInfo.CLIP_TYPE_PENDING) {
                    clipInfo.clipType = ClipInfo.CLIP_TYPE_PROGRESS;
                    isPending = false;
                    break;
                }
            }
        }
        this.mCurClipInfo.clipType = ClipInfo.CLIP_TYPE_PROGRESS;
        this.mCurClipInfo.progress = progress - mLastTotalDuration;
        invalidate();
    }

    public void clipComplete() {
        isInProgress = false;

        mLastTotalDuration = mLastTotalDuration + mCurClipInfo.progress;

        mClipInfoList.add(mCurClipInfo);
        ClipInfo clipInfo = new ClipInfo();
        clipInfo.clipType = ClipInfo.CLIP_TYPE_SPACE;
        clipInfo.progress = 0;
        mClipInfoList.add(clipInfo);
        mCurClipInfo = new ClipInfo();

        startCursorBling();
        invalidate();
    }

    @Override
    public void selectLast() {
        if (mClipInfoList.size() >= 2) {
            ClipInfo clipInfo = mClipInfoList.get(mClipInfoList.size() - 2);
            clipInfo.clipType = ClipInfo.CLIP_TYPE_PENDING;
            isPending = true;
            invalidate();
        }
    }

    @Override
    public void deleteLast() {
        if (mClipInfoList.size() >= 2) {
            mClipInfoList.remove(mClipInfoList.size() - 1);
            ClipInfo clipInfo = mClipInfoList.remove(mClipInfoList.size() - 1);
            mLastTotalDuration = mLastTotalDuration - clipInfo.progress;
        }
        invalidate();
    }

    @NonNull
    private Runnable cursorRunnable = new Runnable() {
        @Override
        public void run() {
            isCursorShow = !isCursorShow;
            mHandler.postDelayed(cursorRunnable, 500);
            invalidate();
        }
    };

    private void startCursorBling() {
        if (mHandler != null) {
            mHandler.postDelayed(cursorRunnable, 500);
        }
    }

    private void stopCursorBling() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

}
