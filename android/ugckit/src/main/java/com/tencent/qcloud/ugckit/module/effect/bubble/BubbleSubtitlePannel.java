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

import android.graphics.Color;
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
import java.util.Arrays;
import java.util.List;

/**
 * 配置气泡字幕样式、以及字体颜色的控件
 */
public class BubbleSubtitlePannel extends FrameLayout implements IBubbleSubtitlePannel, BubbleAdapter.OnItemClickListener, View.OnClickListener, BubbleFontStyleAdapter.OnFontClickListener, GridColorAdapter.OnColorClickListener {
    private View mContentView;
    private RecyclerView mRecycleBubbles;
    private RecyclerView mRecycleFont;
    private RecyclerView mRecycleColor;
    private BubbleAdapter mBubbleAdapter;
    private BubbleFontStyleAdapter mFontAdapter;
    private GridColorAdapter mColorAdapter;
    private List<NewBubbleInfo> mBubbles;
    private List<BubbleFontStyle> mFonts;
    private List<Integer> mColors;
    private ImageView mImageClose;
    private TextView mTextBubbleStyle;   //气泡样式
    private TextView mTextStyle;   //气泡样式
    private TextView mTextColor;         //文字颜色
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
        mRecycleColor = (RecyclerView) contentView.findViewById(R.id.bubble_color_picker);
        mTextBubbleStyle = (TextView) contentView.findViewById(R.id.bubble_iv_bubble);
        mTextBubbleStyle.setOnClickListener(this);
        mTextStyle = (TextView) contentView.findViewById(R.id.bubble_iv_style);
        mTextStyle.setOnClickListener(this);
        mTextColor = (TextView) contentView.findViewById(R.id.bubble_iv_color);
        mTextColor.setOnClickListener(this);
        mEdtSubtitle = (EditText) contentView.findViewById(R.id.edt_subtitle);
        btnSubmit = (CardView) contentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
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
        loadAllColorStyle();
    }

    private void loadAllColorStyle() {
        mColors = new ArrayList<>(Arrays.asList(
                Color.BLACK,
                Color.WHITE,
                Color.parseColor("#EB0023"),
                Color.parseColor("#FDA22B"),
                Color.parseColor("#F4CE47"),
                Color.parseColor("#00AA4B"),
                Color.parseColor("#92E2BF"),
                Color.parseColor("#4791FF"),
                Color.parseColor("#00398F"),
                Color.parseColor("#7655D5"),
                Color.parseColor("#D54DAF"),
                Color.parseColor("#FFD1D8"),
                Color.parseColor("#A4895A"),
                Color.parseColor("#805C1E"),
                Color.parseColor("#BABABA"),
                Color.parseColor("#474747")
        ));
        mColorAdapter = new GridColorAdapter(mColors);
        mColorAdapter.setOnColorClickListener(this);
        mRecycleColor.setAdapter(mColorAdapter);
    }

    private void loadAllFontStyle() {
        mFonts = new ArrayList();
        mFonts.add(new BubbleFontStyle("Poppins  ", R.font.poppins_medium));
        mFonts.add(new BubbleFontStyle("Inter  ", R.font.inter_medium));
        mFonts.add(new BubbleFontStyle("Caveat ", R.font.caveat_bold));
        mFonts.add(new BubbleFontStyle("Playfair  ", R.font.playfair_display_medium));
        mFonts.add(new BubbleFontStyle("Noto serif  ", R.font.noto_serif_bold));

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
        mSubtitleInfo.setText("Halo");
        mEdtSubtitle.setText("");
        mBubbleAdapter.setSelection(0);
        mSubtitleInfo.setBubblePos(0);
        mColorAdapter.setSelected(0);
        mSubtitleInfo.setBubbleInfo(mBubbles.get(0));
    }

    @Override
    public void loadAllBubble(List<NewBubbleInfo> list) {
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
            mRecycleColor.setVisibility(View.GONE);
        } else if (i == R.id.bubble_iv_color) {
            mTextBubbleStyle.setSelected(false);
            mRecycleBubbles.setVisibility(View.GONE);

            mTextStyle.setSelected(false);
            mRecycleFont.setVisibility(View.GONE);

            mTextColor.setSelected(true);
            mRecycleColor.setVisibility(View.VISIBLE);
        } else if (i == R.id.bubble_iv_style) {
            mTextBubbleStyle.setSelected(false);
            mRecycleBubbles.setVisibility(View.GONE);

            mTextStyle.setSelected(true);
            mRecycleFont.setVisibility(View.VISIBLE);

            mRecycleColor.setVisibility(View.GONE);
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

    private void callback() {
        if (mCallback != null) {
            Log.d("callback","calling");
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

    @Override
    public void onColorClick(View view, int position) {
        mSubtitleInfo.setTextColor(mColors.get(position));
        callback();
    }
}
