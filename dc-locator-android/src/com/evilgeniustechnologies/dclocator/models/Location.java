package com.evilgeniustechnologies.dclocator.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Location")
public class Location extends ParseObject {

    public static ParseQuery<Location> getQuery() {
        return ParseQuery.getQuery(Location.class);
    }

    public static ParseQuery<Location> getLocalQuery() {
        return getQuery().fromLocalDatastore();
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        put("city", city);
    }

    public void setcity(String city) {
        put("city", city);
    }

    public void setDepartureDate(Date date) {
        put("departureDate", date);
    }

    public String getFullAddress() {
        return getString("fullAddress");
    }

    public void setFullAddress(String ad) {
        put("fullAddress", ad);
    }

    public Boolean isPublic() {
        return getBoolean("public");
    }

    public void setPublic(Boolean isPublic) {
        put("public", isPublic);
    }

    public String getCountry() {
        return getString("country");
    }

    public void setCountry(String country) {
        put("country", country);
    }

    public ParseGeoPoint getCheckin() {
        return getParseGeoPoint("checkinLocation");
    }

    public void setCheckin(ParseGeoPoint chekin) {
        put("checkinLocation", chekin);
    }

    public Member getUser() {
        return (Member) getParseObject("user");
    }

    public void setUser(Member user) {
        put("user", user);
    }

    public void setState(String state) {
        put("state", state);
    }

    public String getState() {
        return getString("state");
    }

    public ParseGeoPoint getCheckinLocation() {
        return getParseGeoPoint("checkinLocation");
    }

    public void setCheckinLocation(ParseGeoPoint checkinLocation) {
        put("checkinLocation", checkinLocation);
    }
}
