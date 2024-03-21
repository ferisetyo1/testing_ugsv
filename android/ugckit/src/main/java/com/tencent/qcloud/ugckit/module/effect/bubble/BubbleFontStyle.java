package com.tencent.qcloud.ugckit.module.effect.bubble;

public class BubbleFontStyle {
    private String name;
    private int res;

    public BubbleFontStyle(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
