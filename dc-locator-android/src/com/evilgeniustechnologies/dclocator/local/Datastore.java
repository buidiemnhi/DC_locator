package com.evilgeniustechnologies.dclocator.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.evilgeniustechnologies.dclocator.daos.DaoMaster;
import com.evilgeniustechnologies.dclocator.daos.DaoSession;
import com.evilgeniustechnologies.dclocator.daos.EntityConnection;
import com.evilgeniustechnologies.dclocator.daos.EntityConnectionDao;
import com.evilgeniustechnologies.dclocator.daos.LikedConnectionDao;
import com.evilgeniustechnologies.dclocator.daos.Location;
import com.evilgeniustechnologies.dclocator.daos.LocationDao;
import com.evilgeniustechnologies.dclocator.daos.Member;
import com.evilgeniustechnologies.dclocator.daos.MemberDao;
import com.evilgeniustechnologies.dclocator.daos.Message;
import com.evilgeniustechnologies.dclocator.daos.MessageDao;
import com.evilgeniustechnologies.dclocator.daos.MessageGroup;
import com.evilgeniustechnologies.dclocator.daos.MessageGroupDao;
import com.evilgeniustechnologies.dclocator.daos.Metadata;
import com.evilgeniustechnologies.dclocator.daos.MetadataDao;
import com.evilgeniustechnologies.dclocator.daos.ReadConnection;
import com.evilgeniustechnologies.dclocator.daos.ReadConnectionDao;
import com.evilgeniustechnologies.dclocator.utils.Utils;

/**
 * Created by benjamin on 10/4/14.
 */
public class Datastore {
    private static final String TAG = "EGT.Datastore";
    public static final String DATABASE_READY = "DATABASE_READY";
    public static final String PREPARING = "PREPARING";
    public static final String NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED";
    public static final String UPLOAD_IMAGE= "UPLOAD_IMAGE";
    public static final String UNREAD_MESSAGES_COUNTED = "UNREAD_MESSAGES_COUNTED";
    public static final String ALL_MESSAGE_GROUPS_PARSED = "ALL_MESSAGE_GROUPS_PARSED";
    public static final String ALL_MESSAGES_PARSED = "ALL_MESSAGES_PARSED";
    public static final String ALL_MEMBERS_PARSED = "ALL_MEMBERS_PARSED";
    public static final String ALL_MEMBERS_SEARCHED = "ALL_MEMBERS_SEARCHED";

    private static Datastore instance;
    private String status = PREPARING;
    private AtomicInteger progress;
    private Observer observer;
    private Context context;
    private FragmentObserver fragmentObserver;
    private String currentUserId;
    private com.evilgeniustechnologies.dclocator.models.Member currentParseUser;
    private Member currentUser;

    private SQLiteDatabase database;
    private DaoMaster master;
    private DaoSession session;
    private LocationDao locationDao;
    private MemberDao memberDao;
    private MessageGroupDao groupDao;
    private MessageDao messageDao;
    private ReadConnectionDao readConnectionDao;
    private LikedConnectionDao likedConnectionDao;
    private EntityConnectionDao entityConnectionDao;
    private MetadataDao metadataDao;

    private Datastore() {
    }

    private Datastore(Context context) {
        construct(context);
    }

    public static Datastore getInstance() {
        if (instance == null) {
            instance = new Datastore();
        }
        return instance;
    }

    public static Datastore getInstance(Context context) {
        if (instance == null) {
            instance = new Datastore(context);
        }
        return instance;
    }

    private void construct(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "parse-db", null);
        database = helper.getWritableDatabase();
        master = new DaoMaster(database);
        session = master.newSession();
        progress = new AtomicInteger(0);

