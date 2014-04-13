package com.hairforce.grouponalert.data;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DealMap extends HashMap<String, Long> {
	private static final long serialVersionUID = 2085494577150982285L;

	public void load(SQLiteDatabase db) {
		String query = "SELECT * FROM " + Deal.TABLE;
		
		Cursor cursor = db.rawQuery(query, new String[] {});
		
		if(cursor.moveToFirst()) {
			do {
				String id = cursor.getString(0);
				long date = cursor.getLong(1);
				
				put(id, date);
			} while(cursor.moveToNext());
		}
		
		cursor.close();
	}
	
	public void save(SQLiteDatabase db) {
		db.delete(Deal.TABLE, null, null);
		
		for(Entry<String, Long> entry : entrySet()) {
			ContentValues values = new ContentValues();
			values.put(Deal.ID, entry.getKey());
			values.put(Deal.LAST_SEEN, entry.getValue());
			
			db.insert(Deal.TABLE, null, values);
		}
	}
}
