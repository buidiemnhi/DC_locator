package com.evilgeniustechnologies.dclocator.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.daos.Member;

public class HomeListAdapter extends ArrayAdapter<Member> {
    List<Member> members;

    public HomeListAdapter(Context context, List<Member> lastLocation) {
        super(context, R.layout.item_person_list, lastLocation);
        this.members = lastLocation;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_person_list, null);
            ViewHolder holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_author_avatar_list);
            holder.name = (TextView) convertView.findViewById(R.id.name_list);
            holder.location = (TextView) convertView.findViewById(R.id.location_list);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        Member member = members.get(index);

        holder.name.setText(member.getFullName());
        if (member.getLastCheckIn() == null || TextUtils.isEmpty(member.getLastCheckIn().getFullAddress())) {
            holder.location.setVisibility(View.GONE);
        } else {
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText(member.getLastCheckIn().getFullAddress());
        }
        if (!TextUtils.isEmpty(member.getAvatar())) {
            Picasso.with(getContext()).load(member.getAvatar())
                    .transform(new CircleTransform()).into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.user_default);
        }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView location;
    }
}