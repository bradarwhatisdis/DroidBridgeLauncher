/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 *
 * Manual view binding for activity_launcher_settings.xml
 * Generated to avoid requiring the Android Data Binding Library.
 */

package ca.dnamobile.javalauncher.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;

import ca.dnamobile.javalauncher.R;

/**
 * Manual view binding for activity_launcher_settings.xml
 *
 * Binds all views used by LauncherSettingsActivity.
 */
public final class ActivityLauncherSettingsBinding {
    @NonNull
    public final FrameLayout settingsRoot;
    @NonNull
    public final FrameLayout settingsContent;
    @NonNull
    public final ScrollView settingsScrollView;
    @NonNull
    public final MaterialCardView cardAccountSettings;
    @NonNull
    public final View modelPlayerPreview;

    // Account section
    @NonNull
    public final TextView textAccountStatus;
    @NonNull
    public final TextView textSkinStatus;
    @NonNull
    public final MaterialButton buttonSignIn;
    @NonNull
    public final MaterialButton buttonUseMicrosoftAccount;
    @NonNull
    public final MaterialButton buttonManageOfflineAccounts;
    @NonNull
    public final MaterialButton buttonRefreshMicrosoftSkin;
    @NonNull
    public final MaterialButton buttonChangeMicrosoftSkin;
    @NonNull
    public final MaterialButton buttonChangeMicrosoftCape;
    @NonNull
    public final MaterialButton buttonSignOut;

    // Renderer section
    @NonNull
    public final MaterialCardView cardRendererSettings;
    @NonNull
    public final View layoutRendererPickerRow;
    @NonNull
    public final Spinner spinnerRenderer;
    @NonNull
    public final MaterialButton buttonDownloadRenderers;
    @NonNull
    public final View layoutVulkanDriverSettings;
    @NonNull
    public final Spinner spinnerVulkanDriver;
    @NonNull
    public final TextView textVulkanDriverDescription;
    @NonNull
    public final SwitchMaterial switchUseNativeSurface;
    @NonNull
    public final TextView textGameResolutionScale;
    @NonNull
    public final SeekBar sliderGameResolutionScale;
    @NonNull
    public final TextView textGameResolutionScaleSummary;
    @NonNull
    public final SwitchMaterial switchForceFullscreenMode;
    @NonNull
    public final SwitchMaterial switchIgnoreDisplayCutout;
    @NonNull
    public final SwitchMaterial switchAvoidRoundedCorners;
    @NonNull
    public final SwitchMaterial switchUseSystemVulkanDriver;
    @NonNull
    public final SwitchMaterial switchUseOpenGlFor26Plus;
    @NonNull
    public final TextView textRendererPluginConfig;
    @NonNull
    public final MaterialButton buttonGrantRendererStorageAccess;
    @NonNull
    public final MaterialButton buttonRefreshRenderers;
    @NonNull
    public final MaterialButton buttonImportRendererPlugin;
    @NonNull
    public final MaterialButton buttonClearRendererPluginCache;
    @NonNull
    public final TextView textRendererDescription;

    // Controller section
    @NonNull
    public final MaterialCardView cardControllerSettings;
    @NonNull
    public final View layoutControllerSettings;
    @NonNull
    public final MaterialButton buttonEditBuiltInController;
    @NonNull
    public final SwitchMaterial switchTouchControlsEnabled;
    @NonNull
    public final SwitchMaterial switchMinecraftTouchGestures;
    @NonNull
    public final TextView textMinecraftTouchGesturesSummary;
    @NonNull
    public final SwitchMaterial switchDoubleTapToDrop;
    @NonNull
    public final TextView textDoubleTapToDropSummary;
    @NonNull
    public final MaterialButton buttonManageTouchControls;
    @NonNull
    public final TextView textMouseCursorIconSummary;
    @NonNull
    public final MaterialButton buttonMouseCursorIconSettings;
    @NonNull
    public final SwitchMaterial switchForceSdlControllerBridge;

    // Launcher section
    @NonNull
    public final MaterialCardView cardLauncherSettings;
    @NonNull
    public final TextView textAllocatedRam;
    @NonNull
    public final SeekBar sliderAllocatedRam;
    @NonNull
    public final TextView textAvailableRamSummary;
    @NonNull
    public final CheckBox checkKeepLogs;
    @NonNull
    public final SwitchMaterial switchInstallNotifications;
    @NonNull
    public final TextView textInstallNotificationsSummary;
    @NonNull
    public final TextView textSimpleVoiceChatMicrophoneStatus;
    @NonNull
    public final MaterialButton buttonSimpleVoiceChatMicrophonePermission;
    @NonNull
    public final SwitchMaterial switchShowInGameSettingsButton;
    @NonNull
    public final SwitchMaterial switchShowGameLogOverlay;
    @NonNull
    public final MaterialButton buttonShareLatestLog;

