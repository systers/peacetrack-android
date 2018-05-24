package com.peacetrack.models.cohorts;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * *************************************
 * Models the representation Of Cohort
 * **************************************
 */

public class Cohort {

	// Instance Properties
	private int id; // Used to modify existing cohort

	private String name; // Name of the cohort is mandatory and should be unique

	private String description; // Description is Optional

	private int noOfMembers;

	private String ageRange;

	private int noOfMales;

	private int noOfFemales;

	private String position;

	private String otherNotes;

	// Database Table
	public static final String COHORTS_TABLE = "cohorts";

	// Database Table column names
	public static final String COLUMN_ID = "id";

	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_DESCRIPTION = "description";

	public static final String COLUMN_MEMBERS = "members";

	public static final String COLUMN_AGERANGE = "agerange";

	public static final String COLUMN_MALES = "males";

	public static final String COLUMN_FEMALES = "females";

	public static final String COLUMN_POSITION = "position";

	public static final String COLUMN_OTHERNOTES = "othernotes";

	// Database Creation - SQL statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ COHORTS_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_MEMBERS + " integer, "
			+ COLUMN_AGERANGE + " text, "
			+ COLUMN_MALES + " int, "
			+ COLUMN_FEMALES + " int, "
			+ COLUMN_POSITION + " text, "
			+ COLUMN_OTHERNOTES + " text "
			+ ");";

	// Used to create the table
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		Log.i("HEHE", "came");

	}

	// Used to upgrade the table
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("drop table if exists " + COHORTS_TABLE);
		onCreate(database);
	}

	public Cohort() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters for name and description
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
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

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public int getNoOfMembers() {
		return noOfMembers;
	}

	public void setNoOfMembers(int noOfMembers) {
		this.noOfMembers = noOfMembers;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public int getNoOfMales() {
		return noOfMales;
	}

	public void setNoOfMales(int noOfMales) {
		this.noOfMales = noOfMales;
	}

	public int getNoOfFemales() {
		return noOfFemales;
	}

	public void setNoOfFemales(int noOfFemales) {
		this.noOfFemales = noOfFemales;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getOtherNotes() {
		return otherNotes;
	}

	public void setOtherNotes(String otherNotes) {
		this.otherNotes = otherNotes;
	}
}
