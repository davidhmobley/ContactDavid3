package com.mobley.contactdavid3.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Datasource for the Course table
 * @author mobleyd
 *
 */
public class SqlDataSource {
	protected static final String TAG = SqlDataSource.class.getSimpleName();

	private SQLiteDatabase mDatabase;
	private SqlHelper mSqlHelper;

	private String[] allActionsCols = {
			Actions.ACTIONS_COL_ID,
			Actions.ACTIONS_COL_TYPE,
			Actions.ACTIONS_COL_TIMESTAMP,
			Actions.ACTIONS_COL_SENDTO
	};

	/** ctor */
	public SqlDataSource(Context context) {
		mSqlHelper = new SqlHelper(context);
	}

	/**
	 * Open the database
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		mDatabase = mSqlHelper.getWritableDatabase();
	}
	
	/**
	 * Close the database
	 */
	public void close() {
		mSqlHelper.close();
	}
	
	/**
	 * Start a transaction
	 */
	public void beginTransaction() {
		mDatabase.beginTransaction();
	}
	
	/**
	 * Commit a transaction
	 */
	public void commitTransaction() {
		mDatabase.setTransactionSuccessful();
	}

	/**
	 * End a transaction
	 */
	public void endTransaction() {
		mDatabase.endTransaction();
	}


    /*****************************/
    /***** ACTIONS Functions *****/
    /*****************************/

    public void insertActions(String actionType, long actionTimestamp, String sendTo) {
        // INSERT!
        ContentValues values = new ContentValues();

        //values.put(Account.ACCT_COL_ID, acct.getId());
        values.put(Actions.ACTIONS_COL_TYPE, actionType);
		values.put(Actions.ACTIONS_COL_TIMESTAMP, actionTimestamp);
		values.put(Actions.ACTIONS_COL_SENDTO, sendTo);

        // Insert the record
        mDatabase.insert(Actions.ACTIONS_TABLE_NAME, null, values);
    }

    public void deleteActions() {
        mDatabase.delete(Actions.ACTIONS_TABLE_NAME, null, null);
    }

	public List<Actions> getAllActions() {
		List<Actions> actions = new ArrayList<>();

		Cursor c = mDatabase.query(Actions.ACTIONS_TABLE_NAME,
				allActionsCols,
				null,
				null,
				null,
				null,
				null);

		if (c != null) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				actions.add(new Actions(
						//c.getInt(0), // id
						c.getString(1), // type
						c.getLong(2), // timestamp
						c.getString(3))); // sendTo

				c.moveToNext();
			}
			c.close();
		}

		return actions;
	}
}
