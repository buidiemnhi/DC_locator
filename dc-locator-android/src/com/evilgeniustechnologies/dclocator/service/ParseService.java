package com.evilgeniustechnologies.dclocator.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.evilgeniustechnologies.dclocator.commons.ISConnectInternet;
import com.evilgeniustechnologies.dclocator.daos.Location;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.daos.Metadata;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.utils.DialogManager;
import com.evilgeniustechnologies.dclocator.utils.Utils;

/**
 * Created by benjamin on 10/5/14.
 */
public class ParseService extends Service {
	private static final String TAG = "EGT.ParseService";

	private static final String ACTION_PARSE_LOCATIONS = "ACTION_PARSE_LOCATIONS";
	private static final String ACTION_PARSE_MEMBERS = "ACTION_PARSE_MEMBERS";
	private static final String ACTION_PARSE_GROUPS = "ACTION_PARSE_GROUPS";
	private static final String ACTION_PARSE_MESSAGES = "ACTION_PARSE_MESSAGES";
	private static final String ACTION_PARSE_UNREAD_MESSAGES = "ACTION_PARSE_UNREAD_MESSAGES";

	private static final String UPLOAD_IMAGE = "UPLOAD_IMAGE";
	private static final String EXTRA_URI = "com.evilgeniustechnologies.DC_LOCATOR.services.extra.URI";

	private static final String ACTION_COUNT_UNREAD_MESSAGES = "ACTION_COUNT_UNREAD_MESSAGES";
	private static final String ACTION_PARSE_ALL_GROUPS = "ACTION_PARSE_ALL_GROUPS";
	private static final String ACTION_PARSE_ALL_MESSAGES = "ACTION_PARSE_ALL_MESSAGES";
	private static final String ACTION_PARSE_ALL_MEMBERS = "ACTION_PARSE_ALL_MEMBERS";
	private static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

	private static final String ACTION_SEARCH_MEMBERS = "ACTION_SEARCH_MEMBERS";
	private static final String EXTRA_NAME = "EXTRA_NAME";
	private static final String EXTRA_EXPERTISE = "EXTRA_EXPERTISE";
	private static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
	private static final String EXTRA_CITY = "EXTRA_CITY";

	private ExecutorService executor;
	private ScheduledExecutorService monitorExecutor;

	private int totalUnreadSegments;
	private AtomicInteger unreadSegments;

	private int totalGroupSegments;
	private AtomicInteger groupSegments;

	private int totalMessageSegments;
	private AtomicInteger messageSegments;

	private int totalMemberSegments;
	private AtomicInteger memberSegments;
	private boolean inSearchMode = false;

	private Datastore datastore;

	private boolean isSaveSuccessfully;

	@Override
	public void onCreate() {
		super.onCreate();
		executor = Executors.newFixedThreadPool(6);
	}

	public static void startActionCountUnreadMessages(Context context) {
		DialogManager.showProgress(context);
		Intent intent = new Intent(context, ParseService.class);
		intent.setAction(ACTION_COUNT_UNREAD_MESSAGES);
		context.startService(intent);
	}

	public static void startActionParseAllGroups(Context context) {
		DialogManager.showProgress(context);
		Intent intent = new Intent(context, ParseService.class);
		intent.setAction(ACTION_PARSE_ALL_GROUPS);
		context.startService(intent);
	}

	public static void startActionParseAllMessages(Context context,
			String groupId) {
		DialogManager.showProgress(context);
		Intent intent = new Intent(context, ParseService.class);
		intent.setAction(ACTION_PARSE_ALL_MESSAGES);
		intent.putExtra(EXTRA_GROUP_ID, groupId);
		context.startService(intent);
	}

	public static void startActionParseAllMembers(Context context) {
		DialogManager.showProgress(context);
		Intent intent = new Intent(context, ParseService.class);
		intent.setAction(ACTION_PARSE_ALL_MEMBERS);
		context.startService(intent);
	}

