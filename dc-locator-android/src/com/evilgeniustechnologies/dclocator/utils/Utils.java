package com.evilgeniustechnologies.dclocator.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.models.ChatItem;
import com.evilgeniustechnologies.dclocator.models.GroupItemChat;
import com.evilgeniustechnologies.dclocator.models.Member;
import com.evilgeniustechnologies.dclocator.models.Message;
import com.evilgeniustechnologies.dclocator.models.MessageGroups;

public class Utils {
    private static final String TAG = "EGT.Utils";

    public static String getCurrentUserId(Context context) {
        SharedPreferences ref = context.getSharedPreferences(
                Config.SHARE_PREFERENCES, 0);
        return ref.getString(Config.USERID_KEY, null);
    }

    // public static ArrayList<GroupItemChat> groupId(Context context,
    // ArrayList<ChatItem> origins) {
    // SharedPreferences ref = context.getSharedPreferences(
    // Config.SHARE_PREFERENCES, 0);
    // String userId = ref.getString(Config.USERID_KEY, null);
    // if (origins != null)
    // Log.d("groupId", "origins size " + origins.size());
    // else
    // Log.d("groupId", "origins null");
    // HashMap<String, model.GroupItemChat> objectId = new HashMap<String,
    // GroupItemChat>();
    //
    // for (ChatItem arg : origins) {
    // if (arg.parseObject.getParseObject("group") == null) {
    // String id = arg.parseObject.getParseObject(
    // "objectIDOfClassUser").getObjectId();
    // if (!(id.equals(userId))) {
    //
    // if (!objectId.containsKey(id)) {
    // GroupItemChat gic = new GroupItemChat();
    // gic.chatItems = new ArrayList<ChatItem>();
    // gic.isGroup = false;
    // gic.id = id;
    // String[] names = getAvatarUser(id);
    // if (names[0] != null)
    // gic.groupName = names[0];
    // if (names[1] != null)
    // gic.avatar = names[1];
    // gic.chatItems = new ArrayList<ChatItem>();
    // gic.chatItems.add(new ChatItem(arg.parseObject));
    // gic.TimeInterval = arg.parseObject.getUpdatedAt()
    // .getTime();
    // objectId.put(id, gic);
    // } else {
    // GroupItemChat gic = objectId.get(id);
    // gic.chatItems.add(new ChatItem(arg.parseObject));
    // }
    // } else {
    // JSONArray idRes = arg.parseObject.getJSONArray("receivers");
    // for (int j = 0; j < idRes.length(); j++) {
    // try {
    // JSONObject json = new JSONObject();
    // json = idRes.getJSONObject(j);
    // String secdID = json.getString("objectId");
    // if (!secdID.equals(userId)) {
    //
    // if (!objectId.containsKey(secdID)) {
    // GroupItemChat gic = new GroupItemChat();
    // gic.isGroup = false;
    // gic.id = secdID;
    // String[] names = getAvatarUser(secdID);
    // if (names[0] != null)
    // gic.groupName = names[0];
    // if (names[1] != null)
    // gic.avatar = names[1];
    // gic.chatItems = new ArrayList<ChatItem>();
    // gic.chatItems.add(new ChatItem(
    // arg.parseObject));
    // gic.TimeInterval = arg.parseObject
    // .getUpdatedAt().getTime();
    // objectId.put(secdID, gic);
    // } else {
    // GroupItemChat gic = objectId.get(secdID);
    // gic.chatItems.add(new ChatItem(
    // arg.parseObject));
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // } else {
    // String id = arg.parseObject.getParseObject("group")
    // .getObjectId();
    // if (!objectId.containsKey(id)) {
    // GroupItemChat gic = new GroupItemChat();
    // gic.isGroup = true;
    // gic.id = id;
    // String[] names = getAvatarGroup(id);
    // if (names[0] != null)
    // gic.groupName = names[0];
    // if (names[1] != null)
    // gic.avatar = names[1];
    // gic.chatItems = new ArrayList<ChatItem>();
    // gic.chatItems.add(new ChatItem(arg.parseObject));
    // gic.TimeInterval = arg.parseObject.getUpdatedAt().getTime();
    // objectId.put(id, gic);
    // } else {
    // GroupItemChat gic = objectId.get(id);
    // gic.chatItems.add(new ChatItem(arg.parseObject));
    // }
    // }
    // }
    // // Convert HashMap to ArrayList
    // Log.i("GROUP MESSAGE", "size of group chat: " + objectId.size());
    // ArrayList<GroupItemChat> groupItemChats = new ArrayList<GroupItemChat>();
    // Iterator ar = objectId.entrySet().iterator();
    // while (ar.hasNext()) {
    // Map.Entry mapEntry = (Map.Entry) ar.next();
    // GroupItemChat o = (GroupItemChat) mapEntry.getValue();
    // String key = (String) mapEntry.getKey();
    // Log.i("GROUP MESSAGE", "groupid: " + key + " - groupName: "
    // + o.groupName + " - message size: " + o.chatItems.size());
    // Collections.sort(o.chatItems, ChatItemCompare);
    // groupItemChats.add(o);
    // }
    // Collections.sort(groupItemChats, GroupItemChat);
    //
    // for (GroupItemChat groupItemChat : groupItemChats) {
    // Log.d("groupItemChats", groupItemChat.TimeInterval + "");
    // for (ChatItem c : groupItemChat.chatItems) {
    // Log.d("GroupItemChat", groupItemChat.groupName + " - "
    // + c.timeCreate);
    // }
    // }
    //
    // return groupItemChats;
    // }

