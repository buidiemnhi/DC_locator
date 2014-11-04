package com.evilgeniustechnologies.dclocator.daos;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MESSAGE_GROUP.
*/
public class MessageGroupDao extends AbstractDao<MessageGroup, String> {

    public static final String TABLENAME = "MESSAGE_GROUP";

    /**
     * Properties of entity MessageGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", true, "OBJECT_ID");
        public final static Property UpdatedAt = new Property(1, java.util.Date.class, "updatedAt", false, "UPDATED_AT");
        public final static Property Icon = new Property(2, String.class, "icon", false, "ICON");
        public final static Property IsPrivate = new Property(3, Boolean.class, "isPrivate", false, "IS_PRIVATE");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property TempMessageDate = new Property(5, java.util.Date.class, "tempMessageDate", false, "TEMP_MESSAGE_DATE");
        public final static Property LatestMessageDate = new Property(6, java.util.Date.class, "latestMessageDate", false, "LATEST_MESSAGE_DATE");
        public final static Property TempUnreadMessageDate = new Property(7, java.util.Date.class, "tempUnreadMessageDate", false, "TEMP_UNREAD_MESSAGE_DATE");
        public final static Property LatestUnreadMessageDate = new Property(8, java.util.Date.class, "latestUnreadMessageDate", false, "LATEST_UNREAD_MESSAGE_DATE");
        public final static Property LastMessageId = new Property(9, String.class, "lastMessageId", false, "LAST_MESSAGE_ID");
        public final static Property LastSenderId = new Property(10, String.class, "lastSenderId", false, "LAST_SENDER_ID");
        public final static Property OwnerId = new Property(11, String.class, "ownerId", false, "OWNER_ID");
    };

    private DaoSession daoSession;

    private Query<MessageGroup> member_GroupsQuery;

    public MessageGroupDao(DaoConfig config) {
        super(config);
    }
    
    public MessageGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MESSAGE_GROUP' (" + //
                "'OBJECT_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: objectId
                "'UPDATED_AT' INTEGER," + // 1: updatedAt
                "'ICON' TEXT," + // 2: icon
                "'IS_PRIVATE' INTEGER," + // 3: isPrivate
                "'NAME' TEXT," + // 4: name
                "'TEMP_MESSAGE_DATE' INTEGER," + // 5: tempMessageDate
                "'LATEST_MESSAGE_DATE' INTEGER," + // 6: latestMessageDate
                "'TEMP_UNREAD_MESSAGE_DATE' INTEGER," + // 7: tempUnreadMessageDate
                "'LATEST_UNREAD_MESSAGE_DATE' INTEGER," + // 8: latestUnreadMessageDate
                "'LAST_MESSAGE_ID' TEXT," + // 9: lastMessageId
                "'LAST_SENDER_ID' TEXT," + // 10: lastSenderId
                "'OWNER_ID' TEXT);"); // 11: ownerId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MESSAGE_GROUP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MessageGroup entity) {
        stmt.clearBindings();
 
        String objectId = entity.getObjectId();
        if (objectId != null) {
            stmt.bindString(1, objectId);
        }
 
        java.util.Date updatedAt = entity.getUpdatedAt();
        if (updatedAt != null) {
            stmt.bindLong(2, updatedAt.getTime());
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(3, icon);
        }
 
        Boolean isPrivate = entity.getIsPrivate();
        if (isPrivate != null) {
            stmt.bindLong(4, isPrivate ? 1l: 0l);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        java.util.Date tempMessageDate = entity.getTempMessageDate();
        if (tempMessageDate != null) {
            stmt.bindLong(6, tempMessageDate.getTime());
        }
 
        java.util.Date latestMessageDate = entity.getLatestMessageDate();
        if (latestMessageDate != null) {
            stmt.bindLong(7, latestMessageDate.getTime());
        }
 
        java.util.Date tempUnreadMessageDate = entity.getTempUnreadMessageDate();
        if (tempUnreadMessageDate != null) {
            stmt.bindLong(8, tempUnreadMessageDate.getTime());
        }
 
        java.util.Date latestUnreadMessageDate = entity.getLatestUnreadMessageDate();
        if (latestUnreadMessageDate != null) {
            stmt.bindLong(9, latestUnreadMessageDate.getTime());
        }
 
        String lastMessageId = entity.getLastMessageId();
        if (lastMessageId != null) {
            stmt.bindString(10, lastMessageId);
        }
 
        String lastSenderId = entity.getLastSenderId();
        if (lastSenderId != null) {
            stmt.bindString(11, lastSenderId);
        }
 
        String ownerId = entity.getOwnerId();
        if (ownerId != null) {
            stmt.bindString(12, ownerId);
        }
    }

    @Override
    protected void attachEntity(MessageGroup entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MessageGroup readEntity(Cursor cursor, int offset) {
        MessageGroup entity = new MessageGroup( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // objectId
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // updatedAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // icon
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // isPrivate
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // tempMessageDate
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // latestMessageDate
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // tempUnreadMessageDate
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)), // latestUnreadMessageDate
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // lastMessageId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // lastSenderId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // ownerId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MessageGroup entity, int offset) {
        entity.setObjectId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUpdatedAt(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setIcon(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsPrivate(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTempMessageDate(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setLatestMessageDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setTempUnreadMessageDate(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setLatestUnreadMessageDate(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
        entity.setLastMessageId(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setLastSenderId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setOwnerId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(MessageGroup entity, long rowId) {
        return entity.getObjectId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(MessageGroup entity) {
        if(entity != null) {
            return entity.getObjectId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "groups" to-many relationship of Member. */
    public List<MessageGroup> _queryMember_Groups(String ownerId) {
        synchronized (this) {
            if (member_GroupsQuery == null) {
                QueryBuilder<MessageGroup> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.OwnerId.eq(null));
                member_GroupsQuery = queryBuilder.build();
            }
        }
        Query<MessageGroup> query = member_GroupsQuery.forCurrentThread();
        query.setParameter(0, ownerId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMessageDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getMemberDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getMemberDao().getAllColumns());
            builder.append(" FROM MESSAGE_GROUP T");
            builder.append(" LEFT JOIN MESSAGE T0 ON T.'LAST_MESSAGE_ID'=T0.'OBJECT_ID'");
            builder.append(" LEFT JOIN MEMBER T1 ON T.'LAST_SENDER_ID'=T1.'OBJECT_ID'");
            builder.append(" LEFT JOIN MEMBER T2 ON T.'OWNER_ID'=T2.'OBJECT_ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected MessageGroup loadCurrentDeep(Cursor cursor, boolean lock) {
        MessageGroup entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Message lastMessage = loadCurrentOther(daoSession.getMessageDao(), cursor, offset);
        entity.setLastMessage(lastMessage);
        offset += daoSession.getMessageDao().getAllColumns().length;

        Member lastSender = loadCurrentOther(daoSession.getMemberDao(), cursor, offset);
        entity.setLastSender(lastSender);
        offset += daoSession.getMemberDao().getAllColumns().length;

        Member owner = loadCurrentOther(daoSession.getMemberDao(), cursor, offset);
        entity.setOwner(owner);

        return entity;    
    }

    public MessageGroup loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<MessageGroup> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<MessageGroup> list = new ArrayList<MessageGroup>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<MessageGroup> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<MessageGroup> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}