package com.peacetrack.views.welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.peacetrack.models.login.Post;
import com.peacetrack.models.login.Sector;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Login Page for a Volunteer to login to the system and
 * setup the working space
 */

public class LoginActivity extends ActionBarActivity implements
        OnItemSelectedListener {

    private Button loginButton;
    private EditText nameEditText;
    private EditText emailEditText;

    private Spinner postSpinner;
    private Spinner sectorSpinner;

    private List<String> sectorList;
    private ArrayAdapter<String> sectorDataAdapter;

    private JSONObject posts, sectors;
    private ArrayList<Post> postArrayList;
    private ArrayList<Sector> sectorArrayList;

    private static final String USERNAME = "Bhagya";
    private static final String PASSWORD = "lahiru123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isNetworkAvailable()) {
            initialize();
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

        postSpinner = (Spinner) findViewById(R.id.post_spinner);

        posts = null;
        sectors = null;

        postArrayList = new ArrayList<Post>();
        sectorArrayList = new ArrayList<Sector>();

        GetPostListTask postTask = new GetPostListTask();
        postTask.execute();

        GetSectorListTask sectorTask = new GetSectorListTask();
        sectorTask.execute();
    }

    private void createPostList() {
        this.setPostList();
        List<String> postList = this.getAllPosts();
        ArrayAdapter<String> postDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, postList);
        postDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postSpinner.setAdapter(postDataAdapter);
        postSpinner.setOnItemSelectedListener(this);
    }

    private void createSectorList() {
        this.setSectorList();
        sectorSpinner = (Spinner) findViewById(R.id.sector_spinner);
        sectorList = this.getSectors(postArrayList.get(0).getSectors());
        sectorDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sectorList);
        sectorDataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorSpinner.setAdapter(sectorDataAdapter);
    }

    private void setSectorList() {
        if(sectors == null) {
            this.showNoDataDialog();
        }
        else {
            try {
                int count = sectors.getInt("count");
                JSONArray results = sectors.getJSONArray("results");
                for (int i = 0; i < count; i++) {
                    JSONObject resultsJSONObject = results.getJSONObject(i);
                    int id = resultsJSONObject.getInt("id");
                    String name = resultsJSONObject.getString("sector_name");
                    String desc = resultsJSONObject.getString("sector_desc");
                    String code = resultsJSONObject.getString("sector_code");

                    Sector sector = new Sector();
                    sector.setId(id);
                    sector.setName(name);
                    sector.setCode(code);
                    sector.setDes(desc);

                    sectorArrayList.add(sector);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPostList() {
        if(posts == null) {
            this.showNoDataDialog();
        }
        else {
            try {
                int count = posts.getInt("count");
                JSONArray results = posts.getJSONArray("results");
                for (int i = 0; i < count; i++) {
                    JSONObject resultsJSONObject = results.getJSONObject(i);
                    int id = resultsJSONObject.getInt("id");
                    String name = resultsJSONObject.getString("post_name");
                    int region = resultsJSONObject.getInt("post_region");

                    JSONArray array = resultsJSONObject.getJSONArray("sector");
                    int[] sectors = new int[array.length()];
                    for (int j = 0; j < array.length(); j++) {
                        sectors[j] = array.getInt(j);
                    }

                    Post post = new Post();
                    post.setId(id);
                    post.setName(name);
                    post.setRegion(region);
                    post.setSectors(sectors);

                    postArrayList.add(post);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> getAllPosts() {
        ArrayList<String> postList = new ArrayList<String>();
        for(int i=0; i<postArrayList.size(); i++) {
            postList.add(postArrayList.get(i).getName());
        }
        return postList;
    }

    private ArrayList<String> getSectors(int[] sectorIds) {
        ArrayList<String> sectorList = new ArrayList<String>();
        for(int i=0; i<sectorIds.length; i++) {
            sectorList.add(sectorArrayList.get(sectorIds[i]).getName());
        }

        return sectorList;
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
                if(sectorList != null) {
                    // If the post changes, update the associated sectors
                    String selectedPost = parent.getItemAtPosition(position).toString();
                    List<String> newSectors = null;
                    for(int i=0; i<postArrayList.size(); i++) {
                        if(selectedPost.equals(postArrayList.get(i).getName())) {
                            newSectors = getSectors(postArrayList.get(i).getSectors());
                        }
                    }
                    sectorList.clear();
                    sectorList.addAll(newSectors);
                    sectorDataAdapter.notifyDataSetChanged();
                }
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
                .setMessage(getString(R.string.networkUnavailableMessage))
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

    private void showNoDataDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.noDataTitle));
        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.noDataMessage))
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

    class GetPostListTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject posts = this.getRequest();
            return posts;
        }

        public JSONObject getRequest() {
            JSONObject json = null;
            BufferedReader bufferedReader = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet();

                URI uri = new URI("http://54.183.13.103/api/ptposts/?format=json");
                httpGet.setURI(uri);
                httpGet.addHeader(BasicScheme.authenticate(
                        new UsernamePasswordCredentials(USERNAME, PASSWORD),
                        HTTP.UTF_8, false));

                HttpResponse httpResponse = httpClient.execute(httpGet);

                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream));

                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = bufferedReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                json = new JSONObject(responseStrBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        // TODO: handle exception
                    }
                }
            }
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            posts = result;
            createPostList();
        }
    }

    class GetSectorListTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject sectors = this.getRequest();
            return sectors;
        }

        public JSONObject getRequest() {
            JSONObject json = null;
            BufferedReader bufferedReader = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet();

                URI uri = new URI("http://54.183.13.103/api/sectors/?format=json");
                httpGet.setURI(uri);
                httpGet.addHeader(BasicScheme.authenticate(
                        new UsernamePasswordCredentials(USERNAME, PASSWORD),
                        HTTP.UTF_8, false));

                HttpResponse httpResponse = httpClient.execute(httpGet);

                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream));

                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = bufferedReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                json = new JSONObject(responseStrBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        // TODO: handle exception
                    }
                }
            }
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            sectors = result;
            createSectorList();
        }
    }
}