	public static void startActionSearchMembers(Context context, String name,
			String expertise, String country, String city) {
		DialogManager.showProgress(context);
		Intent intent = new Intent(context, ParseService.class);
		intent.setAction(ACTION_SEARCH_MEMBERS);
		intent.putExtra(EXTRA_NAME, name);
		intent.putExtra(EXTRA_EXPERTISE, expertise);
		intent.putExtra(EXTRA_COUNTRY, country);
		intent.putExtra(EXTRA_CITY, city);
		context.startService(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onHandleIntent(intent);
		return START_STICKY;
	}

	private void onHandleIntent(final Intent intent) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					datastore = Datastore.getInstance();
					if (intent != null) {
						final String action = intent.getAction();
						if (ACTION_COUNT_UNREAD_MESSAGES.equals(action)) {
							initActionCountUnreadMessages();
							handleActionCountUnreadMessages();
						} else if (ACTION_PARSE_ALL_GROUPS.equals(action)) {
							initActionParseGroups();
							handleActionParseGroups();
						} else if (ACTION_PARSE_ALL_MESSAGES.equals(action)) {
							final String groupId = intent
									.getStringExtra(EXTRA_GROUP_ID);
							initActionParseMessages();
							handleActionParseMessages(groupId);
						} else if (UPLOAD_IMAGE.equals(action)) {
							int type = intent.getExtras().getInt("type");
							final Uri uri = intent
									.getParcelableExtra(EXTRA_URI);
							if (type == 0) {
								handleActionUploadImage(uri);
							} else {
								handleActionUploadIcon(uri);
							}
						} else if (ACTION_PARSE_ALL_MEMBERS.equals(action)) {
							initActionParseMembers();
							handleActionParseMembers();
						} else if (ACTION_SEARCH_MEMBERS.equals(action)) {
							final String name = intent
									.getStringExtra(EXTRA_NAME);
							final String expertise = intent
									.getStringExtra(EXTRA_EXPERTISE);
							final String country = intent
									.getStringExtra(EXTRA_COUNTRY);
							final String city = intent
									.getStringExtra(EXTRA_CITY);
							initActionSearchMembers();
							handleActionSearchMembers(name, expertise, country,
									city);
						}
					}
				} catch (ParseException e) {
					DialogManager.closeProgress();
					Log.e(TAG, e.getMessage(), e);
				} catch (NullPointerException e) {
					DialogManager.closeProgress();
					Log.e(TAG, e.getMessage(), e);
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		});
	}

	private void initActionSearchMembers() {
		inSearchMode = true;
		memberSegments = new AtomicInteger(0);
	}

	private void handleActionSearchMembers(String name, String expertise,
			String country, String city) throws ParseException {
		if (TextUtils.isEmpty(name) && TextUtils.isEmpty(expertise)
				&& TextUtils.isEmpty(country) && TextUtils.isEmpty(city)) {
			inSearchMode = false;
		}
		totalMemberSegments = (int) Math.ceil(generateSearchQuery(name,
				expertise, country, city).count() / 100.0);
		Log.e(TAG, "totalMemberSegments " + totalMemberSegments);
		if (totalMemberSegments == 0) {
			totalMemberSegments = 1;
			parseTerminator(ACTION_PARSE_MEMBERS, null);
		}
		for (int i = 0; i < totalMemberSegments; i++) {
			executor.submit(new ParseModule(ACTION_PARSE_MEMBERS,
					generateSearchQuery(name, expertise, country, city),
					i * 100, 100, i * 100 + 100));
		}
	}

	private ParseQuery<ParseObject> generateSearchQuery(String name,
			String expertise, String country, String city) {
		ParseQuery<ParseObject> memberQuery = Datastore.getMemberQuery();
		if (!TextUtils.isEmpty(name)) {
			memberQuery.whereMatches("fullName", name, "i");
		}
		if (!TextUtils.isEmpty(expertise)) {
			memberQuery.whereEqualTo("skill", expertise);
		}
		if (!TextUtils.isEmpty(country)) {
			memberQuery.whereEqualTo("country", country);
		}
		if (!TextUtils.isEmpty(city)) {
			memberQuery.whereMatches("city", city, "i");
		}
		if (TextUtils.isEmpty(name) && TextUtils.isEmpty(expertise)
				&& TextUtils.isEmpty(country) && TextUtils.isEmpty(city)) {
			Metadata metadata = datastore.getMetadata();
			if (metadata.getLatestMemberDate() != null) {
				memberQuery.whereGreaterThan("updatedAt",
						metadata.getLatestMemberDate());
			}
		}
		return memberQuery;
	}

	private void initActionParseMembers() {
		memberSegments = new AtomicInteger(0);
	}

	private void handleActionParseMembers() throws ParseException {
		totalMemberSegments = (int) Math
				.ceil(generateMemberQuery().count() / 100.0);
		Log.e(TAG, "totalMemberSegments " + totalMemberSegments);
		if (totalMemberSegments == 0) {
			totalMemberSegments = 1;
			parseTerminator(ACTION_PARSE_MEMBERS, null);
		}
		for (int i = 0; i < totalMemberSegments; i++) {
			executor.submit(new ParseModule(ACTION_PARSE_MEMBERS,
					generateMemberQuery(), i * 100, 100, i * 100 + 100));
		}
	}

	private ParseQuery<ParseObject> generateMemberQuery() {
		Metadata metadata = datastore.getMetadata();
		if (metadata.getLatestMemberDate() != null) {
			return Datastore
					.getMemberQuery()
					.whereEqualTo("available", true)
					.whereNotEqualTo("objectId",
							datastore.getCurrentUser().getObjectId())
					.whereGreaterThan("updatedAt",
							metadata.getLatestMemberDate());
		} else {
			return Datastore
					.getMemberQuery()
					.whereEqualTo("available", true)
					.whereNotEqualTo("objectId",
							datastore.getCurrentUser().getObjectId());
		}
	}

	private void initActionParseMessages() {
		messageSegments = new AtomicInteger(0);
	}

	private void handleActionParseMessages(String groupId)
			throws ParseException {
		MessageGroup group = datastore.getGroupDao().load(groupId);
		totalMessageSegments = (int) Math.ceil(generateMessageQuery(group)
				.count() / 10.0);
		Log.e(TAG, "totalMessageSegments " + totalMessageSegments);
		if (totalMessageSegments == 0) {
			totalMessageSegments = 1;
			parseTerminator(ACTION_PARSE_MESSAGES, null);
		}
		for (int i = 0; i < totalMessageSegments; i++) {
			executor.submit(new ParseModule(ACTION_PARSE_MESSAGES,
					generateMessageQuery(group), groupId, i * 10, 10,
					i * 10 + 10));
		}
	}

	private ParseQuery<ParseObject> generateMessageQuery(MessageGroup group)
			throws ParseException {
		ParseObject parseGroup = Datastore.getMessageGroupQuery().get(
				group.getObjectId());
		if (group.getLatestMessageDate() != null) {
			return Datastore
					.getMessageQuery()
					.whereEqualTo("group", parseGroup)
					.whereGreaterThan("updatedAt", group.getLatestMessageDate());
		} else {
			return Datastore.getMessageQuery()
					.whereEqualTo("group", parseGroup);
		}
	}

	private void initActionParseGroups() {
		groupSegments = new AtomicInteger(0);
	}

	private void handleActionParseGroups() throws ParseException {
		totalGroupSegments = (int) Math
				.ceil(generateGroupQuery().count() / 10.0);
		Log.e(TAG, "totalGroupSegments " + totalGroupSegments);
		if (totalGroupSegments == 0) {
			totalGroupSegments = 1;
			parseTerminator(ACTION_PARSE_GROUPS, null);
		}
		for (int i = 0; i < totalGroupSegments; i++) {
			executor.submit(new ParseModule(ACTION_PARSE_GROUPS,
					generateGroupQuery(), i * 10, 10, i * 10 + 10));
		}
	}

	private ParseQuery<ParseObject> generateGroupQuery() {
		com.evilgeniustechnologies.dclocator.daos.Member user = datastore
				.getCurrentUser();
		if (user.getLatestGroupDate() != null) {
			return Datastore
					.getMessageGroupQuery()
					.whereContainedIn("members",
							Arrays.asList(datastore.getCurrentParseUser()))
					.whereGreaterThan("updatedAt", user.getLatestGroupDate());
		} else {
			return Datastore.getMessageGroupQuery().whereContainedIn("members",
					Arrays.asList(datastore.getCurrentParseUser()));
		}
	}

	private void initActionParseUnreadMessages() {
		unreadSegments = new AtomicInteger(0);
		totalUnreadSegments = 0;
	}

	private void handleActionParseUnreadMessages() throws ParseException {
		initActionParseUnreadMessages();
		ParseQuery<ParseObject> query = generateUnreadMessagesQuery();
		com.evilgeniustechnologies.dclocator.daos.Member user = datastore
				.getCurrentUser();
		user.setTotalUnreadMessages(query.count());
		user.update();
		totalUnreadSegments = (int) Math.ceil(query.count() / 10.0);
		Log.e(TAG, "totalUnreadSegments " + totalUnreadSegments);
		if (totalUnreadSegments == 0) {
			totalUnreadSegments = 1;
			parseTerminator(ACTION_PARSE_UNREAD_MESSAGES, null);
		}
		for (int i = 0; i < totalUnreadSegments; i++) {
			executor.submit(new ParseModule(ACTION_PARSE_UNREAD_MESSAGES,
					query, i * 10, 10, i * 10 + 10));
		}
	}

	private class ParseModule implements Runnable {
		private String action;
		private ParseQuery<ParseObject> query;
		private String groupId;
		private int offset;
		private int limit;
		private int total;

		private ParseModule(String action, ParseQuery<ParseObject> query,
				int offset, int limit, int total) {
			this.action = action;
			this.query = query;
			this.offset = offset;
			this.limit = limit;
			this.total = total;
		}

		private ParseModule(String action, ParseQuery<ParseObject> query,
				String groupId, int offset, int limit, int total) {
			this.action = action;
			this.query = query;
			this.groupId = groupId;
			this.offset = offset;
			this.limit = limit;
			this.total = total;
		}

		@Override
		public void run() {
			try {
				parseRecursive(action, query, groupId, offset, limit, total);
			} catch (ParseException e) {
				Log.e(TAG, "ParseModule", e);
			}
		}
	}

	private void parseRecursive(String action, ParseQuery<ParseObject> query,
			String groupId, int offset, int limit, int total)
			throws ParseException {
		query.setSkip(offset);
		query.setLimit(limit);
		parseFactory(action, query, groupId);
		if (offset + limit < total) {
			parseRecursive(action, query, groupId, offset + limit, limit, total);
		}
	}

	private void parseFactory(String action, ParseQuery<ParseObject> query,
			String groupId) throws ParseException {
		if (ACTION_PARSE_GROUPS.equals(action)) {
			MessageGroup.parseGroups(datastore, query);
		} else if (ACTION_PARSE_MESSAGES.equals(action)) {
			com.evilgeniustechnologies.dclocator.daos.Message.parseMessages(
					datastore, query, groupId);
		} else if (ACTION_PARSE_LOCATIONS.equals(action)) {
			Location.parseLocations(datastore, query);
		} else if (ACTION_PARSE_MEMBERS.equals(action)) {
			com.evilgeniustechnologies.dclocator.daos.Member.parseMembers(
					datastore, query, inSearchMode);
		} else if (ACTION_PARSE_UNREAD_MESSAGES.equals(action)) {
			com.evilgeniustechnologies.dclocator.daos.Message
					.parseUnreadMessages(datastore, query);
		}
		parseTerminator(action, groupId);
	}

	private void parseTerminator(String action, String groupId)
			throws ParseException {
		if (ACTION_PARSE_GROUPS.equals(action)) {
			groupSegments.addAndGet(1);
			Log.e(TAG, groupSegments.get() + " > " + totalGroupSegments);
			if (groupSegments.get() == totalGroupSegments) {
				Member user = datastore.getCurrentUser();
				if (user.getTempGroupDate() != null) {
					user.setLatestGroupDate(user.getTempGroupDate());
					user.update();
				}
				Log.e(TAG, "groups size "
						+ datastore.getGroupDao().loadAll().size());
				datastore.refresh();
				handleActionParseUnreadMessages();
			}
		} else if (ACTION_PARSE_MESSAGES.equals(action)) {
			messageSegments.addAndGet(1);
			Log.e(TAG, messageSegments.get() + " > " + totalMessageSegments);
			if (messageSegments.get() == totalMessageSegments) {
				MessageGroup group = datastore.getGroupDao().load(groupId);
				if (group != null && group.getTempMessageDate() != null) {
					group.setLatestMessageDate(group.getTempMessageDate());
					group.update();
				}
				Log.e(TAG, "messages size "
						+ datastore.getMessageDao().loadAll().size());
				datastore.refresh();
				datastore.notifyFragmentObserver(Datastore.ALL_MESSAGES_PARSED);
				terminate();
			}
		} else if (ACTION_PARSE_UNREAD_MESSAGES.equals(action)) {
			unreadSegments.addAndGet(1);
			Log.e(TAG, unreadSegments.get() + " > " + totalUnreadSegments);
			if (unreadSegments.get() == totalUnreadSegments) {
				List<MessageGroup> groups = datastore.getGroupDao().loadAll();
				for (MessageGroup group : groups) {
					if (group.getTempUnreadMessageDate() != null) {
						group.setLatestUnreadMessageDate(group
								.getTempUnreadMessageDate());
						group.update();
					}
				}
				datastore.refresh();
				datastore
						.notifyFragmentObserver(Datastore.ALL_MESSAGE_GROUPS_PARSED);
				terminate();
			}
		} else if (ACTION_PARSE_MEMBERS.equals(action)) {
			memberSegments.addAndGet(1);
			Log.e(TAG, memberSegments.get() + " > " + totalMemberSegments);
			if (memberSegments.get() == totalMemberSegments) {
				if (!inSearchMode) {
					Metadata metadata = datastore.getMetadata();
					if (metadata.getTempMemberDate() != null) {
						metadata.setLatestMemberDate(metadata
								.getTempMemberDate());
						datastore.getMetadataDao().update(metadata);
					}
				}
				datastore.refresh();
				datastore.notifyFragmentObserver(Datastore.ALL_MEMBERS_PARSED);
				terminate();
			}
		}
	}

	private void initActionCountUnreadMessages() {
		unreadSegments = new AtomicInteger(0);
	}

	private void handleActionCountUnreadMessages() throws ParseException {
		Log.e(TAG, "start handleActionCountUnreadMessages");
		int unreadMessages = generateUnreadMessagesQuery().count();
		totalUnreadSegments = 1;
		com.evilgeniustechnologies.dclocator.daos.Member user = datastore
				.getCurrentUser();
		user.setTotalUnreadMessages(unreadMessages);
		user.update();
		collectUnreadSegments();
	}

	private ParseQuery<ParseObject> generateUnreadMessagesQuery()
			throws ParseException {
		ParseObject parseUser = datastore.getCurrentParseUser();
		ParseQuery<ParseObject> groupQuery = Datastore.getMessageGroupQuery()
				.whereContainedIn("members", Arrays.asList(parseUser));
		return Datastore.getMessageQuery()
				.whereContainedIn("group", groupQuery.find())
				.whereNotEqualTo("objectIDOfClassUser", parseUser)
				.whereNotContainedIn("read_by", Arrays.asList(parseUser));
	}

	private void collectUnreadSegments() {
		Log.e(TAG, "currentSegments " + (unreadSegments.get() + 1));
		if (unreadSegments.addAndGet(1) == totalUnreadSegments) {
			com.evilgeniustechnologies.dclocator.daos.Member user = datastore
					.getCurrentUser();
			Log.e(TAG, "total unread messages " + user.getTotalUnreadMessages());
			datastore.refresh();
			datastore.notifyFragmentObserver(Datastore.UNREAD_MESSAGES_COUNTED);
			terminate();
		}
	}

	private ParseFile handleActionUploadImage(Uri uri) throws ParseException,
			FileNotFoundException {
		// Get the path of the selected image
		String imagePath = Utils.getFileName(this, uri);
		// Construct a new bitmap object from the image path
		Bitmap image = Utils.decodeUri(this, uri);
		// Construct a new ParseFile to store the bitmap data

		final com.parse.ParseFile pFile = new com.parse.ParseFile(imagePath,
				Utils.convertToByte(image));
		ParseObject object = new ParseObject("Files");
		object.put("file", pFile);
		object.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				if (arg0 == null) {
					isSaveSuccessfully = true;
				}

			}
		});

		// Notify main thread activity
		datastore.notifyObserver(Datastore.UPLOAD_IMAGE);
		if (isSaveSuccessfully) {
			return pFile;
		}
		return null;
	}

	private ParseFile handleActionUploadIcon(Uri uri) throws ParseException,
			FileNotFoundException {
		// Get the path of the selected image
		String imagePath = Utils.getFileName(this, uri);
		// Construct a new bitmap object from the image path
		Bitmap image = Utils.decodeUri(this, uri);
		// Construct a new ParseFile to store the bitmap data

		final com.parse.ParseFile pFile = new com.parse.ParseFile(imagePath,
				Utils.convertToByte(image));

		return pFile;
	}

	public static void startActionUploadImage(Activity activity, Uri uri,
			int Type) {
		if (!ISConnectInternet.isConnectedInternet(activity)) {
			ISConnectInternet.showAlertInternet(activity);
			return;
		}
		DialogManager.showProgress(activity, "upload image");
		Intent intent = new Intent(activity, ParseService.class);
		intent.setAction(UPLOAD_IMAGE);
		intent.putExtra(EXTRA_URI, uri);
		intent.putExtra("type", Type);
		activity.startService(intent);
	}

	private void terminate() {
		// executor.shutdownNow();
		// stopSelf();
		DialogManager.closeProgress();
		Log.e(TAG, "terminate");
	}
}