    // Instance section
    @NonNull
    public final MaterialCardView cardInstanceSettings;
    @NonNull
    public final SwitchMaterial switchShowSharedInstalls;
    @NonNull
    public final SwitchMaterial switchRemoveInheritedVanilla;
    @NonNull
    public final TextView textFolder;
    @NonNull
    public final TextView textDroidBridgeBackupSummary;
    @NonNull
    public final MaterialButton buttonBackupDroidBridgeData;
    @NonNull
    public final MaterialButton buttonRestoreDroidBridgeData;

    // Privacy section
    @NonNull
    public final MaterialCardView cardPrivacyPolicySettings;
    @NonNull
    public final TextView textPrivacyPolicySummary;
    @NonNull
    public final MaterialButton buttonOpenMinecraftEula;
    @NonNull
    public final MaterialButton buttonOpenPrivacyPolicy;
    @NonNull
    public final MaterialButton buttonOpenDroidBridgeTerms;
    @NonNull
    public final MaterialButton buttonOpenDroidBridgeLicense;

    // Top bar
    @NonNull
    public final MaterialCardView cardSettingsTabsHeader;
    @NonNull
    public final MaterialButton buttonSettingsBack;
    @NonNull
    public final TextView textSettingsHeaderTitle;
    @NonNull
    public final TabLayout settingsSectionTabs;

    // Scrim
    @NonNull
    public final View settingsTopContentScrim;

    private final View root;

