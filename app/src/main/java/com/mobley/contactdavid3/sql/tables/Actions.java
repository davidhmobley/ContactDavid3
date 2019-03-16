package com.mobley.contactdavid3.sql.tables;

public class Actions {
    protected static final String TAG = Actions.class.getSimpleName();

    public static final String ACTIONS_TABLE_NAME = "Actions";

    public static final String ACTIONS_COL_ID = "_id";
    public static final String ACTIONS_COL_TYPE = "Type";
    public static final String ACTIONS_COL_TIMESTAMP = "Timestamp";

    public long mId;
    public String mType;
    public long mTimestamp;

    public Actions(String type, long timestamp) {
        mType = type;
        mTimestamp = timestamp;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }
}
