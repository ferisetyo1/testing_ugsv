## ugsv_flutter

短视频UGSV Flutter SDK

### 环境准备

- Flutter:
  - Flutter 2.5.0及以上版本
  - Dart 2.19.2及以上3.0以下版本
- Android:
  - Android Studio 3.5及以上版本
  - Android 4.3及以上版本
- iOS:
  - Xcode 11.0及以上版本
  - iOS 9.0及以上版本
  - 请确保您的项目已设置有效的开发者签名

### 快速集成

#### 引入依赖

1. 将SDK源码复制到项目目录下

2. 在`pubspec.yaml`中引入`SDK`

```yaml
ugsv_flutter:
  path: ./ugsv_flutter
```

3. 项目根目录下运行`flutter pub get`命令刷新依赖

> 注：
> 1. 最好在`项目根目录`、`SDK目录`、`SDK Example目录`下分别运行`flutter pub get`命令，不然有可能报错
> 2. `SDK Example目录`为`SDK`的测试项目，如无需要可以删掉

#### Demo运行

1. 进入`Demo/example`目录

2. 执行`flutter pub get`命令获取依赖

3. 执行`flutter run`命令运行程序 (可通过设置参数`--debug`或`--release`选取打包模式)

5. 选取要安装的设备 (如果连接了多设备)

#### 添加原生配置

##### Android

1. 将`ugckit`、`xmagickit`、`beautysettingkit`复制到项目目录

2. 在`android/settings.gradle`中增加如下配置

```gradle
include ':ugckit'
include ':beautysettingkit'
include ':xmagickit'
```

3. 在`android/app/build.gradle`中增加如下配置

```gradle
android {
    packagingOptions {
        pickFirst '**/libc++_shared.so'
        pickFirst '**/libtxsoundtouch.so'
        pickFirst '**/libtxffmpeg.so'
        pickFirst '**/libliteavsdk.so'
        doNotStrip "*/armeabi/libYTCommon.so"
        doNotStrip "*/armeabi-v7a/libYTCommon.so"
        doNotStrip "*/x86/libYTCommon.so"
        doNotStrip "*/arm64-v8a/libYTCommon.so"
    }
}

dependencies {
    implementation 'androidx.multidex:multidex:2.0.1'
}
```

4. 在`android/build.gradle`中增加如下配置

```gradle
ext {
    compileSdkVersion = 29
    buildToolsVersion = "29.0.2"
    supportSdkVersion = "26.1.0"
    minSdkVersion = 19
    targetSdkVersion = 26
    versionCode = 1
    versionName = "v1.1"
    proguard = true
    rootPrj = "$projectDir/.."
    liteavSdk = "com.tencent.liteav:LiteAVSDK_UGC:latest.release"
}
```

5. 在`AndroidManifest.xml`中增加如下配置

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.CAMERA"/>
```

##### iOS

1. 将`UGCKit`、`xmagickit`、`BeautySettingKit`复制到项目目录
   
2. 在`Podfile`中增加如下配置

```ruby
install! 'cocoapods', :disable_input_output_paths => true

def tx_UGCKit(subName)
 pod 'UGCKit', :path => 'UGCKit/UGCKit.podspec', :subspecs => ["#{subName}"]
end
```

3. 在`Podfile`中引入上述依赖

```ruby
target 'Runner' do
  flutter_install_all_ios_pods File.dirname(File.realpath(__FILE__))
  tx_UGCKit 'UGC'
  pod 'BeautySettingKit', :path => 'BeautySettingKit/BeautySettingKit.podspec'
  pod 'xmagickit', :path => 'xmagickit/xmagickit.podspec'
end
```

> 注: 当用户使用`Professional`版本的SDK时，可将`tx_UGCKit 'UGC'`修改为`tx_UGCKit 'Professional'`进行切换

4. 当用户使用`Professional`版本的SDK时还应在`SDK目录/ios/ugsv_flutter.podspec`中修改以下配置

```ruby
# 原依赖
s.dependency 'UGCKit/UGC'

