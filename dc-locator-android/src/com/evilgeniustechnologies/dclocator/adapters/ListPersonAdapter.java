package com.evilgeniustechnologies.dclocator.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.utils.Utils;

public class ListPersonAdapter extends ArrayAdapter<Member> {
	private static final String TAG = "EGT.ListPersonAdapter";
	private List<Member> contents;
	private List<Member> current;
	private MemberFilter memberFilter;
	private OnContactSelectListener listener;
	private int type;
	private MessageGroup group;

	public ListPersonAdapter(Activity activity, List<Member> contents,
			int type, MessageGroup group) {
		super(activity, R.layout.member_item_list, contents);
		this.contents = new ArrayList<Member>(contents);
		this.current = new ArrayList<Member>(contents);
		this.type = type;
		this.group = group;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Member member = current.get(position);
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.member_item_list,
					null);
			ViewHolder holder = new ViewHolder();
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_member_name);
			holder.tvInstall = (TextView) convertView
					.findViewById(R.id.tv_avail);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.iv_member_avatar);
			holder.owner = (TextView) convertView.findViewById(R.id.owner);
			holder.owner.setVisibility(View.GONE);
			holder.itemListener = new ItemListener();
			convertView.setOnClickListener(holder.itemListener);
			convertView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();

		if (type == 0) {
			holder.tvInstall.setVisibility(View.GONE);
		}
		if (member.getObjectId().equals(
				Datastore.getInstance().getCurrentUserId())) {
			holder.tvName.setText("You");

		} else {
			holder.tvName.setText(member.getFullName());

		}
		if (group != null) {
			if (group.getOwnerId().equals(member.getObjectId())) {
				holder.owner.setVisibility(View.VISIBLE);
			} else {
				holder.owner.setVisibility(View.GONE);
			}
		}
		if (member.getAvailable()) {
			holder.tvInstall.setText("Available");
			if (member.getAvatar() != null) {
				Picasso.with(getContext()).load(member.getAvatar())
						.transform(new CircleTransform()).into(holder.avatar);
			}
			holder.tvName.setTextColor(getContext().getResources().getColor(
					android.R.color.black));
			holder.tvInstall.setTextColor(getContext().getResources().getColor(
					android.R.color.black));
			holder.tvInstall.setTypeface(null, Typeface.NORMAL);
		} else {
			holder.tvInstall.setText("*Has not downloaded the app*");
			holder.tvName.setTextColor(getContext().getResources().getColor(
					android.R.color.darker_gray));
			holder.tvInstall.setTextColor(getContext().getResources().getColor(
					android.R.color.darker_gray));
			holder.tvInstall.setTypeface(null, Typeface.ITALIC);
		}
		holder.itemListener.setUserId(member.getObjectId());
		holder.itemListener.setName(member.getFullName());
		holder.itemListener.setInstall(member.getAvailable());
		return convertView;
	}

	public void setOnContactSelectListener(OnContactSelectListener listener) {
		this.listener = listener;
	}

	@Override
	public Filter getFilter() {
		if (memberFilter == null) {
			memberFilter = new MemberFilter();
		}
		return memberFilter;
	}

	private class ItemListener implements OnClickListener {
		private String userId;
		private String name;
		private boolean available;

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setInstall(boolean available) {
			this.available = available;
		}

		@Override
		public void onClick(View v) {
			if (available) {
				listener.onClick(userId, name);
			}
		}
	}

	private static class ViewHolder {
		TextView tvName;
		TextView tvInstall;
		ImageView avatar;
		TextView owner;
		ItemListener itemListener;

	}
	  public ArrayList<String> getObjectIds() {
	        ArrayList<String> list = new ArrayList<String>();
	        for (Member content : contents) {
	        	Utils.Tuple tuple= new Utils.Tuple(content.getObjectId(), content.getFullName());
	            list.add(tuple.head);
	        }
	        return list;
	    }
	public interface OnContactSelectListener {
		public void onClick(String userId, String name);
	}

	private class MemberFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (TextUtils.isEmpty(constraint)) {
				// No filter implemented we return all the list
				results.values = contents;
				results.count = contents.size();
			} else {
				// We perform filtering operation
				List<Member> filtered = new ArrayList<Member>();

				for (Member member : contents) {
					if ((member.getFullName()).toLowerCase().startsWith(
							constraint.toString().toLowerCase())) {
						filtered.add(member);
					} else {
						final String[] words = (member.getFullName())
								.toLowerCase().split(" ");
						for (String word : words) {
							if (word.startsWith(constraint.toString()
									.toLowerCase())) {
								filtered.add(member);
								break;
							}
						}
					}
				}
				results.values = filtered;
				results.count = filtered.size();
				Log.e("search count", String.valueOf(results.count));
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// Now we have to inform the adapter about the new list filtered
			// animationAdapter.reset();
			current = (List<Member>) results.values;
			clear();
			addAll(current);
			Log.e("count", String.valueOf(results.count));
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}