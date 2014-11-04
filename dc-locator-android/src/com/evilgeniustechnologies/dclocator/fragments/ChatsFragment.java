package com.evilgeniustechnologies.dclocator.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.evilgeniustechnologies.dclocator.adapters.GroupAdapter;
import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.service.ParseService;

public class ChatsFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "EGT.ChatsFragment";

    private RelativeLayout rlCreateGroup;
    private TextView createGroup;
    private TextView tv_edit;
    private ListView listHistory;
    private ImageView sendMessage;
    private ArrayAdapter<MessageGroup> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "start onCreateView");

        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        View v = inflater.inflate(R.layout.navigation_chats_fragment, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tv_edit = (TextView) v.findViewById(R.id.tv_edit);
        sendMessage = (ImageView) v.findViewById(R.id.right_chats_fragment);
        rlCreateGroup = (RelativeLayout) view.findViewById(R.id.rl_create_group);
        createGroup = (TextView) view.findViewById(R.id.createGroup);
        listHistory = (ListView) view.findViewById(R.id.lv_tab_chat);
        createGroup.setTextSize((int) (Config.screenWidth * 0.05));
        sendMessage.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        createGroup.setOnClickListener(this);
        ((MainViewActivity) getActivity()).ShowTabbar();
        ParseService.startActionParseAllGroups(getActivity());
        Log.e(TAG, "end onCreateView");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                break;
            case R.id.right_chats_fragment:
                AllPeopleChatFragment all = new AllPeopleChatFragment();
                FragmentTransaction tran = getActivity()
                        .getFragmentManager().beginTransaction();
                tran.replace(R.id.fragment_container, all);
                tran.addToBackStack(null);
                tran.commit();
                break;
            case R.id.createGroup:
                NewGroupFragment fragment = new NewGroupFragment(getActivity());
                FragmentTransaction transaction = getActivity()
                        .getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    protected void onReceived(String status) {
        super.onReceived(status);
        if (Datastore.ALL_MESSAGE_GROUPS_PARSED.equals(status)) {
            ((MainViewActivity) getActivity()).updateCountNotification(datastore.getCurrentUser().getTotalUnreadMessages());
            allMessageGroupsParsed();
        } else if (Datastore.NOTIFICATION_RECEIVED.equals(status)) {
            ParseService.startActionParseAllGroups(getActivity());
        }
    }

    private void allMessageGroupsParsed() {
        adapter = new GroupAdapter(getActivity(), datastore.getAllUserGroups());
        adapter.notifyDataSetChanged();
        listHistory.setAdapter(adapter);
        listHistory.setOnItemClickListener(((GroupAdapter) adapter));
    }
}