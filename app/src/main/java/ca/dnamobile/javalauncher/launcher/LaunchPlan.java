/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.launcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

/**
 * Launch plan containing game and runtime directory information.
 * Used by RendererLaunchEnvironment and modcompat classes.
 */
public final class LaunchPlan {
    @NonNull private final File gameDirectory;
    @NonNull private final File runtimeDirectory;
    @Nullable private final File rootDirectory;
    @NonNull private final String instanceName;
    @NonNull private final String accountName;

    public LaunchPlan(
            @NonNull File gameDirectory,
            @NonNull File runtimeDirectory
    ) {
        this(gameDirectory, runtimeDirectory, null, "Instance", "Player");
    }

    public LaunchPlan(
            @NonNull File gameDirectory,
            @NonNull File runtimeDirectory,
            @Nullable File rootDirectory,
            @NonNull String instanceName,
            @NonNull String accountName
    ) {
        this.gameDirectory = gameDirectory;
        this.runtimeDirectory = runtimeDirectory;
        this.rootDirectory = rootDirectory;
        this.instanceName = instanceName;
        this.accountName = accountName;
    }

    @NonNull
    public File getGameDirectory() {
        return gameDirectory;
    }

    @NonNull
    public File getRuntimeDirectory() {
        return runtimeDirectory;
    }

    @Nullable
    public File getRootDirectory() {
        return rootDirectory;
    }

    @NonNull
    public String getInstanceName() {
        return instanceName;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }
}
