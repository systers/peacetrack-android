/**
 * 
 */
package com.peacetrack.views.cohorts;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peacetrack.R;
import com.peacetrack.backend.cohorts.CohortsDAO;
import com.peacetrack.models.cohorts.Cohort;

public class AddCohortActivity extends ActionBarActivity implements View.OnClickListener {

	private boolean isEdit;
	private String oldName;

	private Button addCohortButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private EditText noOfMembersEditText;
	private EditText noOfMalesEditText;
	private EditText noOfFemalesEditText;
	private EditText ageRangeLowerEditText;
	private EditText ageRangeHigherEditText;
	private EditText positionEditText;
	private EditText otherNotesEditText;

	private Cohort cohort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcohort);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
		if(isEdit) {
			setEditableElements();
		}
		addCohortButton.setOnClickListener(this);
	}

	private void initialize() {
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		if(isEdit) {
			int cohortId = getIntent().getIntExtra("cohortId", 0);
			CohortsDAO cohortsDAO = new CohortsDAO(getApplicationContext());
			cohort = cohortsDAO.getCohortWithID(cohortId);
			oldName = cohort.getName();
		}
		else {
			cohort = new Cohort();
		}
		addCohortButton = (Button) findViewById(R.id.savecohortbutton);
		nameEditText = (EditText) findViewById(R.id.cohortName);
		descriptionEditText = (EditText) findViewById(R.id.cohortDescription);
		noOfMembersEditText = (EditText) findViewById(R.id.cohortNoOfMembers);
		noOfMalesEditText = (EditText) findViewById(R.id.cohortMales);
		noOfFemalesEditText = (EditText) findViewById(R.id.cohortFemales);
		ageRangeLowerEditText = (EditText) findViewById(R.id.cohortAgeRangeLower);
		ageRangeHigherEditText = (EditText) findViewById(R.id.cohortAgeRangeHigher);
		positionEditText = (EditText) findViewById(R.id.cohortPosition);
		otherNotesEditText = (EditText) findViewById(R.id.cohortOtherNotes);
	}

	private void setEditableElements() {
		nameEditText.setText(cohort.getName());
		descriptionEditText.setText(cohort.getDescription());
		noOfMembersEditText.setText(Integer.toString(cohort.getNoOfMembers()));
		noOfMalesEditText.setText(Integer.toString(cohort.getNoOfMales()));
		noOfFemalesEditText.setText(Integer.toString(cohort.getNoOfFemales()));
		positionEditText.setText(cohort.getPosition());
		otherNotesEditText.setText(cohort.getOtherNotes());

		String[] ageRange = cohort.getAgeRange().split("-");
		ageRangeLowerEditText.setText(ageRange[0]);
		ageRangeHigherEditText.setText(ageRange[1]);
	}

	private void saveCohort() {
		String name = nameEditText.getText().toString();
		String noOfMembers = noOfMembersEditText.getText().toString();
		String noOfMales = noOfMalesEditText.getText().toString();
		String noOfFemales = noOfFemalesEditText.getText().toString();
		String ageLower = ageRangeLowerEditText.getText().toString();
		String ageHigher = ageRangeHigherEditText.getText().toString();
		String position = positionEditText.getText().toString();

		if(isEdit && !name.equals(oldName) && checkExistingCohort(name)) {
			Toast.makeText(AddCohortActivity.this,
					getString(R.string.duplicatecohortcheck),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(!isEdit && checkExistingCohort(name)) {
			Toast.makeText(AddCohortActivity.this,
					getString(R.string.duplicatecohortcheck),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(name.length() == 0 || noOfMembers.length() == 0 || noOfMales.length() == 0 || noOfFemales.length() == 0 || ageLower.length() == 0 || ageHigher.length() == 0 || position.length() == 0) {
			Toast.makeText(AddCohortActivity.this,
					getString(R.string.cohortcheck),
					Toast.LENGTH_SHORT).show();
			return;
		}

		cohort.setName(name);
		cohort.setDescription(descriptionEditText.getText().toString());
		cohort.setNoOfMembers(Integer.parseInt(noOfMembers));
		cohort.setNoOfMales(Integer.parseInt(noOfMales));
		cohort.setNoOfFemales(Integer.parseInt(noOfFemales));
		cohort.setAgeRange(ageLower + "-" + ageHigher);
		cohort.setPosition(position);
		cohort.setOtherNotes(otherNotesEditText.getText().toString());

		saveCohortInDatabase();
	}

	/*
	 * Select the new screen when any icon in action bar is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, ListCohortsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		} else {
		}
		return super.onOptionsItemSelected(item);

	}

	private void saveCohortInDatabase() {
		CohortsDAO cohortsDAO = new CohortsDAO(getApplicationContext());
		if(!isEdit)
			cohortsDAO.addCohort(cohort);
		else
			cohortsDAO.updateCohort(cohort);

		Intent intent = new Intent(AddCohortActivity.this,
				ListCohortsActivity.class);
		AddCohortActivity.this.startActivity(intent);
	}

	private boolean checkExistingCohort(String name) {
		CohortsDAO cohortsDAO = new CohortsDAO(getApplicationContext());
		ArrayList<Cohort> allCohorts = cohortsDAO.getAllCohorts();

		for (int i = 0; i < allCohorts.size(); i++) {
			if (allCohorts.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.savecohortbutton) {
			saveCohort();
		}
	}
}
