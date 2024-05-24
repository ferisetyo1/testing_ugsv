import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class UGSV {
  static const MethodChannel _apiChannel = MethodChannel("ugsv_flutter");

  static final StreamController<String> _editorEventNotifier =
      StreamController<String>.broadcast();

  static Future<dynamic> _handleCallback(MethodCall call) async {
    switch (call.method) {
      case "onEditCompleted":
        {
          String json = jsonEncode({
            "method": call.method,
            "outputPath": call.arguments["outputPath"],
          });
          debugPrint("print-onEditCompleted");
          _editorEventNotifier.add(json);
          break;
        }
    }
  }

  static initCallback() {
    _apiChannel.setMethodCallHandler(_handleCallback);
  }

  static EditorEventListener registerEditorEventListener({
    void Function(String outputPath) onEditCompleted = _defaultOnEditCompleted,
  }) {
    return EditorEventListener(
      listener: _editorEventNotifier.stream.listen((event) {
        Map<String, dynamic> json = jsonDecode(event);
        String method = json["method"];
        switch (method) {
          case "onEditCompleted":
            {
              onEditCompleted(json["outputPath"]);
              break;
            }
        }
      }),
    );
  }

  static openVideoChooser() {
    _apiChannel.invokeMethod("openVideoChooser", {});
  }

  static openVideoRecorder() async {
    final result = await _apiChannel.invokeMethod("openVideoRecorder", {
      // "music": {
      //   "id": 5,
      //   "artist_name": "DJ Yatim Fvnky",
      //   "audio_title": "SAH REMIX",
      //   "thumbnail":
      //       "https://storage.sapa.co.id/sapawebapi/images/749bf7bd-9893-47a5-8bc0-71f1e57c2197.jpg",
      //   "file_url":
      //       "https://storage.sapa.co.id/sapawebapi/images/517ebaad-981b-4136-a557-d131192b5446.mp3"
      // }
    });
    if (result == null) {
      return null;
    }
    debugPrint("openVideoRecorder ${result["outputPath"]}"); //type string
    debugPrint(
        "openVideoRecorder ${result["musicId"]}"); //type string (default -1)
    return result;
  }

  static Future<bool> hasLastRecordPart() async {
    return await _apiChannel.invokeMethod("hasLastRecordPart", {});
  }

  static deleteLastRecordPart() {
    _apiChannel.invokeMethod("deleteLastRecordPart", {});
  }

  static Future<String> addWatermarkVideo() async {
    return await _apiChannel.invokeMethod("saveVideoWithWatermark",
        {"url": "/storage/emulated/0/Movies/TXVideo_20240508_135729.mp4"});
  }

  static setUgcLicense({
    required String licenseUrl,
    required String licenseKey,
  }) {
    _apiChannel.invokeMethod("setUgcLicense", {
      "licenseUrl": licenseUrl,
      "licenseKey": licenseKey,
    });
  }

  static setTELicense({
    required String licenseUrl,
    required String licenseKey,
  }) {
    _apiChannel.invokeMethod("setXMagicLicense", {
      "licenseUrl": licenseUrl,
      "licenseKey": licenseKey,
    });
  }
}

class EditorEventListener {
  StreamSubscription? _editorEventListener;

  EditorEventListener({required StreamSubscription listener}) {
    _editorEventListener = listener;
  }

  unregister() {
    _editorEventListener?.cancel();
  }
}

_defaultOnEditCompleted(String) {}
