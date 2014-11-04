package com.evilgeniustechnologies.dclocator.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

public class TabbarFragment extends Fragment {
    LinearLayout tabbar;
    ImageView home, search, chat, profile;
    RelativeLayout tab_noti, rl_tab_home, rl_tab_chat, rl_tab_search, rl_my_profile;
    TextView countNoti;
    Boolean isVisible;
    Activity activity;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabbar_fragment, container, false);
    }

//    public TabbarFragment(Boolean isVisible) {
//        this.isVisible = isVisible;
//    }
}
