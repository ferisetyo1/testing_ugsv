package com.tencent.qcloud.ugckit.module.effect.bubble;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.component.circlebmp.TCCircleView;
import com.tencent.qcloud.ugckit.component.seekbar.TCColorView;


import java.util.ArrayList;
import java.util.List;

/**
 * 配置气泡字幕样式、以及字体颜色的控件
 */
public class BubbleSubtitlePannel extends FrameLayout implements IBubbleSubtitlePannel, BubbleAdapter.OnItemClickListener, View.OnClickListener, TCColorView.OnSelectColorListener, BubbleFontStyleAdapter.OnFontClickListener {
    private View mContentView;
    private RecyclerView mRecycleBubbles;
    private RecyclerView mRecycleFont;
    private BubbleAdapter mBubbleAdapter;
    private BubbleFontStyleAdapter mFontAdapter;
    private List<TCBubbleInfo> mBubbles;
    private List<BubbleFontStyle> mFonts;
    private ImageView mImageClose;
    private TextView mTextBubbleStyle;   //气泡样式
    private TextView mTextStyle;   //气泡样式
    private TextView mTextColor;         //文字颜色
    private TCColorView mColorView;
    private TCCircleView mCvColor;
    private LinearLayout mLlColor;
    private CardView btnSubmit;
    private EditText mEdtSubtitle;

    @Nullable
    private TCSubtitleInfo mSubtitleInfo;
    private OnBubbleSubtitleCallback mCallback;

    public BubbleSubtitlePannel(@NonNull Context context) {
        super(context);
        init();
    }

