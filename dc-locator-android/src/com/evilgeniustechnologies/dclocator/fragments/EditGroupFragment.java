package com.evilgeniustechnologies.dclocator.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.ParseFile;

import com.evilgeniustechnologies.dclocator.adapters.ListPersonAdapter;
import com.evilgeniustechnologies.dclocator.daos.EntityConnection;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.models.Member;
import com.evilgeniustechnologies.dclocator.service.ParseService;
import com.evilgeniustechnologies.dclocator.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditGroupFragment extends BaseFragment implements OnClickListener {

	public EditGroupFragment(MessageGroup group) {
		this.group = group;
	}

	public void OnEditGroupListener(OnEditGroupListener listenner) {
		this.listener = listenner;
	}

	ArrayList<Member> memberList;
	ImageView back;
	public static final int IMAGE = 1;
	TextView Navi_name, count_member;
	TextView save;
	MessageGroup group;
	RelativeLayout add_photo;
	TextView editGroupName, add_edit_member, leave_group, delete_group;
	ListView ListMemberGroup;
	ListPersonAdapter personGroups;
	OnEditGroupListener listener;
	List<com.evilgeniustechnologies.dclocator.daos.Member> memberConnecttion;
	List<EntityConnection> connect;
	private Intent uploadData;
	ParseFile icon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

		View v = inflater.inflate(R.layout.navigation_edit_group, null);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		getActivity().getActionBar().setCustomView(v);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		View view = inflater.inflate(R.layout.edit_group, null);
		back = (ImageView) v.findViewById(R.id.btn_back_edit_group);
		back.setOnClickListener(this);
		Navi_name = (TextView) v.findViewById(R.id.tv_name_group_edit);
		Navi_name.setText("Group Detail");
		save = (TextView) v.findViewById(R.id.tv_save);
		save.setOnClickListener(this);
		add_photo = (RelativeLayout) view
				.findViewById(R.id.add_photo_container_edit_group);
		ListMemberGroup = (ListView) view.findViewById(R.id.list_member_group);
		add_photo.setOnClickListener(this);
		editGroupName = (EditText) view.findViewById(R.id.tv_name_group);
		editGroupName.setText(group.getName());
		add_edit_member = (TextView) view.findViewById(R.id.add_edit_member);
		add_edit_member.setOnClickListener(this);
		leave_group = (TextView) view.findViewById(R.id.leave_group);
		leave_group.setOnClickListener(this);
		delete_group = (TextView) view.findViewById(R.id.tv_delete_group);
		delete_group.setOnClickListener(this);
		count_member = (TextView) view.findViewById(R.id.count_member);
		connect = new ArrayList<EntityConnection>();
		connect = group.getMemberConnections();
		if (group.getOwnerId().equals(
				Datastore.getInstance().getCurrentUserId())) {
			delete_group.setVisibility(View.VISIBLE);
		} else {
			delete_group.setVisibility(View.GONE);
		}
		memberConnecttion = new ArrayList<com.evilgeniustechnologies.dclocator.daos.Member>();
		for (EntityConnection entityConnection : connect) {
			memberConnecttion.add(entityConnection.getMember());
		}
		count_member.setText(String.format("Members :%s",
				memberConnecttion.size()));

		personGroups = new ListPersonAdapter(getActivity(), memberConnecttion,
				0, group);
		ListMemberGroup.setAdapter(personGroups);

		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.add_edit_member:
			AddContactFragment all = new AddContactFragment(group,
					editGroupName.getText().toString());
			FragmentTransaction tran = getActivity().getFragmentManager()
					.beginTransaction();
			tran.replace(R.id.fragment_container, all);
			tran.addToBackStack(null);
			tran.commit();
			break;

		case R.id.leave_group:
			personGroups.remove(Datastore.getInstance().getCurrentUser());
			Utils.CloudCodeEditGroup(getActivity(), editGroupName.getText()
					.toString(), null, personGroups.getObjectIds(), group
					.getObjectId());
			ParseService.startActionParseAllGroups(getActivity());
			getFragmentManager().popBackStack();
			getFragmentManager().popBackStack();
			
			break;
			case R.id.tv_delete_group:
				Iterator<com.evilgeniustechnologies.dclocator.daos.Member> iter= memberConnecttion.iterator();
				while (iter.hasNext()) {
					com.evilgeniustechnologies.dclocator.daos.Member member = (com.evilgeniustechnologies.dclocator.daos.Member) iter.next();
					iter.remove();
				}
				personGroups= new ListPersonAdapter(getActivity(), memberConnecttion, 0, group);
				
				Utils.CloudCodeEditGroup(getActivity(), editGroupName.getText()
						.toString(), null, personGroups.getObjectIds(), group
						.getObjectId());

				getFragmentManager().popBackStack();
				getFragmentManager().popBackStack();
				ParseService.startActionParseAllGroups(getActivity());
				break;
		case R.id.tv_save:
			Utils.CloudCodeEditGroup(getActivity(), editGroupName.getText()
					.toString(), null, personGroups.getObjectIds(), group
					.getObjectId());

		    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
					// Setting Dialog Title
					builder1.setTitle("Group Update");
					builder1.setCancelable(true);
					builder1.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert11 = builder1.create();
					alert11.show();

			// com.evilgeniustechnologies.dclocator.commons.Config.GroupName=
			// editGroupName.getText().toString();
			break;
			case R.id.add_photo_container_edit_group:
				Utils.onUploadImage(getActivity(), IMAGE);
				break;
		default:
			break;
		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				uploadData = data;
				
			ParseService.startActionUploadImage(getActivity(),
						uploadData.getData(), 1);
			}
		}
	}

	public interface OnEditGroupListener {
		public void OnEdit(String newName);
	}
}
