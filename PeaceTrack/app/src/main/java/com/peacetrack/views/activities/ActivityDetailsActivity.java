/**
 * 
 */
package com.peacetrack.views.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.peacetrack.R;
import com.peacetrack.backend.activities.ActivityDAO;
import com.peacetrack.backend.cohorts.CohortsDAO;
import com.peacetrack.backend.outputs.OutputDAO;
import com.peacetrack.models.activities.Activities;
import com.peacetrack.models.cohorts.Cohort;
import com.peacetrack.views.measurements.AddMeasurementActivity;

/**
 * Show the activity details
 */
public class ActivityDetailsActivity extends ActionBarActivity implements
		OnClickListener {

	private Activities activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activitydetails);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
		bindButtonListeners();
		setViewElements();
	}

	private void initialize() {
		int activityId = getIntent().getIntExtra("activityId", 0);
		ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
		activity = activityDAO.getActivityWithID(activityId);
	}

	private void bindButtonListeners() {

	}

	private void setViewElements() {
		TextView name = (TextView) findViewById(R.id.cohortNameTextView);
		TextView description = (TextView) findViewById(R.id.cohortDescriptionTextView);
		TextView cohort = (TextView) findViewById(R.id.activityCohortTextView);
		TextView date = (TextView) findViewById(R.id.activityDateTextView);
		TextView time = (TextView) findViewById(R.id.activityTimeTextView);

		name.setText(activity.getTitle());
		String des = activity.getDescription();

		if(des.equals("")) {
			description.setText("None");
		}
		else {
			description.setText(des);
		}

		cohort.setText(getCohortName());
		date.setText(activity.getDate());
		time.setText(activity.getTime());
	}

	//TODO: Foreign key constraints - if a coho
	private String getCohortName() {
		CohortsDAO cohortsDAO = new CohortsDAO(getApplicationContext());
		Cohort cohort = cohortsDAO.getCohortWithID(activity.getCohort());
		return cohort.getName();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.cohortdetailsmenu, menu);

		return true;
	}

	/*
	 * Select the new screen when any icon in action bar is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, AllActivitiesActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		} else if (itemId == R.id.action_edit) {
			Intent intent = new Intent(this, AddActivityActivity.class);
			intent.putExtra("activityId", activity.getId());
			intent.putExtra("isEdit", true);
			startActivity(intent);
		} else if(itemId == R.id.action_delete) {
			showDeleteActivityDialog();
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void showDeleteActivityDialog() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ActivityDetailsActivity.this);
		// set title
		alertDialogBuilder.setTitle(getString(R.string.activityDeleteTitle));
		// set dialog message
		alertDialogBuilder
				.setMessage(getString(R.string.activityDeleteMessage))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int id) {
								deleteActivity();
								Class actClass = null;
								try {
									actClass = Class.forName("com.peacetrack.views.activities.AllActivitiesActivity");
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								Intent intent = new Intent(ActivityDetailsActivity.this, actClass);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						})
				.setNegativeButton(getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int id) {
								dialog.dismiss();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	private void deleteActivity() {
		ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
		activityDAO.deleteActivity(activity);

		OutputDAO outputDAO = new OutputDAO(getApplicationContext());
		outputDAO.deleteOutputs(activity.getId());

		finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.addnewactivitybutton) {
			Intent intent = new Intent(this, AddActivityActivity.class);
			this.startActivity(intent);
		} else if (id == R.id.addnewmeasurementbutton) {
			Intent intent = new Intent(this, AddMeasurementActivity.class);
			this.startActivity(intent);
		}
	}
}
