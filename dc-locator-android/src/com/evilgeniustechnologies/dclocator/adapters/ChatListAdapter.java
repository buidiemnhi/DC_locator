package com.evilgeniustechnologies.dclocator.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.daos.LikedConnection;
import com.evilgeniustechnologies.dclocator.daos.LikedConnectionDao;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.Message;
import com.evilgeniustechnologies.dclocator.local.Datastore;

public class ChatListAdapter extends BaseAdapter implements OnClickListener {

    private static final String TAG = "EGT.ChatListAdapter";
    private Context context;
    private List<Message> mMessages;
    private Member member;
    private SimpleDateFormat formatter;
    private Message message;
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public ChatListAdapter(Context context, Member member, List<Message> messages) {
        this.context = context;
        this.mMessages = messages;
        this.member = member;
        Log.e(TAG, "messages size " + mMessages.size());
        formatter = new SimpleDateFormat("MMM dd yyyy, h:mm aa");
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addMessage(Message message) {
        mMessages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            Log.e(TAG, "start list chat adapter");
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.chat_item, parent, false);
            holder = new ViewHolder();
            holder.imageLeft = (ImageView) convertView
                    .findViewById(R.id.ivProfileLeft);
            holder.imageRight = (ImageView) convertView
                    .findViewById(R.id.ivProfileRight);
            holder.messageLeft = (TextView) convertView
                    .findViewById(R.id.tvTextLeft);
            holder.messageRight = (TextView) convertView
                    .findViewById(R.id.tvTextRight);
            holder.tvDate = (TextView) convertView
                    .findViewById(R.id.tv_dateTime);
            holder.tvMyName = (TextView) convertView
                    .findViewById(R.id.tvNameMy);
            holder.tvTheirName = (TextView) convertView
                    .findViewById(R.id.tvNameOther);
            holder.likeLeft = (ImageView) convertView.findViewById(R.id.iv_like_left);
            holder.likeLeft.setOnClickListener(this);
            holder.likeRight = (ImageView) convertView.findViewById(R.id.iv_like_right);
            holder.likeRight.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        message = mMessages.get(position);
        final boolean isMe = message.getOwnerId()
                .equals(member.getObjectId());
        final ImageView profileView = isMe ? holder.imageRight
                : holder.imageLeft;
        final ImageView like = isMe ? holder.likeLeft : holder.likeRight;

        holder.tvDate.setText(formatter.format(message.getUpdatedAt()));
        try {
            List<LikedConnection> likedConnections = message.getLikedByConnections();
            if (likedConnections == null || likedConnections.isEmpty()) {
                like.setBackgroundResource(R.drawable.like_clear);
            } else {
                for (LikedConnection likedConnection : likedConnections) {
                    if (likedConnection.getLikedUserId().equals(member.getObjectId())) {
                        like.setBackgroundResource(R.drawable.like_red);
                        setLiked(true);
                    }
                }
                if (!isLiked) {
                    like.setBackgroundResource(R.drawable.like_gray);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "handle likes", e);
        }

        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.tvTheirName.setVisibility(View.GONE);
            holder.tvMyName.setVisibility(View.VISIBLE);
            holder.tvMyName.setText(member.getFullName());
            holder.messageRight.setVisibility(View.VISIBLE);
            holder.messageLeft.setVisibility(View.GONE);
            holder.messageRight.setText(message.getContent());
//            holder.likeRight.setVisibility(View.GONE);
//            holder.likeLeft.setVisibility(View.VISIBLE);
        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.tvMyName.setVisibility(View.GONE);
//            holder.likeLeft.setVisibility(View.GONE);
//            holder.likeRight.setVisibility(View.VISIBLE);
            Member anotherMember = message.getOwner();
            holder.tvTheirName.setVisibility(View.VISIBLE);
            holder.tvTheirName.setText(anotherMember.getFullName());
            holder.messageRight.setVisibility(View.GONE);
            holder.messageLeft.setVisibility(View.VISIBLE);
            holder.messageLeft.setText(message.getContent());
        }

        Member user = message.getOwner();
        if (user.getAvatar() != null) {
            Picasso.with(context).load(user.getAvatar())
                    .transform(new CircleTransform()).into(profileView);
        } else {
            profileView.setBackgroundResource(R.drawable.dynamite_2x);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView messageLeft;
        public TextView messageRight;
        public TextView tvDate;
        public TextView tvMyName;
        public TextView tvTheirName;
        public ImageView likeLeft;
        public ImageView likeRight;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_like_left:
            case R.id.iv_like_right:
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("member", member.getObjectId());
                hashMap.put("message", message.getObjectId());
                if (isLiked()) {
                    hashMap.put("liked", false);
                } else {
                    hashMap.put("liked", true);
                }
                ParseCloud.callFunctionInBackground("markAsLiked", hashMap,
                        new FunctionCallback<ParseObject>() {

                            @Override
                            public void done(ParseObject arg0, ParseException arg1) {
                                if (arg1 != null) {
                                    Log.e(TAG, "markAsLiked 1", arg1);
                                } else {
                                    Member user = Datastore.getInstance().getCurrentUser();
                                    if (isLiked()) {
                                        LikedConnection connection = new LikedConnection();
                                        connection.setLikedUser(user);
                                        connection.setLikedMessage(message);
                                        Datastore.getInstance().getLikedConnectionDao().insert(connection);
                                    } else {
                                        LikedConnection connection = Datastore.getInstance()
                                                .getLikedConnectionDao()
                                                .queryBuilder()
                                                .where(LikedConnectionDao.Properties.LikedUserId.eq(user.getObjectId()))
                                                .where(LikedConnectionDao.Properties.LikedMessageId.eq(message.getObjectId()))
                                                .unique();
                                        if (connection != null) {
                                            Datastore.getInstance().getLikedConnectionDao().deleteInTx(connection);
                                        }
                                    }
                                }
                            }
                        });
                break;
        }
    }
}
