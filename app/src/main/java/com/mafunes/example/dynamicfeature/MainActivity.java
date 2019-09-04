package com.mafunes.example.dynamicfeature;

import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.splitinstall.SplitInstallHelper;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.mafunes.example.sdk.BaseSplitActivity;

public class MainActivity extends BaseSplitActivity {

    private final String dynamicActivity;
    private SplitInstallManager splitInstallManager;
    private String moduleName;
    private SplitInstallStateUpdatedListener listener;
    private Snackbar snackbar;

    public MainActivity() {
        dynamicActivity = "com.mafunes.example.dynamicfeaturetest.DynamicMainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splitInstallManager = SplitInstallManagerFactory.create(this);
        moduleName = getString(R.string.title_dynamicfeature);
        createSplitListener();
        findViewById(R.id.button).setOnClickListener(
                listener -> loadAndLaunchModule(moduleName));
    }

    @Override
    public void onResume() {
        splitInstallManager.registerListener(listener);
        super.onResume();
    }

    @Override
    public void onPause() {
        // Make sure to dispose of the listener once it's no longer needed.
        splitInstallManager.unregisterListener(listener);
        super.onPause();
    }

    /* default */ void loadAndLaunchModule(final String moduleName) {
        if (splitInstallManager.getInstalledModules().contains(moduleName)) {
            //updateProgressMessage("Already installed");
            onSuccessfulLoad(moduleName, true);
            return;
        }

        // Creates a request to install a module.
        final SplitInstallRequest request =
                SplitInstallRequest
                        .newBuilder()
                        // You can download multiple on demand modules per
                        // request by invoking the following method for each
                        // module you want to install.
                        .addModule(moduleName)
                        .build();

        splitInstallManager
                .startInstall(request)
                .addOnSuccessListener(integer -> updateProgressMessage("Please wait download in progress"))
                .addOnFailureListener(e -> {
                    updateProgressMessage("Error:" + e.getMessage() + " for module " + moduleName);
                    e.printStackTrace();
                });

    }

    private void createSplitListener() {
        /** Listener used to handle changes in state for install requests. */
        listener = state -> {
            Boolean multiInstall = state.moduleNames().size() > 1;
            for (String name : state.moduleNames()) {
                // Handle changes in state.
                switch (state.status()) {
                    case SplitInstallSessionStatus.DOWNLOADING:
                        //  In order to see this, the application has to be uploaded to the Play Store.
                        //updateProgressMessage("Downloading " + name + " "  + state.totalBytesToDownload() + " " + state.bytesDownloaded());
                        break;
                    case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                        /*
                          This may occur when attempting to download a sufficiently large module.
                          In order to see this, the application has to be uploaded to the Play Store.
                          Then features can be requested until the confirmation path is triggered.
                         */
                        try {
                            startIntentSender(state.resolutionIntent() != null ? state.resolutionIntent().getIntentSender() : null, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("TAG", "Error on startIntentSender", e);
                        }


                        break;
                    case SplitInstallSessionStatus.INSTALLED:
                        // You need to perform the following only for Android Instant Apps
                        // running on Android 8.0 (API level 26) and higher.
                        if (BuildCompat.isAtLeastO()) {
                            // Updates the app’s context with the code and resources of the
                            // installed module.
                            SplitInstallHelper.updateAppInfo(this);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    onSuccessfulLoad(name, !multiInstall);
                                }
                            });
                        }
                        break;
                    case SplitInstallSessionStatus.INSTALLING:
                        //updateProgressMessage("Installing " + name);
                        break;
                    case SplitInstallSessionStatus.FAILED:
                        updateProgressMessage("Error: " + state.errorCode() + "for module " + state.moduleNames());
                        break;
                }
            }
        };
    }

    private void onSuccessfulLoad(String nameModule, boolean launch) {
        try {
            this.startActivity(new Intent().setClassName(getPackageName(), dynamicActivity));
        } catch (Exception e) {
            updateProgressMessage(e.getMessage());
        }
        finish();
    }

    private void updateProgressMessage(final String message) {
        snackbar = Snackbar.make(findViewById(R.id.content_main), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
