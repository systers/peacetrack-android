package com.peacetrack.views.welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.peacetrack.R;
import com.peacetrack.backend.indicators.IndicatorsDBHandler;

import java.util.List;

/**
 * Login Page for a Volunteer to login to the system and
 * setup the working space
 */

public class LoginActivity extends AppCompatActivity implements
        OnItemSelectedListener {

    private Button loginButton;
    private EditText nameEditText;
    private EditText emailEditText;

    private Spinner postSpinner;
    private Spinner sectorSpinner;

    private IndicatorsDBHandler indicatorsDBHandler;
    private List<String> sectorList;
    private ArrayAdapter<String> sectorDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isNetworkAvailable()) {
            initialize();
            createPostList();
            createSectorList();
            bindLoginButtonListener();
        } else {
            showNetworkErrorDialog();
        }
    }

    // Checks the network connection availability and return a boolean
    private boolean isNetworkAvailable() {
        // Connectivity manager to get the network information of the device
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (info != null && info.isConnected()) {
            isAvailable = true;
        }
        // return the availability
        return isAvailable;
    }

    private void initialize() {
        loginButton = (Button) findViewById(R.id.loginButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.nameEditEmail);

        indicatorsDBHandler = new IndicatorsDBHandler(this);

        postSpinner = (Spinner) findViewById(R.id.post_spinner);
    }

    private void createPostList() {
        List<String> postList = indicatorsDBHandler.getAllPosts();
        ArrayAdapter<String> postDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, postList);
        postDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postSpinner.setAdapter(postDataAdapter);
        postSpinner.setOnItemSelectedListener(this);
    }

    private void createSectorList() {
        sectorSpinner = (Spinner) findViewById(R.id.sector_spinner);
        sectorList = indicatorsDBHandler.getAllSectors();
        sectorDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sectorList);
        sectorDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorSpinner.setAdapter(sectorDataAdapter);
    }

    private void bindLoginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nameEditText.getText().length() == 0
                        && (emailEditText.getText().length() == 0 || !isEmailValid((CharSequence) emailEditText
                        .getText()))) {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.nameandemailcheck),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nameEditText.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.namecheck), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (emailEditText.getText().length() == 0
                        || !isEmailValid((CharSequence) emailEditText.getText())) {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.emailcheck), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

				/*
                 * This would make sure that this login page launches only once
				 * when the user downloads the application. Add a check on these
				 * preferences in main activity i.e. welcome activity. If and
				 * only if these shared preferences are missing , will the
				 * application launch the first login page.
				 */
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.name), nameEditText
                        .getText().toString());
                editor.putString(getString(R.string.email), emailEditText
                        .getText().toString());
                editor.putString(getString(R.string.post), postSpinner
                        .getSelectedItem().toString());
                editor.putString(getString(R.string.sector), sectorSpinner
                        .getSelectedItem().toString());
                editor.commit();

                Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LoginActivity.this.startActivity(i);
                LoginActivity.this.onBackPressed();
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.post_spinner:
                // If the post changes, update the associated sectors
                String selectedPost = parent.getItemAtPosition(position).toString();
                List<String> newSectors = indicatorsDBHandler
                        .getAllSectorsForPost(selectedPost);
                sectorList.clear();
                sectorList.addAll(newSectors);
                sectorDataAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void showNetworkErrorDialog() {
        // If network not available then show an error message
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.networkUnavailableTitle));
        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.networkUnavailableTitle))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                LoginActivity.this.finish();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
