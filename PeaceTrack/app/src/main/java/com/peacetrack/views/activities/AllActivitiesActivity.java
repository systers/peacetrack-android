package com.peacetrack.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peacetrack.R;
import com.peacetrack.backend.activities.ActivityDAO;
import com.peacetrack.models.activities.Activities;
import com.peacetrack.views.cohorts.CohortDetailsActivity;
import com.peacetrack.views.welcome.WelcomeActivity;

import java.util.ArrayList;

public class AllActivitiesActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

	private ListView activityListView;
	private ArrayList<Activities> allActivities;
	private ArrayAdapter<String> adapter;
	private String[] activityNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listactivities);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();
		bindListItemClickListener();
	}

	private void initialize() {
		activityListView = (ListView) findViewById(R.id.activitieslistView);
		createActivitiesList();
	}

	private void createActivitiesList() {
		ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
		allActivities = activityDAO.getAllActivities();
		int i = 0;
		activityNames = new String[allActivities.size()];

		for (Activities activity : allActivities) {
			activityNames[i++] = activity.getTitle();
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, activityNames);
		activityListView.setAdapter(adapter);
	}

	private void bindListItemClickListener() {
		activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String name = adapter.getItem(position).toString();
				int activityId = -1;
				for (int i = 0; i < allActivities.size(); i++) {
					Activities activity = allActivities.get(i);
					if (name.equals(activity.getTitle())) {
						activityId = activity.getId();
						break;
					}
				}

				//TODO: Activity Details page?
				Intent intent = new Intent(AllActivitiesActivity.this, ActivityDetailsActivity.class);
				intent.putExtra("activityId", activityId);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.allcohortsmenu, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);

		getSupportActionBar().setDisplayShowTitleEnabled(true);

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
			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		} else if (itemId == R.id.action_addcohort) {
			Intent intent = new Intent(this, AddActivityActivity.class);
			startActivity(intent);
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return true;
	}

}
