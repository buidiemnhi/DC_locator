package com.evilgeniustechnologies.dclocator.models;

public class ChatItem {

    public Message parseObject;
    public long timeCreate;

    public ChatItem(Message parseObject) {
        this.parseObject = parseObject;
        this.timeCreate = parseObject.getUpdatedAt().getTime();
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "parseObject=" + parseObject +
                ", timeCreate=" + timeCreate +
                '}';
    }
}