    private ActivityLauncherSettingsBinding(@NonNull View root) {
        this.root = root;

        settingsRoot = root.findViewById(R.id.settingsRoot);
        settingsContent = root.findViewById(R.id.settingsContent);
        settingsScrollView = root.findViewById(R.id.settingsScrollView);
        cardAccountSettings = root.findViewById(R.id.cardAccountSettings);
        modelPlayerPreview = root.findViewById(R.id.modelPlayerPreview);

        // Account
        textAccountStatus = root.findViewById(R.id.textAccountStatus);
        textSkinStatus = root.findViewById(R.id.textSkinStatus);
        buttonSignIn = root.findViewById(R.id.buttonSignIn);
        buttonUseMicrosoftAccount = root.findViewById(R.id.buttonUseMicrosoftAccount);
        buttonManageOfflineAccounts = root.findViewById(R.id.buttonManageOfflineAccounts);
        buttonRefreshMicrosoftSkin = root.findViewById(R.id.buttonRefreshMicrosoftSkin);
        buttonChangeMicrosoftSkin = root.findViewById(R.id.buttonChangeMicrosoftSkin);
        buttonChangeMicrosoftCape = root.findViewById(R.id.buttonChangeMicrosoftCape);
        buttonSignOut = root.findViewById(R.id.buttonSignOut);

        // Renderer
        cardRendererSettings = root.findViewById(R.id.cardRendererSettings);
        layoutRendererPickerRow = root.findViewById(R.id.layoutRendererPickerRow);
        spinnerRenderer = root.findViewById(R.id.spinnerRenderer);
        buttonDownloadRenderers = root.findViewById(R.id.buttonDownloadRenderers);
        layoutVulkanDriverSettings = root.findViewById(R.id.layoutVulkanDriverSettings);
        spinnerVulkanDriver = root.findViewById(R.id.spinnerVulkanDriver);
        textVulkanDriverDescription = root.findViewById(R.id.textVulkanDriverDescription);
        switchUseNativeSurface = root.findViewById(R.id.switchUseNativeSurface);
        textGameResolutionScale = root.findViewById(R.id.textGameResolutionScale);
        sliderGameResolutionScale = root.findViewById(R.id.sliderGameResolutionScale);
        textGameResolutionScaleSummary = root.findViewById(R.id.textGameResolutionScaleSummary);
        switchForceFullscreenMode = root.findViewById(R.id.switchForceFullscreenMode);
        switchIgnoreDisplayCutout = root.findViewById(R.id.switchIgnoreDisplayCutout);
        switchAvoidRoundedCorners = root.findViewById(R.id.switchAvoidRoundedCorners);
        switchUseSystemVulkanDriver = root.findViewById(R.id.switchUseSystemVulkanDriver);
        switchUseOpenGlFor26Plus = root.findViewById(R.id.switchUseOpenGlFor26Plus);
        textRendererPluginConfig = root.findViewById(R.id.textRendererPluginConfig);
        buttonGrantRendererStorageAccess = root.findViewById(R.id.buttonGrantRendererStorageAccess);
        buttonRefreshRenderers = root.findViewById(R.id.buttonRefreshRenderers);
        buttonImportRendererPlugin = root.findViewById(R.id.buttonImportRendererPlugin);
        buttonClearRendererPluginCache = root.findViewById(R.id.buttonClearRendererPluginCache);
        textRendererDescription = root.findViewById(R.id.textRendererDescription);

        // Controller
        cardControllerSettings = root.findViewById(R.id.cardControllerSettings);
        layoutControllerSettings = root.findViewById(R.id.layoutControllerSettings);
        buttonEditBuiltInController = root.findViewById(R.id.buttonEditBuiltInController);
        switchTouchControlsEnabled = root.findViewById(R.id.switchTouchControlsEnabled);
        switchMinecraftTouchGestures = root.findViewById(R.id.switchMinecraftTouchGestures);
        textMinecraftTouchGesturesSummary = root.findViewById(R.id.textMinecraftTouchGesturesSummary);
        switchDoubleTapToDrop = root.findViewById(R.id.switchDoubleTapToDrop);
        textDoubleTapToDropSummary = root.findViewById(R.id.textDoubleTapToDropSummary);
        buttonManageTouchControls = root.findViewById(R.id.buttonManageTouchControls);
        textMouseCursorIconSummary = root.findViewById(R.id.textMouseCursorIconSummary);
        buttonMouseCursorIconSettings = root.findViewById(R.id.buttonMouseCursorIconSettings);
        switchForceSdlControllerBridge = root.findViewById(R.id.switchForceSdlControllerBridge);

        // Launcher
        cardLauncherSettings = root.findViewById(R.id.cardLauncherSettings);
        textAllocatedRam = root.findViewById(R.id.textAllocatedRam);
        sliderAllocatedRam = root.findViewById(R.id.sliderAllocatedRam);
        textAvailableRamSummary = root.findViewById(R.id.textAvailableRamSummary);
        checkKeepLogs = root.findViewById(R.id.checkKeepLogs);
        switchInstallNotifications = root.findViewById(R.id.switchInstallNotifications);
        textInstallNotificationsSummary = root.findViewById(R.id.textInstallNotificationsSummary);
        textSimpleVoiceChatMicrophoneStatus = root.findViewById(R.id.textSimpleVoiceChatMicrophoneStatus);
        buttonSimpleVoiceChatMicrophonePermission = root.findViewById(R.id.buttonSimpleVoiceChatMicrophonePermission);
        switchShowInGameSettingsButton = root.findViewById(R.id.switchShowInGameSettingsButton);
        switchShowGameLogOverlay = root.findViewById(R.id.switchShowGameLogOverlay);
        buttonShareLatestLog = root.findViewById(R.id.buttonShareLatestLog);

        // Instance
        cardInstanceSettings = root.findViewById(R.id.cardInstanceSettings);
        switchShowSharedInstalls = root.findViewById(R.id.switchShowSharedInstalls);
        switchRemoveInheritedVanilla = root.findViewById(R.id.switchRemoveInheritedVanilla);
        textFolder = root.findViewById(R.id.textFolder);
        textDroidBridgeBackupSummary = root.findViewById(R.id.textDroidBridgeBackupSummary);
        buttonBackupDroidBridgeData = root.findViewById(R.id.buttonBackupDroidBridgeData);
        buttonRestoreDroidBridgeData = root.findViewById(R.id.buttonRestoreDroidBridgeData);

        // Privacy
        cardPrivacyPolicySettings = root.findViewById(R.id.cardPrivacyPolicySettings);
        textPrivacyPolicySummary = root.findViewById(R.id.textPrivacyPolicySummary);
        buttonOpenMinecraftEula = root.findViewById(R.id.buttonOpenMinecraftEula);
        buttonOpenPrivacyPolicy = root.findViewById(R.id.buttonOpenPrivacyPolicy);
        buttonOpenDroidBridgeTerms = root.findViewById(R.id.buttonOpenDroidBridgeTerms);
        buttonOpenDroidBridgeLicense = root.findViewById(R.id.buttonOpenDroidBridgeLicense);

        // Top bar
        cardSettingsTabsHeader = root.findViewById(R.id.cardSettingsTabsHeader);
        buttonSettingsBack = root.findViewById(R.id.buttonSettingsBack);
        textSettingsHeaderTitle = root.findViewById(R.id.textSettingsHeaderTitle);
        settingsSectionTabs = root.findViewById(R.id.settingsSectionTabs);

        // Scrim
        settingsTopContentScrim = root.findViewById(R.id.settingsTopContentScrim);
    }

    @NonNull
    public static ActivityLauncherSettingsBinding inflate(@NonNull LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.activity_launcher_settings, null, false);
        return new ActivityLauncherSettingsBinding(root);
    }

    @NonNull
    public View getRoot() {
        return root;
    }
}
