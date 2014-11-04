package com.evilgeniustechnologies.dclocator.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by benjamin on 10/5/14.
 */
public class DialogManager {
    private static ProgressDialog progress;

    public static void showProgress(Context context) {
        if (progress == null) {
            progress = new ProgressDialog(context);
            progress.setMessage("Loading...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    public static void showProgress(Context context, String message) {
        if (progress == null) {
            progress = new ProgressDialog(context);
            progress.setMessage(message);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    public static void showProgress(Context context, int newProgress) {
        if (progress == null) {
            progress = new ProgressDialog(context);
            progress.setMessage("Updating");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setProgress(0);
            progress.setMax(100);
            progress.setCancelable(false);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
        progress.setProgress(newProgress);
    }

    public static void closeProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }
}
