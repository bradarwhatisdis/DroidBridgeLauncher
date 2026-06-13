/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class MinecraftVersion {
    @NonNull private final String id;
    @NonNull private final String type;

    public MinecraftVersion(@NonNull String id, @NonNull String type) {
        this.id = id;
        this.type = type;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
