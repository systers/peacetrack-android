package com.peacetrack.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.peacetrack.models.activities.Activities;
import com.peacetrack.models.measurements.Measurement;
import com.peacetrack.models.outputs.Output;
import com.peacetrack.models.cohorts.Cohort;

/*
 * @author Pooja
 * 
 * ********************************************************************************
 * This is a common database helper which is shared by many different model classes
 * ********************************************************************************
 * 
 */
public class CommonDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "database.db";

	public static final int DATABASE_VERSION = 1;

	private static CommonDatabaseHelper commonDatabaseHelper;

	private CommonDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static CommonDatabaseHelper getInstance(Context context) {
		commonDatabaseHelper = new CommonDatabaseHelper(context);
		return commonDatabaseHelper;
	}

	// When database is created this method will be called and within this
	// method further methods will be called to create tables(in case they are
	// not)
	@Override
	public void onCreate(SQLiteDatabase db) {
		Cohort.onCreate(db);
		Activities.onCreate(db);
		Output.onCreate(db);
		Measurement.onCreate(db);
	}

	// Method called during upgradation
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void onOpen(SQLiteDatabase database) {
		super.onOpen(database);
		if (!database.isReadOnly()) {
			// Enable foreign key constraints
			database.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

}
