package com.tencent.qcloud.ugckit;

import android.app.KeyguardManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


import com.google.android.material.tabs.TabLayout;
import com.tencent.qcloud.ugckit.module.PlayerManagerKit;
import com.tencent.qcloud.ugckit.module.effect.AbsVideoEffectUI;
import com.tencent.qcloud.ugckit.module.effect.ConfigureLoader;
import com.tencent.qcloud.ugckit.module.effect.TimelineViewUtil;
import com.tencent.qcloud.ugckit.module.effect.VideoEditerSDK;
import com.tencent.qcloud.ugckit.utils.UIAttributeUtil;
import com.tencent.qcloud.ugckit.component.timeline.VideoProgressController;
import com.tencent.qcloud.ugckit.module.effect.utils.DraftEditer;

public class UGCKitVideoEffect extends AbsVideoEffectUI implements VideoProgressController.VideoProgressSeekListener {

    private FragmentActivity mActivity;
    private int mConfirmIcon;
    private OnVideoEffectListener mOnVideoEffectListener;

    public UGCKitVideoEffect(Context context) {
        super(context);
        initDefault();
    }

    public UGCKitVideoEffect(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefault();
    }

    public UGCKitVideoEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
    }

    private void initDefault() {
        mActivity = (FragmentActivity) getContext();
        // 当上个界面为裁剪界面，此时重置裁剪的开始时间和结束时间
        VideoEditerSDK.getInstance().resetDuration();
        // 加载草稿配置
        ConfigureLoader.getInstance().loadConfigToDraft();

        initTitleBar();

        preivewVideo();

        configureTabs();
    }

    private void configureTabs() {
        TabLayout tabs = getTabLayout();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setEffectType(UGCKitConstants.TYPE_EDITER_TRANSITION);
                        break;
                    case 1:
                        setEffectType(UGCKitConstants.TYPE_EDITER_MOTION);
                        break;
                    default:
                        setEffectType(UGCKitConstants.TYPE_EDITER_SPEED);
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

    private void preivewVideo() {
        // 初始化图片时间轴
        getTimelineView().initVideoProgressLayout();
        // 初始化播放器
        getVideoPlayLayout().initPlayerLayout();
        // 开始播放
        PlayerManagerKit.getInstance().startPlay();
    }

    private void initTitleBar() {
//        mConfirmIcon = UIAttributeUtil.getResResources(mActivity, R.attr.editerConfirmIcon, R.drawable.ugckit_ic_edit_effect_confirm_selector);
        getTitleBar().setLeftIcon(R.drawable.round_close_24);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getTitleBar().getLeftIcon().setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.white)));
        }

        getTitleBar().getRightButton().setText("Lanjut");
        getTitleBar().setEnableRightButton(true);
        getTitleBar().setOnBackClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击"返回",清除当前设置的视频特效
                DraftEditer.getInstance().clear();
                // 还原已经设置给SDK的特效
                VideoEditerSDK.getInstance().restore();

                PlayerManagerKit.getInstance().stopPlay();

                if (mOnVideoEffectListener != null) {
                    mOnVideoEffectListener.onEffectCancel();
                }
            }
        });
        getTitleBar().setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清理时间轴的滑动事件，防止与上一级页面的播放状态冲突
                getTimelineView().getVideoProgressView().getRecyclerView().clearOnScrollListeners();

                // 点击"完成"，应用当前设置的视频特效
                ConfigureLoader.getInstance().saveConfigFromDraft();

                PlayerManagerKit.getInstance().stopPlay();

                if (mOnVideoEffectListener != null) {
                    mOnVideoEffectListener.onEffectApply();
                }
            }
        });
    }

    @Override
    public void start() {
        KeyguardManager manager = (KeyguardManager) UGCKit.getAppContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (!manager.inKeyguardRestrictedInputMode()) {
            PlayerManagerKit.getInstance().restartPlay();
        }
    }

    @Override
    public void stop() {
        PlayerManagerKit.getInstance().stopPlay();
    }

    @Override
    public void release() {
        PlayerManagerKit.getInstance().removeAllPreviewListener();
        PlayerManagerKit.getInstance().removeAllPlayStateListener();
        TimelineViewUtil.getInstance().release();
    }

    @Override
    public void setEffectType(int type) {
        TimelineViewUtil.getInstance().setTimelineView(getTimelineView());
        getPlayControlLayout().updateUIByFragment(type);
        getTimelineView().updateUIByFragment(type);
        showFragmentByType(type);
    }

    @Override
    public void setOnVideoEffectListener(OnVideoEffectListener listener) {
        mOnVideoEffectListener = listener;
    }

    @Override
    public void backPressed() {
        DraftEditer.getInstance().clear();
        PlayerManagerKit.getInstance().stopPlay();

        if (mOnVideoEffectListener != null) {
            mOnVideoEffectListener.onEffectCancel();
        }
    }

    @Override
    public void onVideoProgressSeek(long currentTimeMs) {
        PlayerManagerKit.getInstance().previewAtTime(currentTimeMs);
    }

    @Override
    public void onVideoProgressSeekFinish(long currentTimeMs) {
        PlayerManagerKit.getInstance().previewAtTime(currentTimeMs);
    }

    public void showFragmentByType(int type) {
        hideTabs();
        switch (type) {
            case UGCKitConstants.TYPE_EDITER_BGM:
                showFragment(getMusicFragment(), "bgm_setting_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_MOTION:
                showTabs();
                showFragment(getMotionFragment(), "motion_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_SPEED:
                showTabs();
                showFragment(getTimeFragment(), "time_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_FILTER:
                showFragment(getStaticFilterFragment(), "static_filter_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_PASTER:
                showFragment(getPasterFragment(), "paster_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_SUBTITLE:
                showFragment(getBubbleFragment(), "bubble_fragment");
                break;
            case UGCKitConstants.TYPE_EDITER_TRANSITION:
                showTabs();
                showFragment(getTransitionFragment(), "transition_fragment");
                break;
        }
    }

    private void showFragment(@NonNull Fragment fragment, String tag) {
        if (fragment == getCurrentFragment()) return;
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        if (getCurrentFragment() != null) {
            transaction.hide(getCurrentFragment());
        }
        if (!fragment.isAdded()) {
            transaction.add(R.id.fragment_layout, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        setCurrentFragment(fragment);
        transaction.commit();
    }
}
