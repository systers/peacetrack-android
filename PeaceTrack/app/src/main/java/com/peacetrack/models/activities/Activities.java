package com.peacetrack.models.activities;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * *************************************
 * Models the representation Of Activities
 * **************************************
 */

public class Activities {

	// Instance Properties
	private int id; // Used to modify existing activity

	private String title; // Title of the activity is mandatory and should be unique

	private String description; // Description is Optional

	private String date;

	private String time;

	private int cohort;

	// Database Table
	public static final String ACTIVITY_TABLE = "activities";

	// Database Table column names
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_TITLE = "title";

	public static final String COLUMN_DESCRIPTION = "description";

	public static final String COLUMN_DATE = "date";

	public static final String COLUMN_TIME = "time";

	public static final String COLUMN_COHORT = "cohort";

	// Database Creation - SQL statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ ACTIVITY_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_DATE + " text, "
			+ COLUMN_TIME + " text, "
			+ COLUMN_COHORT + " integer not null "
			+ ");";

	// Used to create the table
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Used to upgrade the table
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("drop table if exists " + ACTIVITY_TABLE);
		onCreate(database);
	}

	public Activities() {
		super();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCohort() {
		return cohort;
	}

	public void setCohort(int cohort) {
		this.cohort = cohort;
	}
}
