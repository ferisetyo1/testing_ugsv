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

  static Future<String?> openVideoRecorder() async {
    final result = await _apiChannel.invokeMethod("openVideoRecorder", {});
    debugPrint("openVideoRecorder $result");
    return result is String ? result : null;
  }

  static Future<bool> hasLastRecordPart() async {
    return await _apiChannel.invokeMethod("hasLastRecordPart", {});
  }

  static deleteLastRecordPart() {
    _apiChannel.invokeMethod("deleteLastRecordPart", {});
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
