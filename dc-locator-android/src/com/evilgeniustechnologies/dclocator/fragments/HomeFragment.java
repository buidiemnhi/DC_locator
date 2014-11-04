package com.evilgeniustechnologies.dclocator.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;
import com.evilgeniustechnologies.dclocator.adapters.HomeListAdapter;
import com.evilgeniustechnologies.dclocator.commons.CircleTransform;
import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.dc_locator.LoginActivity;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;
import com.evilgeniustechnologies.dclocator.dialogs.CheckinDialog;
import com.evilgeniustechnologies.dclocator.dialogs.DialogFactory;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.models.Member;
import com.evilgeniustechnologies.dclocator.service.ParseService;
import com.evilgeniustechnologies.dclocator.type.DialogType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends BaseFragment implements OnClickListener {
    public static final String TAG = "EGT.HomeFragment";

    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();

    private boolean hasSetUpInitialLocation = false;
    private String selectedObjectId;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private float radius;
    private float lastRadius;
    private int DEFAULT_MAP_ZOOM = 15;
    List<ParseObject> Checkin;
    List<Member> haveGeo;
    List<com.evilgeniustechnologies.dclocator.daos.Member> lastLocation;
    CheckinDialog checkin;

    MysupportFragment sfmap;
    GoogleMap gmap;
    MapView mapview;
    ImageView im_dc_home;
    TextView tv_checkIn, tv_logout;

    Activity ac;
    SupportMapFragment frafment;
    ListView listView;
    ParseGeoPoint mypoint;
    List<ParseObject> latlng;
    public Boolean isPublic;
    Member us;
    String userId, userName;

    Boolean isSaveLocalLocation = false;

    public Boolean getIsSaveLocalLocation() {
        return isSaveLocalLocation;
    }

    public void setIsSaveLocalLocation(Boolean isSaveLocalLocation) {
        this.isSaveLocalLocation = isSaveLocalLocation;
    }

    public Member FindUserLoginLocal() {
        try {
            us = Member.getQuery().get(userId);
        } catch (Exception e) {
            Log.e(TAG, "findUserLoginLocal", e);
        }
        return us;
    }

    public void setUs(Member uss) {
        us = uss;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "start onCreateView");

        SharedPreferences ref = getActivity().getSharedPreferences(
                Config.SHARE_PREFERENCES, 0);

        userId = ref.getString(Config.USERID_KEY, null);
        userName = ref.getString(Config.EMAIL_KEY, null);
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);

        View v = inflater.inflate(R.layout.navigation_home_fragment, null);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setCustomView(v);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        setMapTransparent((ViewGroup) view);
        ac = getActivity();
        setUs(FindUserLoginLocal());
        tv_checkIn = (TextView) v.findViewById(R.id.tv_checkin);
        tv_checkIn.setOnClickListener(this);

        tv_logout = (TextView) v.findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);
        im_dc_home = (ImageView) v.findViewById(R.id.iv_dc_home);
        listView = (ListView) view.findViewById(R.id.fm_selected);
        mapview = (MapView) view.findViewById(R.id.mapshere);
        MapsInitializer.initialize(getActivity());
        mapview.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        gmap = mapview.getMap();
        if (gmap == null) {
            Toast.makeText(ac, "Sorry! unable to create maps", Toast.LENGTH_LONG).show();
        } else {
            setMapDefualtProp();
        }

        gmap.getUiSettings().setMyLocationButtonEnabled(false);

        gmap.setMyLocationEnabled(true);

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                Config.lat, Config.lng), DEFAULT_MAP_ZOOM));
        Log.e(TAG, "first load");
        doMapQuery();

        gmap.setTrafficEnabled(true);

        gmap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition pos) {
                Log.e(TAG, "camera changed -> reload");
                gmap.clear();
                doMapQuery();
            }
        });

        ParseService.startActionCountUnreadMessages(getActivity());

        Log.e(TAG, "end onCreateView");
        return view;
    }

    public String getLocation() {
        Geocoder geocoder;
        String ad = null;
        Address fullAddress;
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Config.lat, Config.lng, 1);
        } catch (IOException e) {
            Log.e(TAG, "getLocation", e);
        }
        if (addresses != null) {
            fullAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");
            for (int i = 0; i < fullAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(fullAddress.getAddressLine(i))
                        .append(",");
            }
            ad = strReturnedAddress.toString();
        } else {
            Toast.makeText(getActivity(), "can not get location right now",
                    Toast.LENGTH_LONG).show();
        }
        return ad;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
        gmap.clear();
        doMapQuery();

    }

    public void CheckInLocation() {
        checkin = (CheckinDialog) DialogFactory.GetDialog(getActivity(),
                DialogType.DIALOG_CHECKIN);
        checkin.show();
        checkin.setPublicClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isPublic = true;
                pushToLocation();
                doMapQuery();
                checkin.dismiss();
            }
        });
        checkin.setPrivateClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isPublic = false;
                pushToLocation();
                doMapQuery();
                checkin.dismiss();
            }
        });
        checkin.setClose(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkin.dismiss();
            }
        });
    }

    public void pushToLocation() {
        ParseObject location = new ParseObject("Location");
        String city = getCity();
        if (city != null) {
            location.put("city", city);
        }
        String country = getCountry();
        if (country != null) {
            location.put("country", country);
        }
        location.put("public", isPublic);
        String state = getState();
        if (state != null) {
            location.put("state", state);
        }
        location.put("user", us);
        location.put("fullAddress", getLocation());
        location.put("checkinLocation", new ParseGeoPoint(Config.lat, Config.lng));
        if (!checkin.getIs_home() && isPublic) {
            location.put("departureDate", checkin.getdateTime());
        }
        if (isPublic && checkin.getIs_home()) {
            us.put("homeLocation", new ParseGeoPoint(Config.lat, Config.lng));
            us.put("lastLocation", new ParseGeoPoint(Config.lat, Config.lng));
        }
        if (!isPublic && checkin.getIs_home()) {
            us.put("homeLocation", new ParseGeoPoint(Config.lat, Config.lng));
        }
        if (isPublic && !checkin.getIs_home()) {
            us.put("lastLocation", new ParseGeoPoint(Config.lat, Config.lng));
        }
        try {
            location.save();
        } catch (Exception e) {
            Log.e(TAG, "save location", e);
        }
        setIsSaveLocalLocation(true);
        if (getIsSaveLocalLocation()) {
            if (isPublic) {
                us.put("lastCheckIn", location);
            }
            try {
                us.save();
            } catch (Exception e) {
                Log.e(TAG, "save member", e);
            }
        } else {
            Toast.makeText(getActivity(), "can not save lastCheckIn for user", Toast.LENGTH_LONG).show();
        }
    }

    private List<Address> getAddresses() {
        Geocoder geocoder;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            return geocoder.getFromLocation(Config.lat, Config.lng, 1);
        } catch (IOException e) {
            Log.e(TAG, "getFromLocation", e);
        }
        return null;
    }

    public String getCity() {
        String city = null;
        List<Address> addresses = getAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            city = addresses.get(0).getAdminArea();
            if (city.contains("City")) {
                String[] tmp = city.split("City");
                city = tmp[0];
            }
        }
        return city;
    }

    public String getState() {
        String state = null;
        List<Address> addresses = getAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            state = addresses.get(0).getAdminArea();
            if (state == null) {
                state = addresses.get(0).getSubAdminArea();
            }
            if (state.contains("State")) {
                String[] tmp = state.split("State");
                state = tmp[0];
            }
        }
        return state;
    }

    public String getCountry() {
        String country = null;
        List<Address> addresses = getAddresses();
        if (addresses != null && addresses.isEmpty()) {
            country = addresses.get(0).getCountryName();
        }
        return country;
    }

    public void doMapQuery() {
        ParseQuery<ParseObject> ft = Datastore.getMemberQuery();
        ft.include("lastCheckIn");
        ft.whereNotEqualTo("objectId", userId);
        ft.whereWithinGeoBox(
                "lastLocation",
                new ParseGeoPoint(
                        gmap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude,
                        gmap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude),
                new ParseGeoPoint(
                        gmap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude,
                        gmap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude));
        ft.findInBackground(mapCallback);
    }

    private FindCallback<ParseObject> mapCallback = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> parseObjects, ParseException e) {
            if (e == null) {
                try {
                    lastLocation = com.evilgeniustechnologies.dclocator.daos.Member.parseMembers(datastore, parseObjects);
                } catch (ParseException e1) {
                    Log.e(TAG, "parseMembers", e1);
                }
                if (lastLocation != null && !lastLocation.isEmpty()) {
                    for (final com.evilgeniustechnologies.dclocator.daos.Member member : lastLocation) {
                        ParseGeoPoint geoPoint = new ParseGeoPoint(member.getLastLocationLatitude(), member.getLastLocationLongitude());
                        gmap.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(createPin())));
                        gmap.setInfoWindowAdapter(new WindowAdapter(member));
                        gmap.setOnMarkerClickListener(new MarkerListener());
                        gmap.setOnInfoWindowClickListener(new WindowListener(member));
                    }

                    HomeListAdapter homeListAdapter = new HomeListAdapter(getActivity(), lastLocation);
                    listView.setAdapter(homeListAdapter);
                    listView.invalidate();
                    listView.setOnItemClickListener(mapListener);
                }
            } else {
                Log.e(TAG, "map query", e);
            }
        }
    };

    private OnItemClickListener mapListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            UserProFileFragment user = new UserProFileFragment();
            Config.isHome = true;
            Bundle bundle = new Bundle();
            com.evilgeniustechnologies.dclocator.daos.Member item = lastLocation.get(position);
            bundle.putString("id", item.getObjectId());
            user.setArguments(bundle);
            FragmentTransaction trans = getActivity().getFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.fragment_container, user);
            trans.addToBackStack(null);
            trans.commit();
        }
    };

    private class WindowAdapter implements InfoWindowAdapter {
        private com.evilgeniustechnologies.dclocator.daos.Member member;

        private WindowAdapter(com.evilgeniustechnologies.dclocator.daos.Member member) {
            this.member = member;
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoContents(Marker arg0) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.infor_windows_layout, null);
            ImageView iv_avatar_snippet = (ImageView) v.findViewById(R.id.iv_ava_snippet);
            TextView tv_name_snippet = (TextView) v.findViewById(R.id.tv_name_snippet);
            TextView tv_last_seen = (TextView) v.findViewById(R.id.tv_lastseen_snippet);
            if (!TextUtils.isEmpty(member.getAvatar())) {
                Picasso.with(getActivity()).load(member.getAvatar()).transform(new CircleTransform()).into(iv_avatar_snippet);
            } else {
                iv_avatar_snippet.setImageResource(R.drawable.user_default);
            }
            tv_name_snippet.setText(member.getFullName());
            tv_last_seen.setText("Last seen at " + member.getUpdatedAt().toString());
            return v;
        }
    }

    private class MarkerListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return false;
        }
    }

    private class WindowListener implements GoogleMap.OnInfoWindowClickListener {
        private com.evilgeniustechnologies.dclocator.daos.Member member;

        private WindowListener(com.evilgeniustechnologies.dclocator.daos.Member member) {
            this.member = member;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            UserProFileFragment user = new UserProFileFragment();
            Config.isHome = true;
            Bundle bundle = new Bundle();
            bundle.putString("id", member.getObjectId());
            user.setArguments(bundle);
            FragmentTransaction trans = getActivity().getFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.fragment_container, user);
            trans.addToBackStack(null);
            trans.commit();
        }
    }

    public Bitmap createPin() {
        int widthDevice = com.evilgeniustechnologies.dclocator.utils.ResizeUtils.getSizeDevice(this.getActivity()).x;
        return Bitmap
                .createScaledBitmap(BitmapFactory.decodeResource(
                                getResources(), R.drawable.anchor),
                        (int) (widthDevice * 0.14), (int) (widthDevice * 0.17),
                        false);
    }

    private void setMapDefualtProp() {
        gmap.setMyLocationEnabled(true);
        gmap.setTrafficEnabled(true);
        gmap.getUiSettings().setMyLocationButtonEnabled(true);
        gmap.getUiSettings().setRotateGesturesEnabled(true);
        gmap.getUiSettings().setCompassEnabled(true);
        gmap.getUiSettings().setZoomGesturesEnabled(true);
        gmap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void setMapTransparent(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                setMapTransparent((ViewGroup) child);
            } else if (child instanceof SurfaceView) {
                child.setBackgroundColor(0x00000000);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_checkin:
                CheckInLocation();
                break;
            case R.id.tv_logout:
                SharedPreferences ref = getActivity().getSharedPreferences(
                        Config.SHARE_PREFERENCES, 0);
                SharedPreferences.Editor editor = ref.edit();
                editor.putString(Config.USERID_KEY, null)
                        .putString(Config.EMAIL_KEY, null)
                        .putString(Config.AVATAR_KEY, null);
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                // | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    @Override
    protected void onReceived(String status) {
        super.onReceived(status);
        if (Datastore.UNREAD_MESSAGES_COUNTED.equals(status)) {
            ((MainViewActivity) getActivity()).updateCountNotification(datastore.getCurrentUser().getTotalUnreadMessages());
        } else if (Datastore.ALL_MESSAGE_GROUPS_PARSED.equals(status)) {
            ((MainViewActivity) getActivity()).updateCountNotification(datastore.getCurrentUser().getTotalUnreadMessages());
        }
    }
}