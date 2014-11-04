package com.evilgeniustechnologies.dclocator.fragments;

import com.evilgeniustechnologies.dclocator.local.Datastore;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by benjamin on 10/12/14.
 */
public abstract class BaseFragment extends Fragment implements Datastore.FragmentObserver {
    private static final String TAG = "EGT.BaseFragment";
    protected Datastore datastore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (datastore == null) {
            datastore = Datastore.getInstance();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        datastore.setFragmentObserver(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        datastore.setFragmentObserver(this);
    }

    @Override
    public void onNotified(final String status) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onReceived(status);
            }
        });
    }

    protected void onReceived(String status) {
        Log.e(TAG, status);
        // Override here
    }
}