package com.evilgeniustechnologies.dclocator.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.models.GroupItemChat;

public class ListTabChatAdapter extends ArrayAdapter<GroupItemChat> {
    private static final String TAG = "EGT.ListTabChatAdapter";
    Context context;
    int layoutId;
    List<GroupItemChat> conversation;
    List<String> statuses;
    private String userId;
    ArrayList<String> ListOBjectIDMessage;

    public ArrayList<String> getListOBjectIDMessage() {
        return ListOBjectIDMessage;
    }

    public void setListOBjectIDMessage(ArrayList<String> listOBjectIDMessage) {
        ListOBjectIDMessage = listOBjectIDMessage;
    }

//    String theirName;

    public ListTabChatAdapter(Context context, int LayoutId,
                              List<GroupItemChat> objects) {
        super(context, LayoutId, objects);
        this.context = context;
        this.layoutId = LayoutId;
        this.conversation = objects;
        userId = getUserId();
        statuses = Collections.synchronizedList(new ArrayList<String>());
        for (int i = 0; i < conversation.size(); i++) {
            statuses.add("");
        }
        new MessageTask().execute();
    }

    @Override
    public GroupItemChat getItem(int position) {
        return conversation.get(position);
    }

    @Override
    public int getCount() {
        return conversation.size();
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();
            holder.avatar = (ImageView) convertView
                    .findViewById(R.id.iv_ava_tab_chat);
            holder.name = (TextView) convertView
                    .findViewById(R.id.conversation_name);
            holder.conversation = (TextView) convertView
                    .findViewById(R.id.conversation_text);
            holder.time = (TextView) convertView
                    .findViewById(R.id.conversation_time);
            holder.notificationContain = (RelativeLayout) convertView.findViewById(R.id.tab_notification_container_conversation);
            holder.countNotification = (TextView) convertView.findViewById(R.id.tab_notification_conversation);
            holder.name.setSelected(true);
            holder.conversation.setSelected(true);
            holder.time.setSelected(true);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        GroupItemChat groupItemChat = conversation.get(index);

        if (groupItemChat.chatItems != null && groupItemChat.chatItems.size() != 0) {
            ParseObject chatItem = groupItemChat.chatItems.get(0).parseObject;
            holder.time.setText(calculateTime(chatItem.getUpdatedAt().getTime()));
        }

        holder.name.setText(groupItemChat.groupName);
        if (groupItemChat.listUnread.size() > 0) {
            holder.notificationContain.setVisibility(View.VISIBLE);
            holder.countNotification.setText(String.valueOf(groupItemChat.listUnread.size()));
        } else {
            holder.notificationContain.setVisibility(View.GONE);
        }                            notifyDataSetChanged();


        if (!groupItemChat.avatar.isEmpty()) {
            Picasso.with(context).load(groupItemChat.avatar).transform(new CircleTransform()).into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.no_avatar);
        }

//        if (!chatItem.getParseObject("objectIDOfClassUser").getObjectId().equals(userId)) {
//            theirName = Utils.getAvatarUser(chatItem.getParseObject("objectIDOfClassUser").getObjectId())[0];
//            holder.conversation.setText(String.format("%s" + ":" + "%s", theirName, chatItem.getString("message")));
//        } else {
//            holder.conversation.setText(chatItem.getString("message"));
//        }
        holder.conversation.setText(statuses.get(index));

        return convertView;
    }

    private String getUserId() {
        SharedPreferences ref = context.getSharedPreferences(Config.SHARE_PREFERENCES, 0);
        return ref.getString(Config.USERID_KEY, null);
    }

    private String calculateTime(long created) {
        long current = System.currentTimeMillis();
        long delta = ((current - created) / 1000);
        String hourAgo;
        if (delta <= 59) {
            hourAgo = "Just now";
        } else if (delta >= 60 && delta < 2 * 60) {
            hourAgo = "A minute ago";
        } else if (delta >= 2 * 60 && delta < 60 * 60) {
            hourAgo = (delta / 60) + " minutes ago";
        } else if (delta >= 60 * 60 && delta <= 2 * 60 * 60) {
            hourAgo = "An hour ago";
        } else if (delta >= 2 * 60 * 60 && delta < 24 * 60 * 60) {
            hourAgo = (delta / (60 * 60)) + " hours ago";
        } else if (delta >= 24 * 60 * 60 && delta <= 2 * 24 * 60 * 60) {
            hourAgo = "Yesterday";
        } else if (delta >= 2 * 24 * 60 * 60 && delta < 7 * 24 * 60 * 60) {
            hourAgo = (delta / (24 * 60 * 60)) + " days ago";
        } else if (delta >= 7 * 24 * 60 * 60 && delta < 2 * 7 * 24 * 60 * 60) {
            hourAgo = "A week ago";
        } else if (delta >= 8 * 24 * 60 * 60 && delta < 30 * 24 * 60 * 60) {
            hourAgo = (delta / (7 * 24 * 60 * 60)) + " weeks ago";
        } else {
            int month = (int) (delta / (30 * 24 * 60 * 60));
            if (month == 1)
                hourAgo = "1 month ago";
            else
                hourAgo = month + " months ago";
        }
        return hourAgo;
    }

