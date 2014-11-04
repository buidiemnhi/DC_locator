package com.evilgeniustechnologies.dclocator.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.evilgeniustechnologies.dclocator.adapters.ChatListAdapter;
import com.evilgeniustechnologies.dclocator.daos.EntityConnectionDao;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.Message;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.daos.ReadConnection;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.service.ParseService;
import com.evilgeniustechnologies.dclocator.utils.DialogManager;
import com.evilgeniustechnologies.dclocator.utils.Utils;

public class ConversationFragment extends BaseFragment implements
		OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
	private static final String TAG = "EGT.ConversationFragment";
	private ListView Chat;
	private ChatListAdapter adapter;
	private TextView send, tv_edit_group;
	private EditText etMessage;
	private ImageView back, sendImage;
	private TextView userNameLabel;
	private MessageGroup group;
	private Member anotherMember;
	private ImageView liked;
	private List<String> unreadIds;
	public static final int IMAGE = 1;
	private Intent uploadData;

	public ConversationFragment(Member anotherMember) {
		this.anotherMember = anotherMember;
	}

	public ConversationFragment(MessageGroup group) {
		this.group = group;
		unreadIds = new ArrayList<String>();
		List<Message> unreadMessages = group.getUnreadMessages();
		for (Message unreadMessage : unreadMessages) {
			unreadIds.add(unreadMessage.getObjectId());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

		View v = inflater.inflate(R.layout.navigation_chat_conversation, null);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		getActivity().getActionBar().setCustomView(v);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		back = (ImageView) v.findViewById(R.id.btn_conver_back);
		tv_edit_group = (TextView) v.findViewById(R.id.tv_edit_group);
		tv_edit_group.setOnClickListener(this);
		userNameLabel = (TextView) v
				.findViewById(R.id.tv_user_name_conversation);
		back.setOnClickListener(this);

		View view = inflater.inflate(R.layout.activity_chat, container, false);
		Chat = (ListView) view.findViewById(R.id.lvChat);
		Chat.setStackFromBottom(true);
		etMessage = (EditText) view.findViewById(R.id.etMessage);
		send = (TextView) view.findViewById(R.id.btSend);
		etMessage.addTextChangedListener(this);
		send.setOnClickListener(this);
		sendImage = (ImageView) view.findViewById(R.id.send_image);
		sendImage.setOnClickListener(this);
		sendImage.setVisibility(View.GONE);
		((MainViewActivity) getActivity()).HideTabbar();
		if (group != null) {
			if (group.getIsPrivate()) {
				Member otherMember = datastore
						.getEntityConnectionDao()
						.queryBuilder()
						.where(EntityConnectionDao.Properties.GroupId.eq(group
								.getObjectId()))
						.where(EntityConnectionDao.Properties.MemberId
								.notEq(datastore.getCurrentUser().getObjectId()))
						.unique().getMember();
				if (otherMember != null) {
					userNameLabel.setText(otherMember.getFullName());
				}
			} else {
				userNameLabel.setText(group.getName());
			}
		}
		userNameLabel.setSelected(true);
		back.setOnClickListener(this);

		if (group != null) {
			ParseService.startActionParseAllMessages(getActivity(),
					group.getObjectId());
		}
		
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		hideKeyboard();
	}

	private void markAsRead(final List<String> unreadIds) {
		Log.e(TAG, "call markAsRead");
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("member", datastore.getCurrentUserId());
		myMap.put("messages", unreadIds);
		ParseCloud.callFunctionInBackground("markAsRead", myMap,
				new FunctionCallback<Boolean>() {

					@Override
					public void done(Boolean arg0, ParseException arg1) {
						if (arg1 == null) {
							Log.e(TAG, "markAsRead called without error");
							if (arg0) {
								((MainViewActivity) getActivity())
										.updateCountNotification(((MainViewActivity) getActivity())
												.getCountNotification()
												- unreadIds.size());
								List<Message> unreadMessages = group
										.getUnreadMessages();
								Member user = datastore.getCurrentUser();
								for (Message unreadMessage : unreadMessages) {
									ReadConnection connection = new ReadConnection();
									connection.setReadUser(user);
									connection.setReadMessage(unreadMessage);
									datastore.getReadConnectionDao().insert(
											connection);
									unreadMessage.setUnreadGroupId(null);
									unreadMessage.update();
								}
								user.setTotalUnreadMessages(user
										.getTotalUnreadMessages()
										- unreadIds.size());
								datastore.refresh();
							}
						} else {
							Log.e(TAG, "markAsRead ParseCloud", arg1);
						}
					}
				});
	}

	@Override
	protected void onReceived(String status) {
		super.onReceived(status);
		if (Datastore.ALL_MESSAGES_PARSED.equals(status)) {
			if (unreadIds == null || !unreadIds.isEmpty()) {
				markAsRead(unreadIds);
			}
			adapter = new ChatListAdapter(getActivity(),
					datastore.getCurrentUser(), datastore.getAllMessages(group
							.getObjectId()));
			Chat.setAdapter(adapter);
			Chat.setOnItemClickListener(this);
		} else if (Datastore.NOTIFICATION_RECEIVED.equals(status)) {
			ParseService.startActionParseAllMessages(getActivity(),
					group.getObjectId());
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_edit_group:
			if (group.getIsPrivate()) {
				Toast toast = Toast.makeText(getActivity(),
						"private chat group can not be editted",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else {
				// TODO Fix edit group
				EditGroupFragment editGroupFragment = new EditGroupFragment(
						group);
				FragmentTransaction trans = getActivity().getFragmentManager()
						.beginTransaction();
				trans.replace(R.id.fragment_container, editGroupFragment);
				trans.addToBackStack(null);
				trans.commit();
			}
			break;
		case R.id.btSend:
			hideKeyboard();
			String message = etMessage.getText().toString();
			if (!TextUtils.isEmpty(message)) {
				try {
					Member member = Datastore.getInstance().getCurrentUser();
					HashMap<String, Object> myMap;

					if (group == null) {
						myMap = new HashMap<String, Object>();
						myMap.put("message", message);
						myMap.put("type", 0);
						myMap.put("objectIDOfClassUser", member.getObjectId());
						myMap.put("file", null);
						myMap.put("group", null);
						myMap.put("receiver", anotherMember.getObjectId());
						myMap.put("groupName", "");
						Log.e(TAG,
								"objectIDOfClassUser " + member.getObjectId());
					} else {
						myMap = new HashMap<String, Object>();
						myMap.put("message", message);
						myMap.put("type", 0);
						myMap.put("objectIDOfClassUser", member.getObjectId());
						myMap.put("file", null);
						myMap.put("group", group.getObjectId());
						if (!group.getIsPrivate()) {
							myMap.put("groupName", group.getName());
						} else {
							myMap.put("GroupName", "");
						}
						Log.e(TAG, "objIDOfClassUser " + member.getObjectId());
					}
					DialogManager.showProgress(getActivity(),
							"Sending message...");
					ParseCloud.callFunctionInBackground("sendChatMessage",
							myMap, new FunctionCallback<ParseObject>() {

								@Override
								public void done(ParseObject arg0,
										ParseException arg1) {
									DialogManager.closeProgress();
									if (arg1 == null) {
										Log.e(TAG, "message sent");
										Message message = Message.parseMessage(
												group,
												datastore.getMemberDao(),
												datastore
														.getLikedConnectionDao(),
												datastore
														.getReadConnectionDao(),
												datastore.getMessageDao(), arg0);
										adapter.addMessage(message);
									} else {
										Log.e(TAG, "sendChatMessage", arg1);
										Toast.makeText(getActivity(),
												"Failed to send message",
												Toast.LENGTH_LONG).show();
									}
								}
							});
					etMessage.setText("");
				} catch (Exception e) {
					Log.e(TAG, "sendMessage onClick", e);
				}
			}
			break;
		case R.id.btn_conver_back:
			getFragmentManager().popBackStack();
			break;
		case R.id.send_image:
			Utils.onUploadImage(getActivity(), IMAGE);
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 0) {
			send.setTextColor(getResources().getColor(
					android.R.color.darker_gray));
		} else {
			send.setTextColor(getResources().getColor(R.color.blue_text));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		hideKeyboard();
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

	public void setNameLabel(String NewGroupName) {
		userNameLabel.setText(NewGroupName);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				uploadData = data;
				ParseService.startActionUploadImage(getActivity(),
						uploadData.getData(), 0);
			}
		}
	}

}