        locationDao = session.getLocationDao();
        memberDao = session.getMemberDao();
        groupDao = session.getMessageGroupDao();
        messageDao = session.getMessageDao();
        readConnectionDao = session.getReadConnectionDao();
        likedConnectionDao = session.getLikedConnectionDao();
        entityConnectionDao = session.getEntityConnectionDao();
        metadataDao = session.getMetadataDao();
    }

    public void notifyObserver(String status) {
        if (observer != null) {
            observer.onNotified(status);
        }
    }

    public void notifyFragmentObserver(String status) {
        if (fragmentObserver != null) {
            fragmentObserver.onNotified(status);
        }
    }

    public int getProgress() {
        return progress.get();
    }

    public void setProgress(int progress) {
        this.progress.set(progress);
        notifyObserver(PREPARING);
    }

    public String getCurrentUserId() {
        if (currentUserId == null) {
            currentUserId = Utils.getCurrentUserId(context);
        }
        return currentUserId;
    }

    public com.evilgeniustechnologies.dclocator.models.Member getCurrentParseUser() {
        try {
            if (currentParseUser == null) {
                currentParseUser = com.evilgeniustechnologies.dclocator.models.Member.getQuery().get(getCurrentUserId());
            }
        } catch (ParseException e) {
            Log.e(TAG, "getCurrentParseUser", e);
        }
        return currentParseUser;
    }

    public Member getCurrentUser() {
        currentUser = memberDao.load(getCurrentUserId());
        if (currentUser == null) {
            currentUser = Member.parseMember(getMemberDao(), getCurrentParseUser());
        }
        return currentUser;
    }

    public void refresh() {
        session.clear();
        getCurrentUser();
    }

    public Location getCurrentUserLastCheckIn() {
        Member user = getCurrentUser();
        if (!TextUtils.isEmpty(user.getLastCheckInId()) && user.getLastCheckIn() == null) {
            try {
                ParseObject location = getLocationQuery().get(user.getLastCheckInId());
                return Location.parseLocation(locationDao, location);
            } catch (ParseException e) {
                Log.e(TAG, "getCurrentUserLastCheckIn", e);
            }
        }
        return null;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
        if (observer != null) {
            context = (Context) observer;
        }
    }

    public FragmentObserver getFragmentObserver() {
        return fragmentObserver;
    }

    public void setFragmentObserver(FragmentObserver fragmentObserver) {
        this.fragmentObserver = fragmentObserver;
    }

    public Context getContext() {
        return context;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public MessageGroupDao getGroupDao() {
        return groupDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public ReadConnectionDao getReadConnectionDao() {
        return readConnectionDao;
    }

    public LikedConnectionDao getLikedConnectionDao() {
        return likedConnectionDao;
    }

    public EntityConnectionDao getEntityConnectionDao() {
        return entityConnectionDao;
    }

    public MetadataDao getMetadataDao() {
        return metadataDao;
    }

    public Metadata getMetadata() {
        Metadata metadata = getMetadataDao().load(0l);
        if (metadata == null) {
            metadata = new Metadata(0l);
            getMetadataDao().insert(metadata);
        }
        return metadata;
    }

    public static ParseQuery<ParseObject> getLocationQuery() {
        return ParseQuery.getQuery("Location");
    }

    public static ParseQuery<ParseObject> getMemberQuery() {
        return ParseQuery.getQuery("Member");
    }

    public static ParseQuery<ParseObject> getMessageGroupQuery() {
        return ParseQuery.getQuery("MessageGroups");
    }

    public static ParseQuery<ParseObject> getMessageQuery() {
        return ParseQuery.getQuery("Messages");
    }

    public static ParseQuery<ParseObject> getLocalLocationQuery() {
        return ParseQuery.getQuery("Location").fromLocalDatastore();
    }

    public static ParseQuery<ParseObject> getLocalMemberQuery() {
        return ParseQuery.getQuery("Member").fromLocalDatastore();
    }

    public static ParseQuery<ParseObject> getLocalMessageGroupQuery() {
        return ParseQuery.getQuery("MessageGroups").fromLocalDatastore();
    }

    public static ParseQuery<ParseObject> getLocalMessageQuery() {
        return ParseQuery.getQuery("Messages").fromLocalDatastore();
    }

    public List<Message> getAllMessages(String groupId) {
        return messageDao.queryBuilder()
                .where(MessageDao.Properties.GroupId.eq(groupId))
                .list();
    }

    public List<MessageGroup> getAllUserGroups() {
        Member member = getCurrentUser();
        List<EntityConnection> connections = getEntityConnectionDao().queryBuilder()
                .where(EntityConnectionDao.Properties.MemberId.eq(member.getObjectId()))
                .list();
        List<MessageGroup> groups = new ArrayList<MessageGroup>();
        for (EntityConnection connection : connections) {
            groups.add(connection.getGroup());
        }
        sortGroups(groups);
        return groups;
    }

    public void sortGroups(List<MessageGroup> messageGroups) {
        List<Date> updatedDates = new ArrayList<Date>();
        for (MessageGroup messageGroup : messageGroups) {
            if (messageGroup.getLastMessage() != null) {
                updatedDates.add(messageGroup.getLastMessage().getUpdatedAt());
            } else {
                updatedDates.add(messageGroup.getUpdatedAt());
            }
        }
        for (int i = 0; i < updatedDates.size(); i++) {
            Date currentDate = updatedDates.get(i);
            MessageGroup currentGroup = messageGroups.get(i);
            for (int j = i + 1; j < updatedDates.size(); j++) {
                if (updatedDates.get(j).compareTo(currentDate) > 0) {
                    updatedDates.set(i, updatedDates.get(j));
                    updatedDates.set(j, currentDate);
                    currentDate = updatedDates.get(i);
                    messageGroups.set(i, messageGroups.get(j));
                    messageGroups.set(j, currentGroup);
                    currentGroup = messageGroups.get(i);
                }
            }
        }
    }

    public MessageGroup getPrivateGroup(String otherId) {
        Member currentMember = getCurrentUser();
        Member otherMember = memberDao.load(otherId);
        List<MessageGroup> groups = groupDao.loadAll();
        for (MessageGroup group : groups) {
            boolean hasCurrentUser = false;
            boolean hasOtherUser = false;
            List<EntityConnection> connections = group.getMemberConnections();
            for (EntityConnection connection : connections) {
                if (connection.getMemberId().equals(currentMember.getObjectId())) {
                    hasCurrentUser = true;
                }
                if (connection.getMemberId().equals(otherMember.getObjectId())) {
                    hasOtherUser = true;
                }
                if (hasCurrentUser && hasOtherUser && group.getIsPrivate()) {
                    return group;
                }
            }
        }
        return null;
    }

    public List<String> getUnreadMessageIds(MessageGroup group) {
        Member member = getCurrentUser();
        List<String> messageIds = new ArrayList<String>();
        List<Message> potentialUnreadMessages = Datastore.getInstance().getMessageDao()
                .queryBuilder()
                .where(MessageDao.Properties.GroupId.eq(group.getObjectId()))
                .where(MessageDao.Properties.OwnerId.notEq(member.getObjectId()))
                .list();
        for (Message potentialUnreadMessage : potentialUnreadMessages) {
            List<ReadConnection> connections = potentialUnreadMessage.getReadByConnections();
            boolean read = false;
            if (connections != null && !connections.isEmpty()) {
                for (ReadConnection connection : connections) {
                    if (connection.getReadUserId().equals(member.getObjectId())) {
                        read = true;
                        break;
                    }
                }
            }
            if (!read) {
                messageIds.add(potentialUnreadMessage.getObjectId());
            }
        }
        return messageIds;
    }

    public interface Observer {
        public void onNotified(String status);
    }

    public interface FragmentObserver {
        public void onNotified(String status);
    }
}