//    public int CountUnreadmessage(boolean type, String idFind) {
//
//        try {
//            ArrayList<ParseObject> mess;
//            ParseQuery<ParseObject> alreadyReadMessage = ParseQuery.getQuery("Member");
//            alreadyReadMessage.whereEqualTo("objectId", userId);
//            List<String> alreadyReadMessageIDS = alreadyReadMessage.find().get(0).getList("alreadyReadMessageIDs");
//
//
//            JSONObject obj = new JSONObject();
//            obj.put("objectId", userId);
//            ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
//            arr.add(obj);
//            if (type == false) {
//                ListOBjectIDMessage = new ArrayList<String>();
//                mess = new ArrayList<ParseObject>();
//                ParseQuery<ParseObject> member = ParseQuery.getQuery("Member");
//                member.whereEqualTo("objectId", idFind);
//                ParseQuery<ParseObject> userOther = ParseQuery.getQuery("Messages");
//                userOther.whereDoesNotExist("group");
//                userOther.whereMatchesQuery("objectIDOfClassUser", member);
//                userOther.whereContainedIn("receivers", arr);
//                mess = (ArrayList<ParseObject>) userOther.find();
//                if (mess != null) {
//                    for (ParseObject object : mess) {
//                        ListOBjectIDMessage.add(object.getObjectId());
//                    }
//                }
//
//
//            } else {
//                ListOBjectIDMessage = new ArrayList<String>();
//                mess = new ArrayList<ParseObject>();
//                ParseQuery<ParseObject> group = ParseQuery.getQuery("MessageGroups");
//                group.whereEqualTo("objectId", idFind);
//                ParseQuery<ParseObject> userOther = ParseQuery.getQuery("Messages");
//                userOther.whereExists("group");
//                userOther.whereMatchesQuery("group", group);
//                userOther.whereContainedIn("receivers", arr);
//                mess = (ArrayList<ParseObject>) userOther.find();
//                if (mess != null) {
//                    for (ParseObject object : mess) {
//                        ListOBjectIDMessage.add(object.getObjectId());
//                    }
//                }
//
//
//            }
//            if (ListOBjectIDMessage.size() > Utils.getAlreadyReadMessageIDs(userId).size()) {
//                setListOBjectIDMessage(ListOBjectIDMessage);
//
//                return ListOBjectIDMessage.size() - Utils.getAlreadyReadMessageIDs(userId).size();
//            }
//
//        } catch (Exception e) {
//        }
//        return 0;
//
//    }

    private static class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView conversation;
        public TextView time;
        public RelativeLayout notificationContain;
        public TextView countNotification;
    }

    private class MessageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "conversation size " + conversation.size());
            if (conversation.size() > 0) {
                for (int i = 0; i < conversation.size(); i++) {
                    Log.e(TAG, "chatItems null? " + (conversation.get(i).chatItems == null));
                    Log.e(TAG, "chatItems size " + conversation.get(i).chatItems.size());
                    if (conversation.get(i).chatItems != null && conversation.get(i).chatItems.size() > 0) {
                        ParseObject chatItem = conversation.get(i).chatItems.get(0).parseObject;
                        String id = chatItem.getParseObject("objectIDOfClassUser").getObjectId();
                        Log.e(TAG, "message " + chatItem.getString("message"));
                        if (!id.equals(userId)) {
                            fetchAvatarUser(chatItem.getString("message"), id, i);
                        } else {
                            statuses.set(i, chatItem.getString("message"));
                        }
                        Log.e(TAG, statuses.toString());
                    }
                }
            }
            return null;
        }

        public void fetchAvatarUser(final String message, String id, final int index) {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
                ParseObject object = query.get(id);

                String name = "";
                if (object.getString("fullName") != null) {
                    name = object.getString("fullName");
                }
                statuses.set(index, name + ":" + message);
                Log.e(TAG, "fetchAvatarUser " + message);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            } catch (ParseException e) {
                Log.e(TAG, "fetchAvatarUser", e);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e(TAG, "reset");
            notifyDataSetChanged();
        }
    }
}
