/**
 * 
 */
package com.peacetrack.views.measurements;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.peacetrack.R;
import com.peacetrack.backend.cohorts.CohortsDAO;
import com.peacetrack.backend.measurements.MeasurementDAO;
import com.peacetrack.models.cohorts.Cohort;
import com.peacetrack.models.measurements.Measurement;

import java.util.ArrayList;
import java.util.Calendar;

public class AddMeasurementForCohortActivity extends ActionBarActivity implements
		OnClickListener {

	private boolean isEdit;
	private String oldTitle;
	private int cohortId;

	private Measurement measurement;

	private Button addMeasurementButton;

	private EditText titleEditText;
	private EditText descriptionEditText;
	private EditText outcomeEditText;

	static final int DATE_DIALOG_ID = 100;
	private EditText dateEditText;
	private int year, month, day;

	private Spinner outcomeSpinner;
	private String[] outcomes = {"Outcome 1", "Outcome 2", "Outcome 3", "Outcome 4", "Outcome 5"};
	private ArrayAdapter<String> outcomeDataAdapter;

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into EditText
			if(day<10 && month<10)
				dateEditText.setText(new StringBuilder().append("0").append(day)
						.append("-").append("0").append(month + 1).append("-").append(year).append(" "));
			else if(day<10 && month>=10)
				dateEditText.setText(new StringBuilder().append("0").append(day)
						.append("-").append(month + 1).append("-").append(year).append(" "));
			else if(day>=10 && month<10)
				dateEditText.setText(new StringBuilder().append(day)
						.append("-").append("0").append(month + 1).append("-").append(year).append(" "));
			else
				dateEditText.setText(new StringBuilder().append(day)
						.append("-").append(month + 1).append("-").append(year).append(" "));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmeasurement_cohort);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
		if(isEdit) {
			setEditableElements();
		}
	}

	private void initialize() {
		cohortId = getIntent().getIntExtra("cohortId", 0);

		isEdit = getIntent().getBooleanExtra("isEdit", false);
		if(isEdit) {
			int measurementId = getIntent().getIntExtra("measurementId", 0);
			MeasurementDAO measurementDAO = new MeasurementDAO(getApplicationContext());
			measurement = measurementDAO.getMeasurementWithID(measurementId);
			oldTitle = measurement.getTitle();
		} else {
			measurement = new Measurement();
		}

		titleEditText = (EditText) findViewById(R.id.measurementName);
		descriptionEditText = (EditText) findViewById(R.id.measurementDescription);
		outcomeEditText = (EditText) findViewById(R.id.measurementOutcomeData);

		final Calendar calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			dateEditText = (EditText) findViewById(R.id.measurementDate);
			dateEditText.setFocusable(false);
			dateEditText.setClickable(true);
			dateEditText.setOnClickListener(this);

			outcomeSpinner = (Spinner) findViewById(R.id.measurementOutcomeSpinner);
			outcomeDataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, outcomes);
			outcomeSpinner.setAdapter(outcomeDataAdapter);

			addMeasurementButton = (Button) findViewById(R.id.saveMeasurementButton);
			addMeasurementButton.setOnClickListener(this);
	}

	private void setEditableElements() {
		titleEditText.setText(measurement.getTitle());
		descriptionEditText.setText(measurement.getDescription());
		outcomeEditText.setText(measurement.getOutcomeData());
		dateEditText.setText(measurement.getDate());
		outcomeSpinner.setSelection(measurement.getOutcome());
	}

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

	private void saveMeasurement() {
		String title = titleEditText.getText().toString();
		String outcome = outcomeEditText.getText().toString();
		String date = dateEditText.getText().toString();

		if(isEdit && !title.equals(oldTitle) && checkExistingMeasurement(title)) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.duplicatemeasurementcheck),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(!isEdit && checkExistingMeasurement(title)) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.duplicatemeasurementcheck),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (title.length() == 0 && outcome.length() == 0 && date.length() == 0) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.measurementcheck), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (title.length() == 0) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.measurementtitlecheck), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (outcome.length() == 0) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.measurementoutcomecheck), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (date.length() == 0) {
			Toast.makeText(AddMeasurementForCohortActivity.this,
					getString(R.string.activitydatecheck), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		measurement.setTitle(title);
		measurement.setDescription(descriptionEditText.getText().toString());
		measurement.setOutcomeData(outcome);
		measurement.setCohort(cohortId);
		measurement.setOutcome(getSelectedOutcomeId());
		measurement.setDate(date);

		saveMeasurementInDatabase();
	}

	private int getSelectedOutcomeId() {
		String outcome = outcomeSpinner.getSelectedItem().toString();
		int id = -1;
		for(int i=0; i<outcomes.length; i++) {
			if(outcome.equals(outcomes[i])) {
				id = i;
				break;
			}
		}
		return id;
	}

	private void saveMeasurementInDatabase() {
		MeasurementDAO measurementDAO = new MeasurementDAO(getApplicationContext());
		if(!isEdit)
			measurementDAO.addMeasurement(measurement);
		else
			measurementDAO.updateMeasurement(measurement);

		Intent intent = new Intent(AddMeasurementForCohortActivity.this, AllMeasurementsActivity.class);
		startActivity(intent);
	}

	private boolean checkExistingMeasurement(String name) {
		MeasurementDAO measurementDAO = new MeasurementDAO(getApplicationContext());
		ArrayList<Measurement> allMeasurements = measurementDAO.getAllMeasurements();

		for (int i = 0; i < allMeasurements.size(); i++) {
			if (allMeasurements.get(i).getTitle().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.measurementDate:
				showDialog(DATE_DIALOG_ID);
				break;
			case R.id.saveMeasurementButton:
				saveMeasurement();
				break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				// set date picker as current date
				return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}
}