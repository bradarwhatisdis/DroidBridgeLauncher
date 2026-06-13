/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ca.dnamobile.javalauncher.data.AccountStore;

/**
 * Loads and renders player head (face) images from skin files or URLs.
 */
public final class PlayerHeadLoader {
    private static final int HEAD_SIZE = 8; // 8x8 pixels in skin atlas
    private static final int SKIN_WIDTH = 64;
    private static final int SKIN_HEIGHT = 64;

    private PlayerHeadLoader() {
    }

    /**
     * Load the face portion of a player's skin into the given ImageView.
     * Falls back to a placeholder if no skin is available.
     */
    public static void loadInto(
            @NonNull Context context,
            @NonNull ImageView imageView,
            @Nullable AccountStore.Account account,
            @Nullable Object unused
    ) {
        if (account != null && account.hasOfflineSkin() && account.offlineSkinPath != null) {
            File skinFile = new File(account.offlineSkinPath);
            if (skinFile.isFile()) {
                Bitmap head = loadHeadFromSkinFile(skinFile);
                if (head != null) {
                    imageView.setImageBitmap(head);
                    return;
                }
            }
        }

        // Fallback: clear the image (caller should set placeholder background)
        imageView.setImageDrawable(null);
    }

    /**
     * Extract the 8x8 face region from a 64x64 Minecraft skin file.
     * Returns null if the file cannot be decoded.
     */
    @Nullable
    public static Bitmap loadHeadFromSkinFile(@NonNull File skinFile) {
        try (InputStream in = new FileInputStream(skinFile)) {
            Bitmap fullSkin = BitmapFactory.decodeStream(in);
            if (fullSkin == null) return null;

            // Normalize to 64x64
            int srcWidth = fullSkin.getWidth();
            int srcHeight = fullSkin.getHeight();

            if (srcWidth < HEAD_SIZE || srcHeight < HEAD_SIZE) {
                fullSkin.recycle();
                return null;
            }

            // The face is in the top-left 8x8 region of the skin
            int faceX = 8;   // Face starts at x=8 in the default skin layout
            int faceY = 8;   // Face starts at y=8
            // Scale to a viewable size (e.g., HEAD_SIZE * SCALE)
            int displaySize = Math.min(64, Math.max(HEAD_SIZE * 4, srcWidth / 4));

            // If skin is 64x32 (old format), face is still at 8,8
            Bitmap head;

            // Scale the face region up
            float scaleX = (float) HEAD_SIZE / srcWidth;
            float scaleY = (float) HEAD_SIZE / srcHeight;
            int scaledFaceX = (int) (faceX * scaleX * (srcWidth / HEAD_SIZE));
            int scaledFaceY = (int) (faceY * scaleY * (srcHeight / HEAD_SIZE));

            // Simpler approach: scale the whole thing and crop
            int sampleSize = 1;
            if (srcWidth > displaySize) {
                sampleSize = srcWidth / displaySize;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
            // Re-decode with sample size
            try (InputStream in2 = new FileInputStream(skinFile)) {
                Bitmap sampled = BitmapFactory.decodeStream(in2, null, opts);
                if (sampled == null) {
                    fullSkin.recycle();
                    return null;
                }

                int sWidth = sampled.getWidth();
                int sHeight = sampled.getHeight();

                // In 64x64 skin, face = 8,8 to 16,16
                float hRatio = (float) sWidth / srcWidth;
                float vRatio = (float) sHeight / srcHeight;

                int cropX = Math.round(8 * hRatio);
                int cropY = Math.round(8 * vRatio);
                int cropSize = Math.round(8 * Math.min(hRatio, vRatio));

                cropX = Math.max(0, Math.min(cropX, sWidth - 1));
                cropY = Math.max(0, Math.min(cropY, sHeight - 1));
                cropSize = Math.max(1, Math.min(cropSize, Math.min(sWidth - cropX, sHeight - cropY)));

                head = Bitmap.createBitmap(sampled, cropX, cropY, cropSize, cropSize);

                if (sampled != fullSkin && sampled != head) sampled.recycle();
                fullSkin.recycle();
                return head;
            }
        } catch (Throwable ignored) {
        }
        return null;
    }
}
