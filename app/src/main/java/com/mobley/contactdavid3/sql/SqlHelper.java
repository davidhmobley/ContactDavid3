package com.mobley.contactdavid3.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobley.contactdavid3.sql.tables.Actions;

/**
 * Specialized version of SQLiteOpenHelper for use with ContactDavid3
 * @author mobleyd
 *
 */
public class SqlHelper extends SQLiteOpenHelper {
	protected static final String TAG = SqlHelper.class.getSimpleName();
	
	private static final String DATABASE_NAME = "ibext.db";
	private static final int DATABASE_VERSION = 2;

	public SqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*************/
	/** ACCOUNT **/
	/*************/
	private static final String ACTIONS_TABLE_CREATE =
			"create table " + Actions.ACTIONS_TABLE_NAME + "( " +
					Actions.ACTIONS_COL_ID + " integer primary key autoincrement, " +
					Actions.ACTIONS_COL_TYPE + " text not null, " +
					Actions.ACTIONS_COL_TIMESTAMP + " integer not null, " +
					Actions.ACTIONS_COL_SENDTO + " text not null);";
	private static final String ACTIONS_TABLE_DROP = "drop table if exists " + Actions.ACTIONS_TABLE_NAME;

	/**
	private static final String ACCOUNT_UNIQUE_INDEX_CREATE =
			"create unique index if not exists " + Account.ACCT_UNIQUE_INDEX_NAME +
					" on " + Account.ACCT_TABLE_NAME + " (" + Account.ACCT_COL_ACCOUNT_ID + ");";
	private static final String ACCOUNT_UNIQUE_INDEX_DROP = "drop index if exists " + Account.ACCT_UNIQUE_INDEX_NAME;
	 **/

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ACTIONS_TABLE_CREATE);
		//db.execSQL(ACCOUNT_UNIQUE_INDEX_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(ACCOUNT_UNIQUE_INDEX_DROP);
        db.execSQL(ACTIONS_TABLE_DROP);

		onCreate(db);
	}
}
