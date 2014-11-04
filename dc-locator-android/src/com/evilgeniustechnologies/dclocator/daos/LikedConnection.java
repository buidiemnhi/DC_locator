package com.evilgeniustechnologies.dclocator.daos;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table LIKED_CONNECTION.
 */
public class LikedConnection {

    private Long id;
    private String likedMessageId;
    private String likedUserId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient LikedConnectionDao myDao;

    private Message likedMessage;
    private String likedMessage__resolvedKey;

    private Member likedUser;
    private String likedUser__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public LikedConnection() {
    }

    public LikedConnection(Long id) {
        this.id = id;
    }

    public LikedConnection(Long id, String likedMessageId, String likedUserId) {
        this.id = id;
        this.likedMessageId = likedMessageId;
        this.likedUserId = likedUserId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLikedConnectionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLikedMessageId() {
        return likedMessageId;
    }

    public void setLikedMessageId(String likedMessageId) {
        this.likedMessageId = likedMessageId;
    }

    public String getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(String likedUserId) {
        this.likedUserId = likedUserId;
    }

    /** To-one relationship, resolved on first access. */
    public Message getLikedMessage() {
        String __key = this.likedMessageId;
        if (likedMessage__resolvedKey == null || likedMessage__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message likedMessageNew = targetDao.load(__key);
            synchronized (this) {
                likedMessage = likedMessageNew;
            	likedMessage__resolvedKey = __key;
            }
        }
        return likedMessage;
    }

    public void setLikedMessage(Message likedMessage) {
        synchronized (this) {
            this.likedMessage = likedMessage;
            likedMessageId = likedMessage == null ? null : likedMessage.getObjectId();
            likedMessage__resolvedKey = likedMessageId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Member getLikedUser() {
        String __key = this.likedUserId;
        if (likedUser__resolvedKey == null || likedUser__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MemberDao targetDao = daoSession.getMemberDao();
            Member likedUserNew = targetDao.load(__key);
            synchronized (this) {
                likedUser = likedUserNew;
            	likedUser__resolvedKey = __key;
            }
        }
        return likedUser;
    }

    public void setLikedUser(Member likedUser) {
        synchronized (this) {
            this.likedUser = likedUser;
            likedUserId = likedUser == null ? null : likedUser.getObjectId();
            likedUser__resolvedKey = likedUserId;
        }
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
    @Override
    public String toString() {
        return "LikedConnection{" + '\n' +
                "id=" + id + '\n' +
                ", likedMessageId='" + likedMessageId + '\'' + '\n' +
                ", likedUserId='" + likedUserId + '\'' + '\n' +
                ", likedMessage__resolvedKey='" + likedMessage__resolvedKey + '\'' + '\n' +
                ", likedUser__resolvedKey='" + likedUser__resolvedKey + '\'' + '\n' +
                '}';
    }
    // KEEP METHODS END

}
