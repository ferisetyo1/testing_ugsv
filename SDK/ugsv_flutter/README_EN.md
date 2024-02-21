## ugsv_flutter

Short video UGSV Flutter SDK

### Environment Setup

- Flutter:
  - Flutter 2.5.0 and above
  - Dart 2.19.2 and above, below 3.0
- Android:
  - Android Studio 3.5 and above
  - Android 4.3 and above
- iOS:
  - Xcode 11.0 and above
  - iOS 9.0 and above
  - Please ensure that your project has a valid developer signature.

### Quick Integration

#### Add Dependencies

1. Copy the SDK source code to your project directory.

2. Add the SDK to `pubspec.yaml`.

```yaml
ugsv_flutter:
  path: ./ugsv_flutter
```

3. Run the `flutter pub get` command in the project root directory to refresh dependencies.

> Note:
> 1. It is recommended to run the `flutter pub get` command separately in the `project root directory`, `SDK directory`, and `SDK Example directory` to avoid potential errors.
> 2. The `SDK example directory` is the test project for the SDK. You can delete it if not needed.

#### Demo Execution

1. Go to the Demo/example directory.

2. Run the command `flutter pub get` to fetch dependencies.

2. Run the command `flutter run` to execute the program (you can choose the packaging mode by setting the `--debug` or `--release` parameters).

3. Select the device to install (if multiple devices are connected).

#### Native Configuration

##### Android

1. Copy `ugckit`, `xmagickit`, and `beautysettingkit` to your project directory.

2. Add the following configuration to `android/settings.gradle`:

```gradle
include ':ugckit'
include ':beautysettingkit'
include ':xmagickit'
```

3. Add the following configuration to `android/app/build.gradle`:

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

4. Add the following configuration to `android/build.gradle`:

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

5. Add the following configuration to `AndroidManifest.xml`:

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

1. Copy `UGCKit`, `xmagickit`, and `BeautySettingKit` to your project directory.

2. Add the following configuration to `Podfile`:

```ruby
install! 'cocoapods', :disable_input_output_paths => true

def tx_UGCKit(subName)
 pod 'UGCKit', :path => 'UGCKit/UGCKit.podspec', :subspecs => ["#{subName}"]
end
```

3. Include the dependencies in `Podfile`:

```ruby
target 'Runner' do
  flutter_install_all_ios_pods File.dirname(File.realpath(__FILE__))
  tx_UGCKit 'UGC'
  pod 'BeautySettingKit', :path => 'BeautySettingKit/BeautySettingKit.podspec'
  pod 'xmagickit', :path => 'xmagickit/xmagickit.podspec'
end
```

> Note: When using the Professional version of the SDK, you can modify tx_UGCKit 'UGC' to tx_UGCKit 'Professional' to switch.

4. If using the `Professional` version of the SDK, modify the following configuration in `SDK directory/ios/ugsv_flutter.podspec`:

```ruby
# Original dependency
s.dependency 'UGCKit/UGC'

# Modified version
s.dependency 'UGCKit/Professional'
```

5. Add the following configuration to `Info.plist` in iOS:

```xml
<key>NSPhotoLibraryUsageDescription</key>
<string>Photo Library Permission</string>
<key>NSCameraUsageDescription</key>
<string>Camera Permission</string>
<key>NSMicrophoneUsageDescription</key>
<string>Microphone Permission</string>
<key>CFBundleAllowMixedLocalizations</key>
<true/>
```

### Usage

- Import the file:

```dart
import 'package:ugsv_flutter/ugsv.dart';
```

### API

#### Set UGC License

```dart
UGSV.setUgcLicense(licenseUrl: "", licenseKey: "");
```

> Note: Necessary operation

#### Set Beauty License

```dart
UGSV.setTELicense(licenseUrl: "", licenseKey: "");
```

> Note: If advanced beauty features are required, set this `License`

#### Register Listener for Editing Completed Output Video

```dart
EditorEventListener editorEventListener = UGSV.registerEditorEventListener(
      onEditCompleted: (String outputPath) {},
);
```

#### Unregister Listener

```dart
editorEventListener.unregister();
```

#### Open video selection page:

```dart
UGSV.openVideoChooser();
```

#### Open video recording page:

```dart
UGSV.openVideoRecorder();
```

#### Check if there is an unfinished recording:

```dart
bool? hasDraft = UGSV.hasLastRecordPart();
```

#### Delete the last unfinished recording:

```dart
UGSV.deleteLastRecordPart();
```

### Customization

#### Android

> Note: Customization is achieved by modifying local native layout resources.

##### Video Selection Page

