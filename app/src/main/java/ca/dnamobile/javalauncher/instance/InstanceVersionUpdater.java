/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.instance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

/**
 * Stub implementation for updating instance base versions.
 * In a full build this would download and install Minecraft versions
 * and loader profiles via PojavLauncher's version management.
 */
public final class InstanceVersionUpdater {
    private InstanceVersionUpdater() {
    }

    public static final class UpdateResult {
        @NonNull
        public final String loader;
        @NonNull
        public final String baseVersionId;
        @NonNull
        public final String minecraftVersionId;
        @NonNull
        public final String versionType;
        @Nullable
        public final String loaderVersion;

        public UpdateResult(
                @NonNull String loader,
                @NonNull String baseVersionId,
                @NonNull String minecraftVersionId,
                @NonNull String versionType,
                @Nullable String loaderVersion
        ) {
            this.loader = loader;
            this.baseVersionId = baseVersionId;
            this.minecraftVersionId = minecraftVersionId;
            this.versionType = versionType;
            this.loaderVersion = loaderVersion;
        }
    }

    public interface Listener {
        void onStatus(@NonNull String message);
        void onProgress(int current, int total);
    }

    @NonNull
    public static UpdateResult updateInstanceVersion(
            @NonNull Context context,
            @Nullable File rootDirectory,
            @NonNull File gameDirectory,
            @NonNull String instanceName,
            @NonNull String loader,
            @NonNull String minecraftVersionId,
            @NonNull Listener listener
    ) throws Exception {
        throw new UnsupportedOperationException(
                "Instance version updates are not available in this build."
        );
    }
}
