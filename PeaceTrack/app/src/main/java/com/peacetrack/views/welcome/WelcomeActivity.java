/**
 *
 */
package com.peacetrack.views.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.peacetrack.R;
import com.peacetrack.views.activities.AddActivityActivity;
import com.peacetrack.views.cohorts.ListCohortsActivity;
import com.peacetrack.views.measurements.AddMeasurementActivity;

/**
 * If the user has login to the application, then
 * show the dashboard to the user
 */
public class WelcomeActivity extends ActionBarActivity implements
        OnClickListener {

    private Button cohortsButton;
    private Button addActivityButton;
    private Button addMeasurementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Checks if the user has login
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (!preferences.contains(getString(R.string.name))) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            initialize();
            bindListeners();
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    private void initialize() {
        cohortsButton = (Button) findViewById(R.id.cohortsButton);
        addActivityButton = (Button) findViewById(R.id.addActivityButton);
        addMeasurementButton = (Button) findViewById(R.id.addMeasurementButton);
    }

    private void bindListeners() {
        cohortsButton.setOnClickListener(this);
        addActivityButton.setOnClickListener(this);
        addMeasurementButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.cohortsButton:
                intent = new Intent(this, ListCohortsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.addActivityButton:
                intent = new Intent(this, AddActivityActivity.class);
                this.startActivity(intent);
                break;
            case R.id.addMeasurementButton:
                intent = new Intent(this, AddMeasurementActivity.class);
                this.startActivity(intent);
                break;
        }
    }

}
