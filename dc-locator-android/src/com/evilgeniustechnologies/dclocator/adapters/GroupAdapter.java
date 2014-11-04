package com.evilgeniustechnologies.dclocator.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.daos.EntityConnection;
import com.evilgeniustechnologies.dclocator.daos.EntityConnectionDao;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.Message;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.fragments.ConversationFragment;
import com.evilgeniustechnologies.dclocator.local.Datastore;

/**
 * Created by benjamin on 10/11/14.
 */
public class GroupAdapter extends ArrayAdapter<MessageGroup> implements AdapterView.OnItemClickListener {
    private static final String TAG = "EGT.GroupAdapter";
    List<MessageGroup> groups;

    public GroupAdapter(Context context, List<MessageGroup> groups) {
        super(context, R.layout.item_conversation, groups);
        this.groups = new ArrayList<MessageGroup>(groups);
    }

    public void update(List<MessageGroup> groups) {
        clear();
        this.groups.clear();
        this.groups = new ArrayList<MessageGroup>(groups);
        addAll(this.groups);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_conversation, null);
            ViewHolder holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_ava_tab_chat);
            holder.name = (TextView) convertView.findViewById(R.id.conversation_name);
            holder.conversation = (TextView) convertView.findViewById(R.id.conversation_text);
            holder.time = (TextView) convertView.findViewById(R.id.conversation_time);
            holder.notificationContain = (RelativeLayout) convertView.findViewById(R.id.tab_notification_container_conversation);
            holder.countNotification = (TextView) convertView.findViewById(R.id.tab_notification_conversation);
            holder.unreadMessageIds = new ArrayList<String>();
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        MessageGroup group = groups.get(index);

        if (!group.getIsPrivate()) {
            holder.name.setText(group.getName());
        } else {
            List<EntityConnection> connections = group.getMemberConnections();
            if (connections.get(0).getMemberId().equals(Datastore.getInstance().getCurrentUser().getObjectId())) {
                holder.name.setText(connections.get(1).getMember().getFullName());
            } else {
                holder.name.setText(connections.get(0).getMember().getFullName());
            }
        }

        Message message = group.getLastMessage();
        if (message != null) {
            holder.time.setText(calculateTime(message.getUpdatedAt().getTime()));
            holder.conversation.setText(message.getContent());
            holder.conversation.setTypeface(null, Typeface.NORMAL);
            holder.conversation.setTextColor(getContext().getResources().getColor(android.R.color.black));
        } else {
            holder.time.setText(calculateTime(group.getUpdatedAt().getTime()));
            holder.conversation.setText("*No messages*");
            holder.conversation.setTypeface(null, Typeface.ITALIC);
            holder.conversation.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        }

        int unreadMessages = group.getUnreadMessages().size();
        if (unreadMessages > 0) {
            holder.notificationContain.setVisibility(View.VISIBLE);
            holder.countNotification.setText(unreadMessages + "");
        } else {
            holder.notificationContain.setVisibility(View.GONE);
        }

        if (group.getIsPrivate()) {
            Member otherMember = Datastore.getInstance()
                    .getEntityConnectionDao()
                    .queryBuilder()
                    .where(EntityConnectionDao.Properties.MemberId.notEq(Datastore.getInstance().getCurrentUser().getObjectId()))
                    .where(EntityConnectionDao.Properties.GroupId.eq(group.getObjectId()))
                    .unique()
                    .getMember();
            if (otherMember != null && otherMember.getAvatar() != null) {
                Picasso.with(getContext()).load(otherMember.getAvatar()).transform(new CircleTransform()).into(holder.avatar);
            } else {
                holder.avatar.setImageResource(R.drawable.user_default);
            }
        } else {
            if (group.getIcon() != null) {
                Picasso.with(getContext()).load(group.getIcon()).transform(new CircleTransform()).into(holder.avatar);
            } else {
                holder.avatar.setImageResource(R.drawable.user_default);
            }
        }
        return convertView;
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

//    public void setTotalUnreadMessages(Map<String, Integer> unreadMessages) {
//        this.unreadMessages = unreadMessages;
//        Log.e(TAG, "unread size " + unreadMessages.size());
//        notifyDataSetChanged();
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageGroup selectedGroup = groups.get(position);
        ConversationFragment fragment = new ConversationFragment(selectedGroup);
        FragmentTransaction transaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView conversation;
        public TextView time;
        public RelativeLayout notificationContain;
        public TextView countNotification;
        public List<String> unreadMessageIds;
    }
}
