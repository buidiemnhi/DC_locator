package com.evilgeniustechnologies.dclocator.daos;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
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
 * Entity mapped to table MEMBER.
 */
public class Member {

    private String objectId;
    /** Not-null value. */
    private java.util.Date updatedAt;
    private Boolean available;
    private String avatar;
    private String city;
    private String country;
    private String email;
    private String expertise;
    private String firstName;
    private String fullName;
    private String loginEmail;
    private String phone;
    private String profileURL;
    private String skill;
    private String skypeId;
    private String twitter;
    private String website;
    private Double homeLocationLatitude;
    private Double homeLocationLongitude;
    private Double lastLocationLatitude;
    private Double lastLocationLongitude;
    private java.util.Date tempGroupDate;
    private java.util.Date latestGroupDate;
    private Integer totalUnreadMessages;
    private java.util.Date latestUnreadMessagesDate;
    private String homeCheckInId;
    private String lastCheckInId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MemberDao myDao;

    private Location homeCheckIn;
    private String homeCheckIn__resolvedKey;

    private Location lastCheckIn;
    private String lastCheckIn__resolvedKey;

    private List<LikedConnection> likedByConnections;
    private List<ReadConnection> readByConnections;
    private List<EntityConnection> groupConnections;
    private List<MessageGroup> groups;

    // KEEP FIELDS - put your custom fields here
    private static final String TAG = "EGT.Member";
    // KEEP FIELDS END

    public Member() {
    }

    public Member(String objectId) {
        this.objectId = objectId;
    }

