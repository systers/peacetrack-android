package com.peacetrack.models.outputs;

import android.database.sqlite.SQLiteDatabase;

/*
 * ***********************************
 * Models the representation Of Output
 * ***********************************
 */

public class Output {

	// Instance Properties
	private int id;

	private int outputId;

	private int activityId;

	private String data;

	// Database Table
	public static final String OUTPUT_TABLE = "output";

	// Database Table column names
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_OUTPUT = "output";

	public static final String COLUMN_ACTIVITY = "activity";

	public static final String COLUMN_DATA = "data";

	// Database Creation - SQL statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ OUTPUT_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_OUTPUT + " integer not null, "
			+ COLUMN_ACTIVITY + " integer not null, "
			+ COLUMN_DATA + " text "
			+ ");";

	// Used to create the table
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Used to upgrade the table
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("drop table if exists " + OUTPUT_TABLE);
		onCreate(database);
	}

	public Output() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getOutputId() {
		return outputId;
	}

	public void setOutputId(int outputId) {
		this.outputId = outputId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}

