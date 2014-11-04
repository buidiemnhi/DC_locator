package com.evilgeniustechnologies.dclocator.fragments;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

import java.util.List;

import com.evilgeniustechnologies.dclocator.adapters.ListPersonAdapter;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MemberDao;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.service.ParseService;
import com.evilgeniustechnologies.dclocator.utils.DialogManager;

public class AllPeopleChatFragment extends BaseFragment implements
        ListPersonAdapter.OnContactSelectListener {
    private static final String TAG = "EGT.AllPeopleChatFragment";

    private ListPersonAdapter adapter;
    private ListView lv_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);

        View v = inflater.inflate(R.layout.navigation_chat_fragment_search_member, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        View view = inflater.inflate(R.layout.all_member_list, container, false);
        lv_all = (ListView) view.findViewById(R.id.lv_all_member);
        TextView cancel = (TextView) v.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        attachSearchListener(v);

        ParseService.startActionParseAllMembers(getActivity());
        return view;
    }

    @Override
    public void onClick(String userId, String name) {
        DialogManager.showProgress(getActivity());
        MessageGroup selectedGroup = datastore.getPrivateGroup(userId);
        DialogManager.closeProgress();
        if (selectedGroup != null) {
            ParseService.startActionParseAllMessages(getActivity(), selectedGroup.getObjectId());
            ConversationFragment fragment = new ConversationFragment(selectedGroup);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Member anotherMember = datastore.getMemberDao().load(userId);
            ConversationFragment fragment = new ConversationFragment(anotherMember);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void attachSearchListener(View view) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) view.findViewById(R.id.search_bar_navi);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }

    @Override
    protected void onReceived(String status) {
        super.onReceived(status);
        if (Datastore.ALL_MEMBERS_PARSED.equals(status)) {
            List<Member> members = datastore.getMemberDao()
                    .queryBuilder()
                    .orderAsc(MemberDao.Properties.FullName)
                    .where(MemberDao.Properties.ObjectId.notEq(datastore.getCurrentUser().getObjectId()))
                    .list();
            adapter = new ListPersonAdapter(getActivity(), members,1,null);
            adapter.setOnContactSelectListener(AllPeopleChatFragment.this);
            lv_all.setAdapter(adapter);
        }
    }
}