    public Member(String objectId, java.util.Date updatedAt, Boolean available, String avatar, String city, String country, String email, String expertise, String firstName, String fullName, String loginEmail, String phone, String profileURL, String skill, String skypeId, String twitter, String website, Double homeLocationLatitude, Double homeLocationLongitude, Double lastLocationLatitude, Double lastLocationLongitude, java.util.Date tempGroupDate, java.util.Date latestGroupDate, Integer totalUnreadMessages, java.util.Date latestUnreadMessagesDate, String homeCheckInId, String lastCheckInId) {
        this.objectId = objectId;
        this.updatedAt = updatedAt;
        this.available = available;
        this.avatar = avatar;
        this.city = city;
        this.country = country;
        this.email = email;
        this.expertise = expertise;
        this.firstName = firstName;
        this.fullName = fullName;
        this.loginEmail = loginEmail;
        this.phone = phone;
        this.profileURL = profileURL;
        this.skill = skill;
        this.skypeId = skypeId;
        this.twitter = twitter;
        this.website = website;
        this.homeLocationLatitude = homeLocationLatitude;
        this.homeLocationLongitude = homeLocationLongitude;
        this.lastLocationLatitude = lastLocationLatitude;
        this.lastLocationLongitude = lastLocationLongitude;
        this.tempGroupDate = tempGroupDate;
        this.latestGroupDate = latestGroupDate;
        this.totalUnreadMessages = totalUnreadMessages;
        this.latestUnreadMessagesDate = latestUnreadMessagesDate;
        this.homeCheckInId = homeCheckInId;
        this.lastCheckInId = lastCheckInId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMemberDao() : null;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /** Not-null value. */
    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Double getHomeLocationLatitude() {
        return homeLocationLatitude;
    }

    public void setHomeLocationLatitude(Double homeLocationLatitude) {
        this.homeLocationLatitude = homeLocationLatitude;
    }

    public Double getHomeLocationLongitude() {
        return homeLocationLongitude;
    }

    public void setHomeLocationLongitude(Double homeLocationLongitude) {
        this.homeLocationLongitude = homeLocationLongitude;
    }

    public Double getLastLocationLatitude() {
        return lastLocationLatitude;
    }

    public void setLastLocationLatitude(Double lastLocationLatitude) {
        this.lastLocationLatitude = lastLocationLatitude;
    }

    public Double getLastLocationLongitude() {
        return lastLocationLongitude;
    }

    public void setLastLocationLongitude(Double lastLocationLongitude) {
        this.lastLocationLongitude = lastLocationLongitude;
    }

    public java.util.Date getTempGroupDate() {
        return tempGroupDate;
    }

    public void setTempGroupDate(java.util.Date tempGroupDate) {
        this.tempGroupDate = tempGroupDate;
    }

    public java.util.Date getLatestGroupDate() {
        return latestGroupDate;
    }

    public void setLatestGroupDate(java.util.Date latestGroupDate) {
        this.latestGroupDate = latestGroupDate;
    }

    public Integer getTotalUnreadMessages() {
        return totalUnreadMessages;
    }

    public void setTotalUnreadMessages(Integer totalUnreadMessages) {
        this.totalUnreadMessages = totalUnreadMessages;
    }

    public java.util.Date getLatestUnreadMessagesDate() {
        return latestUnreadMessagesDate;
    }

    public void setLatestUnreadMessagesDate(java.util.Date latestUnreadMessagesDate) {
        this.latestUnreadMessagesDate = latestUnreadMessagesDate;
    }

    public String getHomeCheckInId() {
        return homeCheckInId;
    }

    public void setHomeCheckInId(String homeCheckInId) {
        this.homeCheckInId = homeCheckInId;
    }

    public String getLastCheckInId() {
        return lastCheckInId;
    }

    public void setLastCheckInId(String lastCheckInId) {
        this.lastCheckInId = lastCheckInId;
    }

    /** To-one relationship, resolved on first access. */
    public Location getHomeCheckIn() {
        String __key = this.homeCheckInId;
        if (homeCheckIn__resolvedKey == null || homeCheckIn__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationDao targetDao = daoSession.getLocationDao();
            Location homeCheckInNew = targetDao.load(__key);
            synchronized (this) {
                homeCheckIn = homeCheckInNew;
            	homeCheckIn__resolvedKey = __key;
            }
        }
        return homeCheckIn;
    }

    public void setHomeCheckIn(Location homeCheckIn) {
        synchronized (this) {
            this.homeCheckIn = homeCheckIn;
            homeCheckInId = homeCheckIn == null ? null : homeCheckIn.getObjectId();
            homeCheckIn__resolvedKey = homeCheckInId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Location getLastCheckIn() {
        String __key = this.lastCheckInId;
        if (lastCheckIn__resolvedKey == null || lastCheckIn__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationDao targetDao = daoSession.getLocationDao();
            Location lastCheckInNew = targetDao.load(__key);
            synchronized (this) {
                lastCheckIn = lastCheckInNew;
            	lastCheckIn__resolvedKey = __key;
            }
        }
        return lastCheckIn;
    }

    public void setLastCheckIn(Location lastCheckIn) {
        synchronized (this) {
            this.lastCheckIn = lastCheckIn;
            lastCheckInId = lastCheckIn == null ? null : lastCheckIn.getObjectId();
            lastCheckIn__resolvedKey = lastCheckInId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<LikedConnection> getLikedByConnections() {
        if (likedByConnections == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LikedConnectionDao targetDao = daoSession.getLikedConnectionDao();
            List<LikedConnection> likedByConnectionsNew = targetDao._queryMember_LikedByConnections(objectId);
            synchronized (this) {
                if(likedByConnections == null) {
                    likedByConnections = likedByConnectionsNew;
                }
            }
        }
        return likedByConnections;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetLikedByConnections() {
        likedByConnections = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ReadConnection> getReadByConnections() {
        if (readByConnections == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReadConnectionDao targetDao = daoSession.getReadConnectionDao();
            List<ReadConnection> readByConnectionsNew = targetDao._queryMember_ReadByConnections(objectId);
            synchronized (this) {
                if(readByConnections == null) {
                    readByConnections = readByConnectionsNew;
                }
            }
        }
        return readByConnections;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetReadByConnections() {
        readByConnections = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EntityConnection> getGroupConnections() {
        if (groupConnections == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EntityConnectionDao targetDao = daoSession.getEntityConnectionDao();
            List<EntityConnection> groupConnectionsNew = targetDao._queryMember_GroupConnections(objectId);
            synchronized (this) {
                if(groupConnections == null) {
                    groupConnections = groupConnectionsNew;
                }
            }
        }
        return groupConnections;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGroupConnections() {
        groupConnections = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<MessageGroup> getGroups() {
        if (groups == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageGroupDao targetDao = daoSession.getMessageGroupDao();
            List<MessageGroup> groupsNew = targetDao._queryMember_Groups(objectId);
            synchronized (this) {
                if(groups == null) {
                    groups = groupsNew;
                }
            }
        }
        return groups;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGroups() {
        groups = null;
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
    public static List<Member> parseMembers(Datastore datastore, List<ParseObject> parseObjects) throws ParseException {
        Log.e(TAG, "memberParseObjects size " + parseObjects.size());
        List<Member> members = new ArrayList<Member>();
        MemberDao memberDao = datastore.getMemberDao();
        Log.e(TAG, "start parse " + parseObjects.size());
        for (ParseObject parseObject : parseObjects) {
            Member member = parseMember(memberDao, parseObject);
            members.add(member);
        }
        Log.e(TAG, "end parse " + parseObjects.size());
        return members;
    }

    public static void parseMembers(Datastore datastore, ParseQuery<ParseObject> query, boolean inSearchMode) throws ParseException {
        List<ParseObject> parseObjects = query.find();
        Log.e(TAG, "memberParseObjects size " + parseObjects.size());
        MemberDao memberDao = datastore.getMemberDao();
        MetadataDao metadataDao = datastore.getMetadataDao();
        Metadata metadata = datastore.getMetadata();
        Log.e(TAG, "start parse " + parseObjects.size());
        for (ParseObject parseObject : parseObjects) {
            Member member = parseMember(memberDao, parseObject);
            if (!inSearchMode) {
                if (metadata.getTempMemberDate() == null || metadata.getTempMemberDate().compareTo(member.getUpdatedAt()) < 0) {
                    metadata.setTempMemberDate(member.getUpdatedAt());
                    metadataDao.update(metadata);
                }
            }
        }
        Log.e(TAG, "end parse " + parseObjects.size());
    }

    public static com.evilgeniustechnologies.dclocator.daos.Member parseMember(MemberDao memberDao, ParseObject parseObject) {
        com.evilgeniustechnologies.dclocator.daos.Member member = memberDao.load(parseObject.getObjectId());
        if (member == null) {
            member = new com.evilgeniustechnologies.dclocator.daos.Member();
            member.setObjectId(parseObject.getObjectId());
            member.setUpdatedAt(parseObject.getUpdatedAt());
            member.setAvailable(parseObject.getBoolean("available"));
            ParseFile avatar = parseObject.getParseFile("avatar");
            if (avatar != null) {
                member.setAvatar(avatar.getUrl());
            }
            member.setCity(parseObject.getString("city"));
            member.setCountry(parseObject.getString("country"));
            member.setEmail(parseObject.getString("expertise"));
            member.setFirstName(parseObject.getString("firstName"));
            member.setFullName(parseObject.getString("fullName"));
            member.setLoginEmail(parseObject.getString("login_email"));
            member.setPhone(parseObject.getString("phone"));
            member.setProfileURL(parseObject.getString("profileURL"));
            member.setSkill(parseObject.getString("skill"));
            member.setSkypeId(parseObject.getString("skypeId"));
            member.setTwitter(parseObject.getString("twitter"));
            member.setWebsite(parseObject.getString("website"));
            ParseObject homeCheckIn = parseObject.getParseObject("homeCheckIn");
            if (homeCheckIn != null) {
                member.setHomeCheckInId(homeCheckIn.getObjectId());
            }
            ParseObject lastCheckIn = parseObject.getParseObject("lastCheckIn");
            if (lastCheckIn != null) {
                member.setLastCheckInId(lastCheckIn.getObjectId());
            }
            ParseGeoPoint homeGeoPoint = parseObject.getParseGeoPoint("homeLocation");
            if (homeGeoPoint != null) {
                member.setHomeLocationLatitude(homeGeoPoint.getLatitude());
                member.setHomeLocationLongitude(homeGeoPoint.getLongitude());
            }
            ParseGeoPoint lastGeoPoint = parseObject.getParseGeoPoint("lastLocation");
            if (lastGeoPoint != null) {
                member.setLastLocationLatitude(lastGeoPoint.getLatitude());
                member.setLastLocationLongitude(lastGeoPoint.getLongitude());
            }
            memberDao.insert(member);
        } else if (member.getUpdatedAt().compareTo(parseObject.getUpdatedAt()) != 0) {
            member.setUpdatedAt(parseObject.getUpdatedAt());
            member.setAvailable(parseObject.getBoolean("available"));
            ParseFile avatar = parseObject.getParseFile("avatar");
            if (avatar != null) {
                member.setAvatar(avatar.getUrl());
            }
            member.setCity(parseObject.getString("city"));
            member.setCountry(parseObject.getString("country"));
            member.setEmail(parseObject.getString("expertise"));
            member.setFirstName(parseObject.getString("firstName"));
            member.setFullName(parseObject.getString("fullName"));
            member.setLoginEmail(parseObject.getString("login_email"));
            member.setPhone(parseObject.getString("phone"));
            member.setProfileURL(parseObject.getString("profileURL"));
            member.setSkill(parseObject.getString("skill"));
            member.setSkypeId(parseObject.getString("skypeId"));
            member.setTwitter(parseObject.getString("twitter"));
            member.setWebsite(parseObject.getString("website"));
            ParseObject homeCheckIn = parseObject.getParseObject("homeCheckIn");
            if (homeCheckIn != null) {
                member.setHomeCheckInId(homeCheckIn.getObjectId());
            } else {
                member.setHomeCheckInId(null);
            }
            ParseObject lastCheckIn = parseObject.getParseObject("lastCheckIn");
            if (lastCheckIn != null) {
                member.setLastCheckInId(lastCheckIn.getObjectId());
            } else {
                member.setLastCheckInId(null);
            }
            ParseGeoPoint homeGeoPoint = parseObject.getParseGeoPoint("homeLocation");
            if (homeGeoPoint != null) {
                member.setHomeLocationLatitude(homeGeoPoint.getLatitude());
                member.setHomeLocationLongitude(homeGeoPoint.getLongitude());
            } else {
                member.setHomeLocationLatitude(null);
                member.setHomeLocationLongitude(null);
            }
            ParseGeoPoint lastGeoPoint = parseObject.getParseGeoPoint("lastLocation");
            if (lastGeoPoint != null) {
                member.setLastLocationLatitude(lastGeoPoint.getLatitude());
                member.setLastLocationLongitude(lastGeoPoint.getLongitude());
            } else {
                member.setLastLocationLatitude(null);
                member.setLastLocationLongitude(null);
            }
            memberDao.update(member);
        }
        return member;
    }

    @Override
    public String toString() {
        return "Member{" + '\n' +
                "objectId='" + objectId + '\'' + '\n' +
                ", updatedAt=" + updatedAt + '\n' +
                ", available=" + available + '\n' +
                ", avatar='" + avatar + '\'' + '\n' +
                ", city='" + city + '\'' + '\n' +
                ", country='" + country + '\'' + '\n' +
                ", email='" + email + '\'' + '\n' +
                ", expertise='" + expertise + '\'' + '\n' +
                ", firstName='" + firstName + '\'' + '\n' +
                ", fullName='" + fullName + '\'' + '\n' +
                ", loginEmail='" + loginEmail + '\'' + '\n' +
                ", phone='" + phone + '\'' + '\n' +
                ", profileURL='" + profileURL + '\'' + '\n' +
                ", skill='" + skill + '\'' + '\n' +
                ", skypeId='" + skypeId + '\'' + '\n' +
                ", twitter='" + twitter + '\'' + '\n' +
                ", website='" + website + '\'' + '\n' +
                ", homeLocationLatitude=" + homeLocationLatitude + '\n' +
                ", homeLocationLongitude=" + homeLocationLongitude + '\n' +
                ", lastLocationLatitude=" + lastLocationLatitude + '\n' +
                ", lastLocationLongitude=" + lastLocationLongitude + '\n' +
                ", tempGroupDate=" + tempGroupDate + '\n' +
                ", latestGroupDate=" + latestGroupDate + '\n' +
                ", totalUnreadMessages=" + totalUnreadMessages + '\n' +
                ", latestUnreadMessagesDate=" + latestUnreadMessagesDate + '\n' +
                ", homeCheckInId='" + homeCheckInId + '\'' + '\n' +
                ", lastCheckInId='" + lastCheckInId + '\'' + '\n' +
                ", homeCheckIn__resolvedKey='" + homeCheckIn__resolvedKey + '\'' + '\n' +
                ", lastCheckIn__resolvedKey='" + lastCheckIn__resolvedKey + '\'' + '\n' +
                ", likedByConnections=" + getLikedByConnections().size() + '\n' +
                ", readByConnections=" + getReadByConnections().size() + '\n' +
                ", groupConnections=" + getGroupConnections().size() + '\n' +
                ", groups=" + groups + '\n' +
                '}';
    }
// KEEP METHODS END

}