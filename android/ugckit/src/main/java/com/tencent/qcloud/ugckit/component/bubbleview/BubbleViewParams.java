package com.tencent.qcloud.ugckit.component.bubbleview;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.qcloud.ugckit.module.effect.bubble.NewBubbleInfo;
import com.tencent.qcloud.ugckit.module.effect.bubble.TCBubbleInfo;
import com.tencent.qcloud.ugckit.module.effect.bubble.TCSubtitleInfo;

/**
 * 用于初始化气泡字幕控件{@link BubbleView} 的参数配置
 */
public class BubbleViewParams {
    @Nullable
    public Bitmap         bubbleBitmap;
    public Bitmap         iconBitmap;
    public TCSubtitleInfo wordParamsInfo;
    public String         text;

    @NonNull
    public static BubbleViewParams createDefaultParams(String text) {
        BubbleViewParams params = new BubbleViewParams();
        params.bubbleBitmap = null;
        params.text = text;

        TCSubtitleInfo info = new TCSubtitleInfo();
        info.setTextColor(Color.WHITE);

        // 初始化为无字幕的 配置信息
        // 创建一个默认的

        info.setBubbleInfo(new NewBubbleInfo(false,true,Color.BLACK,false));

        params.wordParamsInfo = info;
        return params;
    }
}
