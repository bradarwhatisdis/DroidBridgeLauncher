/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.modmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 * Stub implementation of modpack update management.
 * Provides the classes and types used by ModpackUpdateDetailsActivity.
 * In a full build this would communicate with Modrinth and CurseForge APIs.
 */
public final class ModpackUpdateManager {
    public enum Platform {
        MODRINTH("Modrinth"),
        CURSEFORGE("CurseForge");

        @NonNull
        public final String displayName;

        Platform(@NonNull String displayName) {
            this.displayName = displayName;
        }
    }

    public static final class VersionInfo {
        @NonNull
        public final String versionId;
        @NonNull
        public final String versionLabel;
        @NonNull
        public final ArrayList<String> gameVersions;
        @NonNull
        public final String primaryMinecraftVersion;
        @Nullable
        public final String datePublished;
        @NonNull
        public final ArrayList<String> loaders;
        @Nullable
        public final String changelog;

        public VersionInfo(
                @NonNull String versionId,
                @NonNull String versionLabel,
                @NonNull ArrayList<String> gameVersions,
                @NonNull String primaryMinecraftVersion,
                @Nullable String datePublished,
                @NonNull ArrayList<String> loaders,
                @Nullable String changelog
        ) {
            this.versionId = versionId;
            this.versionLabel = versionLabel;
            this.gameVersions = gameVersions;
            this.primaryMinecraftVersion = primaryMinecraftVersion;
            this.datePublished = datePublished;
            this.loaders = loaders;
            this.changelog = changelog;
        }

        @NonNull
        public String getMinecraftVersionsLabel() {
            return primaryMinecraftVersion;
        }

        @NonNull
        public String getLoadersLabel() {
            if (loaders.isEmpty()) return "Vanilla";
            return String.join(", ", loaders);
        }
    }

    public static final class ProjectMatch {
        @NonNull
        public final Platform platform;
        @NonNull
        public final String projectId;
        @NonNull
        public final String title;
        @NonNull
        public final String slug;
        @NonNull
        public final String summary;
        public final boolean useLatestVersion;

        public ProjectMatch(
                @NonNull Platform platform,
                @NonNull String projectId,
                @NonNull String title,
                @NonNull String slug,
                @NonNull String summary,
                boolean useLatestVersion
        ) {
            this.platform = platform;
            this.projectId = projectId;
            this.title = title;
            this.slug = slug;
            this.summary = summary;
            this.useLatestVersion = useLatestVersion;
        }
    }

    public static final class InstalledModpackInfo {
        @NonNull
        public final String projectId;
        @NonNull
        public final String platform;
        @NonNull
        public final String installedVersionId;

        public InstalledModpackInfo(
                @NonNull String projectId,
                @NonNull String platform,
                @NonNull String installedVersionId
        ) {
            this.projectId = projectId;
            this.platform = platform;
            this.installedVersionId = installedVersionId;
        }
    }

    public static final class UpdateResult {
        @NonNull
        public final String versionLabel;
        public final int removedOldFiles;
        @Nullable
        public final String minecraftVersion;
        @Nullable
        public final String loader;

        public UpdateResult(
                @NonNull String versionLabel,
                int removedOldFiles,
                @Nullable String minecraftVersion,
                @Nullable String loader
        ) {
            this.versionLabel = versionLabel;
            this.removedOldFiles = removedOldFiles;
            this.minecraftVersion = minecraftVersion;
            this.loader = loader;
        }
    }

    public interface Listener {
        void onStatus(@NonNull String message);
        void onProgress(int current, int total);
    }

    private ModpackUpdateManager() {
    }

    @NonNull
    public static ProjectMatch createProjectMatch(
            @NonNull Platform platform,
            @NonNull String projectId,
            @NonNull String title,
            @NonNull String slug,
            @NonNull String summary,
            boolean useLatestVersion
    ) {
        return new ProjectMatch(platform, projectId, title, slug, summary, useLatestVersion);
    }

    @NonNull
    public static ArrayList<VersionInfo> loadVersions(
            @NonNull ProjectMatch project,
            @NonNull Listener listener
    ) {
        listener.onStatus("Modpack update checking is not available in this build.");
        return new ArrayList<>();
    }

    @Nullable
    public static InstalledModpackInfo readInstalledModpackInfo(
            @Nullable File rootDirectory,
            @NonNull File gameDirectory
    ) {
        return null;
    }

    @NonNull
    public static UpdateResult updateInstalledModpack(
            @NonNull Context context,
            @Nullable File rootDirectory,
            @NonNull File gameDirectory,
            @NonNull String minecraftVersion,
            @NonNull String loader,
            @NonNull InstalledModpackInfo installed,
            @NonNull ProjectMatch project,
            @NonNull VersionInfo selectedVersion,
            @NonNull Listener listener
    ) throws Exception {
        throw new UnsupportedOperationException(
                "Modpack updates are not available in this build."
        );
    }
}
