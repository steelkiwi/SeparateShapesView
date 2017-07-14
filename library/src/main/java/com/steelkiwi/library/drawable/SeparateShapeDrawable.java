package com.steelkiwi.library.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;

/**
 * Created by yaroslav on 7/13/17.
 */

public class SeparateShapeDrawable extends Drawable {

    private TextPaint textPaint;
    // shapes
    private Bitmap leftBitmap;
    private Bitmap rightBitmap;
    // center in the drawable
    private int centerX;
    // left drawable text title
    private String leftShapeTitle;
    // left drawable text title
    private String rightShapeTitle;
    // flag for check if all text caps
    private boolean isTextAllCaps;
    // flag to check if need draw text
    private boolean isDrawTitle = true;
    // shape bounds
    private Rect leftShapeBounds;
    private Rect rightShapeBounds;
    // flag for check if set single shape drawable
    private boolean isSingleShape;
    // center drawable text title
    private String centerShapeTitle;

    public SeparateShapeDrawable(Context context) {
        initRect();
        prepareTextPaint(context);
    }

    private void prepareTextPaint(Context context) {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initRect() {
        leftShapeBounds = new Rect();
        rightShapeBounds = new Rect();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawBackgroundBitmaps(canvas);
        drawText(canvas);
    }

    private void drawBackgroundBitmaps(Canvas canvas) {
        centerX = canvas.getWidth() / 2;
        if(leftBitmap != null && rightBitmap != null) {
            canvas.drawBitmap(leftBitmap, 0f, 0f, null);
            canvas.drawBitmap(rightBitmap, centerX, 0f, null);
            // prepare left and right shape bounds for check if user touch inside
            leftShapeBounds.set(0, 0, leftBitmap.getWidth(), leftBitmap.getHeight());
            rightShapeBounds.set(centerX, 0, centerX + rightBitmap.getWidth(), rightBitmap.getHeight());
        }
    }

    private void drawText(Canvas canvas) {
        String text = getCenterShapeTitle();
        if(isSingleShape() && text != null) {
            drawSingleTitle(canvas, text);
        } else {
            drawLeftTitle(canvas);
            drawRightTitle(canvas);
        }
    }

    private void drawSingleTitle(Canvas canvas, String text) {
        if(isDrawTitle() && text != null) {
            int centerX = canvas.getWidth() / 2;
            int centerY = canvas.getHeight() / 2;
            // text size
            float textWidth = textPaint.measureText(text);
            float textHeight = getTextHeight(text);
            // draw text
            canvas.drawText(text, centerX - textWidth / 2, centerY + textHeight / 2, textPaint);
        }
    }

    private void drawLeftTitle(Canvas canvas) {
        String text = getLeftShapeTitle();
        if(isDrawTitle() && text != null) {
            int bitmapCenterX = leftBitmap.getWidth() / 2;
            int bitmapCenterY = leftBitmap.getHeight() / 2;
            // text size
            float textWidth = textPaint.measureText(text);
            float textHeight = getTextHeight(text);
            // draw text
            canvas.drawText(text, bitmapCenterX - textWidth / 2, bitmapCenterY + textHeight / 2, textPaint);
        }
    }

    private void drawRightTitle(Canvas canvas) {
        String text = getRightShapeTitle();
        if(isDrawTitle() && text != null) {
            int bitmapCenterX = centerX + rightBitmap.getWidth() / 2;
            int bitmapCenterY = rightBitmap.getHeight() / 2;
            // text size
            float textWidth = textPaint.measureText(text);
            float textHeight = getTextHeight(text);
            // draw text
            canvas.drawText(text, bitmapCenterX - textWidth / 2, bitmapCenterY + textHeight / 2, textPaint);
        }
    }

    private int getTextHeight(String text) {
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    public void setAlpha(int alpha) {
        // no need
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // no need
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void createLeftDrawablePart(Drawable drawable, int width, int height) {
        leftBitmap = convertToBitmap(drawable, width, height);
    }

    public void createRightDrawablePart(Drawable drawable, int width, int height) {
        rightBitmap = convertToBitmap(drawable, width, height);
    }

    private Bitmap convertToBitmap(Drawable drawable, int width, int height) {
        if(drawable != null) {
            Bitmap mutableBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mutableBitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            return mutableBitmap;
        }
        return null;
    }

    public void setTypeface(Typeface typeface) {
        if(typeface != null) {
            typeface = Typeface.DEFAULT;
        }
        textPaint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
    }

    private String getLeftShapeTitle() {
        return isTextAllCaps ? leftShapeTitle.toUpperCase() : leftShapeTitle;
    }

    public void setLeftShapeTitle(String leftShapeTitle) {
        this.leftShapeTitle = leftShapeTitle;
    }

    private String getRightShapeTitle() {
        return isTextAllCaps ? rightShapeTitle.toUpperCase() : rightShapeTitle;
    }

    public void setRightShapeTitle(String rightShapeTitle) {
        this.rightShapeTitle = rightShapeTitle;
    }

    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }

    public boolean isTextAllCaps() {
        return isTextAllCaps;
    }

    public void setTextAllCaps(boolean textAllCaps) {
        isTextAllCaps = textAllCaps;
    }

    private boolean isDrawTitle() {
        return isDrawTitle;
    }

    public void setDrawTitle(boolean drawTitle) {
        isDrawTitle = drawTitle;
    }

    public Rect getLeftShapeBounds() {
        return leftShapeBounds;
    }

    public Rect getRightShapeBounds() {
        return rightShapeBounds;
    }

    private boolean isSingleShape() {
        return isSingleShape;
    }

    public void setSingleShape(boolean singleShape) {
        isSingleShape = singleShape;
    }

    private String getCenterShapeTitle() {
        return centerShapeTitle;
    }

    public void setCenterShapeTitle(String centerShapeTitle) {
        this.centerShapeTitle = centerShapeTitle;
    }
}
