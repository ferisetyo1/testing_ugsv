import 'dart:async';

import 'package:flutter/material.dart';
import 'package:ugsv_flutter/ugsv.dart';

class IndexWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return IndexState();
  }
}

class IndexState extends State<IndexWidget> {
  static const String _licenceUrl =
      "https://license.vod-control.com/license/v2/1320157540_1/v_cube.license";
  static const String _licenseKey = "344839e1090f45642915ae7e51bda035";

  EditorEventListener? _editorEventListener;

  _onVideoEditorClick() async {
    UGSV.openVideoChooser();
  }

  _onVideoRecorderClick(BuildContext context) {
    UGSV.hasLastRecordPart().then((has) {
      if (has) {
        _showDeleteConfirmDialog(context).then((bool? confirm) {
          if (confirm == true) {
            UGSV.deleteLastRecordPart();
          }
          UGSV.openVideoRecorder();
        });
      } else {
        UGSV.openVideoRecorder();
      }
    });
  }

  _init() {
    UGSV.initCallback();
    UGSV.setUgcLicense(licenseUrl: _licenceUrl, licenseKey: _licenseKey);
    UGSV.setTELicense(licenseUrl: _licenceUrl, licenseKey: _licenseKey);
    _editorEventListener = UGSV.registerEditorEventListener(
      onEditCompleted: (String outputPath) {
        showDialog<bool>(
          context: context,
          builder: (context) {
            return AlertDialog(
              title: const Text("Success"),
              content: Text(outputPath),
              actions: <Widget>[
                TextButton(
                  child: const Text("Close"),
                  onPressed: () => Navigator.of(context).pop(false),
                ),
              ],
            );
          },
        );
      },
    );
  }

  Future<bool?> _showDeleteConfirmDialog(BuildContext context) {
    return showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("您有未录制完成的视频"),
          content: const Text("是否要删除历史缓存"),
          actions: <Widget>[
            TextButton(
              child: const Text("取消"),
              onPressed: () => Navigator.of(context).pop(false),
            ),
            TextButton(
              child: const Text("删除"),
              onPressed: () => Navigator.of(context).pop(true),
            ),
          ],
        );
      },
    );
  }

  @override
  void initState() {
    super.initState();
    _init();
  }

  @override
  void dispose() {
    super.dispose();
    _editorEventListener?.unregister();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.white,
        child: SafeArea(
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 100),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                MaterialButton(
                  color: const Color.fromRGBO(89, 12, 228, 1),
                  minWidth: 12,
                  height: 36,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(5.0),
                  ),
                  textTheme: ButtonTextTheme.accent,
                  onPressed: () {
                    _onVideoEditorClick();
                  },
                  child: const Center(
                    child: Text(
                      "Edit Video",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),
                const Padding(padding: EdgeInsets.only(top: 20)),
                MaterialButton(
                  color: const Color.fromRGBO(89, 12, 228, 1),
                  minWidth: 12,
                  height: 36,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(5.0),
                  ),
                  textTheme: ButtonTextTheme.accent,
                  onPressed: () {
                    _onVideoRecorderClick(context);
                  },
                  child: const Center(
                    child: Text(
                      "Camera",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),
                FilledButton(
                    onPressed: () {
                      UGSV.addWatermarkVideo();
                    },
                    child: Text("save watermark"))
              ],
            ),
          ),
        ),
      ),
    );
  }
}
