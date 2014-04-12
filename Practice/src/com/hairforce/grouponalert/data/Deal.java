package com.hairforce.grouponalert.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * TODO Put here a description of what this class does.
 *
 * @author rosspa.
 *         Created Apr 12, 2014.
 */
public class Deal {
	
	
	private static final String ID = "id";

	private static final String LAST_SEEN = "lastSeen";

	public static final String TABLE = "deals";
	
	public static final String CREATE = "CREATE TABLE " + TABLE + "(" + ID + " TEXT PRIMARY KEY NOT NULL, " + LAST_SEEN + " INTEGER)";

	public Merchant merchant;
	
	public String announcementTitle;
	
	public String dealUrl;
	
	public String title;
	
	public Boolean isSoldOut;
	
	public String shortAnnouncementTitle;
	
	public String id;
	
	public long lastSeen;
	
	public List<Option> options;
	
	public void insert(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(LAST_SEEN, new Date().getTime());
		
		db.insert(TABLE, null, values);
	}
	
	public void update(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(LAST_SEEN, new Date().getTime());
		
		db.update(TABLE, values , id + "= ?", new String[] {id});
	}
	
	public void delete(SQLiteDatabase db) {
		db.delete(TABLE, id + " = ?", new String[] {id});
	}
	
	public static List<Deal> getAll(SQLiteDatabase db) {
		List<Deal> deals = new ArrayList<Deal>();
		
		String query = "SELECT * FROM " + TABLE;
		
		Cursor cursor = db.rawQuery(query, new String[] {});
		
		if(cursor.moveToFirst()) {
			do {
				String id = cursor.getString(0);
				long date = cursor.getLong(1);
				
				Deal deal = new Deal();
				deal.id = id;
				deal.lastSeen = date;
				
				deals.add(deal);
			} while(cursor.moveToNext());
		}
		
		cursor.close();
		
		return deals;
	}
}
