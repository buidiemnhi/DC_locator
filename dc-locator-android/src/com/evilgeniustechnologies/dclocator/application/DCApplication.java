package com.evilgeniustechnologies.dclocator.application;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;
import com.evilgeniustechnologies.dclocator.models.Location;
import com.evilgeniustechnologies.dclocator.models.Member;
import com.evilgeniustechnologies.dclocator.models.Message;
import com.evilgeniustechnologies.dclocator.models.MessageGroups;

public class DCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Member.class);
        ParseObject.registerSubclass(Location.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(MessageGroups.class);
        Parse.initialize(this, "DT6EDI70cB1fmic24GvQSqjXGzergfJe3eQfZdTo",
                "PSv3Z48pi5wzxawQiZU1xLk3qohZwlI6ZjrzjPMd");

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        PushService.setDefaultPushCallback(this, MainViewActivity.class);
        
        ParsePush.subscribeInBackground("message", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null ) {
                    Log.e("EGT.DCApplication", "subscribe", e);
                } else {
                    Log.e("EGT.DCApplication", "subscribed");
                }
            }
        });
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
