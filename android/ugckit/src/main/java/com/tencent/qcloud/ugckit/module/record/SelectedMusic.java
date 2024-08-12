package com.tencent.qcloud.ugckit.module.record;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.qcloud.ugckit.R;

public class SelectedMusic extends LinearLayout {
    private TextView mText;

    public SelectedMusic(Context context) {
        super(context);
        init(context);
    }

    public SelectedMusic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectedMusic(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_selected_music, this);
        mText = (TextView) findViewById(R.id.text);

        mText.setSelected(true);
        mText.setHorizontallyScrolling(true);
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public void check() {
        if (mText.getText().length()>0){
           setVisibility(VISIBLE);
        }else{
            hide();
        }
    }

    public void hide(){
        setVisibility(GONE);
    }
}
