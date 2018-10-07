/**
 * 
 */
package com.peacetrack.views.cohorts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.peacetrack.R;
import com.peacetrack.views.activities.AddActivityActivity;
import com.peacetrack.views.measurements.AddMeasurementActivity;

/**
 * Show the cohort details
 */
public class CohortDetailsActivity extends AppCompatActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cohortdetails);
	}

	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		Button addnewactivitybutton = (Button) findViewById(R.id.addnewactivitybutton);
		Button addnewmeasurementbutton = (Button) findViewById(R.id.addnewmeasurementbutton);
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
