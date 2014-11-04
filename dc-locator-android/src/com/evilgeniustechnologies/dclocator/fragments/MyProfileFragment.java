package com.evilgeniustechnologies.dclocator.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.squareup.picasso.Picasso;

import com.evilgeniustechnologies.dclocator.daos.Location;
import com.evilgeniustechnologies.dclocator.daos.Member;

public class MyProfileFragment extends BaseFragment {
    private static final String TAG = "EGT.MyProfileFragment";
    ImageView avatar, dcAvar;
    TextView nameLabel, exLabel, webSiteLabel, TwitLabel, SkypeLabel, tv_name,
            tv_ex, tv_web, tv_twit, tv_skype;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_my_profile, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
        avatar = (ImageView) view.findViewById(R.id.my_avatar);
        dcAvar = (ImageView) view.findViewById(R.id.dc_avatar);
        nameLabel = (TextView) view.findViewById(R.id.tv_lable_name_profile);
        exLabel = (TextView) view.findViewById(R.id.tv_lable_expertise_profile);
        webSiteLabel = (TextView) view.findViewById(R.id.tv_lable_website_profile);
        TwitLabel = (TextView) view.findViewById(R.id.tv_label_twitter_profile);
        SkypeLabel = (TextView) view.findViewById(R.id.tv_lable_skype_profile);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_ex = (TextView) view.findViewById(R.id.tv_expertise);
        tv_web = (TextView) view.findViewById(R.id.tv_website);
        tv_twit = (TextView) view.findViewById(R.id.tv_twitter);
        tv_skype = (TextView) view.findViewById(R.id.tv_skype);
        tv_web.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(String.format("http://%s", tv_web.getText().toString())));
                getActivity().startActivity(intent);
            }
        });

        Member member = datastore.getCurrentUser();
        tv_name.setText(member.getFullName());
        tv_name.setSelected(true);
        tv_ex.setText(member.getSkill());
        tv_ex.setSelected(true);
        tv_skype.setText(member.getSkypeId());
        tv_skype.setSelected(true);
        tv_twit.setText(member.getTwitter());
        tv_twit.setSelected(true);
        tv_web.setText(member.getWebsite());
        tv_web.setSelected(true);
        if (!TextUtils.isEmpty(member.getAvatar())) {
            Picasso.with(this.getActivity()).load(member.getAvatar()).into(avatar);
        } else {
            avatar.setImageResource(R.drawable.user_default);
        }

        Location location = datastore.getCurrentUserLastCheckIn();
        if (location != null && !TextUtils.isEmpty(location.getFullAddress())) {
            ((TextView) view.findViewById(R.id.last_seen)).setText("Last seen at " + location.getFullAddress());
        }

        return view;
    }
}