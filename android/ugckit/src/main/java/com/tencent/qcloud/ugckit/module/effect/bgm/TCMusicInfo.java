package com.tencent.qcloud.ugckit.module.effect.bgm;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class TCMusicInfo {
    public int id;
    @SerializedName("audio_title")
    public String name;
    @SerializedName("artist_name")
    public String artistName;
    public String thumbnail;
    @SerializedName("file_url")
    public String url;
    public int    status = STATE_UNDOWNLOAD;
    public int    statusMusic = MUSIC_STATE_STOP;
    public int    progress;
    @Nullable
    public String localPath;

    public static final int MUSIC_STATE_STOP = 1;
    public static final int MUSIC_STATE_PLAYING  = 2;
    public static final int STATE_UNDOWNLOAD  = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_DOWNLOADED  = 3;
    public static final int STATE_USED        = 4;
}
