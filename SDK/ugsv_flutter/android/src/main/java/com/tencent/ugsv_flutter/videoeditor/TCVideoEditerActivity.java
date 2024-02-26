package com.tencent.ugsv_flutter.videoeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.tencent.qcloud.ugckit.UGCKitConstants;
import com.tencent.qcloud.ugckit.UGCKitVideoEdit;
import com.tencent.qcloud.ugckit.basic.UGCKitResult;
import com.tencent.qcloud.ugckit.module.editer.IVideoEditKit;
import com.tencent.qcloud.ugckit.module.editer.UGCKitEditConfig;
import com.tencent.qcloud.ugckit.module.effect.VideoEditerSDK;
import com.tencent.qcloud.ugckit.utils.ToastUtil;
// import com.tencent.qcloud.xiaoshipin.mainui.TCMainActivity;
import com.tencent.ugsv_flutter.FlutterCallback;
import com.tencent.ugsv_flutter.R;
import com.tencent.ugsv_flutter.manager.PermissionManager;
import com.tencent.ugsv_flutter.videopublish.TCVideoPublisherActivity;

import java.util.HashMap;
import java.util.Map;


public class TCVideoEditerActivity extends FragmentActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionManager.OnStoragePermissionGrantedListener {

    private static final String TAG = "TCVideoEditerActivity";
    /**
     * 视频路径
     */
    private String mVideoPath;
    private UGCKitVideoEdit mUGCKitVideoEdit;
    // 背景音
    private TextView mTvBgm;
    // 动态滤镜
    private TextView mTvMotion;
    // 时间特效
    private TextView mTvSpeed;
    // 静态滤镜
    private TextView mTvFilter;
    // 贴纸
    private TextView mTvPaster;
    // 字幕
    private TextView mTvSubtitle;
    // 转场
    private TextView mTextTransition;

    private PermissionManager mStoragePermissionManager;

    private IVideoEditKit.OnEditListener mOnVideoEditListener = new IVideoEditKit.OnEditListener() {
        @Override
        public void onEditCompleted(UGCKitResult ugcKitResult) {
            if (ugcKitResult.errorCode == 0) {
                // TODO: Callback output path
                Map<String, String> args = new HashMap<>();
                args.put("outputPath", ugcKitResult.outputPath);
                FlutterCallback.call("onEditCompleted", args);
                startPreviewActivity(ugcKitResult);
            } else {
                ToastUtil.toastShortMessage("edit video failed. error code:" + ugcKitResult.errorCode + ",desc msg:" + ugcKitResult.descMsg);
            }
        }

        @Override
        public void onEditCanceled() {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindowParam();
        // 必须在代码中设置主题(setTheme)或者在AndroidManifest中设置主题(android:theme)
        setTheme(R.style.EditerActivityTheme);
        setContentView(R.layout.activity_video_editer);
        initData();
        mUGCKitVideoEdit = (UGCKitVideoEdit) findViewById(R.id.video_edit);
        mStoragePermissionManager = new PermissionManager(this, PermissionManager.PermissionType.STORAGE);
        mStoragePermissionManager.setOnStoragePermissionGrantedListener(this);
        mStoragePermissionManager.checkoutIfShowPermissionIntroductionDialog();
        UGCKitEditConfig config = new UGCKitEditConfig();
        config.isPublish = true;
        mUGCKitVideoEdit.setConfig(config);
        if (!TextUtils.isEmpty(mVideoPath)) {
            mUGCKitVideoEdit.setVideoPath(mVideoPath);
        }
        // 初始化播放器
        mUGCKitVideoEdit.initPlayer();

        mTvBgm = (TextView) findViewById(R.id.tv_bgm);
        mTvMotion = (TextView) findViewById(R.id.tv_motion);
        mTvSpeed = (TextView) findViewById(R.id.tv_speed);
        mTvFilter = (TextView) findViewById(R.id.tv_filter);
        mTvPaster = (TextView) findViewById(R.id.tv_paster);
        mTvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        mTextTransition = (TextView) findViewById(R.id.tv_transition);

        mTvBgm.setOnClickListener(this);
        mTvMotion.setOnClickListener(this);
        mTvSpeed.setOnClickListener(this);
        mTvFilter.setOnClickListener(this);
        mTvPaster.setOnClickListener(this);
        mTvSubtitle.setOnClickListener(this);
        mTextTransition.setOnClickListener(this);
    }

    private void initWindowParam() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initData() {
        mVideoPath = getIntent().getStringExtra(UGCKitConstants.VIDEO_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUGCKitVideoEdit.initPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUGCKitVideoEdit.setOnVideoEditListener(mOnVideoEditListener);
        mUGCKitVideoEdit.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUGCKitVideoEdit.stop();
        mUGCKitVideoEdit.setOnVideoEditListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUGCKitVideoEdit.release();
    }

    private void startPreviewActivity(UGCKitResult ugcKitResult) {
        Log.d("startPreviewActivity", String.valueOf(ugcKitResult.isPublish));
        if (TextUtils.isEmpty(ugcKitResult.outputPath)) {
            return;
        }
        long duration = VideoEditerSDK.getInstance().getVideoPlayDuration();
        if (ugcKitResult.isPublish) {
            Context applicationContext = getApplicationContext();
            Intent intent = new Intent(applicationContext, TCVideoPublisherActivity.class);
            intent.putExtra(UGCKitConstants.VIDEO_PATH, ugcKitResult.outputPath);
            if (!TextUtils.isEmpty(ugcKitResult.coverPath)) {
                intent.putExtra(UGCKitConstants.VIDEO_COVERPATH, ugcKitResult.coverPath);
            }
            intent.putExtra(UGCKitConstants.VIDEO_RECORD_DURATION, duration);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        mUGCKitVideoEdit.backPressed();
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.tv_bgm) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_BGM);
        } else if (id == R.id.tv_motion) {
//            startEffectActivity(UGCKitConstants.TYPE_EDITER_MOTION);
            startEffectActivity(UGCKitConstants.TYPE_EDITER_TRANSITION);
        } else if (id == R.id.tv_speed) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_SPEED);
        } else if (id == R.id.tv_filter) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_FILTER);
        } else if (id == R.id.tv_paster) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_PASTER);
        } else if (id == R.id.tv_subtitle) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_SUBTITLE);
        } else if (id == R.id.tv_transition) {
            startEffectActivity(UGCKitConstants.TYPE_EDITER_TRANSITION);
        }
    }

    /**
     * 跳转到视频特效编辑界面
     *
     * @param effectType {@link UGCKitConstants#TYPE_EDITER_BGM} 添加背景音</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_MOTION} 添加动态滤镜</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_SPEED} 添加时间特效</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_FILTER} 添加静态滤镜</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_PASTER} 添加贴纸</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_SUBTITLE} 添加字幕</p>
     *                   {@link UGCKitConstants#TYPE_EDITER_TRANSITION} 添加转场特效</p>
     */
    private void startEffectActivity(int effectType) {
        Intent intent = new Intent(this, TCVideoEffectActivity.class);
        intent.putExtra(UGCKitConstants.KEY_FRAGMENT, effectType);
        startActivityForResult(intent, UGCKitConstants.ACTIVITY_OTHER_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mStoragePermissionManager.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onStoragePermissionGranted() {

    }
}
