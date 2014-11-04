package com.evilgeniustechnologies.dclocator.daos;

import android.text.TextUtils;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.DaoException;
import com.evilgeniustechnologies.dclocator.local.Datastore;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table MESSAGE_GROUP.
 */
public class MessageGroup {

    private String objectId;
    private java.util.Date updatedAt;
    private String icon;
    private Boolean isPrivate;
    private String name;
    private java.util.Date tempMessageDate;
    private java.util.Date latestMessageDate;
    private java.util.Date tempUnreadMessageDate;
    private java.util.Date latestUnreadMessageDate;
    private String lastMessageId;
    private String lastSenderId;
    private String ownerId;
    private String MemberListString;
    private ArrayList MemberList;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MessageGroupDao myDao;

    private Message lastMessage;
    private String lastMessage__resolvedKey;

    private Member lastSender;
    private String lastSender__resolvedKey;

    private Member owner;
    private String owner__resolvedKey;
    private ArrayList<Member> memberList;


	private String memberList_resolvedKey;

    private List<EntityConnection> memberConnections;
    private List<Message> unreadMessages;

    // KEEP FIELDS - put your custom fields here
    private static final String TAG = "EGT.MessageGroup";
    // KEEP FIELDS END

    public MessageGroup() {
    }

    public MessageGroup(String objectId) {
        this.objectId = objectId;
    }

    public MessageGroup(String objectId, java.util.Date updatedAt, String icon, Boolean isPrivate, String name, java.util.Date tempMessageDate, java.util.Date latestMessageDate, java.util.Date tempUnreadMessageDate, java.util.Date latestUnreadMessageDate, String lastMessageId, String lastSenderId, String ownerId) {
        this.objectId = objectId;
        this.updatedAt = updatedAt;
        this.icon = icon;
        this.isPrivate = isPrivate;
        this.name = name;
        this.tempMessageDate = tempMessageDate;
        this.latestMessageDate = latestMessageDate;
        this.tempUnreadMessageDate = tempUnreadMessageDate;
        this.latestUnreadMessageDate = latestUnreadMessageDate;
        this.lastMessageId = lastMessageId;
        this.lastSenderId = lastSenderId;
        this.ownerId = ownerId;
      //  this.MemberListString= memberList;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageGroupDao() : null;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Date getTempMessageDate() {
        return tempMessageDate;
    }

    public void setTempMessageDate(java.util.Date tempMessageDate) {
        this.tempMessageDate = tempMessageDate;
    }

    public java.util.Date getLatestMessageDate() {
        return latestMessageDate;
    }

    public void setLatestMessageDate(java.util.Date latestMessageDate) {
        this.latestMessageDate = latestMessageDate;
    }

    public java.util.Date getTempUnreadMessageDate() {
        return tempUnreadMessageDate;
    }

    public void setTempUnreadMessageDate(java.util.Date tempUnreadMessageDate) {
        this.tempUnreadMessageDate = tempUnreadMessageDate;
    }

    public java.util.Date getLatestUnreadMessageDate() {
        return latestUnreadMessageDate;
    }

