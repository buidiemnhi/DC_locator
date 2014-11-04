package com.evilgeniustechnologies.dclocator.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Member")
public class Member extends ParseObject {

    public static ParseQuery<Member> getQuery() {
        return ParseQuery.getQuery(Member.class);
    }

    public static ParseQuery<Member> getLocalQuery() {
        return getQuery().fromLocalDatastore();
    }

//    public List<String> getAlreadyReadMessageIDs() {
//        return getList("alreadyReadMessageIDs");
//    }

    public ParseFile getAvatar() {
        return getParseFile("avatar");
    }

    public String getCity() {
        return getString("city");
    }

    public String getCountry() {
        return getString("country");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getExpertise() {
        return getString("expertise");
    }

    public String getFirstName() {
        return getString("firstName");
    }

    public String getFullName() {
        return getString("fullName");
    }

    public ParseGeoPoint getHomeLocation() {
        return getParseGeoPoint("homeLocation");
    }

    public ParseGeoPoint getLastLocation() {
        return getParseGeoPoint("lastLocation");
    }
    public Location getLastCheckIn(){
    	return (Location) getParseObject("lastCheckIn");
    }

    public String getLoginEmail() {
        return getString("login_email");
    }

    public String getSkill() {
        return getString("skill");
    }

    public String getSkypeId() {
        return getString("skypeId");
    }

    public String getTwitter() {
        return getString("twitter");
    }

    public String getWebsite() {
        return getString("website");
    }

    public boolean available(){
    	return getBoolean("available");
    }
}
