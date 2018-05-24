package com.peacetrack.backend.outputs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.peacetrack.backend.CommonDatabaseHelper;
import com.peacetrack.models.outputs.Output;

import java.util.ArrayList;

public class OutputDAO {

	private CommonDatabaseHelper commonDatabaseHelper;

	private SQLiteDatabase readDatabase;

	private SQLiteDatabase writeDatabase;

	public OutputDAO(Context context) {
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
	 * Method to get all the outputs from the local database
	 */
	public ArrayList<Output> getAllOutputs() {
		openDB();

		ArrayList<Output> allOutputs = null;
		String[] columns = new String[4];
		columns[0] = Output.COLUMN_ID;
		columns[1] = Output.COLUMN_OUTPUT;
		columns[2] = Output.COLUMN_ACTIVITY;
		columns[3] = Output.COLUMN_DATA;

		Cursor returnData = readDatabase.query(Output.OUTPUT_TABLE, columns,
				null, null, null, null, null);

		allOutputs = extractOutputs(returnData);
		closeDB();
		return allOutputs;
	}

	public ArrayList<Output> getAllOutputsForActivity(int activityId) {
		openDB();

		ArrayList<Output> allOutputs = null;
		String[] columns = new String[4];
		columns[0] = Output.COLUMN_ID;
		columns[1] = Output.COLUMN_OUTPUT;
		columns[2] = Output.COLUMN_ACTIVITY;
		columns[3] = Output.COLUMN_DATA;

		String where = Output.COLUMN_ACTIVITY + "=" + activityId;

		Cursor returnData = readDatabase.query(Output.OUTPUT_TABLE, columns,
				where, null, null, null, null);

		allOutputs = extractOutputs(returnData);
		closeDB();
		return allOutputs;
	}

	/*
	 * Method used to add a new output.
	 */
	public void addOutput(Output output) {
		openDB();

		ContentValues contentValues = new ContentValues(3);
		contentValues.put(Output.COLUMN_OUTPUT, output.getOutputId());
		contentValues.put(Output.COLUMN_ACTIVITY, output.getActivityId());
		contentValues.put(Output.COLUMN_DATA, output.getData());

		writeDatabase.insert(Output.OUTPUT_TABLE, null, contentValues);

		closeDB();
	}

	/*
	 * Method to update/modify an existing activity. This will use activity ID in
	 * where clause.
	 */
	public void updateOutput(Output output) {
		openDB();

		ContentValues contentValues = new ContentValues(3);
		contentValues.put(Output.COLUMN_OUTPUT, output.getOutputId());
		contentValues.put(Output.COLUMN_ACTIVITY, output.getActivityId());
		contentValues.put(Output.COLUMN_DATA, output.getData());

		String where = Output.COLUMN_OUTPUT + "=" + output.getOutputId() + " AND " + Output.COLUMN_ACTIVITY + "=" + output.getActivityId();

		writeDatabase.update(Output.OUTPUT_TABLE, contentValues, where, null);

		closeDB();
	}

	public int deleteOutput(Output output) {
		openDB();

		String where = Output.COLUMN_OUTPUT + "=" + output.getOutputId() + " AND " + Output.COLUMN_ACTIVITY + "=" + output.getActivityId();
		// Number of rows removed
		int numRemoved = writeDatabase.delete(Output.OUTPUT_TABLE, where,
				null);

		closeDB();
		return numRemoved;
	}

	public int deleteOutputs(int activityId) {
		openDB();

		String where = Output.COLUMN_ACTIVITY + "=" + activityId;
		// Number of rows removed
		int numRemoved = writeDatabase.delete(Output.OUTPUT_TABLE, where,
				null);

		closeDB();
		return numRemoved;
	}

	/*
	 * Method to extract activities from the query.
	 */
	private ArrayList<Output> extractOutputs(Cursor returnData) {
		// The output ArrayList is initialized
		ArrayList<Output> outputs = new ArrayList<Output>();
		// Move the counter to the first item in the return data
		returnData.moveToFirst();
		int count = 0;
		// While there are still values in the return data
		while (!returnData.isAfterLast()) {
			// Add the new Project to the ArrayList
			Output output = new Output();
			output.setId(Integer.parseInt(returnData.getString(0)));
			output.setOutputId(Integer.parseInt(returnData.getString(1)));
			output.setActivityId(Integer.parseInt(returnData.getString(2)));
			output.setData(returnData.getString(3));

			outputs.add(count, output);
			// Advance the Cursor
			returnData.moveToNext();
			// Advance the counter
			count++;
		}
		// Return the ArrayList
		return outputs;
	}

}
