package com.hairforce.grouponalert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hairforce.grouponalert.data.Deal;

/**
 * TODO Put here a description of what this class does.
 *
 * @author rosspa.
 *         Created Apr 12, 2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context) {
		super(context, "grouponalert", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Deal.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.delete(Deal.TABLE, null, null);
		
		db.execSQL(Deal.CREATE);
	}

}
