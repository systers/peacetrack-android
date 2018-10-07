/**
 * 
 */
package com.peacetrack.views.measurements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.peacetrack.R;

/**
 * @author Pooja
 * 
 */
public class AddMeasurementActivity extends AppCompatActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_addmeasurement);
	}

	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater menuInflater
	 * = getMenuInflater(); menuInflater.inflate(R.menu.addmenu, menu);
	 * 
	 * getSupportActionBar().setDisplayShowTitleEnabled(true); return true;
	 * 
	 * }
	 */

	/*
	 * Select the new screen when any icon in action bar is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, AllMeasurementsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
