package com.peacetrack.backend.indicators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.peacetrack.models.indicators.Indicators;

public class IndicatorsDBHandler extends SQLiteOpenHelper {

	private String databasePath;
	private static String DB_NAME = "indicators.sqlite";
	private SQLiteDatabase sqLiteDatabase;
	private final Context context;

	public IndicatorsDBHandler(Context context) {
		super(context, DB_NAME, null, 1);
		this.databasePath = context.getDatabasePath(DB_NAME).getAbsolutePath();
		this.context = context;
		this.sqLiteDatabase = this.getReadableDatabase();
		try {
			this.createDataBase();
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}

		closeDB();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		this.sqLiteDatabase = this.getReadableDatabase();

		try {
			copyDataBaseToSystem();
		} catch (IOException e) {
			throw new Error("Error!! Could not copy database to the system");
		}

	}

	/*
	 * public void createDataBase() throws IOException { boolean dbExist =
	 * checkDataBase();
	 * 
	 * if (dbExist) { openDB(); } else { this.sqLiteDatabase =
	 * this.getReadableDatabase();
	 * 
	 * try { copyDataBaseToSystem(); } catch (IOException e) { throw new
	 * Error("Error!! Could not copy database to the system"); } } }
	 */

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			checkDB = SQLiteDatabase.openDatabase(databasePath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 **/
	private void copyDataBaseToSystem() throws IOException {
		// Open your local db as an input stream
		InputStream myInput = context.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = databasePath;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/*
	 * public void openDataBase() throws SQLException { // Open the database
	 * sqLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null,
	 * SQLiteDatabase.OPEN_READONLY); }
	 */

	@Override
	public synchronized void close() {
		if (sqLiteDatabase != null)
			sqLiteDatabase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Indicators.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Indicators.onUpgrade(db, oldVersion, newVersion);
	}

	private void openDB() {
		if (!sqLiteDatabase.isOpen()) {
			sqLiteDatabase = this.getReadableDatabase();
		}
	}

	private void closeDB() {
		if (sqLiteDatabase.isOpen()) {
			sqLiteDatabase.close();
		}
	}

	public List<String> getAllPosts() {
		openDB();
		List<String> allPosts = null;
		String queryString = "select distinct " + Indicators.COLUMN_POST
				+ " from " + Indicators.INDICATORS_TABLE + " order by "
				+ Indicators.COLUMN_POST + " asc";
		try {
			Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
			allPosts = extractStrings(cursor);
		} catch (Exception e) {
			throw new Error("Error in cursor ");
		}

		closeDB();
		return allPosts;
	}

	public List<String> getAllSectors() {
		openDB();
		List<String> allSectors = null;
		String queryString = "select distinct " + Indicators.COLUMN_SECTOR
				+ " from " + Indicators.INDICATORS_TABLE + " order by "
				+ Indicators.COLUMN_SECTOR + " asc";
		Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
		allSectors = extractStrings(cursor);
		closeDB();
		return allSectors;
	}

	public List<String> getAllSectorsForPost(String post) {
		openDB();
		List<String> allSectors = null;
		String queryString = "select distinct " + Indicators.COLUMN_PROJECT
				+ " from " + Indicators.INDICATORS_TABLE + " where "
				+ Indicators.COLUMN_POST + " = '" + post + "'" + " order by "
				+ Indicators.COLUMN_PROJECT + " asc";
		Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
		allSectors = extractStrings(cursor);
		closeDB();
		return allSectors;
	}

	private ArrayList<String> extractStrings(Cursor cursor) {
		// The output ArrayList is initialized
		ArrayList<String> output = new ArrayList<String>();
		// Move the counter to the first item in the cursor
		cursor.moveToFirst();
		int count = 0;
		// looping through all rows and adding to list
		while (!cursor.isAfterLast()) {
			output.add(count, cursor.getString(0));
			// Advance the Cursor
			cursor.moveToNext();
			// Advance the counter
			count++;
		}
		// Return the ArrayList
		return output;
	}
}
