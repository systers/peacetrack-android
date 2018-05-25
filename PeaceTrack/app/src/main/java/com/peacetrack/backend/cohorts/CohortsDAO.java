/**
 * 
 */
package com.peacetrack.backend.cohorts;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.peacetrack.backend.CommonDatabaseHelper;
import com.peacetrack.models.cohorts.Cohort;

public class CohortsDAO {

	private CommonDatabaseHelper commonDatabaseHelper;

	private SQLiteDatabase readDatabase;

	private SQLiteDatabase writeDatabase;

	public CohortsDAO(Context context) {
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
	 * Method to get all the cohorts from the local database
	 */
	public ArrayList<Cohort> getAllCohorts() {
		openDB();

		ArrayList<Cohort> allCohorts = null;
		String[] columns = new String[9];
		columns[0] = Cohort.COLUMN_ID;
		columns[1] = Cohort.COLUMN_NAME;
		columns[2] = Cohort.COLUMN_DESCRIPTION;
		columns[3] = Cohort.COLUMN_MEMBERS;
		columns[4] = Cohort.COLUMN_AGERANGE;
		columns[5] = Cohort.COLUMN_MALES;
		columns[6] = Cohort.COLUMN_FEMALES;
		columns[7] = Cohort.COLUMN_POSITION;
		columns[8] = Cohort.COLUMN_OTHERNOTES;

		Cursor returnData = readDatabase.query(Cohort.COHORTS_TABLE, columns,
				null, null, null, null, null);

		allCohorts = extractCohorts(returnData);
		closeDB();
		return allCohorts;
	}

	/*
	 * Method to get a particular cohort given its ID. Mainly used to modify the
	 * existing cohort.
	 */
	public Cohort getCohortWithID(int id) {
		openDB();

		Cohort cohort = new Cohort();
		String[] columns = new String[9];
		columns[0] = Cohort.COLUMN_ID;
		columns[1] = Cohort.COLUMN_NAME;
		columns[2] = Cohort.COLUMN_DESCRIPTION;
		columns[3] = Cohort.COLUMN_MEMBERS;
		columns[4] = Cohort.COLUMN_AGERANGE;
		columns[5] = Cohort.COLUMN_MALES;
		columns[6] = Cohort.COLUMN_FEMALES;
		columns[7] = Cohort.COLUMN_POSITION;
		columns[8] = Cohort.COLUMN_OTHERNOTES;

		String where = Cohort.COLUMN_ID + "=" + id;

		Cursor returnData = readDatabase.query(Cohort.COHORTS_TABLE, columns,
				where, null, null, null, null);
		returnData.moveToFirst();

		cohort.setId(Integer.parseInt(returnData.getString(0)));
		cohort.setName(returnData.getString(1));
		cohort.setDescription(returnData.getString(2));
		cohort.setNoOfMembers(returnData.getInt(3));
		cohort.setAgeRange(returnData.getString(4));
		cohort.setNoOfMales(returnData.getInt(5));
		cohort.setNoOfFemales(returnData.getInt(6));
		cohort.setPosition(returnData.getString(7));
		cohort.setOtherNotes(returnData.getString(8));

		closeDB();

		return cohort;
	}

	/*
	 * Method used to add a new cohort.
	 */
	public void addCohort(Cohort cohort) {
		openDB();

		ContentValues contentValues = new ContentValues(8);
		contentValues.put(Cohort.COLUMN_NAME, cohort.getName());
		contentValues.put(Cohort.COLUMN_DESCRIPTION, cohort.getDescription());
		contentValues.put(Cohort.COLUMN_MEMBERS, cohort.getNoOfMembers());
		contentValues.put(Cohort.COLUMN_AGERANGE, cohort.getAgeRange());
		contentValues.put(Cohort.COLUMN_MALES, cohort.getNoOfMales());
		contentValues.put(Cohort.COLUMN_FEMALES, cohort.getNoOfFemales());
		contentValues.put(Cohort.COLUMN_POSITION, cohort.getPosition());
		contentValues.put(Cohort.COLUMN_OTHERNOTES, cohort.getOtherNotes());

		writeDatabase.insert(Cohort.COHORTS_TABLE, null, contentValues);

		closeDB();
	}

	/*
	 * Method to update/modify an existing cohort. This will use cohort ID in
	 * where clause.
	 */
	public void updateCohort(Cohort cohort) {
		openDB();

		ContentValues contentValues = new ContentValues(8);
		contentValues.put(Cohort.COLUMN_NAME, cohort.getName());
		contentValues.put(Cohort.COLUMN_DESCRIPTION, cohort.getDescription());
		contentValues.put(Cohort.COLUMN_MEMBERS, cohort.getNoOfMembers());
		contentValues.put(Cohort.COLUMN_AGERANGE, cohort.getAgeRange());
		contentValues.put(Cohort.COLUMN_MALES, cohort.getNoOfMales());
		contentValues.put(Cohort.COLUMN_FEMALES, cohort.getNoOfFemales());
		contentValues.put(Cohort.COLUMN_POSITION, cohort.getPosition());
		contentValues.put(Cohort.COLUMN_OTHERNOTES, cohort.getOtherNotes());

		String where = Cohort.COLUMN_ID + "=" + cohort.getId();
		writeDatabase.update(Cohort.COHORTS_TABLE, contentValues, where, null);

		closeDB();
	}

	public int deleteCohort(Cohort cohort) {
		openDB();

		String where = Cohort.COLUMN_ID + "=" + cohort.getId();
		// Number of rows removed
		int numRemoved = writeDatabase.delete(Cohort.COHORTS_TABLE, where,
				null);

		closeDB();
		return numRemoved;
	}

	/*
	 * Method to extract cohorts from the query.
	 */
	private ArrayList<Cohort> extractCohorts(Cursor returnData) {
		// The output ArrayList is initialized
		ArrayList<Cohort> output = new ArrayList<Cohort>();
		// Move the counter to the first item in the return data
		returnData.moveToFirst();
		int count = 0;
		// While there are still values in the return data
		while (!returnData.isAfterLast()) {
			// Add the new Project to the ArrayList
			Cohort cohort = new Cohort();
			cohort.setId(Integer.parseInt(returnData.getString(0)));
			cohort.setName(returnData.getString(1));
			cohort.setDescription(returnData.getString(2));
			cohort.setNoOfMembers(returnData.getInt(3));
			cohort.setAgeRange(returnData.getString(4));
			cohort.setNoOfMales(returnData.getInt(5));
			cohort.setNoOfFemales(returnData.getInt(6));
			cohort.setPosition(returnData.getString(7));
			cohort.setOtherNotes(returnData.getString(8));

			output.add(count, cohort);
			// Advance the Cursor
			returnData.moveToNext();
			// Advance the counter
			count++;
		}
		// Return the ArrayList
		return output;
	}

}
