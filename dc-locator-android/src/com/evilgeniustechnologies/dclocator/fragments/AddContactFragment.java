package com.evilgeniustechnologies.dclocator.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

import java.util.List;

import com.evilgeniustechnologies.dclocator.adapters.ContactAdapter;
import com.evilgeniustechnologies.dclocator.adapters.ListPersonAdapter;
import com.evilgeniustechnologies.dclocator.daos.EntityConnection;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MemberDao;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.service.ParseService;
import com.evilgeniustechnologies.dclocator.utils.Utils;

/**
 * Created by benjamin on 10/3/14.
 */
public class AddContactFragment extends BaseFragment implements
		ListPersonAdapter.OnContactSelectListener, View.OnClickListener,
		ContactAdapter.OnContactRemoveListener{
	private static final String TAG = "EGT.AddContactFragment";
	private ListView searchListView;
	private ListView contactListView;
	private TextView tv_create;
	private MessageGroup groups;
	private TextView tvAddContact, discription, tv_cancel;
	private ListPersonAdapter adapter;
	private ContactAdapter contactAdapter;
	private String groupName;
	private RelativeLayout currentGroup, available;
	List<com.evilgeniustechnologies.dclocator.daos.Member> memberConnecttion;
	private String query;
	private OnEditinAddContact lisAddContact;
public void onEditAddContacts(OnEditinAddContact listener) {
	this.lisAddContact= listener;
	
}
	public AddContactFragment(String groupName) {
		this.groupName = groupName;
	}

	public AddContactFragment(MessageGroup group, String GroupName) {
		this.groups = group;
		this.groupName = GroupName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

		View v = inflater.inflate(R.layout.navigation_add_contact, null);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		getActivity().getActionBar().setCustomView(v);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);

		getActivity().getActionBar().getCustomView()
				.findViewById(R.id.tv_back_new_group).setOnClickListener(this);
		tv_create = (TextView) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.tv_create);
		tv_create.setOnClickListener(this);
		tv_create.setTextColor(getActivity().getResources().getColor(
				android.R.color.darker_gray));
		tvAddContact = (TextView) v.findViewById(R.id.tv_add_contact);

		if (groups != null) {
			tvAddContact.setText("Group detail");
		} else {
			tvAddContact.setText(groupName);
		}

		View view = inflater.inflate(R.layout.add_contact, container, false);
		available = (RelativeLayout) view
				.findViewById(R.id.rl_available_to_add);
		available.setVisibility(View.VISIBLE);
		discription = (TextView) view.findViewById(R.id.discription);
		currentGroup = (RelativeLayout) view
				.findViewById(R.id.rl_current_group);
		currentGroup.setVisibility(View.GONE);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel_search);
		tv_cancel.setOnClickListener(this);
		searchListView = (ListView) view
				.findViewById(R.id.add_contact_list_view_search);
		contactListView = (ListView) view
				.findViewById(R.id.add_contact_list_view_contacts);
		contactAdapter = new ContactAdapter(getActivity());
		List<EntityConnection> connect = groups.getMemberConnections();
		for (EntityConnection entityConnection : connect) {
			Member member = entityConnection.getMember();
			contactAdapter.addItem(new Utils.Tuple(member.getObjectId(), member
					.getFullName()));
			Log.e(TAG,
					"contactAdapter" + member.getObjectId()
							+ member.getFullName());
		}
		contactListView.setAdapter(contactAdapter);
		if (contactAdapter != null) {
			searchListView.setVisibility(View.GONE);
			contactListView.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) contactListView
					.getLayoutParams();
			tmp.addRule(RelativeLayout.BELOW, available.getId());
		}
		// addRule();
		attachSearchListener(view);
		ParseService.startActionParseAllMembers(getActivity());
		return view;
	}

	public void attachSearchListener(View view) {
		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) view
				.findViewById(R.id.add_contact_search_view);

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				if (adapter != null) {
					searchListView.setVisibility(View.VISIBLE);
					contactListView.setVisibility(View.GONE);
					available.setVisibility(View.VISIBLE);
					adapter.getFilter().filter(newText);
				}
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				if (adapter != null) {
					searchListView.setVisibility(View.VISIBLE);
					contactListView.setVisibility(View.GONE);
					adapter.getFilter().filter(query);
				}
				return true;
			}
		};
		SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				searchListView.setVisibility(View.GONE);
				contactListView.setVisibility(View.VISIBLE);
				return true;
			}
		};
		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchListView.setVisibility(View.VISIBLE);
				contactListView.setVisibility(View.GONE);
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);
		searchView.setOnCloseListener(closeListener);
		searchView.setOnSearchClickListener(clickListener);
	}

	@Override
	public void onClick(String userId, String name) {
		if (contactAdapter == null) {
			contactAdapter = new ContactAdapter(getActivity());
			contactListView.setAdapter(contactAdapter);
			if (groups != null
					&& groups.getOwnerId().equals(
							datastore.getInstance().getCurrentUserId())) {
				contactAdapter.setOnContactRemoveListener(this);
			} else {
				contactAdapter.setOnContactRemoveListener(null);
			}
		}
		contactAdapter.addItem(new Utils.Tuple(userId, name));
		searchListView.setVisibility(View.GONE);
		contactListView.setVisibility(View.VISIBLE);
		if (contactAdapter.getContents().size() >= 1) {
			tv_create.setTextColor(getActivity().getResources().getColor(
					R.color.blue_text));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel_search:
			hideKeyboard();
			searchListView.setVisibility(View.GONE);
			contactListView.setVisibility(View.VISIBLE);
			available.setVisibility(View.GONE);
			currentGroup.setVisibility(View.VISIBLE);
			
			RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) contactListView
					.getLayoutParams();
			tmp.addRule(RelativeLayout.BELOW, currentGroup.getId());
			RelativeLayout.LayoutParams tmp1 = (RelativeLayout.LayoutParams) discription
					.getLayoutParams();
			tmp1.addRule(RelativeLayout.BELOW, contactListView.getId());
			break;
		case R.id.tv_back_new_group:
			getFragmentManager().popBackStackImmediate();
			break;
		case R.id.tv_create:
			Log.e(TAG, "create Group");

			hideKeyboard();
			if (groups == null) {
				Utils.CloudCodeCreateGroup(getActivity(), groupName, null,
						contactAdapter.getObjectIds());
				getFragmentManager().popBackStack();
				getFragmentManager().popBackStack();
			} else {
				Utils.CloudCodeEditGroup(getActivity(), groupName, null,
						contactAdapter.getObjectIds(), groups.getObjectId());
				lisAddContact.OnEditAddContact(groupName);
				getFragmentManager().popBackStack();
				
			
			}

			
			break;
		}
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
	public void onRemove(int size) {
		if (contactAdapter.getContents().size() < 2) {
			tv_create.setTextColor(getActivity().getResources().getColor(
					android.R.color.darker_gray));
		}
	}

	public void addRule() {
		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) contactListView
				.getLayoutParams();
		tmp.addRule(RelativeLayout.BELOW, available.getId());
		tmp.addRule(RelativeLayout.BELOW, currentGroup.getId());

	}

	@Override
	protected void onReceived(String status) {
		super.onReceived(status);
		if (Datastore.ALL_MEMBERS_PARSED.equals(status)) {
			List<Member> members = datastore
					.getMemberDao()
					.queryBuilder()
					.orderAsc(MemberDao.Properties.FullName)
					.where(MemberDao.Properties.ObjectId.notEq(datastore
							.getCurrentUser().getObjectId())).list();
			adapter = new ListPersonAdapter(getActivity(), members, 0, null);
			adapter.setOnContactSelectListener(AddContactFragment.this);
			searchListView.setAdapter(adapter);
		}
	}
public interface OnEditinAddContact{
		public void OnEditAddContact(String Newname);
		}
}