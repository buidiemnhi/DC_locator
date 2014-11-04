package com.evilgeniustechnologies.dclocator.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.List;

@ParseClassName("Messages")
public class Message extends ParseObject {

    public static ParseQuery<Message> getLocalQuery() {
        return getQuery().fromLocalDatastore();
    }

    public String getMessage() {
        return getString("message");
    }

    public static ParseQuery<Message> getQuery() {
        return ParseQuery.getQuery(Message.class);
    }

    public Member getUser() {
        return (Member) getParseObject("objectIDOfClassUser");
    }

    public JSONArray getReceivers() {
        return getJSONArray("receivers");
    }

    public JSONArray getLikedBy() {
        return getJSONArray("liked_by");
    }

    public List<Member> getLikedByList() {
        return getList("liked_by");
    }

    public MessageGroups getGroup() {
        return (MessageGroups) getParseObject("group");
    }
}