    public static Comparator<GroupItemChat> GroupItemChat = new Comparator<GroupItemChat>() {

        @Override
        public int compare(GroupItemChat ob1, GroupItemChat ob2) {
            int value = 0;
            if (ob2.TimeInterval > ob1.TimeInterval)
                value = 1;
            else if (ob2.TimeInterval < ob1.TimeInterval)
                value = -1;
            else if (ob2.TimeInterval == ob1.TimeInterval)
                value = 0;
            return value;
        }
    };

    public static String[] getAvatarUser(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
        query.whereEqualTo("objectId", id);
        String[] myStringArray = new String[2];
        try {
            ArrayList<ParseObject> object = (ArrayList<ParseObject>) query
                    .find();
            String name;
            if (object.get(0).getString("fullName") != null) {
                name = object.get(0).getString("fullName");
            } else {
                name = "";
            }
            myStringArray[0] = name;
            String Avatar;
            if (object.get(0).getParseFile("avatar") != null) {
                Avatar = object.get(0).getParseFile("avatar").getUrl();
            } else {
                Avatar = "";
            }
            myStringArray[1] = Avatar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myStringArray;
    }

    public static String[] getAvatarGroup(String id) {
        ParseQuery<ParseObject> groupName = ParseQuery
                .getQuery("MessageGroups");
        groupName.whereEqualTo("objectId", id);
        String[] myStringArray = new String[2];
        try {
            ArrayList<ParseObject> obj = (ArrayList<ParseObject>) groupName
                    .find();
            String name = "";
            if (obj.get(0).getString("name") != null) {
                name = obj.get(0).getString("name");
            } else {
                name = "";
            }
            myStringArray[0] = name;
            if (obj.get(0).getParseFile("icon") != null) {
                String avartar = obj.get(0).getParseFile("icon").getUrl();
                myStringArray[1] = avartar;
            } else {
                myStringArray[1] = "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myStringArray;
    }

    public static ArrayList<ParseObject> List(String userID) {

        ArrayList<ParseObject> obj = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> member = ParseQuery.getQuery("Member");
        member.fromLocalDatastore();
        member.whereEqualTo("objectId", userID);
        ParseQuery<ParseObject> ClassObSenderUser = ParseQuery
                .getQuery("Messages");
        ClassObSenderUser.fromLocalDatastore();
        ClassObSenderUser.include("objectIDOfClassUser");
        ClassObSenderUser.whereMatchesQuery("objectIDOfClassUser", member);

        ArrayList<ParseObject> obj1 = null;
        try {
            obj1 = (ArrayList<ParseObject>) ClassObSenderUser.find();
        } catch (ParseException e1) {

            e1.printStackTrace();
        }

        ParseQuery<ParseObject> Receive = ParseQuery.getQuery("Messages");
        Receive.fromLocalDatastore();
        Receive.include("objectIDOfClassUser");
        JSONObject json = new JSONObject();
        try {
            json.put("__type", "Pointer");
            json.put("className", "Member");
            json.put("objectId", userID);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        ArrayList<JSONObject> a = new ArrayList<JSONObject>();
        a.add(json);

        Receive.whereContainedIn("receivers", a);
        ArrayList<ParseObject> obj2 = null;
        try {
            obj2 = (ArrayList<ParseObject>) Receive.find();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        if (obj1 != null && !obj1.isEmpty()) {

            obj.addAll(obj1);

        }
        if (obj2 != null && !obj2.isEmpty()) {
            obj.addAll(obj2);
        }

        return obj;
    }

    // public static ArrayList<ChatItem> SortList(ArrayList<ParseObject>
    // listParse) {
    // ArrayList<ChatItem> chatitem = new ArrayList<ChatItem>();
    //
    // for (ParseObject object : listParse) {
    // ChatItem ci = new ChatItem(object);
    // chatitem.add(ci);
    // }
    // Collections.sort(chatitem, ChatItemCompare);
    // // Collections.sort(obj, CreateAtCompare);
    // for (ChatItem it : chatitem) {
    // Log.i("time", it.timeCreate + "_" + it.parseObject.getObjectId());
    // }
    // return chatitem;
    // }

    public static Comparator<ChatItem> ChatItemCompare = new Comparator<ChatItem>() {

        @Override
        public int compare(ChatItem ob1, ChatItem ob2) {
            int value = 0;
            if (ob2.timeCreate > ob1.timeCreate)
                value = 1;
            else if (ob2.timeCreate < ob1.timeCreate)
                value = -1;
            else if (ob2.timeCreate == ob1.timeCreate)
                value = 0;
            return value;
        }
    };

    public static int CounUnreadyMessage(String userId) {
        try {
            ParseQuery<ParseObject> userOther = ParseQuery.getQuery("Messages");
            userOther.fromLocalDatastore();
            JSONObject obj = new JSONObject();
            obj.put("objectId", userId);
            ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
            arr.add(obj);

            userOther.whereContainedIn("receivers", arr);
            int count = userOther.find().size();
            return count;
        } catch (Exception e) {
            Log.e(TAG, "CounUnreadyMessage", e);
        }
        return 0;
    }

    public static ArrayList<String> getAlreadyReadMessageIDs(String userId) {

        ArrayList<String> alreadyReadMessageIDS = new ArrayList<String>();
        try {
            String OBJECTID;
            ParseObject mess;
            ParseQuery<ParseObject> alreadyReadMessage = ParseQuery
                    .getQuery("Member");
            alreadyReadMessage.fromLocalDatastore();
            alreadyReadMessage.whereEqualTo("objectId", userId);
            mess = alreadyReadMessage.find().get(0);

            java.util.List<String> arr = new ArrayList<String>();
            arr = mess.getList("alreadyReadMessageIDs");
            for (int i = 0; i < arr.size(); i++) {
                OBJECTID = arr.get(i);
                alreadyReadMessageIDS.add(OBJECTID);
            }
        } catch (Exception e) {

        }
        return alreadyReadMessageIDS;

    }

    public static ArrayList<ParseObject> ListMessagePrivate(String userID,
                                                            String OtherUserId) {

        ArrayList<ParseObject> obj = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> member = ParseQuery.getQuery("Member");
        member.fromLocalDatastore();
        member.whereEqualTo("objectId", userID);
        ParseQuery<ParseObject> ClassObSenderUser = ParseQuery
                .getQuery("Messages");
        ClassObSenderUser.whereDoesNotExist("group");
        ClassObSenderUser.include("objectIDOfClassUser");
        ClassObSenderUser.whereMatchesQuery("objectIDOfClassUser", member);
        JSONObject json = new JSONObject();
        try {
            json.put("__type", "Pointer");
            json.put("className", "Member");
            json.put("objectId", OtherUserId);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        ArrayList<JSONObject> a = new ArrayList<JSONObject>();
        a.add(json);

        ClassObSenderUser.whereContainedIn("receivers", a);

        ArrayList<ParseObject> obj1 = null;
        try {
            obj1 = (ArrayList<ParseObject>) ClassObSenderUser.find();
        } catch (ParseException e1) {

            e1.printStackTrace();
        }

        ParseQuery<ParseObject> membersender = ParseQuery.getQuery("Member");
        membersender.fromLocalDatastore();
        member.whereEqualTo("objectId", OtherUserId);
        ParseQuery<ParseObject> ClassObreceiver = ParseQuery
                .getQuery("Messages");
        ClassObreceiver.whereDoesNotExist("group");
        ClassObreceiver.include("objectIDOfClassUser");
        ClassObreceiver.whereMatchesQuery("objectIDOfClassUser", membersender);
        JSONObject json2 = new JSONObject();
        try {
            json.put("__type", "Pointer");
            json.put("className", "Member");
            json.put("objectId", userID);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        ArrayList<JSONObject> ae = new ArrayList<JSONObject>();
        a.add(json);

        ClassObreceiver.whereContainedIn("receivers", ae);

        ArrayList<ParseObject> obj2 = null;
        try {
            obj2 = (ArrayList<ParseObject>) ClassObreceiver.find();
        } catch (ParseException e1) {

            e1.printStackTrace();
        }

        if (obj1 != null && !obj1.isEmpty()) {

            obj.addAll(obj1);

        }
        if (obj2 != null && !obj2.isEmpty()) {
            obj.addAll(obj2);
        }

        return obj;
    }
    public static void onUploadImage(Activity activity,int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		activity.startActivityForResult(intent, requestCode);
	}
    public static List<MessageGroups> getAllGroupsOfPerson(Context context) {
        try {
            ParseQuery<MessageGroups> parseQuery = MessageGroups.getQuery();
            parseQuery.whereContainedIn("members", generateFormatPointer(context));
            return parseQuery.find();
        } catch (Exception e) {
            Log.e(TAG, "getAllGroupsOfPerson", e);
        }
        return null;
    }
    public static byte[] convertToByte(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, blob);
        return blob.toByteArray();
    }
    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 232;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static String getFileName(Context context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(
                selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }
    public static List<JSONObject> generateFormatPointer(Context context) {
        try {
            JSONObject json = new JSONObject();
            json.put("__type", "Pointer");
            json.put("className", "Member");
            json.put("objectId", Datastore.getInstance().getCurrentUserId());
            return Arrays.asList(json);
        } catch (JSONException e) {
            Log.e(TAG, "generateFormatPointer", e);
        }
        return null;
    }

    public static int CountMessageInTabbar(Context context) {
        List<MessageGroups> messgroup;
        messgroup = getAllGroupsOfPerson(context);
        String userId = getCurrentUserId(context);
        int countUnread = 0;
        try {
            Log.e(TAG, "messageGroups size " + messgroup.size());
            if (messgroup.size() > 0) {
                List<JSONObject> pointer = generateFormatPointer(context);

                for (MessageGroups messageGroups : messgroup) {
                    ParseQuery<Message> messageUnread = Message.getQuery();
                    // ... the message's owner is not the currentUser ...
                    messageUnread.whereDoesNotMatchQuery("objectIDOfClassUser",
                            MemberQuery(userId));

                    // ... has not been read by the currentUser ...
                    messageUnread.whereNotContainedIn("read_by", pointer);
                    // ... belong to this GroupMessage ...
                    messageUnread.whereMatchesQuery("group",
                            GroupQuery(messageGroups.getObjectId()));
                    int k = messageUnread.find().size();
                    countUnread = countUnread + k;
                    Log.e(TAG, "countUnread in loop " + countUnread);
                }
                Log.e(TAG, "countUnread out loop " + countUnread);
                return countUnread;
            }
        } catch (Exception e) {
            Log.e(TAG, "CountMessageInTabbar", e);
        }
        Log.e(TAG, "countUnread end of method " + countUnread);
        return countUnread;
    }

    public static MessageGroups getPrivateGroup(String otherId) {
        try {
            Member currentUser = Datastore.getInstance().getCurrentParseUser();
            Member anotherMember = Member.getQuery().get(otherId);
            return MessageGroups.getQuery()
                    .whereContainedIn("members", Arrays.asList(currentUser, anotherMember))
                    .whereEqualTo("isPrivate", true)
                    .getFirst();
        } catch (Exception e) {
            Log.e(TAG, "getPrivateGroup", e);
        }
        return null;
    }

    public static Member getMember(String MemberId) {
        try {
            return Member.getQuery().get(MemberId);
        } catch (Exception e) {
            Log.e(TAG, "getMember", e);
        }
        return null;
    }

    public static ArrayList<GroupItemChat> getMessagesOfEachGroup(Context context, List<MessageGroups> MessageGroups) {
        Log.e(TAG, "start getMessagesOfEachGroup");
        ArrayList<GroupItemChat> groupitems = new ArrayList<GroupItemChat>();
        String userId = getCurrentUserId(context);
        try {
            List<JSONObject> pointer = generateFormatPointer(context);

            ParseQuery<Member> mem = Member.getQuery();
            mem.whereEqualTo("objectId", getCurrentUserId(context));
            Log.e(TAG, "groupMessage size " + MessageGroups.size());
            for (MessageGroups parseObject : MessageGroups) {
                GroupItemChat groupItemChat = new GroupItemChat();
                groupItemChat.id = parseObject.getObjectId();

                // get unread list
                // Logic: find unread messages where ...
                ParseQuery<Message> messageUnread = Message.getQuery();
                // ... the message's owner is not the currentUser ...
                messageUnread.whereDoesNotMatchQuery("objectIDOfClassUser", MemberQuery(userId));
                // ... has not been read by the currentUser ...
                messageUnread.whereNotContainedIn("read_by", pointer);
                // ... belong to this MessageGroups ...
                messageUnread.whereMatchesQuery("group", GroupQuery(parseObject.getObjectId()));
                // ... Voila!
                ArrayList<Message> listUnread = (ArrayList<Message>) messageUnread.find();
                // For each unread message, add to unread list of groupChatItem
                for (Message message : listUnread) {
                    ChatItem unread = new ChatItem(message);
                    groupItemChat.listUnread.add(unread);
                }

                // list message
                // Logic: find all messages which ...
                ParseQuery<Message> messages = Message.getQuery();
                // ... belong to this MessageGroups ...
                messages.whereMatchesQuery("group", GroupQuery(parseObject.getObjectId()));
                // ... Add descending order ...
                messages.orderByDescending("createdAt");
                // ... Voila!
                ArrayList<Message> listMessage = (ArrayList<Message>) messages.find();
                // List them out, boys
                ArrayList<ChatItem> listChatItem = new ArrayList<ChatItem>();
                if (listMessage.size() > 0) {
                    for (Message parseObject2 : listMessage) {
                        ChatItem item = new ChatItem(parseObject2);
                        listChatItem.add(item);
                    }
                    // Sort messages according to timeCreated
                    Collections.sort(listChatItem, ChatItemCompare);
                    // Set timeInterval of the group as the timeCreated of the latest message
                    groupItemChat.TimeInterval = listChatItem.get(0).timeCreate;
                }

                groupItemChat.isPrivate = parseObject.isPrivate();
                // Add those messages to group
                groupItemChat.chatItems = listChatItem;
                groupItemChat.groupName = parseObject.getName();
                if (!parseObject.isPrivate()) {
                    // If this group is a real "group", give it a shiny avatar
                    if (parseObject.getIcon() != null) {
                        groupItemChat.avatar = parseObject.getIcon().getUrl();
                    } else {
                        groupItemChat.avatar = "";
                    }
                } else {
                    // Otherwise, set the avatar of the group to the person's
                    // avatar that you (currentUser) had conversation with
                    JSONArray arrayMember = parseObject.getMembers();
                    for (int i = 0; i < arrayMember.length(); i++) {
                        JSONObject other = arrayMember.getJSONObject(i);
                        String OtherId = other.getString("objectId");
                        if (!OtherId.equals(getCurrentUserId(context))) {
                            Member otherMem = getMember(OtherId);
                            if (otherMem != null) {
                                groupItemChat.groupName = otherMem.getFullName();
                                if (otherMem.getAvatar() != null) {
                                    groupItemChat.avatar = otherMem.getAvatar()
                                            .getUrl();
                                } else {
                                    groupItemChat.avatar = "";
                                }
                            }
                        }
                    }
                }
                // Voila
                groupitems.add(groupItemChat);
                Collections.sort(groupitems, GroupItemChat);
                Log.e(TAG, "group size " + groupitems.size());
            }
        } catch (Exception e) {
            Log.e(TAG, "getMessagesOfEachGroup", e);
        }
        Log.e(TAG, "end getMessagesOfEachGroup");
        Log.e(TAG, groupitems.toString());
        return groupitems;
    }

    public static ParseQuery<ParseObject> MemberQuery(String memberId) {
        ParseQuery<ParseObject> mem = ParseQuery.getQuery("Member");
        mem.whereEqualTo("objectId", memberId);
        return mem;
    }

    public static ParseQuery<ParseObject> GroupQuery(String GroupId) {
        ParseQuery<ParseObject> group = ParseQuery.getQuery("MessageGroups");
        group.whereEqualTo("objectId", GroupId);
        return group;
    }

    public static void CloudCodeCreateGroup(Context context, String GroupName, ParseFile icon, ArrayList<String> ArrReceivers) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("objectIDOfClassUser", getCurrentUserId(context));
        map.put("groupName", GroupName);
        map.put("icon", icon);
        map.put("receivers", ArrReceivers);
        try {
            ParseCloud.callFunction("createMessageGroup", map);
        } catch (ParseException e) {
            Log.e(TAG, "createMessageGroup", e);
        }
    }
    public static void CloudCodeEditGroup(Context context, String GroupName, ParseFile icon, ArrayList<String> ArrMember,String GroupId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("objectIDOfClassUser", getCurrentUserId(context));
        map.put("groupName", GroupName);
        map.put("icon", icon);
        map.put("members", ArrMember);
        map.put("group", GroupId);
        try {
            ParseCloud.callFunction("editMessageGroup", map);
        
        } catch (ParseException e) {
            Log.e(TAG, "editMessageGroup", e);
        }
    }

    public static class Tuple {
        public final String head;
        public final String tail;

        public Tuple(String head, String tail) {
            this.head = head;
            this.tail = tail;

        }
    }
}
