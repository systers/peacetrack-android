/**
 *
 */
package com.peacetrack.views.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.peacetrack.R;
import com.peacetrack.backend.activities.ActivityDAO;
import com.peacetrack.models.activities.Activities;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivityForCohortActivity extends ActionBarActivity implements
        View.OnClickListener {

    private boolean isEdit;
    private String oldTitle;

    private int cohortId;

    private Activities activity;

    static final int DATE_DIALOG_ID = 100;
    static final int TIME_DIALOG_ID = 101;

    private EditText nameEditText;
    private EditText descriptionEditText;

    private EditText dateEditText;
    private int year, month, day;
    private EditText timeEditText;
    private int hour, minute;

    private String[] items = {" Output1", " Output2", " Output3", " Output4", " Output5"};

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
        setContentView(R.layout.activity_addactivity_cohort);

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
        if (isEdit) {
            int activityId = getIntent().getIntExtra("activityId", 0);
            ActivityDAO activityDAO = new ActivityDAO(getApplicationContext());
            activity = activityDAO.getActivityWithID(activityId);
            oldTitle = activity.getTitle();
        }
        else {
            activity = new Activities();
        }

        nameEditText = (EditText) findViewById(R.id.cohortName);
        descriptionEditText = (EditText) findViewById(R.id.cohortDescription);

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
    }

    private void setEditableElements() {
        nameEditText.setText(activity.getTitle());
        descriptionEditText.setText(activity.getDescription());
        dateEditText.setText(activity.getDate());
        timeEditText.setText(activity.getTime());
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
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.duplicateactivitycheck),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.length() == 0 && (date.length() == 0 || time.length() == 0)) {
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.activitycheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (title.length() == 0) {
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.activitynamecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (date.length() == 0) {
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.activitydatecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (time.length() == 0) {
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.activitytimecheck), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if(!isEdit && checkExistingActivity(title)) {
            Toast.makeText(AddActivityForCohortActivity.this,
                    getString(R.string.duplicateactivitycheck),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        activity.setTitle(title);
        activity.setDescription(descriptionEditText.getText().toString());
        activity.setDate(date);
        activity.setTime(time);
        activity.setCohort(cohortId);

        saveActivityInDatabase();
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
        intent.putExtra("forCohort", true);
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
