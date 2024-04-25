package com.tencent.qcloud.ugckit.module.effect.bubble;


import android.graphics.Color;

import com.tencent.qcloud.ugckit.R;

/**
 * 保存 从{@link BubbleSubtitlePannel} 之后的设定的 气泡字幕index、以及字体颜色的数据结构
 */
public class TCSubtitleInfo {
    private int bubblePos;
    private int textColor = Color.BLACK;
    private int textStyle = R.font.poppins;
    private NewBubbleInfo bubbleInfo;

    private String text;

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBubblePos() {
        return bubblePos;
    }

    public void setBubblePos(int bubblePos) {
        this.bubblePos = bubblePos;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (this.bubbleInfo != null) {
            this.bubbleInfo.setColorBubble(textColor);
        }
    }

    public NewBubbleInfo getBubbleInfo() {
        return bubbleInfo;
    }

    public void setBubbleInfo(NewBubbleInfo bubbleInfo) {
        this.bubbleInfo = bubbleInfo;
        this.bubbleInfo.setColorBubble(this.textColor);
    }

}
