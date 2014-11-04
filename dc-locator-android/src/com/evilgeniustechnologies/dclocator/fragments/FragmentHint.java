package com.evilgeniustechnologies.dclocator.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

public class FragmentHint extends Fragment implements OnClickListener {
    TextView tvHint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_search_fragment_result, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        View view = inflater.inflate(R.layout.fragment_hint, container, false);
        tvHint = (TextView) view.findViewById(R.id.tv_hint);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_search:
                FragmentManager trans = getActivity().getFragmentManager();
                trans.popBackStack();
                break;
        }

    }

}
