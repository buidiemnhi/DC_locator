package com.evilgeniustechnologies.dclocator.dc_locator;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.ParseAnalytics;

import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.fragments.ChatsFragment;
import com.evilgeniustechnologies.dclocator.fragments.HomeFragment;
import com.evilgeniustechnologies.dclocator.fragments.MyProfileFragment;
import com.evilgeniustechnologies.dclocator.fragments.SearchFragment;
import com.evilgeniustechnologies.dclocator.local.Datastore;
import com.evilgeniustechnologies.dclocator.utils.ResizeUtils;

public class MainViewActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "EGT.MainViewActivity";
    private Fragment tabFragment;
    private Fragment naviFragment;
    private Fragment homeActivity;
    private ChatsFragment chatFragment;
    private Fragment proactivity;
    private Fragment searchactivity;
    //	Bundle bun;
    ImageView home, search, chat, profile;
    RelativeLayout tab_noti, rl_tab_home, rl_tab_chat, rl_tab_search,
            rl_my_profile;
    TextView countNoti;
    private Tab tab;

    ProgressDialog progressDialog;

    com.evilgeniustechnologies.dclocator.utils.GPSTracker gps;

    Handler handle;
    Boolean isEnable = false;

    Boolean isSaveAll = false;
    Boolean isSaveAllInstall = false;
    static String userId;

    private int countNotification;

    public Boolean getIsSaveAllInstall() {
        return isSaveAllInstall;
    }

    public void setIsSaveAllInstall(Boolean isSaveAllInstall) {
        this.isSaveAllInstall = isSaveAllInstall;
    }

    public Boolean getIsSaveAll() {
        return isSaveAll;
    }

    public void setIsSaveAll(Boolean isSaveAll) {
        this.isSaveAll = isSaveAll;
    }

    String username;

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }


    public enum Tab {
        HOME, MYPROFILE, CHAT, SEARCH
    }

    private void displayTab() {

        home.setImageResource(R.drawable.home_anim);
        chat.setImageResource(R.drawable.chats_anim);
        search.setImageResource(R.drawable.search_anim);
        profile.setImageResource(R.drawable.profile_anim);
        switch (tab) {
            case HOME:
                home.setImageResource(R.drawable.blue_home);
                chat.setImageResource(R.drawable.gray_chats);
                profile.setImageResource(R.drawable.gray_profile);
                search.setImageResource(R.drawable.gray_search);
                break;
            case CHAT:
                chat.setImageResource(R.drawable.blue_chats);
                home.setImageResource(R.drawable.gray_home);
                profile.setImageResource(R.drawable.gray_profile);
                search.setImageResource(R.drawable.gray_search);
                break;
            case SEARCH:
                search.setImageResource(R.drawable.blue_search);
                home.setImageResource(R.drawable.gray_home);
                chat.setImageResource(R.drawable.gray_chats);
                profile.setImageResource(R.drawable.gray_profile);
                break;
            case MYPROFILE:
                profile.setImageResource(R.drawable.blue_profile);
                home.setImageResource(R.drawable.gray_home);
                chat.setImageResource(R.drawable.gray_chats);
                search.setImageResource(R.drawable.gray_search);
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpened(getIntent());

        setContentView(R.layout.main_view);

        gps = new com.evilgeniustechnologies.dclocator.utils.GPSTracker(MainViewActivity.this);

        if (gps.canGetLocation()) {
            Config.lat = gps.getLatitude();
            Config.lng = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
        SharedPreferences ref = getSharedPreferences(Config.SHARE_PREFERENCES,
                0);

        userId = ref.getString(Config.USERID_KEY, null);
        username = ref.getString(Config.EMAIL_KEY, null);
        handle = new Handler();
        progressDialog = new ProgressDialog(this);

        tabFragment = getFragmentManager().findFragmentById(
                R.id.tab_fragment);
        home = (ImageView) tabFragment.getView().findViewById(R.id.tab_home);
        chat = (ImageView) tabFragment.getView().findViewById(R.id.tab_chats);
        search = (ImageView) tabFragment.getView()
                .findViewById(R.id.tab_search);

        profile = (ImageView) tabFragment.getView().findViewById(
                R.id.tab_profile);
        tab_noti = (RelativeLayout) tabFragment.getView().findViewById(
                R.id.tab_notification_container);
        countNoti = (TextView) tabFragment.getView().findViewById(
                R.id.tab_notification);
        rl_my_profile = (RelativeLayout) tabFragment.getView().findViewById(
                R.id.rl_tab_profile);
        rl_tab_chat = (RelativeLayout) tabFragment.getView().findViewById(
                R.id.rl_tab_chat);
        rl_tab_home = (RelativeLayout) tabFragment.getView().findViewById(
                R.id.rl_tab_home);
        rl_tab_search = (RelativeLayout) tabFragment.getView().findViewById(
                R.id.rl_tab_search);

        AdjustUserInterface();
        rl_my_profile.setOnClickListener(this);
        rl_tab_home.setOnClickListener(this);
        rl_tab_chat.setOnClickListener(this);
        rl_tab_search.setOnClickListener(this);

//        setCountNotification(Utils.CountMessageInTabbar(this));
//        if (CountNotification > 0) {
//            countNoti.setText(String.valueOf(CountNotification));
//            tab_noti.setVisibility(View.VISIBLE);
//        } else {
//            tab_noti.setVisibility(View.GONE);
//        }
        onSelectTabHome(null);
//        ParseService.startActionCountUnreadMessages(this);
//        ParseService.startActionParseAllGroups(this);
//        String groupId = datastore.getGroupDao().loadAll().get(5).getObjectId();
//        Log.e(TAG, "groupId " + groupId);
//        ParseService.startActionParseAllMessages(this, groupId);
    }

    public void AdjustUserInterface() {

        ResizeUtils.resizeRelativeLayout(rl_tab_home,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenWidth * 0.25),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.1), 0, 0, 0, 0);
        ResizeUtils.resizeRelativeLayout(rl_tab_chat,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenWidth * 0.25),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.1), 0, 0, 0, 0);
        ResizeUtils.resizeRelativeLayout(rl_my_profile,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenWidth * 0.25),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.1), 0, 0, 0, 0);
        ResizeUtils.resizeRelativeLayout(rl_tab_search,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenWidth * 0.25),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.1), 0, 0, 0, 0);
        ResizeUtils.resizeImageView(chat,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09), 0, 0,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.01), 0,
                RelativeLayout.CENTER_IN_PARENT);
        ResizeUtils.resizeImageView(home,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09), 0, 0,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.01), 0,
                RelativeLayout.CENTER_IN_PARENT);
        ResizeUtils.resizeImageView(search,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09), 0, 0,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.01), 0,
                RelativeLayout.CENTER_IN_PARENT);
        ResizeUtils.resizeImageView(profile,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09),
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.09), 0, 0,
                (int) (com.evilgeniustechnologies.dclocator.commons.Config.screenHeight * 0.01), 0,
                RelativeLayout.CENTER_IN_PARENT);
    }

    public void onSelectTabHome(View v) {
        if (tab != Tab.HOME) {
            tab = Tab.HOME;
            if (homeActivity == null) {
                homeActivity = new HomeFragment();
            }
            selectTab(homeActivity);
            displayTab();

        }
    }

    public void onSelectTabchat(View v) {
        if (tab != Tab.CHAT) {
            tab = Tab.CHAT;
            if (chatFragment == null) {
                chatFragment = new ChatsFragment();
            }
            selectTab(chatFragment);

            displayTab();
        }
    }

    public void onSelectTabSearch(View v) {
        if (tab != Tab.SEARCH) {
            tab = Tab.SEARCH;
            if (searchactivity == null) {
                searchactivity = new SearchFragment();
            }
            selectTab(searchactivity);

            displayTab();
        }
    }

    public void onSelectTabMyProFile(View v) {
        if (tab != Tab.MYPROFILE) {
            tab = Tab.MYPROFILE;
            if (proactivity == null) {
                proactivity = new MyProfileFragment();

            }
            selectTab(proactivity);

            displayTab();
        }
    }


    private void selectTab(Fragment fragment) {
        if (findViewById(R.id.fragment_container) != null) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), 0);
            }
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            transaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_tab_chat:
                onSelectTabchat(null);
                break;
            case R.id.rl_tab_profile:
                onSelectTabMyProFile(null);
                break;
            case R.id.rl_tab_home:
                onSelectTabHome(null);
                break;
            case R.id.rl_tab_search:
                onSelectTabSearch(null);
                break;
            default:
                break;
        }
    }

    public int getCountNotification() {
        return countNotification;
    }

    public void updateCountNotification(int countNotification) {
        this.countNotification = countNotification;
        if (countNotification > 0) {
            countNoti.setText(String.valueOf(countNotification));
            tab_noti.setVisibility(View.VISIBLE);
        } else {
            tab_noti.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(
                "update_data"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

public void HideTabbar(){
	getFragmentManager().beginTransaction().hide(tabFragment).commit();
}
public void ShowTabbar(){
	getFragmentManager().beginTransaction().show(tabFragment).commit();
}

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("mMessageReceiver", "onReceive");
            String message = intent.getStringExtra("message");
            Bundle bundle = intent.getExtras();
            for (String s : bundle.keySet()) {
                Log.e(TAG, s + " : " + bundle.get(s).toString());
            }
            Log.e(TAG, "message received " + message);
            datastore.notifyFragmentObserver(Datastore.NOTIFICATION_RECEIVED);
            Toast.makeText(MainViewActivity.this, bundle.getString("alert"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onReceived(String status) {
        Log.e(TAG, status);
    }
}
