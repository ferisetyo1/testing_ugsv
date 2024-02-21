package com.tencent.ugsv_flutter;

import io.flutter.plugin.common.MethodChannel;

import java.util.Map;

public class FlutterCallback {

    private static MethodChannel apiChannel;

    public static void init(MethodChannel apiChannel) {
        FlutterCallback.apiChannel = apiChannel;
    }

    public static void call(String method, Map args) {
        if (apiChannel == null) return;
        apiChannel.invokeMethod(method, args);
    }
}
