package com.evilgeniustechnologies.dclocator.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class GroupItemChat implements Parcelable {

    public boolean isPrivate;
    public String id;
    public String groupName;
    public ArrayList<ChatItem> chatItems;
    public ArrayList<ChatItem> listUnread = new ArrayList<ChatItem>();
    public String avatar;
    public long TimeInterval;
    public int ReadMessage = 0;

    public int getReadMessage() {
        return ReadMessage;
    }

    public void setReadMessage(int ReadMessage) {
        this.ReadMessage = ReadMessage;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return "GroupItemChat{" +
                "isPrivate=" + isPrivate +
                ", id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", chatItems=" + chatItems +
                ", listUnread=" + listUnread +
                ", avatar='" + avatar + '\'' +
                ", TimeInterval=" + TimeInterval +
                ", ReadMessage=" + ReadMessage +
                '}';
    }
}
