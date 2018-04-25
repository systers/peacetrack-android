/**
 * 
 */
package com.peacetrack.backend.measurements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.peacetrack.backend.CommonDatabaseHelper;
import com.peacetrack.models.activities.Activities;
import com.peacetrack.models.measurements.Measurement;

import java.util.ArrayList;

public class MeasurementDAO {

	private CommonDatabaseHelper commonDatabaseHelper;

	private SQLiteDatabase readDatabase;

	private SQLiteDatabase writeDatabase;

	public MeasurementDAO(Context context) {
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
	 * Method to get all the measurements from the local database
	 */
	public ArrayList<Measurement> getAllMeasurements() {
		openDB();

		ArrayList<Measurement> allMeasurements = null;
		String[] columns = new String[7];
		columns[0] = Measurement.COLUMN_ID;
		columns[1] = Measurement.COLUMN_TITLE;
		columns[2] = Measurement.COLUMN_DESCRIPTION;
		columns[3] = Measurement.COLUMN_OUTCOME;
		columns[4] = Measurement.COLUMN_OUTCOME_DATA;
		columns[5] = Measurement.COLUMN_COHORT;
		columns[6] = Measurement.COLUMN_DATE;

		Cursor returnData = readDatabase.query(Measurement.MEASUREMENT_TABLE, columns,
				null, null, null, null, null);

		allMeasurements = extractMeasurements(returnData);
		closeDB();
		return allMeasurements;
	}

	public ArrayList<Measurement> getAllMeasurementsForCohort(int cohortId) {
		openDB();

		ArrayList<Measurement> allMeasurements = null;
		String[] columns = new String[7];
		columns[0] = Measurement.COLUMN_ID;
		columns[1] = Measurement.COLUMN_TITLE;
		columns[2] = Measurement.COLUMN_DESCRIPTION;
		columns[3] = Measurement.COLUMN_OUTCOME;
		columns[4] = Measurement.COLUMN_OUTCOME_DATA;
		columns[5] = Measurement.COLUMN_COHORT;
		columns[6] = Measurement.COLUMN_DATE;

		String where = Measurement.COLUMN_COHORT + "=" + cohortId;

		Cursor returnData = readDatabase.query(Measurement.MEASUREMENT_TABLE, columns,
				where, null, null, null, null);

		allMeasurements = extractMeasurements(returnData);
		closeDB();
		return allMeasurements;
	}

	public Measurement getMeasurementWithID(int id) {
		openDB();

		Measurement measurement = new Measurement();
		String[] columns = new String[7];
		columns[0] = Measurement.COLUMN_ID;
		columns[1] = Measurement.COLUMN_TITLE;
		columns[2] = Measurement.COLUMN_DESCRIPTION;
		columns[3] = Measurement.COLUMN_OUTCOME;
		columns[4] = Measurement.COLUMN_OUTCOME_DATA;
		columns[5] = Measurement.COLUMN_COHORT;
		columns[6] = Measurement.COLUMN_DATE;

		String where = Measurement.COLUMN_ID + "=" + id;

		Cursor returnData = readDatabase.query(Measurement.MEASUREMENT_TABLE, columns,
				where, null, null, null, null);
		returnData.moveToFirst();

		measurement.setId(returnData.getInt(0));
		measurement.setTitle(returnData.getString(1));
		measurement.setDescription(returnData.getString(2));
		measurement.setOutcome(returnData.getInt(3));
		measurement.setOutcomeData(returnData.getString(4));
		measurement.setCohort(returnData.getInt(5));
		measurement.setDate(returnData.getString(6));

		closeDB();

		return measurement;
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
	 * Method used to add a new measurement.
	 */
	public void addMeasurement(Measurement measurement) {
		openDB();

		ContentValues contentValues = new ContentValues(6);
		contentValues.put(Measurement.COLUMN_TITLE, measurement.getTitle());
		contentValues.put(Measurement.COLUMN_DESCRIPTION, measurement.getDescription());
		contentValues.put(Measurement.COLUMN_OUTCOME, measurement.getOutcome());
		contentValues.put(Measurement.COLUMN_OUTCOME_DATA, measurement.getOutcomeData());
		contentValues.put(Measurement.COLUMN_COHORT, measurement.getCohort());
		contentValues.put(Measurement.COLUMN_DATE, measurement.getDate());

		writeDatabase.insert(Measurement.MEASUREMENT_TABLE, null, contentValues);

		closeDB();
	}

	/*
	 * Method to update/modify an existing measurement. This will use measurement ID in
	 * where clause.
	 */
	public void updateMeasurement(Measurement measurement) {
		openDB();

		ContentValues contentValues = new ContentValues(6);
		contentValues.put(Measurement.COLUMN_TITLE, measurement.getTitle());
		contentValues.put(Measurement.COLUMN_DESCRIPTION, measurement.getDescription());
		contentValues.put(Measurement.COLUMN_OUTCOME, measurement.getOutcome());
		contentValues.put(Measurement.COLUMN_OUTCOME_DATA, measurement.getOutcomeData());
		contentValues.put(Measurement.COLUMN_COHORT, measurement.getCohort());
		contentValues.put(Measurement.COLUMN_DATE, measurement.getDate());

		String where = Measurement.COLUMN_ID + "=" + measurement.getId();

		writeDatabase.update(Measurement.MEASUREMENT_TABLE, contentValues, where, null);

		closeDB();
	}

	public int deleteMeasurement(Measurement measurement) {
		openDB();

		String where = Measurement.COLUMN_ID + "=" + measurement.getId();
		// Number of rows removed
		int numRemoved = writeDatabase.delete(Measurement.MEASUREMENT_TABLE, where,
				null);

		closeDB();
		return numRemoved;
	}

	/*
	 * Method to extract measurements from the query.
	 */
	private ArrayList<Measurement> extractMeasurements(Cursor returnData) {
		// The output ArrayList is initialized
		ArrayList<Measurement> output = new ArrayList<Measurement>();
		// Move the counter to the first item in the return data
		returnData.moveToFirst();
		int count = 0;
		// While there are still values in the return data
		while (!returnData.isAfterLast()) {
			// Add the new Project to the ArrayList
			Measurement measurement = new Measurement();
			measurement.setId(Integer.parseInt(returnData.getString(0)));
			measurement.setTitle(returnData.getString(1));
			measurement.setDescription(returnData.getString(2));
			measurement.setOutcome(returnData.getInt(3));
			measurement.setOutcomeData(returnData.getString(4));
			measurement.setCohort(returnData.getInt(5));
			measurement.setDate(returnData.getString(6));

			output.add(count, measurement);
			// Advance the Cursor
			returnData.moveToNext();
			// Advance the counter
			count++;
		}
		// Return the ArrayList
		return output;
	}

}
