package com.peacetrack.models.measurements;

import android.database.sqlite.SQLiteDatabase;

/*
 * *************************************
 * Models the representation Of Measurements
 * **************************************
 */

public class Measurement {

	// Instance Properties
	private int id; // Used to modify existing activity

	private String title; // Title of the activity is mandatory and should be unique

	private String description; // Description is Optional

	private int cohort;

	private int outcome;

	private String outcomeData;

	private String date;

	// Database Table
	public static final String MEASUREMENT_TABLE = "measurement";

	// Database Table column names
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_TITLE = "title";

	public static final String COLUMN_DESCRIPTION = "description";

	public static final String COLUMN_OUTCOME = "outcome";

	public static final String COLUMN_OUTCOME_DATA = "outcome_data";

	public static final String COLUMN_COHORT = "cohort";

	public static final String COLUMN_DATE = "date";

	// Database Creation - SQL statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ MEASUREMENT_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_OUTCOME + " integer not null, "
			+ COLUMN_OUTCOME_DATA + " text, "
			+ COLUMN_COHORT + " integer not null, "
			+ COLUMN_DATE + " text "
			+ ");";

	// Used to create the table
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Used to upgrade the table
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("drop table if exists " + MEASUREMENT_TABLE);
		onCreate(database);
	}

	public Measurement() {
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

	public int getCohort() {
		return cohort;
	}

	public void setCohort(int cohort) {
		this.cohort = cohort;
	}

	public int getOutcome() {
		return outcome;
	}

	public void setOutcome(int outcome) {
		this.outcome = outcome;
	}

	public String getOutcomeData() {
		return outcomeData;
	}

	public void setOutcomeData(String outcomeData) {
		this.outcomeData = outcomeData;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
