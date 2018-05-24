/**
 *
 */
package com.peacetrack.views.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.peacetrack.R;
import com.peacetrack.backend.activities.ActivityDAO;
import com.peacetrack.backend.cohorts.CohortsDAO;
import com.peacetrack.models.activities.Activities;
import com.peacetrack.models.cohorts.Cohort;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivityActivity extends ActionBarActivity implements
        View.OnClickListener {

    private boolean isEdit;
    private String oldTitle;

    private Activities activity;

    static final int DATE_DIALOG_ID = 100;
    static final int TIME_DIALOG_ID = 101;

    private EditText nameEditText;
    private EditText descriptionEditText;

    private ArrayList<Cohort> cohortList;
    private Spinner cohortSpinner;
    private ArrayAdapter<String> cohortDataAdapter;

    private EditText dateEditText;
    private int year, month, day;
    private EditText timeEditText;
    private int hour, minute;

    private Button nextButton;

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

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into EditText
            timeEditText.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));
        }
    };

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
        if(isEdit) {
            setEditableElements();
        }
    }

    private void initialize() {
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            int activityId = getIntent().getIntExtra("activityId", 0);
            ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
            activity = activityDAO.getActivityWithID(activityId);
            oldTitle = activity.getTitle();
        }
        else {
            activity = new Activities();
        }
        CohortsDAO cohortsDBHandler = new CohortsDAO(this);
        cohortSpinner = (Spinner) findViewById(R.id.activity_cohort_spinner);

        cohortList = cohortsDBHandler.getAllCohorts();

        if (cohortList.size() > 0) {
            nameEditText = (EditText) findViewById(R.id.cohortName);
            descriptionEditText = (EditText) findViewById(R.id.cohortDescription);

            createCohortList();

            final Calendar calendar = Calendar.getInstance();

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            dateEditText = (EditText) findViewById(R.id.activityDate);
            dateEditText.setFocusable(false);
            dateEditText.setClickable(true);
            dateEditText.setOnClickListener(this);

            timeEditText = (EditText) findViewById(R.id.activityTime);
            timeEditText.setFocusable(false);
            timeEditText.setClickable(true);
            timeEditText.setOnClickListener(this);

            nextButton = (Button) findViewById(R.id.nextbutton);
            nextButton.setOnClickListener(this);
        } else {
            showNoCohortsDialog();
        }
    }

    private void setEditableElements() {
        nameEditText.setText(activity.getTitle());
        descriptionEditText.setText(activity.getDescription());
        dateEditText.setText(activity.getDate());
        timeEditText.setText(activity.getTime());

        int spinnerPosition = cohortDataAdapter.getPosition(getSelectedCohortName());
        cohortSpinner.setSelection(spinnerPosition);
    }

    private void createCohortList() {
        int i = 0;
        String[] cohortNames = new String[cohortList.size()];

        for (Cohort cohort : cohortList) {
            cohortNames[i++] = cohort.getName();
        }

        cohortDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cohortNames);
        cohortDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cohortSpinner.setAdapter(cohortDataAdapter);
    }

    private void showNoCohortsDialog() {
        // If no cohorts are available then show an error message
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddActivityActivity.this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.cohortListEmptyTitle));
        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.cohortListEmptyMessage))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                try {
                                    Class actClass = Class
                                            .forName("com.peacetrack.views.cohorts.AddCohortActivity");
                                    Intent intent = new Intent(
                                            AddActivityActivity.this, actClass);
                                    startActivity(intent);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                AddActivityActivity.this.finish();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
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
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void saveActivity() {
        String title = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();

        if(isEdit && !title.equals(oldTitle) && checkExistingActivity(title)) {
                Toast.makeText(AddActivityActivity.this,
                        getString(R.string.duplicateactivitycheck),
                        Toast.LENGTH_SHORT).show();
                return;
        }
        if (title.length() == 0 && (date.length() == 0 || time.length() == 0)) {
            Toast.makeText(AddActivityActivity.this,
                    getString(R.string.activitycheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (title.length() == 0) {
            Toast.makeText(AddActivityActivity.this,
                    getString(R.string.activitynamecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (date.length() == 0) {
            Toast.makeText(AddActivityActivity.this,
                    getString(R.string.activitydatecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (time.length() == 0) {
            Toast.makeText(AddActivityActivity.this,
                    getString(R.string.activitytimecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if(!isEdit && checkExistingActivity(title)) {
            Toast.makeText(AddActivityActivity.this,
                    getString(R.string.duplicateactivitycheck),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        activity.setTitle(title);
        activity.setDescription(descriptionEditText.getText().toString());
        activity.setDate(date);
        activity.setTime(time);
        activity.setCohort(getSelectedCohortId());

        saveActivityInDatabase();
    }

    private int getSelectedCohortId() {
        String cohort = cohortSpinner.getSelectedItem().toString();
        int id = -1;
        for(int i=0; i<cohortList.size(); i++) {
            if(cohort.equals(cohortList.get(i).getName())) {
                id = cohortList.get(i).getId();
                break;
            }
        }
        return id;
    }

    private String getSelectedCohortName() {
        String cohort = null;
        for(int i=0; i<cohortList.size(); i++) {
            if(cohortList.get(i).getId() == activity.getCohort()) {
                cohort = cohortList.get(i).getName();
                break;
            }
        }
        return cohort;
    }

    private void saveActivityInDatabase() {
        ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
        if(!isEdit)
            activityDAO.addActivity(activity);
        else
            activityDAO.updateActivity(activity);

        int activityId = activityDAO.getActivityWithTitle(activity.getTitle()).getId();
        Intent intent = new Intent(this, AddIndicatorsActivity.class);
        intent.putExtra("activityId", activityId);
        intent.putExtra("isEdit", isEdit);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.activityDate:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.activityTime:
                showDialog(TIME_DIALOG_ID);
                break;
            case R.id.nextbutton:
                saveActivity();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month, day);
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private boolean checkExistingActivity(String name) {
        ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
        ArrayList<Activities> allActivities = activityDAO.getAllActivities();

        for (int i = 0; i < allActivities.size(); i++) {
            if (allActivities.get(i).getTitle().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
