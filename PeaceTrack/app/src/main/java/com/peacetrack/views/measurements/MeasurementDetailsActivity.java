/**
 * 
 */
package com.peacetrack.views.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.peacetrack.R;
import com.peacetrack.backend.measurements.MeasurementDAO;
import com.peacetrack.models.measurements.Measurement;
import com.peacetrack.views.activities.AddActivityActivity;

public class MeasurementDetailsActivity extends ActionBarActivity implements
		OnClickListener {

	private Measurement measurement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measurementdetails);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
		setViewElements();
	}

	private void initialize() {
		int measurementId = getIntent().getIntExtra("measurementId", 0);
		MeasurementDAO measurementDAO = new MeasurementDAO(getApplicationContext());
		measurement = measurementDAO.getMeasurementWithID(measurementId);
	}

	private void setViewElements() {
		//TODO
		TextView title = (TextView) findViewById(R.id.cohortNameTextView);
		TextView description = (TextView) findViewById(R.id.cohortDescriptionTextView);

		title.setText(measurement.getTitle());
		String des = measurement.getDescription();
		if(des.equals("")) {
			description.setText("None");
		}
		else {
			description.setText(des);
		}
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
