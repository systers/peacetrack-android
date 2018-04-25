/**
 * 
 */
package com.peacetrack.backend.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.peacetrack.backend.CommonDatabaseHelper;
import com.peacetrack.models.activities.Activities;

import java.util.ArrayList;

public class ActivityDAO {

	private CommonDatabaseHelper commonDatabaseHelper;

	private SQLiteDatabase readDatabase;

	private SQLiteDatabase writeDatabase;

	public ActivityDAO(Context context) {
		super();
		this.commonDatabaseHelper = CommonDatabaseHelper.getInstance(context);
		this.readDatabase = commonDatabaseHelper.getReadableDatabase();
		this.writeDatabase = commonDatabaseHelper.getWritableDatabase();
	}

	private void openDB() {
		if (!readDatabase.isOpen()) {
			readDatabase = commonDatabaseHelper.getReadableDatabase();
		}
		if (!writeDatabase.isOpen()) {
			writeDatabase = commonDatabaseHelper.getWritableDatabase();
		}
	}

	private void closeDB() {
		if (readDatabase.isOpen()) {
			readDatabase.close();
		}
		if (writeDatabase.isOpen()) {
			writeDatabase.close();
		}
	}

	/*
	 * Method to get all the activities from the local database
	 */
	public ArrayList<Activities> getAllActivities() {
		openDB();

		ArrayList<Activities> allActivities = null;
		String[] columns = new String[6];
		columns[0] = Activities.COLUMN_ID;
		columns[1] = Activities.COLUMN_TITLE;
		columns[2] = Activities.COLUMN_DESCRIPTION;
		columns[3] = Activities.COLUMN_COHORT;
		columns[4] = Activities.COLUMN_DATE;
		columns[5] = Activities.COLUMN_TIME;

		Cursor returnData = readDatabase.query(Activities.ACTIVITY_TABLE, columns,
				null, null, null, null, null);

		allActivities = extractActivities(returnData);
		closeDB();
		return allActivities;
	}

	public String[] getActivityDatesForCohort(int cohortId) {
		openDB();

		String[] dates = null;
		String[] columns = new String[1];
		columns[0] = Activities.COLUMN_DATE;

		String where = Activities.COLUMN_COHORT + "=" + cohortId;
		Cursor returnData = readDatabase.query(Activities.ACTIVITY_TABLE, columns,
				where, null, null, null, null);

		dates = extractColumnData(returnData);
		closeDB();
		return dates;
	}

	/*
	 * Method to get a particular activity given its ID. Mainly used to modify the
	 * existing activity.
	 */
	public Activities getActivityWithID(int id) {
		openDB();

		Activities activity = new Activities();
		String[] columns = new String[6];
		columns[0] = Activities.COLUMN_ID;
		columns[1] = Activities.COLUMN_TITLE;
		columns[2] = Activities.COLUMN_DESCRIPTION;
		columns[3] = Activities.COLUMN_COHORT;
		columns[4] = Activities.COLUMN_DATE;
		columns[5] = Activities.COLUMN_TIME;

		String where = Activities.COLUMN_ID + "=" + id;

		Cursor returnData = readDatabase.query(Activities.ACTIVITY_TABLE, columns,
				where, null, null, null, null);
		returnData.moveToFirst();

		activity.setId(Integer.parseInt(returnData.getString(0)));
		activity.setTitle(returnData.getString(1));
		activity.setDescription(returnData.getString(2));
		activity.setCohort(returnData.getInt(3));
		activity.setDate(returnData.getString(4));
		activity.setTime(returnData.getString(5));

		closeDB();

		return activity;
	}

	public Activities getActivityWithTitle(String title) {
		openDB();

		Activities activity = new Activities();
		String[] columns = new String[6];
		columns[0] = Activities.COLUMN_ID;
		columns[1] = Activities.COLUMN_TITLE;
		columns[2] = Activities.COLUMN_DESCRIPTION;
		columns[3] = Activities.COLUMN_COHORT;
		columns[4] = Activities.COLUMN_DATE;
		columns[5] = Activities.COLUMN_TIME;

		String where = Activities.COLUMN_TITLE + "= '" + title +"'";

		Cursor returnData = readDatabase.query(Activities.ACTIVITY_TABLE, columns,
				where, null, null, null, null);
		returnData.moveToFirst();

		activity.setId(Integer.parseInt(returnData.getString(0)));
		activity.setTitle(returnData.getString(1));
		activity.setDescription(returnData.getString(2));
		activity.setCohort(returnData.getInt(3));
		activity.setDate(returnData.getString(4));
		activity.setTime(returnData.getString(5));

		closeDB();

		return activity;
	}

	/*
	 * Method used to add a new activity.
	 */
	public void addActivity(Activities activity) {
		openDB();

		ContentValues contentValues = new ContentValues(5);
		contentValues.put(Activities.COLUMN_TITLE, activity.getTitle());
		contentValues.put(Activities.COLUMN_DESCRIPTION, activity.getDescription());
		contentValues.put(Activities.COLUMN_COHORT, activity.getCohort());
		contentValues.put(Activities.COLUMN_DATE, activity.getDate());
		contentValues.put(Activities.COLUMN_TIME, activity.getTime());

		writeDatabase.insert(Activities.ACTIVITY_TABLE, null, contentValues);

		closeDB();
	}

	/*
	 * Method to update/modify an existing activity. This will use activity ID in
	 * where clause.
	 */
	public void updateActivity(Activities activity) {
		openDB();

		ContentValues contentValues = new ContentValues(5);
		contentValues.put(Activities.COLUMN_TITLE, activity.getTitle());
		contentValues.put(Activities.COLUMN_DESCRIPTION, activity.getDescription());
		contentValues.put(Activities.COLUMN_COHORT, activity.getCohort());
		contentValues.put(Activities.COLUMN_DATE, activity.getDate());
		contentValues.put(Activities.COLUMN_TIME, activity.getTime());

		String where = Activities.COLUMN_ID + "=" + activity.getId();
		writeDatabase.update(Activities.ACTIVITY_TABLE, contentValues, where, null);

		closeDB();
	}

	public int deleteActivity(Activities activity) {
		openDB();

		String where = Activities.COLUMN_ID + "=" + activity.getId();
		// Number of rows removed
		int numRemoved = writeDatabase.delete(Activities.ACTIVITY_TABLE, where,
				null);

		closeDB();
		return numRemoved;
	}

	/*
	 * Method to extract activities from the query.
	 */
	private ArrayList<Activities> extractActivities(Cursor returnData) {
		// The output ArrayList is initialized
		ArrayList<Activities> output = new ArrayList<Activities>();
		// Move the counter to the first item in the return data
		returnData.moveToFirst();
		int count = 0;
		// While there are still values in the return data
		while (!returnData.isAfterLast()) {
			// Add the new Project to the ArrayList
			Activities activity = new Activities();
			activity.setId(Integer.parseInt(returnData.getString(0)));
			activity.setTitle(returnData.getString(1));
			activity.setDescription(returnData.getString(2));
			activity.setCohort(returnData.getInt(3));
			activity.setDate(returnData.getString(4));
			activity.setTime(returnData.getString(5));
			output.add(count, activity);
			// Advance the Cursor
			returnData.moveToNext();
			// Advance the counter
			count++;
		}
		// Return the ArrayList
		return output;
	}

	private String[] extractColumnData(Cursor returnData) {
		String[] output = new String[returnData.getCount()];
		returnData.moveToFirst();
		int count = 0;
		while (!returnData.isAfterLast()) {
			output[count] = returnData.getString(0);
			returnData.moveToNext();
			count++;
		}
		return output;
	}
}
