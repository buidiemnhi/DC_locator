package com.evilgeniustechnologies.dclocator.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchResult implements Parcelable {
    public String Name;
    public String Install;
    public boolean avail;
    public String userId;
    public String Website;
    public String Skill;
    public String skypeId;
    public String Twitter;
    public String avatar;

    public String getInstall() {
        return Install;
    }

    public void setInstall(String install) {
        Install = install;
    }

    public boolean getAvail() {
        return avail;
    }

    public void setAvail(boolean avail) {
        this.avail = avail;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getTwitter() {
        return Twitter;
    }

    public void setTwitter(String twitter) {
        Twitter = twitter;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "Name='" + Name + '\'' +
                ", Install='" + Install + '\'' +
                ", avail='" + avail + '\'' +
                ", userId='" + userId + '\'' +
                ", Website='" + Website + '\'' +
                ", Skill='" + Skill + '\'' +
                ", skypeId='" + skypeId + '\'' +
                ", Twitter='" + Twitter + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
