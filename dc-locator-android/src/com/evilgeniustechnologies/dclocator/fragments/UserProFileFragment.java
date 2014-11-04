package com.evilgeniustechnologies.dclocator.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.models.Location;
import com.evilgeniustechnologies.dclocator.service.ParseService;

public class UserProFileFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "EGT.UserProFileFragment";

    ImageView avatar, dcAvar, iv_back_home, iv_go_chat;
    TextView nameLabel, exLabel, webSiteLabel, TwitLabel, SkypeLabel, tv_name,
            tv_ex, tv_web, tv_twit, tv_skype, tv_name_user;
    Member member;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_home_user_profile_fragment, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
        iv_back_home = (ImageView) v.findViewById(R.id.btn_back_home_profile);
        tv_name_user = (TextView) v.findViewById(R.id.tv_user_name_home_user);
        tv_name_user.setSelected(true);
        iv_back_home.setOnClickListener(this);

        iv_go_chat = (ImageView) v.findViewById(R.id.right_chats_home_profile);
        iv_go_chat.setOnClickListener(this);
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

        String id = getArguments().getString("id");
        member = datastore.getMemberDao().load(id);
        tv_name.setText(member.getFullName());
        tv_ex.setText(member.getSkill());
        tv_skype.setText(member.getSkypeId());
        tv_twit.setText(member.getTwitter());
        tv_web.setText(member.getWebsite());
        if (!TextUtils.isEmpty(member.getAvatar())) {
            Picasso.with(getActivity()).load(member.getAvatar()).into(avatar);
        } else {
            avatar.setImageResource(R.drawable.user_default);
        }
        tv_name_user.setText(member.getFullName());
        tv_web.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //  intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(String.format("http://%s", tv_web.getText().toString())));
                getActivity().startActivity(intent);
            }
        });

        Location location = datastore.getCurrentParseUser().getLastCheckIn();
        if (location != null) {
            try {
                location.fetch();
                if (!TextUtils.isEmpty(location.getFullAddress())) {
                    ((TextView) view.findViewById(R.id.last_seen)).setText("Last seen at " + location.getFullAddress());
                }
            } catch (ParseException e) {
                Log.e(TAG, "lastCheckIn", e);
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_home_profile:
                FragmentManager trans = getActivity()
                        .getFragmentManager();
                trans.popBackStackImmediate();
                break;
            case R.id.right_chats_home_profile:
                MessageGroup selectedGroup = datastore.getPrivateGroup(member.getObjectId());
                if (member.getAvailable()) {
                    if (selectedGroup != null) {
                        ParseService.startActionParseAllMessages(getActivity(), selectedGroup.getObjectId());
                        ConversationFragment fragment = new ConversationFragment(selectedGroup);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        ConversationFragment fragment = new ConversationFragment(member);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "User has not downloaded the app", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
                break;
        }
    }
}