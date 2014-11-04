package com.evilgeniustechnologies.dclocator.dc_locator;

import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.utils.DialogManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by benjamin on 10/5/14.
 */
public abstract class BaseActivity extends Activity implements Datastore.Observer {
    private static final String TAG = "EGT.BaseActivity";
    protected Datastore datastore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (datastore == null) {
            datastore = Datastore.getInstance(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        datastore.setObserver(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        datastore.setObserver(this);
    }

    @Override
    public void onNotified(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                display(status);
            }
        });
    }

    private void display(String status) {
        if (status.equals(Datastore.PREPARING)) {
            DialogManager.showProgress(this, datastore.getProgress());
            Log.e(TAG, datastore.getProgress() + "");
        } else if (status.equals(Datastore.DATABASE_READY)) {
            DialogManager.closeProgress();
            onReceived(status);
        } else {
            onReceived(status);
        }
    }

    protected void onReceived(String status) {
        // Concrete activities may override here
    }
}
