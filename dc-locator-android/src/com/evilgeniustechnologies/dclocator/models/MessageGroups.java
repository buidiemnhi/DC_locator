package com.evilgeniustechnologies.dclocator.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.List;

@ParseClassName("MessageGroups")
public class MessageGroups extends ParseObject {

    public static ParseQuery<MessageGroups> getQuery() {
        return ParseQuery.getQuery(MessageGroups.class);
    }

    public static ParseQuery<MessageGroups> getLocalQuery() {
        return getQuery().fromLocalDatastore();
    }

    public String getName() {
        return getString("name");
    }

    public JSONArray getMembers() {
        return getJSONArray("members");
    }

    public List<Member> getMemberList() {
        return getList("members");
    }

    public Member getOwner() {
        return (Member) getParseObject("owner");
    }

    public ParseFile getIcon() {
        return getParseFile("icon");
    }

    public Message getLastMessage() {
        return (Message) getParseObject("lastMessage");
    }

    public Member getLastSender() {
        return (Member) getParseObject("lastSender");
    }

    public boolean isPrivate(){
    	return getBoolean("isPrivate");
    }
}
