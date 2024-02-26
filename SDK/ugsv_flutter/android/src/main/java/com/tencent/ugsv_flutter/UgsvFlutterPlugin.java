package com.tencent.ugsv_flutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.activity.*;

import com.tencent.qcloud.ugckit.UGCKit;
import com.tencent.qcloud.ugckit.UGCKitConstants;
import com.tencent.qcloud.ugckit.module.record.draft.RecordDraftInfo;
import com.tencent.qcloud.ugckit.module.record.draft.RecordDraftManager;
import com.tencent.qcloud.ugckit.utils.FileUtils;
import com.tencent.qcloud.ugckit.utils.TCUserMgr;
import com.tencent.ugsv_flutter.config.TCConfigManager;
import com.tencent.ugsv_flutter.manager.LicenseManager;
import com.tencent.ugsv_flutter.videochoose.TCVideoPickerActivity;
import com.tencent.ugsv_flutter.videoeditor.TCVideoCutActivity;
import com.tencent.ugsv_flutter.videorecord.TCVideoRecordActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                openVideoRecorder();
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
        mainActivity =  binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        mainActivity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {
        mainActivity =  binding.getActivity();
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

    void openVideoRecorder() {
        Intent intent = new Intent(mainActivity, TCVideoRecordActivity.class);
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
}
