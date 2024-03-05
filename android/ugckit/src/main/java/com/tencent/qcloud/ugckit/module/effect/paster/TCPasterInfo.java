package com.tencent.qcloud.ugckit.module.effect.paster;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.qcloud.ugckit.utils.BitmapJi;

public class TCPasterInfo {
    private String iconPath;
    private String name;
    private String pasterPath;
    private int    pasterType;

    private String emote;

    public int getPasterType() {
        return pasterType;
    }

    public void setPasterType(int pasterType) {
        this.pasterType = pasterType;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmote() {
        return emote;
    }

    public void setEmote(String emote) {
        this.emote = emote;
    }

    public Bitmap bitmapEmote(Context context){
        return BitmapJi.create(context,emote,100);
    }
}
