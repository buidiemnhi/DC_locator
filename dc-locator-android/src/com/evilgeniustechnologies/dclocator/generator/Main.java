package com.evilgeniustechnologies.dclocator.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by benjamin on 10/15/14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "daos");
        schema.enableKeepSectionsByDefault();
        createTables(schema);
        new DaoGenerator().generateAll(schema, "../dc-locator-android/src/");
    }

    private static void createTables(Schema schema) {
        // Location entity
        Entity location = schema.addEntity("Location");

        location.addStringProperty("objectId").primaryKey();
        location.addDateProperty("updatedAt").notNull();

        location.addStringProperty("city");
        location.addStringProperty("country");
        location.addDateProperty("departureDate");
        location.addStringProperty("fullAddress");
        location.addBooleanProperty("isPublic"); // "public"
        location.addStringProperty("state");
        location.addDoubleProperty("checkInLocationLatitude");
        location.addDoubleProperty("checkInLocationLongitude");

        // Member entity
        Entity member = schema.addEntity("Member");

        member.addStringProperty("objectId").primaryKey();
        member.addDateProperty("updatedAt").notNull();

        member.addBooleanProperty("available");
        member.addStringProperty("avatar");
        // "cached_avatar_url"
        member.addStringProperty("city");
        member.addStringProperty("country");
        member.addStringProperty("email");
        member.addStringProperty("expertise");
        member.addStringProperty("firstName");
        member.addStringProperty("fullName");
        // "likedMessages" ?
        member.addStringProperty("loginEmail");
        // "messageRadius" ?
        // "ningId" ?
        // "ningOAuthToken" ?
        // "ningOAuthTokenSecret" ?
        // "owner" ?
        member.addStringProperty("phone");
        member.addStringProperty("profileURL");
        // "searchSkill" ?
        member.addStringProperty("skill");
        // "skillWebsite" ?
        member.addStringProperty("skypeId");
        member.addStringProperty("twitter");
        member.addStringProperty("website");
        member.addDoubleProperty("homeLocationLatitude");
        member.addDoubleProperty("homeLocationLongitude");
        member.addDoubleProperty("lastLocationLatitude");
        member.addDoubleProperty("lastLocationLongitude");
        member.addDateProperty("tempGroupDate");
        member.addDateProperty("latestGroupDate");
        member.addIntProperty("totalUnreadMessages");
        member.addDateProperty("latestUnreadMessagesDate");

        // "homeCheckIn"
        Property homeCheckInProperty = member.addStringProperty("homeCheckInId").getProperty();
        member.addToOne(location, homeCheckInProperty, "homeCheckIn");

        // "lastCheckIn"
        Property lastCheckInProperty = member.addStringProperty("lastCheckInId").getProperty();
        member.addToOne(location, lastCheckInProperty, "lastCheckIn");

        // Location: "user"
        Property userProperty = location.addStringProperty("userId").getProperty();
        location.addToOne(member, userProperty, "user");

        // Message entity
        Entity message = schema.addEntity("Message");

        message.addStringProperty("objectId").primaryKey();
        message.addDateProperty("updatedAt").notNull();

        message.addStringProperty("file");
        // "gpsCoordinates"
        message.addBooleanProperty("isPrivate");
        message.addStringProperty("content"); // "message"
        message.addIntProperty("type");

        // "objectIDOfClassUser"
        Property objectIDOfClassUserProperty = message.addStringProperty("ownerId").getProperty();
        message.addToOne(member, objectIDOfClassUserProperty, "owner");

        // LikedConnection entity aka "liked_by"
        Entity likedConnection = schema.addEntity("LikedConnection");

        likedConnection.addIdProperty().autoincrement();

        Property likedMessageId = likedConnection.addStringProperty("likedMessageId").getProperty();
        message.addToMany(likedConnection, likedMessageId, "likedByConnections");
        likedConnection.addToOne(message, likedMessageId, "likedMessage");

        Property likedUserId = likedConnection.addStringProperty("likedUserId").getProperty();
        member.addToMany(likedConnection, likedUserId, "likedByConnections");
        likedConnection.addToOne(member, likedUserId, "likedUser");

        // ReadConnection entity aka "read_by"
        Entity readConnection = schema.addEntity("ReadConnection");

        readConnection.addIdProperty().autoincrement();

        Property readMessageId = readConnection.addStringProperty("readMessageId").getProperty();
        message.addToMany(readConnection, readMessageId, "readByConnections");
        readConnection.addToOne(message, readMessageId, "readMessage");

        Property readUserId = readConnection.addStringProperty("readUserId").getProperty();
        member.addToMany(readConnection, readMessageId, "readByConnections");
        readConnection.addToOne(member, readUserId, "readUser");

        // Group entity aka MessageGroup
        Entity group = schema.addEntity("MessageGroup");

        group.addStringProperty("objectId").primaryKey();
        group.addDateProperty("updatedAt");

        group.addStringProperty("icon");
        group.addBooleanProperty("isPrivate");
        group.addStringProperty("name");
        group.addDateProperty("tempMessageDate");
        group.addDateProperty("latestMessageDate");
        group.addDateProperty("tempUnreadMessageDate");
        group.addDateProperty("latestUnreadMessageDate");

        // "lastMessage"
        Property lastMessageId = group.addStringProperty("lastMessageId").getProperty();
        group.addToOne(message, lastMessageId, "lastMessage");

        // "lastSender"
        Property lastSenderId = group.addStringProperty("lastSenderId").getProperty();
        group.addToOne(member, lastSenderId, "lastSender");

        // entityConnection entity "members"
        Entity entityConnection = schema.addEntity("EntityConnection");

        entityConnection.addIdProperty().autoincrement();

        Property memberIdProperty = entityConnection.addStringProperty("memberId").getProperty();
        member.addToMany(entityConnection, memberIdProperty, "groupConnections");
        entityConnection.addToOne(member, memberIdProperty, "member");

        Property groupIdProperty = entityConnection.addStringProperty("groupId").getProperty();
        group.addToMany(entityConnection, groupIdProperty, "memberConnections");
        entityConnection.addToOne(group, groupIdProperty, "group");

        // "owner"
        Property ownerIdProperty = group.addStringProperty("ownerId").getProperty();
        member.addToMany(group, ownerIdProperty, "groups");
        group.addToOne(member, ownerIdProperty, "owner");

        // Message: "group"
        Property messageGroupIdProperty = message.addStringProperty("groupId").getProperty();
        message.addToOne(group, messageGroupIdProperty, "group");

        // Group: unread messages of current user
        Property unreadMessageGroupIdProperty = message.addStringProperty("unreadGroupId").getProperty();
        group.addToMany(message, unreadMessageGroupIdProperty, "unreadMessages");
        message.addToOne(group, unreadMessageGroupIdProperty, "unreadGroup");

        // Metadata entity
        Entity metadata = schema.addEntity("Metadata");

        metadata.addIdProperty().autoincrement();

        metadata.addDateProperty("tempLocationDate");
        metadata.addDateProperty("latestLocationDate");
        metadata.addDateProperty("tempMemberDate");
        metadata.addDateProperty("latestMemberDate");
    }
}
