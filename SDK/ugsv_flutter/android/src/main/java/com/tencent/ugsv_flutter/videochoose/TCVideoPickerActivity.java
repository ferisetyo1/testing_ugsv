package com.tencent.ugsv_flutter.videochoose;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.tencent.qcloud.ugckit.UGCKitConstants;
import com.tencent.qcloud.ugckit.UGCKitVideoPicker;
import com.tencent.qcloud.ugckit.module.picker.data.TCVideoFileInfo;
import com.tencent.qcloud.ugckit.module.picker.view.IPickerLayout;
import com.tencent.qcloud.ugckit.utils.DialogUtil;
import com.tencent.ugsv_flutter.R;
import com.tencent.ugsv_flutter.manager.PermissionManager;
import com.tencent.ugsv_flutter.videoeditor.TCVideoCutActivity;
import com.tencent.ugsv_flutter.videojoiner.TCVideoJoinerActivity;

import java.util.ArrayList;

public class TCVideoPickerActivity extends FragmentActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionManager.OnStoragePermissionGrantedListener {
    private UGCKitVideoPicker mUGCKitVideoPicker;
    private PermissionManager mStoragePermissionManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        initWindowParam();
        // 必须在代码中设置主题(setTheme)或者在AndroidManifest中设置主题(android:theme)
        setTheme(R.style.PickerActivityTheme);
        setContentView(R.layout.activity_video_picker);
        mUGCKitVideoPicker = (UGCKitVideoPicker) findViewById(R.id.video_choose_layout);
        mStoragePermissionManager = new PermissionManager(this, PermissionManager.PermissionType.STORAGE);
        mStoragePermissionManager.setLauncher(storageActivityResultLauncher);
        mStoragePermissionManager.setOnStoragePermissionGrantedListener(this);
        mStoragePermissionManager.checkoutIfShowPermissionIntroductionDialog();
        mUGCKitVideoPicker.getTitleBar().setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUGCKitVideoPicker.setOnPickerListener(new IPickerLayout.OnPickerListener() {
            @Override
            public void onPickedList(ArrayList list) {
                if (list == null || list.size() == 0) {
                    return;
                }
                if (list.size() == 1) {
                    TCVideoFileInfo fileInfo = (TCVideoFileInfo) list.get(0);
                    startVideoCutActivity(fileInfo);
                } else {
                    startVideoJoinActivity(list);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initWindowParam() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUGCKitVideoPicker.pauseRequestBitmap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUGCKitVideoPicker.resumeRequestBitmap();
    }

    public void startVideoCutActivity(TCVideoFileInfo fileInfo) {
        Intent intent = new Intent(this, TCVideoCutActivity.class);
        intent.putExtra(UGCKitConstants.VIDEO_PATH, fileInfo.getFilePath());
        if (fileInfo.getFileUri() != null) {
            intent.putExtra(UGCKitConstants.VIDEO_URI, fileInfo.getFileUri().toString());
        }
        startActivity(intent);
    }

    public void startVideoJoinActivity(ArrayList<TCVideoFileInfo> videoPathList) {
        Intent intent = new Intent(this, TCVideoJoinerActivity.class);
        intent.putExtra(UGCKitConstants.INTENT_KEY_MULTI_CHOOSE, videoPathList);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mStoragePermissionManager.onRequestPermissionsResult(requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStoragePermissionGranted() {
        mUGCKitVideoPicker.getPickerListLayout().showProgressBar();
        mUGCKitVideoPicker.loadVideoList();
    }

    @Override
    public void onStoragePermissionDenied() {
        DialogUtil.showDialog(this,"Akses Penyimpanan","Dibutuhkan akses storage untuk melanjutkan aksi ini, pilih izinkan semua!",(v)->{
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
    }

    private ActivityResultLauncher<String[]> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    o -> {
                        onStoragePermissionGranted();
                    });
}
