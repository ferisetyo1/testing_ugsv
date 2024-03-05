package com.tencent.qcloud.ugckit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.TypedValue;

public class BitmapJi {
    /**
     * Create a bitmap given the emoji.
     * @param context The context
     * @param emoji An emoji (or any String, really)
     * @param size The size to make the bitmap. The largest size possible is 93dp (not sure why).
     */
    public static Bitmap create(Context context, String emoji, float size) {
        CaptureTextView captureView = new CaptureTextView(context, size);
        return captureView.capture(emoji);
    }

    private static class CaptureTextView extends androidx.appcompat.widget.AppCompatTextView {
        private float size;

        public CaptureTextView(Context context, float size) {
            super(context);
            this.size = size;
        }

        public Bitmap capture(String emoji) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            // Otherwise it is semi-transparent
            setTextColor(Color.BLACK);
            setText(emoji);
            measure(
                    MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            );
            layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            draw(c);
            return b;
        }
    }
}

