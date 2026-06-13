/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Validates and analyzes Minecraft skin files.
 */
public final class CustomSkinStore {
    private final Context context;

    public CustomSkinStore(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Validate that a file is a properly-sized PNG skin image.
     */
    public static boolean isSkinValid(@NonNull File skinFile) {
        if (!skinFile.isFile() || !skinFile.getName().toLowerCase(java.util.Locale.US).endsWith(".png")) {
            return false;
        }

        try (InputStream in = new FileInputStream(skinFile)) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);

            int width = opts.outWidth;
            int height = opts.outHeight;

            // Valid Minecraft skin dimensions: 64x64 or 64x32
            return (width == 64 && (height == 64 || height == 32))
                    || (width == 64 && height == 64);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Detect the skin model type from a skin file.
     * SLIM (Alex model) has a slightly different arm pixel arrangement.
     * For simplicity, we default to the classic model and check pixel transparency.
     */
    @NonNull
    public static SkinModelType getSkinModel(@NonNull File skinFile) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(skinFile.getAbsolutePath());
            if (bitmap == null) return SkinModelType.CLASSIC;

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // SLIM detection: check if the left arm (pixels 44-48, y=20-28 in a 64x64 skin)
            // has transparent pixels (3px wide arm for slim vs 4px wide for classic)
            // This is a heuristic. For 64x32 skins, the arm starts at different coords.
            if (width >= 64 && height >= 32) {
                // Use a simplified check: check pixel at position that distinguishes slim from classic
                // In classic: column 46 (in a certain region) is filled
                // In slim: column 46 is more transparent
                int armCheckX = 46;
                int armCheckY = 20;

                if (armCheckX < width && armCheckY < height) {
                    int pixel = bitmap.getPixel(armCheckX, armCheckY);
                    int alpha = (pixel >> 24) & 0xFF;
                    if (alpha < 128) {
                        bitmap.recycle();
                        return SkinModelType.SLIM;
                    }
                }
            }

            bitmap.recycle();
        } catch (Throwable ignored) {
        }

        return SkinModelType.CLASSIC;
    }
}
