package com.evilgeniustechnologies.dclocator.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.fragments.UserProFileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<Member> implements AdapterView.OnItemClickListener {
    List<Member> members;

    public SearchResultAdapter(Context context, List<Member> members) {
        super(context, R.layout.item_search_person_tab_search, members);
        this.members = new ArrayList<Member>(members);
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_search_person_tab_search, null);
            ViewHolder holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avarresult);
            holder.name = (TextView) convertView.findViewById(R.id.tv_nameResult);
            holder.home = (TextView) convertView.findViewById(R.id.tv_nameHome);
            convertView.setTag(holder);
        }
        Member member = members.get(index);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (!TextUtils.isEmpty(member.getAvatar())) {
            Picasso.with(getContext()).load(member.getAvatar())
                    .transform(new CircleTransform()).into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.user_default);
        }
        holder.name.setText(member.getFullName());
        String homeTown = "";
        if (!TextUtils.isEmpty(member.getCity())) {
            homeTown = member.getCity();
        }
        if (!TextUtils.isEmpty(member.getCountry())) {
            if (!TextUtils.isEmpty(homeTown)) {
                homeTown += ", ";
            }
            homeTown += member.getCity();
        }
        if (!TextUtils.isEmpty(homeTown)) {
            holder.home.setText(homeTown);
            holder.home.setVisibility(View.VISIBLE);
        } else {
            holder.home.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Member member = members.get(position);
        UserProFileFragment user = new UserProFileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", member.getObjectId());
        user.setArguments(bundle);
        FragmentTransaction trans = ((Activity) getContext())
                .getFragmentManager().beginTransaction();
        trans.replace(R.id.fragment_container, user);
        trans.addToBackStack(null);
        trans.commit();
    }

    private static class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView home;
    }
}