    public void setLatestUnreadMessageDate(java.util.Date latestUnreadMessageDate) {
        this.latestUnreadMessageDate = latestUnreadMessageDate;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastSenderId() {
        return lastSenderId;
    }

    public void setLastSenderId(String lastSenderId) {
        this.lastSenderId = lastSenderId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getMemberListString() {
		return MemberListString;
	}

	public void setMemberListString(String memberListString) {
		MemberListString = memberListString;
	}

	
    /** To-one relationship, resolved on first access. */
    public Message getLastMessage() {
        String __key = this.lastMessageId;
        if (lastMessage__resolvedKey == null || lastMessage__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message lastMessageNew = targetDao.load(__key);
            synchronized (this) {
                lastMessage = lastMessageNew;
            	lastMessage__resolvedKey = __key;
            }
        }
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        synchronized (this) {
            this.lastMessage = lastMessage;
            lastMessageId = lastMessage == null ? null : lastMessage.getObjectId();
            lastMessage__resolvedKey = lastMessageId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Member getLastSender() {
        String __key = this.lastSenderId;
        if (lastSender__resolvedKey == null || lastSender__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MemberDao targetDao = daoSession.getMemberDao();
            Member lastSenderNew = targetDao.load(__key);
            synchronized (this) {
                lastSender = lastSenderNew;
            	lastSender__resolvedKey = __key;
            }
        }
        return lastSender;
    }

    public void setLastSender(Member lastSender) {
        synchronized (this) {
            this.lastSender = lastSender;
            lastSenderId = lastSender == null ? null : lastSender.getObjectId();
            lastSender__resolvedKey = lastSenderId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Member getOwner() {
        String __key = this.ownerId;
        if (owner__resolvedKey == null || owner__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MemberDao targetDao = daoSession.getMemberDao();
            Member ownerNew = targetDao.load(__key);
            synchronized (this) {
                owner = ownerNew;
            	owner__resolvedKey = __key;
            }
        }
        return owner;
    }

    public void setOwner(Member owner) {
        synchronized (this) {
            this.owner = owner;
            ownerId = owner == null ? null : owner.getObjectId();
            owner__resolvedKey = ownerId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EntityConnection> getMemberConnections() {
        if (memberConnections == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EntityConnectionDao targetDao = daoSession.getEntityConnectionDao();
            List<EntityConnection> memberConnectionsNew = targetDao._queryMessageGroup_MemberConnections(objectId);
            synchronized (this) {
                if(memberConnections == null) {
                    memberConnections = memberConnectionsNew;
                }
            }
        }
        return memberConnections;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMemberConnections() {
        memberConnections = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Message> getUnreadMessages() {
        if (unreadMessages == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            List<Message> unreadMessagesNew = targetDao._queryMessageGroup_UnreadMessages(objectId);
            synchronized (this) {
                if(unreadMessages == null) {
                    unreadMessages = unreadMessagesNew;
                }
            }
        }
        return unreadMessages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetUnreadMessages() {
        unreadMessages = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public static void parseGroups(Datastore datastore, ParseQuery<ParseObject> query) throws ParseException {
        List<ParseObject> parseObjects = query.find();
        Log.e(TAG, "groupParseObjects size " + parseObjects.size());
        Member currentUser = datastore.getCurrentUser();
        MessageGroupDao groupDao = datastore.getGroupDao();
        EntityConnectionDao connectionDao = datastore.getEntityConnectionDao();
        MessageDao messageDao = datastore.getMessageDao();
        MemberDao memberDao = datastore.getMemberDao();
        ReadConnectionDao readConnectionDao = datastore.getReadConnectionDao();
        LikedConnectionDao likedConnectionDao = datastore.getLikedConnectionDao();
        for (ParseObject parseObject : parseObjects) {
            Log.e(TAG, "start parse " + parseObject.getObjectId());
            MessageGroup group = parseGroup(groupDao, connectionDao, parseObject);
            Log.e(TAG, "done parse " + group.getObjectId());
            if (currentUser.getTempGroupDate() == null || currentUser.getTempGroupDate().compareTo(group.updatedAt) < 0) {
                currentUser.setTempGroupDate(group.getUpdatedAt());
                currentUser.update();
            }
            String lastMessageId = group.getLastMessageId();
            if (!TextUtils.isEmpty(lastMessageId)) {
                ParseObject lastMessage = Datastore.getMessageQuery().get(lastMessageId);
                if (lastMessage != null) {
                    com.evilgeniustechnologies.dclocator.daos.Message message = Message.parseMessage(group, memberDao, likedConnectionDao, readConnectionDao, messageDao, lastMessage);
                    Log.e(TAG, "lastMessageId " + message.getObjectId());
                }
            }
            String lastSenderId = group.getLastSenderId();
            if (!TextUtils.isEmpty(lastSenderId)) {
                ParseObject lastSender = Datastore.getMemberQuery().get(lastSenderId);
                if (lastSender != null) {
                    com.evilgeniustechnologies.dclocator.daos.Member member = Member.parseMember(memberDao, lastSender);
                    Log.e(TAG, "lastSenderId " + member.getObjectId());
                }
            }
            if (group.getIsPrivate()) {
                for (EntityConnection connection : group.getMemberConnections()) {
                    if (!connection.getMemberId().equals(datastore.getCurrentParseUser().getObjectId())) {
                        ParseObject parseMember = Datastore.getMemberQuery().get(connection.getMemberId());
                        Log.e(TAG, "parse member");
                        com.evilgeniustechnologies.dclocator.daos.Member member = Member.parseMember(memberDao, parseMember);
                        Log.e(TAG, "memberId " + member.getObjectId());
                        break;
                    }
                }
            }
        }
    }

    public static MessageGroup parseGroup(MessageGroupDao groupDao, EntityConnectionDao connectionDao, ParseObject parseObject) {
        MessageGroup group = groupDao.load(parseObject.getObjectId());
        if (group == null) {
            group = new MessageGroup();
            group.setObjectId(parseObject.getObjectId());
            group.setUpdatedAt(parseObject.getUpdatedAt());
            ParseFile icon = parseObject.getParseFile("icon");
            if (icon != null) {
                group.setIcon(icon.getUrl());
            }
            group.setIsPrivate(parseObject.getBoolean("isPrivate"));
            group.setName(parseObject.getString("name"));
            ParseObject lastMessage = parseObject.getParseObject("lastMessage");
            if (lastMessage != null) {
                group.setLastMessageId(lastMessage.getObjectId());
            }
            ParseObject lastSender = parseObject.getParseObject("lastSender");
            if (lastSender != null) {
                group.setLastSenderId(lastSender.getObjectId());
            }
            ParseObject owner = parseObject.getParseObject("owner");
            if (owner != null) {
                group.setOwnerId(owner.getObjectId());
            }
            List<ParseObject> members = parseObject.getList("members");
            if (members != null && !members.isEmpty()) {
                for (ParseObject member : members) {
                    EntityConnection connection = new EntityConnection();
                    connection.setGroup(group);
                    connection.setMemberId(member.getObjectId());
                    connectionDao.insert(connection);
                }
            }
            groupDao.insert(group);
        } else if (group.getUpdatedAt().compareTo(parseObject.getUpdatedAt()) != 0) {
            group.setUpdatedAt(parseObject.getUpdatedAt());
            ParseFile icon = parseObject.getParseFile("icon");
            if (icon != null) {
                group.setIcon(icon.getUrl());
            }
            group.setIsPrivate(parseObject.getBoolean("isPrivate"));
            group.setName(parseObject.getString("name"));
            ParseObject lastMessage = parseObject.getParseObject("lastMessage");
            if (lastMessage != null) {
                group.setLastMessageId(lastMessage.getObjectId());
            } else {
                group.setLastMessageId(null);
            }
            ParseObject lastSender = parseObject.getParseObject("lastSender");
            if (lastSender != null) {
                group.setLastSenderId(lastSender.getObjectId());
            } else {
                group.setLastSenderId(null);
            }
            ParseObject owner = parseObject.getParseObject("owner");
            if (owner != null) {
                group.setOwnerId(owner.getObjectId());
            } else {
                group.setOwnerId(null);
            }
            if (group.getMemberConnections() != null && !group.getMemberConnections().isEmpty()) {
                connectionDao.deleteInTx(group.getMemberConnections());
            }
            List<ParseObject> members = parseObject.getList("members");
            if (members != null && !members.isEmpty()) {
                for (ParseObject member : members) {
                    EntityConnection connection = new EntityConnection();
                    connection.setGroup(group);
                    connection.setMemberId(member.getObjectId());
                    connectionDao.insert(connection);
                }
            }
            groupDao.update(group);
        }
        return group;
    }

    @Override
    public String toString() {
        return "MessageGroup{" + '\n' +
                "objectId='" + objectId + '\'' + '\n' +
                ", updatedAt=" + updatedAt + '\n' +
                ", icon='" + icon + '\'' + '\n' +
                ", isPrivate=" + isPrivate + '\n' +
                ", name='" + name + '\'' + '\n' +
                ", tempMessageDate=" + tempMessageDate + '\n' +
                ", latestMessageDate=" + latestMessageDate + '\n' +
                ", tempUnreadMessageDate=" + tempUnreadMessageDate + '\n' +
                ", latestUnreadMessageDate=" + latestUnreadMessageDate + '\n' +
                ", lastMessageId='" + lastMessageId + '\'' + '\n' +
                ", lastSenderId='" + lastSenderId + '\'' + '\n' +
                ", ownerId='" + ownerId + '\'' + '\n' +
                ", lastMessage__resolvedKey='" + lastMessage__resolvedKey + '\'' + '\n' +
                ", lastSender__resolvedKey='" + lastSender__resolvedKey + '\'' + '\n' +
                ", owner__resolvedKey='" + owner__resolvedKey + '\'' + '\n' +
                ", memberConnections=" + getMemberConnections().size() + '\n' +
                ", unreadMessages=" + getUnreadMessages().size() + '\n' +
                '}';
    }
    // KEEP METHODS END

}