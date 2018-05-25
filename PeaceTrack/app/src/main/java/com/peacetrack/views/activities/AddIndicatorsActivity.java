/**
 * 
 */
package com.peacetrack.views.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.peacetrack.R;
import com.peacetrack.backend.activities.ActivityDAO;
import com.peacetrack.backend.outputs.OutputDAO;
import com.peacetrack.models.activities.Activities;
import com.peacetrack.models.outputs.Output;
import com.peacetrack.views.welcome.WelcomeActivity;

import java.util.ArrayList;

public class AddIndicatorsActivity extends ActionBarActivity implements
		OnClickListener {

	private boolean isEdit;
	private boolean forCohort;

	private Button saveButton;
	private Button backButton;

	private int activityId;

	private EditText outputEditText;
	private String[] items = {" Output1", " Output2", " Output3", " Output4", " Output5"};
	//private String[] itemsTemp;
	private boolean[] oldIsChecked, isChecked, newIsChecked;

	private ListView outputListView;

	private ArrayList<String> selected;
	private ArrayAdapter<String> adapter;
	private ArrayList<Output> outputs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addindicators);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
	}

	private void initialize() {
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		forCohort = getIntent().getBooleanExtra("forCohort", false);
		activityId = getIntent().getIntExtra("activityId", 0);

		saveButton = (Button) findViewById(R.id.savebutton);
		saveButton.setOnClickListener(this);

		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(this);

		outputEditText = (EditText) findViewById(R.id.activityOutputs);
		outputEditText.setFocusable(false);
		outputEditText.setClickable(true);
		outputEditText.setOnClickListener(this);

		oldIsChecked = new boolean[items.length];
		isChecked = new boolean[items.length];
	//	itemsTemp = new String[items.length];
		initializeCheckedItems();

		selected = getSelectedItems();
		outputListView = (ListView) findViewById(R.id.outputListView);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selected);
		outputListView.setAdapter(adapter);
	}

	private void initializeCheckedItems() {
		for(int i=0; i<items.length; i++) {
			oldIsChecked[i] = false;
			isChecked[i] = false;
		}

		if(isEdit) {
			OutputDAO outputDAO = new OutputDAO(getApplicationContext());
			outputs = outputDAO.getAllOutputsForActivity(activityId);

			for (Output anOutput : outputs) {
				isChecked[anOutput.getOutputId()] = true;
				oldIsChecked[anOutput.getOutputId()] = true;
				//itemsTemp[anOutput.getOutputId()] = anOutput.getData();
			}
		}
		else {
			outputs = new ArrayList<Output>();
		}
	}

	private ArrayList<String> getSelectedItems() {
		ArrayList<String> selected = new ArrayList<>();
		for(Output output: outputs) {
			selected.add(items[output.getOutputId()]);
		}
		return selected;
	}

	private void showOutputs() {
		copyIsChecked();
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.addActivityOutputTitle);
		builder.setMultiChoiceItems(items, newIsChecked,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int selectedItemId,
										boolean isSelected) {
						if (isSelected) {
							newIsChecked[selectedItemId] = true;
						} else if (isChecked[selectedItemId]) {
							newIsChecked[selectedItemId] = false;
						}
					}
				})
				.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						isChecked = newIsChecked;
						addAndRemoveOutputsSelected();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						newIsChecked = null;
					}
				});
		dialog = builder.create();
		dialog.show();
	}

	private void copyIsChecked() {
		newIsChecked = new boolean[items.length];
		for(int i=0; i<items.length; i++) {
			newIsChecked[i] = isChecked[i];
		}
	}

	private void addAndRemoveOutputsSelected() {
		outputs = new ArrayList<Output>();
		for(int i=0; i<items.length; i++) {
			if(newIsChecked[i]) {
				Output output = new Output();
				output.setOutputId(i);
				output.setActivityId(activityId);
				//output.setData(itemsTemp[i]);

				outputs.add(output);
			}
		}
		selected = getSelectedItems();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, selected);
		outputListView.setAdapter(adapter);
	}

	private void saveOutputInDatabase() {
		OutputDAO outputDAO = new OutputDAO(getApplicationContext());

		if(isChecked != null) {
			for (int i = 0; i < items.length; i++) {
				Output output = new Output();
				output.setOutputId(i);
				output.setActivityId(activityId);
				//output.setData(itemsTemp[i]);

				if (!oldIsChecked[i] && !isChecked[i]) {
					continue;
				}
				else if (!oldIsChecked[i] && isChecked[i]) {
					//Add
					outputDAO.addOutput(output);
				} else if (oldIsChecked[i] && !isChecked[i]) {
					//Remove
					outputDAO.deleteOutput(output);
				} else if (oldIsChecked[i] && isChecked[i]) {
					//Update
					outputDAO.updateOutput(output);
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(AddIndicatorsActivity.this, WelcomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent;
		switch (id) {
			case R.id.activityOutputs:
				showOutputs();
				break;
			case R.id.savebutton:
				saveOutputInDatabase();
				intent = new Intent(AddIndicatorsActivity.this, AllActivitiesActivity.class);
				startActivity(intent);
				break;
			case R.id.backbutton:
				saveOutputInDatabase();
				if(forCohort) {
					intent = new Intent(this, AddActivityForCohortActivity.class);
					ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
					Activities activity = activityDAO.getActivityWithID(activityId);
					intent.putExtra("cohortId", activity.getCohort());
				}
				else {
					intent = new Intent(this, AddActivityActivity.class);
				}
				intent.putExtra("isEdit", true);
				intent.putExtra("activityId", activityId);
				startActivity(intent);
				break;
		}
	}

	/*
	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(outputs != null && outputs.size() != 0){
				return outputs.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return outputs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//ViewHolder holder = null;
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = AddIndicatorsActivity.this.getLayoutInflater();
				convertView = inflater.inflate(R.layout.output_listitem, null);
				holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
				holder.editText1 = (EditText) convertView.findViewById(R.id.editText1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.ref = position;

			holder.textView1.setText(items[outputs.get(position).getOutputId()]);
			holder.editText1.setText(itemsTemp[outputs.get(position).getOutputId()]);
			holder.editText1.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
											  int arg3) {
				}

				@Override
				public void afterTextChanged(Editable arg0) {
					itemsTemp[holder.ref] = arg0.toString();
				}
			});

			return convertView;
		}

		private class ViewHolder {
			TextView textView1;
			EditText editText1;
			int ref;
		}
	}
	*/
}
