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
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.peacetrack.R;
import com.peacetrack.backend.cohorts.CohortsDAO;
import com.peacetrack.models.cohorts.Cohorts;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivityActivity extends AppCompatActivity implements
        View.OnClickListener {

    static final int DATE_DIALOG_ID = 100;
    static final int TIME_DIALOG_ID = 101;
    private CohortsDAO cohortsDBHandler;
    private ArrayList<Cohorts> cohortList;
    private Spinner cohortSpinner;
    private EditText dateEditText;
    private int year, month, day;
    private EditText timeEditText;
    private int hour, minute;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into EditText
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

    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cohortsDBHandler = new CohortsDAO(this);
        cohortSpinner = (Spinner) findViewById(R.id.activity_cohort_spinner);

        cohortList = cohortsDBHandler.getAllCohorts();

        if (cohortList.size() > 0) {

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

            TextView timeTextView = (TextView) findViewById(R.id.activityTime);
            timeTextView.setFocusable(false);
            timeTextView.setClickable(true);
            timeTextView.setOnClickListener(this);

            TextView textView = (TextView) findViewById(R.id.associatedoutputdata);
            textView.setFocusable(false);
            textView.setClickable(true);
            textView.setOnClickListener(this);
        } else {
            // If network not available then show an error message
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
    }

    private void createCohortList() {
        int i = 0;
        String[] cohortNames = new String[cohortList.size()];

        for (Cohorts cohort : cohortList) {
            cohortNames[i++] = cohort.getName();
        }

        ArrayAdapter<String> cohortDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cohortNames);
        cohortDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cohortSpinner.setAdapter(cohortDataAdapter);
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
            case R.id.associatedoutputdata:
                Intent intent = new Intent(AddActivityActivity.this,
                        AddIndicatorsActivity.class);
                AddActivityActivity.this.startActivity(intent);
                break;
            case R.id.saveactivitybutton:

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

}
