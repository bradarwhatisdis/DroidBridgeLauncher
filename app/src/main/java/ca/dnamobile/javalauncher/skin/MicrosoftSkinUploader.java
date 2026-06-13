/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.skin;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Stub for Microsoft skin upload.
 * In offline mode, skin upload to Microsoft servers is unavailable.
 */
public final class MicrosoftSkinUploader {
    private MicrosoftSkinUploader() {
    }

    /**
     * Upload a skin to Microsoft services.
     * In offline mode this always throws because there's no access token.
     */
    public static void uploadSkin(
            @NonNull String accessToken,
            @NonNull File skinFile,
            @NonNull SkinModelType model
    ) throws Exception {
        throw new UnsupportedOperationException(
                "Microsoft skin upload is not available in offline mode. "
                        + "Please sign in to a Microsoft account to upload skins."
        );
    }
}
