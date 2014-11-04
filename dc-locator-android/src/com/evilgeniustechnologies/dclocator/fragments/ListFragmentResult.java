package com.evilgeniustechnologies.dclocator.fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.evilgeniustechnologies.dclocator.R;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import com.evilgeniustechnologies.dclocator.adapters.SearchResultAdapter;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MemberDao;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.models.SearchResult;
import com.evilgeniustechnologies.dclocator.service.ParseService;

public class ListFragmentResult extends BaseFragment implements OnClickListener {
	ListView lvRes;
	TextView lvHint;
	ImageView back;
	SearchResultAdapter adapter;
	ArrayList<SearchResult> res;

	String name;
	String expertise;
	String country;
	String city;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);
		View v = inflater.inflate(R.layout.navigation_search_fragment_result,
				null);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
		getActivity().getActionBar().setCustomView(v);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);

		View view = inflater.inflate(R.layout.fragment_list_result, container,
				false);
		lvRes = (ListView) view.findViewById(R.id.lv_search);
		lvHint = (TextView) view.findViewById(R.id.lv_hint);
		back = (ImageView) v.findViewById(R.id.btn_back_search);
		back.setOnClickListener(this);
		name = getArguments().getString("name");
		expertise = getArguments().getString("expertise");
		country = getArguments().getString("country");
		city = getArguments().getString("city");

		ParseService.startActionSearchMembers(getActivity(), name, expertise,
				country, city);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_search:
			FragmentManager trans = getActivity().getFragmentManager();
			trans.popBackStack();
			break;
		}
	}
	
	
	
	@Override
	protected void onReceived(String status) {
		super.onReceived(status);
		if (Datastore.ALL_MEMBERS_PARSED.equals(status)) {
			QueryBuilder<Member> queryBuilder = Datastore.getInstance()
					.getMemberDao().queryBuilder().orderAsc(MemberDao.Properties.FullName);
			if (!TextUtils.isEmpty(name)) {
			
				queryBuilder.where(MemberDao.Properties.FullName.like(
						name + "%"));
			}
			if(!TextUtils.isEmpty(name)){
				queryBuilder.where(MemberDao.Properties.FullName.like("%"+
						name+"%"));
			}
			if (!TextUtils.isEmpty(expertise)) {
				queryBuilder.where(MemberDao.Properties.Skill.eq(expertise));
			}
			if (!TextUtils.isEmpty(country)) {
				queryBuilder.where(MemberDao.Properties.Country.eq(country));
			}
			if (!TextUtils.isEmpty(city)) {
				queryBuilder.where(MemberDao.Properties.City.like("%" + city
						+ "%"));
			}
			List<Member> members = queryBuilder.list();
			
			if (members != null && !members.isEmpty()) {
				
				adapter = new SearchResultAdapter(getActivity(), members);
				lvRes.setAdapter(adapter);
				lvRes.setOnItemClickListener(adapter);
			} else {
				lvRes.setVisibility(View.GONE);
				lvHint.setVisibility(View.VISIBLE);
			}
		}
	}
}