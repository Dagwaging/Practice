package com.hairforce.grouponalert.data;

import java.util.List;

/**
 * TODO Put here a description of what this class does.
 *
 * @author rosspa.
 *         Created Apr 12, 2014.
 */
public class Deal {
	
	
	static final String ID = "id";

	static final String LAST_SEEN = "lastSeen";

	public static final String TABLE = "deals";
	
	public static final String CREATE = "CREATE TABLE " + TABLE + "(" + ID + " TEXT PRIMARY KEY NOT NULL, " + LAST_SEEN + " INTEGER)";

	public Merchant merchant;
	
	public String announcementTitle;
	
	public String dealUrl;
	
	public String title;
	
	public Boolean isSoldOut;
	
	public String shortAnnouncementTitle;
	
	public String id;
	
	public List<Option> options;
}
