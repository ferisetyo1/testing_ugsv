package com.tencent.qcloud.ugckit.module.effect.bubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.tencent.qcloud.ugckit.R;

public class NewBubbleInfo {
    private Boolean withBorder;
    private Boolean onlyText;
    private int colorIcon;
    private int colorBubble = Color.BLACK;
    private Boolean isTransparant;
    private float textSize = 40;
    private float paddingTop = 20;
    private float paddingBottom = 20;
    private float paddingLeft = 20;
    private float paddingRight = 20;

    public NewBubbleInfo(Boolean withBorder, Boolean onlyText, int color, Boolean isTransparant) {
        this.withBorder = withBorder;
        this.onlyText = onlyText;
        this.colorIcon = color;
        this.isTransparant = isTransparant;
    }

    public Bitmap buildBubbleBitmap() {
        Log.d("buildBubbleBitmap", "draw");
        return drawCanvas(336, 180, 4, 12, 20, getBgColorBubble(), getInnerStrokeBubble(), getBgColorBubble(), null);
    }

    private Bitmap drawCanvas(int width, int height, int outerStroke, int innerStroke, float rounded, int colorBg, int colorInnerStroke, int colorOuterStroke, Paint paint) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (onlyText) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap);

        RectF rectBorderInner = new RectF();
        rectBorderInner.set(innerStroke / 2, innerStroke / 2, width - (innerStroke / 2), height - (innerStroke / 2));

        RectF rectBorderOuter = new RectF();
        rectBorderOuter.set(outerStroke / 2, outerStroke / 2, width - (outerStroke / 2), height - (outerStroke / 2));

        RectF rectF = new RectF();
        rectF.set(0, 0, width, height);

        // Draw the rounded rectangle
        canvas.drawRoundRect(withBorder ? rectBorderInner : rectF, rounded, rounded, bubblePaint(colorBg));
        if (withBorder) {
            canvas.drawRoundRect(rectBorderInner, rounded, rounded, borderInnerPaint(innerStroke, colorInnerStroke));
            canvas.drawRoundRect(rectBorderOuter, rounded, rounded, borderOuterPaint(outerStroke, colorOuterStroke));
        }
        if (paint != null) {
            canvas.drawText("Aa", bitmap.getWidth() / 2, bitmap.getHeight() / 2 + 7, paint);
        }
        Log.d("drawCanvas", "draw " + width + ":" + height);
        return bitmap;
    }

    private Paint borderInnerPaint(int stroke, int color) {
        Paint borderInnerPaint = new Paint();
        borderInnerPaint.setColor(color);
        borderInnerPaint.setStrokeWidth(stroke);
        borderInnerPaint.setStyle(Paint.Style.STROKE);
        borderInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        return borderInnerPaint;
    }

    private Paint bubblePaint(int color) {
        Paint bubblePaint = new Paint();
        bubblePaint.setColor(color);
        bubblePaint.setStyle(Paint.Style.FILL);
        return bubblePaint;
    }

    private Paint borderOuterPaint(int stroke, int color) {
        Paint borderOuterPaint = new Paint();
        borderOuterPaint.setColor(color);
        borderOuterPaint.setStrokeWidth(stroke);
        borderOuterPaint.setStyle(Paint.Style.STROKE);
        borderOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        return borderOuterPaint;
    }

    private int getBgColorIcon() {
        int red = Color.red(colorIcon);
        int green = Color.green(colorIcon);
        int blue = Color.blue(colorIcon);
        int newColor = Color.argb(isTransparant ? (int) (255 * 0.8) : 255, red, green, blue);
        return newColor;
    }

    private int getBgColorBubble() {
        int red = Color.red(colorBubble);
        int green = Color.green(colorBubble);
        int blue = Color.blue(colorBubble);
        int newColor = Color.argb(isTransparant ? (int) (255 * 0.8) : 255, red, green, blue);
        return newColor;
    }

    private int getInnerStrokeBubble() {
        return Color.WHITE != colorBubble ? Color.WHITE : Color.BLACK;
    }

    private int getInnerStrokeIcon() {
        return Color.WHITE != colorBubble ? Color.WHITE : Color.BLACK;
    }

    public Bitmap buildIconBitmap(Context context) {
        Paint paint = new Paint();
        paint.setTextSize(20f);
        paint.setAntiAlias(true);
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_medium));
        paint.setTextAlign(Paint.Align.CENTER);
        if (onlyText) {
            paint.setColor(Color.BLACK);
            Bitmap bitmap = Bitmap.createBitmap(54, 47, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawText("Aa", bitmap.getWidth() / 2, bitmap.getHeight() / 2 + 7, paint);
            return bitmap;
        }
        paint.setColor(Color.WHITE);
        return drawCanvas(54, 47, 2, 6, 8, getBgColorIcon(), getInnerStrokeIcon(), getBgColorIcon(), paint);

    }

    public float getTextSize() {
        return textSize;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public int getColorBubble() {
        return colorBubble;
    }

    public void setColorBubble(int colorBubble) {
        this.colorBubble = colorBubble;
    }

    public Boolean getOnlyText() {
        return onlyText;
    }
}