- Album Video List

`ugckit -> ugckit_picture_list_layout.xml`

- Bottom Video Multi-selection Drag List

    - Overall

    `ugckit -> ugckit_picture_pick_layout.xml`

    - Elements

    `ugckit -> ugckit_swipe_menu_item.xml`

##### Video Trimming Page

- Bottom Draggable Selection Bar

`ugckit -> ugckit_video_cut_kit.xml`

##### Video Editing Page

- Bottom Selection Bar

`ugckit -> ugckit_video_cutt_kit.xml`

##### Effects

- Playback Control

`ugckit -> ugckit_play_control_view.xml`

- Music Selection

    - Overall

    `ugckit -> ugckit_activity_bgm_select.xml`

    - Elements

    `ugckit -> ugckit_item_editer_bgm.xml`

- Music Settings

`ugckit -> ugckit_fragment_bgm.xml`

- Actions

`ugckit -> ugckit_fragment_motion.xml`

- Speed

`ugckit -> ugckit_fragment_time.xml`

- Filters

    - Overall

    `ugckit -> ugckit_fragment_static_filter.xml`

    - Elements

    `ugckit -> ugckit_filter_layout.xml`

- Stickers

    - Overall

    `ugckit -> ugckit_fragment_paster.xml`

    - Elements

    `ugckit -> ugckit_item_add_paster.xml`

- Subtitles

    - Overall

    `ugckit -> ugckit_fragment_bubble_subtitle.xml`

    - Elements

    `ugckit -> ugckit_item_bubble_img.xml`

- Transitions

    - Overall

    `ugckit -> ugckit_fragment_transition.xml`

    - Elements

    `ugckit -> ugckit_item_transition.xml`

##### Video Recording Page

- Right Selection Button Area

`ugckit -> ugckit_record_right_layout.xml`

- Bottom Recording Control Area

    - Overall

    `ugckit -> ugckit_record_bottom_layout.xml`

    - Progress Bar

    `com.tencent.qcloud.ugckit.module.record.RecordProgressView`

    - Speed Selection Bar

    `ugckit -> ugckit_record_speed_layout.xml`

    - Record Button

    `ugckit -> ugckit_record_button.xml`

    - Shooting Mode Selection Bar

    `ugckit -> ugckit_record_mode_layout.xml`

- Music Settings

`ugckit -> ugckit_layout_reocrd_music.xml`

- Sound Effects

`ugckit -> ugckit_layout_sound_effects.xml`

- Basic Beauty

`ugckit -> beauty_view_layout.xml`

##### Other Components

- Common Header

`ugckit -> ugckit_title_bar_layout.xml`

- Video Trimming/Joining/Generation Progress Bar

`ugckit -> ugckit_layout_joiner_progress.xml`

- Video Playback Progress Bar

`ugckit -> ugckit_video_timeline.xml`

#### iOS

##### Video Selection Page

- Album Video List

`UGCKitMediaPickerViewController`

- Bottom Video Multi-selection Drag List

    - Overall

    `UGCKitImageScrollerViewController`

    - Elements

    `_UGCKitImageScrollerCellView`

##### Video Trimming Page

- Overall

`UGCKitCutViewController`

##### Video Editing Page

- Overall

`UGCKitEditViewController`

- Bottom Selection Bar

`UGCKitEditBottomMenu`

- Bottom Options

`UGCKitEditBottomMenuItem`

- Effects Selection Bar

`UGCKitEffectSelectView`

##### Effects

- Music Selection

    - Overall

    `UGCKitBGMListViewController`

    - Elements

    `UGCKitBGMCell`

- Music Settings

`UGCKitVideoRecordMusicView`

- Sticker Add Panel

`UGCKitPasterAddView`

##### Video Recording Page

- Overall

`UGCKitRecordViewController`

- Right Selection Button Area

    - Aspect Ratio Button

    `UGCKitSlideButton`

- Bottom Recording Control Area

    - Overall

    `UGCKitRecordControlView`

    - Progress Bar

    `UGCKitVideoRecordProcessView`

    - Shooting Mode Selection Bar

    `UGCKitSlideOptionControl`

- Music Selection

    - Overall

    `UGCKitBGMListViewController`

    - Elements

    `UGCKitBGMCell`

- Music Settings

`UGCKitVideoRecordMusicView`

- Sound Effects Panel

`UGCKitAudioEffectPanel`

- Basic Beauty Panel

`TCBeautyPanel`

- Advanced Beauty Panel

`BeautyView`

##### Other Components

- Video Playback Progress/Selection Bar

`UGCKitVideoCutView`
