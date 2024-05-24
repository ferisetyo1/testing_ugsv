package com.tencent.ugsv_flutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tencent.qcloud.ugckit.UGCKit;
import com.tencent.qcloud.ugckit.UGCKitConstants;
import com.tencent.qcloud.ugckit.basic.JumpActivityMgr;
import com.tencent.qcloud.ugckit.basic.OnUpdateUIListener;
import com.tencent.qcloud.ugckit.component.dialogfragment.ProgressFragmentUtil;
import com.tencent.qcloud.ugckit.module.VideoGenerateKit;
import com.tencent.qcloud.ugckit.module.effect.VideoEditerSDK;
import com.tencent.qcloud.ugckit.module.record.draft.RecordDraftInfo;
import com.tencent.qcloud.ugckit.module.record.draft.RecordDraftManager;
import com.tencent.qcloud.ugckit.utils.FileUtils;
import com.tencent.qcloud.ugckit.utils.TCUserMgr;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoEditer;
import com.tencent.ugc.TXVideoInfoReader;
import com.tencent.ugsv_flutter.config.TCConfigManager;
import com.tencent.ugsv_flutter.manager.LicenseManager;
import com.tencent.ugsv_flutter.videochoose.TCVideoPickerActivity;
import com.tencent.ugsv_flutter.videoeditor.TCVideoCutActivity;
import com.tencent.ugsv_flutter.videorecord.TCVideoRecordActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class UgsvFlutterPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private MethodChannel apiChannel;
    private Activity mainActivity;

    public static MethodChannel.Result result;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        apiChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "ugsv_flutter");
        apiChannel.setMethodCallHandler(this);
        FlutterCallback.init(apiChannel);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "openVideoChooser": {
                openVideoChooser();
                break;
            }
            case "openVideoEditor": {
                String videoPath = call.argument("videoPath");
                openVideoEditor(videoPath);
                break;
            }
            case "openVideoRecorder": {
                HashMap music = call.argument("music");
                openVideoRecorder(music);
                UgsvFlutterPlugin.result = result;
                break;
            }
            case "hasLastRecordPart": {
                boolean has = hasLastRecordPart();
                result.success(has);
                break;
            }
            case "deleteLastRecordPart": {
                deleteLastRecordPart();
                break;
            }
            case "setUgcLicense": {
                String licenseUrl = call.argument("licenseUrl");
                String licenseKey = call.argument("licenseKey");
                setUgcLicense(licenseUrl, licenseKey);
                break;
            }
            case "setXMagicLicense": {
                String licenseUrl = call.argument("licenseUrl");
                String licenseKey = call.argument("licenseKey");
                setXMagicLicense(licenseUrl, licenseKey);
                break;
            }
            case "saveVideoWithWatermark":{
                String url = call.argument("url");
                setVideoWatermark(url,result);
                break;
            }
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        apiChannel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
        Context applicationContext = binding.getActivity().getApplicationContext();
        TCConfigManager.init(applicationContext);
        UGCKit.init(applicationContext);
        TCUserMgr.getInstance().initContext(applicationContext);
        mainActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        mainActivity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {
        mainActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        mainActivity = null;
    }

    void openVideoChooser() {
        Intent intent = new Intent(mainActivity, TCVideoPickerActivity.class);
        mainActivity.startActivity(intent);
    }

    void openVideoEditor(String videoPath) {
        Intent intent = new Intent(mainActivity, TCVideoCutActivity.class);
        intent.putExtra(UGCKitConstants.VIDEO_PATH, videoPath);
        mainActivity.startActivity(intent);
    }

    void openVideoRecorder(HashMap music) {
        Intent intent = new Intent(mainActivity, TCVideoRecordActivity.class);
        try {
            if (music != null) {
                intent.putExtra(UGCKitConstants.MUSIC_ID,  (int) music.get("id"));
                intent.putExtra(UGCKitConstants.MUSIC_NAME, (String) music.get("audio_title"));
                intent.putExtra(UGCKitConstants.MUSIC_PATH, (String) music.get("file_url"));
                intent.putExtra(UGCKitConstants.MUSIC_THUMBNAIL, (String) music.get("thumbnail"));
                intent.putExtra(UGCKitConstants.MUSIC_ARTIST, (String) music.get("artist_name"));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mainActivity.startActivity(intent);
    }

    boolean hasLastRecordPart() {
        final RecordDraftManager recordDraftManager = new RecordDraftManager(mainActivity);
        RecordDraftInfo lastDraftInfo = recordDraftManager.getLastDraftInfo();
        if (lastDraftInfo == null) {
            return false;
        }
        final List<RecordDraftInfo.RecordPart> recordPartList = lastDraftInfo.getPartList();
        return recordPartList != null && !recordPartList.isEmpty();
    }

    void deleteLastRecordPart() {
        final RecordDraftManager recordDraftManager = new RecordDraftManager(mainActivity);
        RecordDraftInfo lastDraftInfo = recordDraftManager.getLastDraftInfo();
        if (lastDraftInfo == null) {
            return;
        }
        final List<RecordDraftInfo.RecordPart> recordPartList = lastDraftInfo.getPartList();
        recordDraftManager.deleteLastRecordDraft();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (final RecordDraftInfo.RecordPart recordPart : recordPartList) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    FileUtils.deleteFile(recordPart.getPath());
                }
            });
        }
    }

    void setUgcLicense(String licenseUrl, String licenseKey) {
        LicenseManager.setUgcLicense(mainActivity, licenseUrl, licenseKey);
    }

    void setXMagicLicense(String licenseUrl, String licenseKey) {
        LicenseManager.setXMagicLicense(licenseUrl, licenseKey);
    }

    void setVideoWatermark(String path, Result result){
        setVideoPath(path);
        VideoGenerateKit.getInstance().addWaterMark();
        VideoGenerateKit.getInstance().setmSaveToDCIM(true);
        VideoGenerateKit.getInstance().setOnUpdateUIListener(new OnUpdateUIListener() {
            @Override
            public void onUIProgress(float progress) {

            }

            @Override
            public void onUIComplete(int retCode, String descMsg) {
                Log.d("generateWatermark",descMsg);
                result.success(VideoGenerateKit.getInstance().getVideoOutputPath());
            }

            @Override
            public void onUICancel() {
                result.error("100","UI Cancel","Canceled UI");
            }
        });
        VideoGenerateKit.getInstance().startGenerate();

    }

    public void setVideoPath(String videoPath) {
        // 获取TXVideoEditer，兼容"快速导入"之前没有初始化TXVideoEditer；"全功能导入"，裁剪时已经预处理了视频，此时初始化了TXVideoEditer
        TXVideoEditer editer = VideoEditerSDK.getInstance().getEditer();
        if (editer == null) {
            VideoEditerSDK.getInstance().initSDK();
        }
//        Log.i(TAG, "[UGCKit][VideoEdit][QuickImport]setVideoPath:" + videoPath);
        VideoEditerSDK.getInstance().setVideoPath(videoPath);

        // 获取TXVideoInfo，兼容"快速导入"新传入videoPath，之前没有获取视频信息;"全功能导入"，裁剪时已经获取视频基本信息。
        TXVideoEditConstants.TXVideoInfo info = VideoEditerSDK.getInstance().getTXVideoInfo();
        if (info == null) {
            // 从"录制"进入，录制勾选了"进入编辑"，info在录制界面已设置，此处不为null
            // 从"录制"进入，录制不勾选"进入编辑"，info为null，需要重新获取
            info = TXVideoInfoReader.getInstance(UGCKit.getAppContext()).getVideoFileInfo(videoPath);
            VideoEditerSDK.getInstance().setTXVideoInfo(info);
        }

        // 初始化缩略图列表，编辑缩略图1秒钟一张(先清空缩略图列表)
        VideoEditerSDK.getInstance().clearThumbnails();

        long startTime = VideoEditerSDK.getInstance().getCutterStartTime();
        long endTime = VideoEditerSDK.getInstance().getCutterEndTime();
        if (endTime > startTime) {
//            Log.i(TAG, "[UGCKit][VideoEdit][QuickImport]load thumbnail start time:" + startTime + ",end time:" + endTime);
        }

        VideoEditerSDK.getInstance().setCutterStartTime(0, info.duration);
        VideoEditerSDK.getInstance().setVideoDuration(info.duration);
        VideoEditerSDK.getInstance().initThumbnailList(new TXVideoEditer.TXThumbnailListener() {
            @Override
            public void onThumbnail(final int index, long timeMs, final Bitmap bitmap) {
//                Log.d(TAG, "onThumbnail index:" + index + ",timeMs:" + timeMs);
                VideoEditerSDK.getInstance().addThumbnailBitmap(timeMs, bitmap);
            }
        }, 1000);

        JumpActivityMgr.getInstance().setQuickImport(true);
    }
}
