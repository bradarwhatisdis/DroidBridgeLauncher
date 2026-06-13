/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 *
 * Manual view binding for item_launcher_instance.xml
 */

package ca.dnamobile.javalauncher.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

/**
 * Manual view binding for the instance list item layout.
 * Since no item_launcher_instance.xml layout exists in the layout directory,
 * this binding creates a programmatic layout that mirrors the expected structure.
 *
 * The actual inflate method creates the views programmatically so the adapter
 * can use standard binding patterns without a layout XML file.
 */
public final class ItemLauncherInstanceBinding {
    @NonNull
    public final MaterialCardView instanceCard;
    @NonNull
    public final ImageView imageInstanceIcon;
    @NonNull
    public final TextView textInstanceName;
    @NonNull
    public final TextView textInstanceMeta;
    @NonNull
    public final TextView textInstanceState;
    @NonNull
    public final ImageButton buttonDeleteInstance;

    @NonNull
    private final View root;

    private ItemLauncherInstanceBinding(@NonNull View root) {
        this.root = root;
        this.instanceCard = (MaterialCardView) root;
        this.imageInstanceIcon = root.findViewById(android.R.id.icon);
        this.textInstanceName = root.findViewById(android.R.id.title);
        this.textInstanceMeta = root.findViewById(android.R.id.summary);
        this.textInstanceState = root.findViewById(android.R.id.text1);
        this.buttonDeleteInstance = root.findViewById(android.R.id.button1);
    }

    @NonNull
    public static ItemLauncherInstanceBinding inflate(
            @NonNull LayoutInflater inflater,
            @NonNull ViewGroup parent,
            boolean attachToParent
    ) {
        // Create a MaterialCardView as the root
        MaterialCardView card = new MaterialCardView(inflater.getContext());
        card.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        card.setUseCompatPadding(true);
        card.setRadius(inflater.getContext().getResources().getDisplayMetrics().density * 12);

        // We use a simple tag-based approach rather than real view binding IDs
        // since there's no XML layout. The adapter accesses views via this binding class.

        // For simplicity, create placeholder views with system IDs
        ImageView icon = new ImageView(inflater.getContext());
        icon.setId(android.R.id.icon);
        icon.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        TextView title = new TextView(inflater.getContext());
        title.setId(android.R.id.title);
        title.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        TextView summary = new TextView(inflater.getContext());
        summary.setId(android.R.id.summary);
        summary.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        TextView stateText = new TextView(inflater.getContext());
        stateText.setId(android.R.id.text1);
        stateText.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        ImageButton button = new ImageButton(inflater.getContext());
        button.setId(android.R.id.button1);
        button.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        card.addView(icon);
        card.addView(title);
        card.addView(summary);
        card.addView(stateText);
        card.addView(button);

        if (attachToParent) {
            parent.addView(card);
        }

        return new ItemLauncherInstanceBinding(card);
    }

    @NonNull
    public View getRoot() {
        return root;
    }
}