# 修改后
s.dependency 'UGCKit/Professional'
```

5. 在`iOS`的`Info.plist`中增加如下配置

```xml
<key>NSPhotoLibraryUsageDescription</key>
<string>相册权限</string>
<key>NSCameraUsageDescription</key>
<string>相机权限</string>
<key>NSMicrophoneUsageDescription</key>
<string>麦克风权限</string>
<key>CFBundleAllowMixedLocalizations</key>
<true/>
```

### 使用

- 引入文件

```dart
import 'package:ugsv_flutter/ugsv.dart';
```

### API

#### 设置UGC License

```dart
UGSV.setUgcLicense(licenseUrl: "", licenseKey: "");
```

> 注: 必要操作

#### 设置美颜 License

```dart
UGSV.setTELicense(licenseUrl: "", licenseKey: "");
```

> 注: 若需要高级美颜，可设置此`License`

#### 注册编辑完成输出视频的监听

```dart
EditorEventListener editorEventListener = UGSV.registerEditorEventListener(
      onEditCompleted: (String outputPath) {},
);
```

#### 取消监听

```dart
editorEventListener.unregister();
```

#### 打开视频选择页

```dart
UGSV.openVideoChooser();
```

#### 打开视频录制页

```dart
UGSV.openVideoRecorder();
```

#### 检查是否有未完成录制

```dart
bool? hasDraft = UGSV.hasLastRecordPart();
```

#### 删除未完成录制

```dart
UGSV.deleteLastRecordPart();
```

### 定制化

#### Android

> 注: 通过修改本地原生布局资源实现

##### 视频选择页

- 相册视频列表

`ugckit` -> `ugckit_picture_list_layout.xml`

- 底部视频多选可拖动列表

    - 整体

    `ugckit` -> `ugckit_picture_pick_layout.xml`

    - 元素

    `ugckit` -> `ugckit_swipe_menu_item.xml`

##### 视频裁剪页

- 底部可拖动选取条

`ugckit` -> `ugckit_video_cut_kit.xml`

##### 视频编辑页

- 底部选择栏

`ugckit` -> `ugckit_video_cutt_kit.xml`

##### 效果

- 播放控制

`ugckit` -> `ugckit_play_control_view.xml`

- 音乐选择

    - 整体

    `ugckit` -> `ugckit_activity_bgm_select.xml`

    - 元素
  
    `ugckit` -> `ugckit_item_editer_bgm.xml`

- 音乐设置

`ugckit` -> `ugckit_fragment_bgm.xml`

- 动作

`ugckit` -> `ugckit_fragment_motion.xml`

- 速度

`ugckit` -> `ugckit_fragment_time.xml`

- 滤镜

    - 整体

    `ugckit` -> `ugckit_fragment_static_filter.xml`

    - 元素

    `ugckit` -> `ugckit_filter_layout.xml`

- 贴纸

    - 整体

    `ugckit` -> `ugckit_fragment_paster.xml`

    - 元素

    `ugckit` -> `ugckit_item_add_paster.xml`

- 字幕

    - 整体

    `ugckit` -> `ugckit_fragment_bubble_subtitle.xml`

    - 元素

    `ugckit` -> `ugckit_item_bubble_img.xml`

- 转场

    - 整体

    `ugckit` -> `ugckit_fragment_transition.xml`

    - 元素

    `ugckit` -> `ugckit_item_transition.xml`

##### 视频录制页

- 右侧选择按钮区域

`ugckit` -> `ugckit_record_right_layout.xml`

- 底部录制控制区域

    - 整体

    `ugckit` -> `ugckit_record_bottom_layout.xml`

    - 进度条

    `com.tencent.qcloud.ugckit.module.record.RecordProgressView`

    - 速度选择条

    `ugckit` -> `ugckit_record_speed_layout.xml`

    - 录制按钮

    `ugckit` -> `ugckit_record_button.xml`

    - 拍摄模式选择条

    `ugckit` -> `ugckit_record_mode_layout.xml`

- 音乐设置

`ugckit` -> `ugckit_layout_reocrd_music.xml`

- 音效

`ugckit` -> `ugckit_layout_sound_effects.xml`

- 基础美颜

`ugckit` -> `beauty_view_layout.xml`

##### 其他组件

- 通用头部

`ugckit` -> `ugckit_title_bar_layout.xml`

- 视频裁剪/拼接/生成进度条

`ugckit` -> `ugckit_layout_joiner_progress.xml`

- 视频播放进度条

`ugckit` -> `ugckit_video_timeline.xml`

#### iOS

##### 视频选择页

- 相册视频列表

`UGCKitMediaPickerViewController`

- 底部视频多选可拖动列表

    - 整体

    `UGCKitImageScrollerViewController`

    - 元素

    `_UGCKitImageScrollerCellView`

##### 视频裁剪页

- 整体

`UGCKitCutViewController`

##### 视频编辑页

- 整体

`UGCKitEditViewController`

- 底部选择栏

`UGCKitEditBottomMenu`

- 底部选项

`UGCKitEditBottomMenuItem`

- 特效选择条

`UGCKitEffectSelectView`

##### 效果

- 音乐选择

    - 整体

    `UGCKitBGMListViewController`

    - 元素
  
    `UGCKitBGMCell`

- 音乐设置

`UGCKitVideoRecordMusicView`

- 贴纸添加面板

`UGCKitPasterAddView`

##### 视频录制页

- 整体

`UGCKitRecordViewController`

- 右侧选择按钮区域

    - 屏占比按钮

    `UGCKitSlideButton`

- 底部录制控制区域

    - 整体

    `UGCKitRecordControlView`

    - 进度条

    `UGCKitVideoRecordProcessView`

    - 拍摄模式选择条

    `UGCKitSlideOptionControl`

- 音乐选择

    - 整体

    `UGCKitBGMListViewController`

    - 元素
  
    `UGCKitBGMCell`

- 音乐设置

`UGCKitVideoRecordMusicView`

- 音效面板

`UGCKitAudioEffectPanel`

- 基础美颜面板

`TCBeautyPanel`

- 高级美颜面板

`BeautyView`

##### 其他组件

- 视频播放进度/选取条

`UGCKitVideoCutView`
