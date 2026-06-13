/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.skin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Simple player model preview view for the launcher settings screen.
 * Renders a stylized player silhouette.
 */
public class PlayerModelPreviewView extends View {
    private static final int BODY_COLOR = 0xFF6B8FC6;
    private static final int HAT_COLOR = 0xFF4A6FA5;
    private static final int BG_COLOR = 0x1A000000;

    private final Paint bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF tmpRect = new RectF();

    public PlayerModelPreviewView(@NonNull Context context) {
        super(context);
        init();
    }

    public PlayerModelPreviewView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerModelPreviewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bodyPaint.setColor(BODY_COLOR);
        bodyPaint.setStyle(Paint.Style.FILL);
        hatPaint.setColor(HAT_COLOR);
        hatPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(BG_COLOR);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float cx = w / 2f;

        // Background circle
        float radius = Math.min(w, h) / 2f - dp(8);
        if (radius <= 0) return;
        canvas.drawCircle(cx, h / 2f, radius, bgPaint);

        // Head (square with rounded corners)
        float headSize = radius * 0.45f;
        float headTop = h / 2f - radius * 0.55f;
        tmpRect.set(cx - headSize / 2f, headTop, cx + headSize / 2f, headTop + headSize);
        canvas.drawRoundRect(tmpRect, dp(4), dp(4), bodyPaint);

        // Hat (top part)
        float hatHeight = headSize * 0.3f;
        float hatWidth = headSize * 0.7f;
        float hatTop = headTop - hatHeight * 0.6f;
        tmpRect.set(cx - hatWidth / 2f, hatTop, cx + hatWidth / 2f, hatTop + hatHeight);
        canvas.drawRoundRect(tmpRect, dp(2), dp(2), hatPaint);

        // Body
        float bodyWidth = headSize * 0.6f;
        float bodyTop = headTop + headSize;
        float bodyHeight = radius * 0.5f;
        tmpRect.set(cx - bodyWidth / 2f, bodyTop, cx + bodyWidth / 2f, bodyTop + bodyHeight);
        canvas.drawRoundRect(tmpRect, dp(3), dp(3), bodyPaint);
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }
}