    public BubbleSubtitlePannel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleSubtitlePannel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mSubtitleInfo = new TCSubtitleInfo();

        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.ugckit_layout_bubble_window, this, true);
        initViews(mContentView);
    }

    private void initViews(@NonNull View contentView) {
        mImageClose = (ImageView) contentView.findViewById(R.id.iv_close);
        mImageClose.setOnClickListener(this);
        mRecycleBubbles = (RecyclerView) contentView.findViewById(R.id.bubble_rv_style);
        mRecycleFont = (RecyclerView) contentView.findViewById(R.id.rv_font_style);
        mTextBubbleStyle = (TextView) contentView.findViewById(R.id.bubble_iv_bubble);
        mTextBubbleStyle.setOnClickListener(this);
        mTextStyle = (TextView) contentView.findViewById(R.id.bubble_iv_style);
        mTextStyle.setOnClickListener(this);
        mTextColor = (TextView) contentView.findViewById(R.id.bubble_iv_color);
        mTextColor.setOnClickListener(this);
        mLlColor = (LinearLayout) contentView.findViewById(R.id.bubble_ll_color);
        mCvColor = (TCCircleView) contentView.findViewById(R.id.bubble_cv_color);
        mColorView = (TCColorView) contentView.findViewById(R.id.bubble_color_view);
        mEdtSubtitle = (EditText) contentView.findViewById(R.id.edt_subtitle);
        btnSubmit = (CardView) contentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        mColorView.setOnSelectColorListener(this);
        mTextBubbleStyle.setSelected(true);
        mRecycleBubbles.setVisibility(View.VISIBLE);

        mEdtSubtitle.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });

        loadAllFontStyle();
    }

    private void loadAllFontStyle() {
     mFonts= new ArrayList();
     mFonts.add(new BubbleFontStyle("Poppins (Medium)",R.font.poppins_medium));
     mFonts.add(new BubbleFontStyle("Inter (Medium)",R.font.inter_medium));
     mFonts.add(new BubbleFontStyle("Caveat (Bold)",R.font.caveat_bold));
     mFonts.add(new BubbleFontStyle("Playfair Display (Bold)",R.font.playfair_display_bold));
     mFonts.add(new BubbleFontStyle("Noto serif (Bold)",R.font.playfair_display_bold));

     mFontAdapter = new BubbleFontStyleAdapter(mFonts);
     mFontAdapter.setOnFontClickListener(this);
     mRecycleFont.setAdapter(mFontAdapter);
    }

    private void enterAnimator() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.play(translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                BubbleSubtitlePannel.this.setVisibility(VISIBLE);
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

    private void resetInfo() {
        mSubtitleInfo.setText("Isi field text lalu tekan submit untuk mengubah!");
        mEdtSubtitle.setText("");
        mSubtitleInfo.setBubblePos(0);
        // 创建一个默认的
        TCBubbleInfo info = new TCBubbleInfo();
        info.setHeight(0);
        info.setWidth(0);
        info.setDefaultSize(40);
        info.setBubblePath(null);
        info.setIconPath(null);
        info.setRect(0, 0, 0, 0);
        mSubtitleInfo.setBubbleInfo(info);
    }

    @Override
    public void loadAllBubble(List<TCBubbleInfo> list) {
        mBubbles = list;
        mRecycleBubbles.setVisibility(View.VISIBLE);
        mBubbleAdapter = new BubbleAdapter(list);
        mBubbleAdapter.setOnItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(mContentView.getContext(), 4, GridLayoutManager.VERTICAL, false);
        mRecycleBubbles.setLayoutManager(manager);
        mRecycleBubbles.setAdapter(mBubbleAdapter);
    }

    @Override
    public void show(@Nullable TCSubtitleInfo info) {
        if (info == null) {
            resetInfo();
        } else {
            mSubtitleInfo = info;
            mEdtSubtitle.setText(info.getText());
        }
        mContentView.post(new Runnable() {
            @Override
            public void run() {
                enterAnimator();
            }
        });
    }

    @Override
    public void dismiss() {
        exitAnimator();
        mCallback.dimiss();
    }

    private void exitAnimator() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mContentView, "translationY", 0,
                mContentView.getHeight());
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
                BubbleSubtitlePannel.super.setVisibility(GONE);
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
    public void onItemClick(View view, int position) {
        mSubtitleInfo.setBubblePos(position);
        mSubtitleInfo.setBubbleInfo(mBubbles.get(position));
        callback();
    }

    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.bubble_iv_bubble) {
            mTextBubbleStyle.setSelected(true);
            mRecycleBubbles.setVisibility(View.VISIBLE);

            mTextStyle.setSelected(false);
            mRecycleFont.setVisibility(View.GONE);

            mTextColor.setSelected(false);
            mLlColor.setVisibility(View.GONE);
        } else if (i == R.id.bubble_iv_color) {
            mTextBubbleStyle.setSelected(false);
            mRecycleBubbles.setVisibility(View.GONE);

            mTextStyle.setSelected(false);
            mRecycleFont.setVisibility(View.GONE);

            mTextColor.setSelected(true);
            mLlColor.setVisibility(View.VISIBLE);
        } else if (i == R.id.bubble_iv_style) {
            mTextBubbleStyle.setSelected(false);
            mRecycleBubbles.setVisibility(View.GONE);

            mTextStyle.setSelected(true);
            mRecycleFont.setVisibility(View.VISIBLE);

            mLlColor.setVisibility(View.GONE);
            mTextColor.setSelected(false);
        } else if (i == R.id.iv_close) {

            dismiss();

        } else if (i == R.id.btn_submit) {
            String text = mEdtSubtitle.getText().toString();
            if (text.isEmpty()) {
                return;
            }
            mSubtitleInfo.setText(text);
            callback();
        }
    }

    @Override
    public void onFinishColor(@ColorInt int color) {
        mSubtitleInfo.setTextColor(color);
        callback();
    }

    @Override
    public void onProgressColor(@ColorInt int color) {
        mCvColor.setColor(color);
    }

    private void callback() {
        if (mCallback != null) {
            mCallback.onBubbleSubtitleCallback(mSubtitleInfo);
        }
    }

    @Override
    public void setOnBubbleSubtitleCallback(OnBubbleSubtitleCallback callback) {
        mCallback = callback;
    }

    public void setTabTextColor(int selectedColor, int normalColor) {
        int[] colors = new int[]{selectedColor, normalColor};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled};
        ColorStateList colorList = new ColorStateList(states, colors);

        mTextBubbleStyle.setTextColor(colorList);
        mTextColor.setTextColor(colorList);
    }

    public void setTabTextSize(int size) {
        mTextBubbleStyle.setTextSize(size);
        mTextColor.setTextSize(size);
    }

    public void setCancelIconResource(int resid) {
        mImageClose.setImageResource(resid);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onFontClick(View view, int position) {
        mSubtitleInfo.setTextStyle(mFonts.get(position).getRes());
        callback();
    }
}
