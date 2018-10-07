package com.peacetrack.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.peacetrack.R;
import com.peacetrack.views.welcome.WelcomeActivity;

/**
 * @author Pooja
 * 
 */
public class AllActivitiesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listactivities);
	}

	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.allactivitiesmenu, menu);

		// When user types i.e. query for the item in the search bar, how would
		// the search bar behave

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO
				return true;
			}
		};

		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		searchView.setOnQueryTextListener(queryTextListener);

		final MenuItem addActivity = menu.findItem(R.id.action_addactivity);

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
		} else if (itemId == R.id.action_addactivity) {
			Intent intent = new Intent(this, AddActivityActivity.class);
			startActivity(intent);
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	// TODO Populate Activities data to the next screen from here.

}
