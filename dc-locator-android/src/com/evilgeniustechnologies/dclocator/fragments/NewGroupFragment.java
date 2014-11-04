package com.evilgeniustechnologies.dclocator.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;

/**
 * Created by benjamin on 10/3/14.
 */
public class NewGroupFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EGT.NewGroupFragment";
    private EditText groupName;
    Activity activity;
    TextView tv_cancel_new_group, tv_next;

    public NewGroupFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_new_group, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        View view = inflater.inflate(R.layout.new_group, container, false);
        tv_cancel_new_group = (TextView) v.findViewById(R.id.tv_cancel_new_group);
        tv_next = (TextView) v.findViewById(R.id.tv_next);
        TextView addPhoto = (TextView) view.findViewById(R.id.add_photo);
        groupName = (EditText) view.findViewById(R.id.add_group_name);
        ((MainViewActivity) getActivity()).HideTabbar();
        tv_cancel_new_group.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        addPhoto.setOnClickListener(this);

        return view;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() == null) {
            inputManager.hideSoftInputFromWindow(null,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            inputManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClicked");
        switch (v.getId()) {
            case R.id.tv_next:

                hideKeyboard();

                String groupNameText = groupName.getText().toString();
                if (!TextUtils.isEmpty(groupNameText)) {
                    // Go to Add Contact fragment
                    AddContactFragment all = new AddContactFragment(groupNameText);
                    FragmentTransaction tran = getActivity()
                            .getFragmentManager().beginTransaction();
                    tran.replace(R.id.fragment_container, all);
                    tran.addToBackStack(null);
                    tran.commit();
                }
                break;
            case R.id.tv_cancel_new_group:

                hideKeyboard();

                FragmentManager trans = getActivity()
                        .getFragmentManager();
                trans.popBackStackImmediate();
                break;
            case R.id.add_photo:
                // Browse for photo
                break;
        }
    }
}