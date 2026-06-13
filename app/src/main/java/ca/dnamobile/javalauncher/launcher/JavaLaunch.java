/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 *
 * Main launcher Activity for DroidBridge Launcher.
 * Replaces the proprietary JavaLaunch with an offline-capable version
 * that lists installed Minecraft versions and launches them via VMLauncher.
 */

package ca.dnamobile.javalauncher.launcher;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ca.dnamobile.javalauncher.LauncherSettingsActivity;
import ca.dnamobile.javalauncher.data.AccountStore;
import ca.dnamobile.javalauncher.feature.log.Logging;
import ca.dnamobile.javalauncher.renderer.RendererLaunchEnvironment;
import ca.dnamobile.javalauncher.renderer.Renderers;
import ca.dnamobile.javalauncher.settings.LauncherPreferences;
import ca.dnamobile.javalauncher.utils.path.PathManager;
import ca.dnamobile.javalauncher.utils.FullscreenUtils;
import com.oracle.dalvik.VMLauncher;

/**
 * Main launcher Activity. Displays installed Minecraft versions and
 * provides the Launch button that starts the game via VMLauncher.launchJVM().
 */
public class JavaLaunch extends AppCompatActivity {
    private static final String TAG = "JavaLaunch";

    private AccountStore accountStore;
    // Package-private; accessed by AccountStore (import from ca.dnamobile.javalauncher.data)
    private final ArrayList<VersionEntry> versions = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView textAccountName;
    private TextView textStatus;
    private int selectedVersionIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PathManager.initContextConstants(this);
        accountStore = new AccountStore(this);
        buildLayout();
        FullscreenUtils.enableImmersive(this);
        scanVersions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullscreenUtils.enableImmersive(this);
        refreshAccountDisplay();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) FullscreenUtils.enableImmersive(this);
    }

    // ---- UI construction ----

    private void buildLayout() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));
        root.setFitsSystemWindows(true);
        setContentView(root);

        // Account header
        MaterialCardView accountCard = new MaterialCardView(this);
        accountCard.setUseCompatPadding(true);
        accountCard.setRadius(dp(12));
        accountCard.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout accountRow = new LinearLayout(this);
        accountRow.setOrientation(LinearLayout.HORIZONTAL);
        accountRow.setGravity(Gravity.CENTER_VERTICAL);
        accountRow.setPadding(dp(16), dp(12), dp(16), dp(12));

        textAccountName = new TextView(this);
        textAccountName.setTextSize(18);
        textAccountName.setTypeface(textAccountName.getTypeface(), Typeface.BOLD);
        textAccountName.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));
        accountRow.addView(textAccountName);

        MaterialButton btnSettings = new MaterialButton(this);
        btnSettings.setText("Settings");
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, LauncherSettingsActivity.class));
        });
        accountRow.addView(btnSettings);

        accountCard.addView(accountRow);
        root.addView(accountCard, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Title
        TextView title = new TextView(this);
        title.setText("Minecraft Versions");
        title.setTextSize(22);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setPadding(0, dp(16), 0, dp(8));
        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Version list
        ListView listView = new ListView(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedVersionIndex = position;
        });
        root.addView(listView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));

        // Status text
        textStatus = new TextView(this);
        textStatus.setText("Scanning for installed Minecraft versions...");
        textStatus.setPadding(0, dp(8), 0, dp(8));
        textStatus.setTextSize(14);
        root.addView(textStatus, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Launch button
        MaterialButton btnLaunch = new MaterialButton(this);
        btnLaunch.setText("Launch");
        btnLaunch.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        btnLaunch.setOnClickListener(v -> launchSelectedVersion());
        root.addView(btnLaunch);
    }

    private void refreshAccountDisplay() {
        AccountStore.Account account = accountStore != null ? accountStore.load() : null;
        if (account != null) {
            textAccountName.setText("\uD83D\uDC64 " + account.getBestDisplayName());
        } else {
            textAccountName.setText("\uD83D\uDC64 Offline Player");
        }
    }

    // ---- Version scanning ----

    private static final class VersionEntry {
        @NonNull final String id;
        @NonNull final String type;
        @NonNull final String mainClass;
        @NonNull final List<String> libraries;
        @NonNull final List<String> jvmArgs;
        @NonNull final List<String> gameArgs;
        @Nullable final String assetIndexId;

        VersionEntry(
                @NonNull String id,
                @NonNull String type,
                @NonNull String mainClass,
                @NonNull List<String> libraries,
                @NonNull List<String> jvmArgs,
                @NonNull List<String> gameArgs,
                @Nullable String assetIndexId
        ) {
            this.id = id;
            this.type = type;
            this.mainClass = mainClass;
            this.libraries = libraries;
            this.jvmArgs = jvmArgs;
            this.gameArgs = gameArgs;
            this.assetIndexId = assetIndexId;
        }
    }

    private void scanVersions() {
        versions.clear();

        String mcHome = PathManager.DIR_MINECRAFT_HOME;
        if (mcHome == null) {
            textStatus.setText("Minecraft home directory not available.");
            return;
        }

        File versionsDir = new File(mcHome, "versions");
        if (!versionsDir.isDirectory()) {
            textStatus.setText("No versions directory found at " + versionsDir.getAbsolutePath());
            return;
        }

        File[] versionDirs = versionsDir.listFiles(File::isDirectory);
        if (versionDirs == null || versionDirs.length == 0) {
            textStatus.setText("No installed Minecraft versions found.");
            return;
        }

        for (File dir : versionDirs) {
            VersionEntry entry = parseVersionJson(dir);
            if (entry != null) {
                versions.add(entry);
            }
        }

        if (versions.isEmpty()) {
            textStatus.setText("No valid Minecraft versions found.");
            return;
        }

        ArrayList<String> labels = new ArrayList<>();
        for (VersionEntry v : versions) {
            String label = v.id;
            if (!"release".equals(v.type)) {
                label += " (" + v.type + ")";
            }
            labels.add(label);
        }
        adapter.clear();
        adapter.addAll(labels);
        adapter.notifyDataSetChanged();
        textStatus.setText(versions.size() + " version(s) found. Select one and tap Launch.");
    }

    @Nullable
    private VersionEntry parseVersionJson(@NonNull File versionDir) {
        String id = versionDir.getName();
        File jsonFile = new File(versionDir, id + ".json");
        if (!jsonFile.isFile()) {
            // Try any JSON file in the directory
            File[] jsons = versionDir.listFiles((d, name) -> name.endsWith(".json"));
            if (jsons == null || jsons.length == 0) return null;
            jsonFile = jsons[0];
        }

        try (InputStream in = new FileInputStream(jsonFile)) {
            byte[] data = new byte[(int) Math.min(jsonFile.length(), 65536)];
            int read = in.read(data);
            if (read <= 0) return null;
            String content = new String(data, 0, read, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);

            String versionId = json.optString("id", id);
            String type = json.optString("type", "release");
            String mainClass = json.optString("mainClass",
                    "net.minecraft.client.main.Main");

            // Parse libraries
            ArrayList<String> libraries = new ArrayList<>();
            JSONArray libs = json.optJSONArray("libraries");
            if (libs != null) {
                for (int i = 0; i < libs.length(); i++) {
                    JSONObject lib = libs.optJSONObject(i);
                    if (lib == null) continue;
                    // Only include non-native, client-required libraries
                    JSONObject downloads = lib.optJSONObject("downloads");
                    JSONObject artifact = downloads != null ? downloads.optJSONObject("artifact") : null;
                    if (artifact != null && artifact.has("path")) {
                        String libPath = artifact.optString("path", "");
                        if (!libPath.isEmpty()) {
                            File libFile = new File(PathManager.DIR_MINECRAFT_HOME, "libraries/" + libPath);
                            if (libFile.isFile()) {
                                libraries.add(libFile.getAbsolutePath());
                            }
                        }
                    }
                }
            }

            // Parse JVM arguments
            ArrayList<String> jvmArgs = new ArrayList<>();
            JSONObject args = json.optJSONObject("arguments");
            if (args != null) {
                JSONArray jvm = args.optJSONArray("jvm");
                if (jvm != null) {
                    for (int i = 0; i < jvm.length(); i++) {
                        Object obj = jvm.opt(i);
                        if (obj instanceof String) {
                            jvmArgs.add((String) obj);
                        }
                    }
                }
            }

            // Parse game arguments
            ArrayList<String> gameArgs = new ArrayList<>();
            // Pre-1.13 format
            String mcArgs = json.optString("minecraftArguments", null);
            if (mcArgs != null && !mcArgs.isEmpty()) {
                gameArgs.addAll(Arrays.asList(mcArgs.split(" ")));
            }
            // 1.13+ format
            if (args != null) {
                JSONArray game = args.optJSONArray("game");
                if (game != null) {
                    gameArgs.clear(); // Prefer new format
                    for (int i = 0; i < game.length(); i++) {
                        Object obj = game.opt(i);
                        if (obj instanceof String) {
                            gameArgs.add((String) obj);
                        }
                    }
                }
            }

            String assetIndexId = null;
            JSONObject assetIndex = json.optJSONObject("assetIndex");
            if (assetIndex != null) {
                assetIndexId = assetIndex.optString("id", null);
            }

            return new VersionEntry(
                    versionId, type, mainClass,
                    libraries, jvmArgs, gameArgs, assetIndexId
            );
        } catch (Throwable throwable) {
            Logging.w(TAG, "Failed to parse version at " + versionDir.getAbsolutePath(), throwable);
            return null;
        }
    }

    // ---- Game launch ----

    private void launchSelectedVersion() {
        if (selectedVersionIndex < 0 || selectedVersionIndex >= versions.size()) {
            Toast.makeText(this, "Please select a Minecraft version first.", Toast.LENGTH_SHORT).show();
            return;
        }

        VersionEntry version = versions.get(selectedVersionIndex);
        AccountStore.Account account = accountStore != null ? accountStore.load() : null;
        String playerName = account != null ? account.getBestDisplayName() : "Player";
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // Build JVM arguments
        ArrayList<String> jvmArgList = new ArrayList<>();

        // Memory
        int ramMb = LauncherPreferences.getAllocatedMemoryMb(this, 512);
        jvmArgList.add("-Xms" + Math.min(ramMb, 256) + "M");
        jvmArgList.add("-Xmx" + ramMb + "M");

        // Add version JVM args (skip LWJGL library path, we handle it via env)
        for (String arg : version.jvmArgs) {
            if (arg.startsWith("-Djava.library.path=")) continue;
            if (arg.startsWith("-cp") || arg.startsWith("-classpath")) continue;
            jvmArgList.add(arg);
        }

        // Classpath
        StringBuilder classpath = new StringBuilder();
        String mcHome = PathManager.DIR_MINECRAFT_HOME;
        File versionJar = new File(mcHome, "versions/" + version.id + "/" + version.id + ".jar");
        if (versionJar.isFile()) {
            classpath.append(versionJar.getAbsolutePath());
        }

        for (String lib : version.libraries) {
            if (classpath.length() > 0) classpath.append(":");
            classpath.append(lib);
        }

        if (classpath.length() > 0) {
            jvmArgList.add("-cp");
            jvmArgList.add(classpath.toString());
        }

        // Main class
        jvmArgList.add(version.mainClass);

        // Game arguments
        String gameDir = PathManager.DIR_MINECRAFT_HOME;
        String assetsDir = gameDir + "/assets";
        String assetIndex = version.assetIndexId != null ? version.assetIndexId : "legacy";

        for (String arg : version.gameArgs) {
            String resolved = arg
                    .replace("${auth_player_name}", playerName)
                    .replace("${auth_session}", "token")
                    .replace("${auth_access_token}", "offline:" + uuid)
                    .replace("${auth_uuid}", uuid)
                    .replace("${version_name}", version.id)
                    .replace("${version_type}", version.type)
                    .replace("${game_directory}", gameDir)
                    .replace("${game_dir}", gameDir)
                    .replace("${assets_root}", assetsDir)
                    .replace("${assets_index_name}", assetIndex)
                    .replace("${user_properties}", "{}")
                    .replace("${user_type}", "mojang")
                    .replace("${profile_name}", playerName);
            jvmArgList.add(resolved);
        }

        // Apply renderer environment
        try {
            LaunchPlan plan = new LaunchPlan(
                    new File(gameDir),
                    new File(PathManager.DIR_MULTIRT_HOME, "Internal-8")
            );
            // Apply renderer environment before JVM launch
            RendererLaunchEnvironment.applyBeforeJvmLaunch(
                    this,
                    plan,
                    Renderers.getCurrentRenderer(this)
            );
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to apply renderer environment; continuing", throwable);
        }

        // Convert to String[]
        String[] jvmArgs = jvmArgList.toArray(new String[0]);

        Logging.i(TAG, "Launching Minecraft " + version.id + " as " + playerName);
        Logging.i(TAG, "JVM args: " + TextUtils.join(" ", jvmArgs));

        // Launch via VMLauncher
        try {
            int exitCode = VMLauncher.launchJVM(jvmArgs);
            if (exitCode != 0) {
                showExitDialog("Minecraft exited with code " + exitCode);
            }
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to launch JVM", throwable);
            showExitDialog("Failed to launch Minecraft: " + throwable.getMessage());
        }
    }

    private void showExitDialog(@NonNull String message) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Game Exited")
                .setMessage(message + "\n\nCheck latestlog.txt for details.")
                .setPositiveButton(android.R.string.ok, null)
                .show());
    }

    // ---- Utilities ----

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
