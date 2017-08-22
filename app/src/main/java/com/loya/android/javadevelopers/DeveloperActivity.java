package com.loya.android.javadevelopers;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeveloperActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<List<Developer>> {

    public static final String PROFILE_URL = "profileUrl";
    public static final String IMAGE_URL = "imageUrl";
    public static final String USERNAME = "username";

    private LoaderManager loaderManager;

    private ConnectivityManager cm;

    private boolean isConnected;

    private NetworkInfo activeNetwork;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Spinner to display a list of cities in Nigeria
     */
    private Spinner mLocationSpinner;

    //view for the loading indicator
    View loadingIndicator;

    public static final String LOG_TAG = DeveloperActivity.class.getName();
    /**
     * URL for Java Developer data from Github
     */
    private static String GITHUB_REQUEST_URL;
    /**
     * Constant value for the developer loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int DEVELOPER_LOADER_ID = 1;

    /* * Adapter for the list of earthquakes
     */
    private DeveloperAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get a reference to the loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);
        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of developers as input
        mAdapter = new DeveloperAdapter(this, new ArrayList<Developer>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Developer currentDeveloper = mAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                String developerUsername = currentDeveloper.getmUserName();
                String developerImageUrl = currentDeveloper.getmImageUrl();

                String developerUrl = currentDeveloper.getmProfileUrl();

                intent.putExtra(USERNAME, developerUsername);
                intent.putExtra(IMAGE_URL, developerImageUrl);
                intent.putExtra(PROFILE_URL, developerUrl);

                startActivity(intent);


            }
        });

        String val = sharedPreferences.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_lagos_value));
        if (val.equals(getString(R.string.pref_location_lagos_value))) {
            GITHUB_REQUEST_URL = getString(R.string.lagos_github_url);
        } else if (val.equals(getString(R.string.pref_location_calabar_value))) {
            GITHUB_REQUEST_URL = getString(R.string.calabar_github_url);
        } else if (val.equals(getString(R.string.pref_location_portharcourt_value))) {
            GITHUB_REQUEST_URL = getString(R.string.portharcourt_github_url);
        } else if (val.equals(getString(R.string.pref_location_abuja_value))) {
            GITHUB_REQUEST_URL = getString(R.string.abuja_github_url);
        } else {
            GITHUB_REQUEST_URL = getString(R.string.uyo_github_url);
        }
        //register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        //check if there is a network connection
        // if there is a network connection the LoaderManager is called but
        //  displays a message if there's no network connection
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getLoaderManager();

        if (isConnected) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(DEVELOPER_LOADER_ID, null, this);
        } else {

            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, getString(R.string.in_onCreateLoader));

        // cursor.setNotificationUri(getContext().getContentResolver(), uri);
        // Create a new loader for the given URL
        return new DeveloperLoader(this, GITHUB_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> developers) {
        Log.v(LOG_TAG, getString(R.string.in_onLoadFinished_callback));
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No developer found."
        mEmptyStateTextView.setText(R.string.no_developer_found);
        // Clear the adapter of previous developer data
        mAdapter.clear();

        // If there is a valid list of {@link Developer}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (developers != null && !developers.isEmpty()) {
            mAdapter.addAll(developers);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        Log.v(LOG_TAG, getString(R.string.in_onLoaderReset_callback));
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_location) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //unregisters the preference changeListener when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    //this listens for changes in the shared preferences and sets the Github query url
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_location_key))) {

            String val = sharedPreferences.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_lagos_value));
            if (val.equals(getString(R.string.pref_location_lagos_value))) {
                GITHUB_REQUEST_URL = getString(R.string.lagos_github_url);
            } else if (val.equals(getString(R.string.pref_location_calabar_value))) {
                GITHUB_REQUEST_URL = getString(R.string.calabar_github_url);
            } else if (val.equals(getString(R.string.pref_location_portharcourt_value))) {
                GITHUB_REQUEST_URL = getString(R.string.portharcourt_github_url);
            } else if (val.equals(getString(R.string.pref_location_abuja_value))) {
                GITHUB_REQUEST_URL = getString(R.string.abuja_github_url);
            } else {
                GITHUB_REQUEST_URL = getString(R.string.uyo_github_url);
            }


            if (isConnected) {
                loadingIndicator.setVisibility(View.VISIBLE);

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.restartLoader(DEVELOPER_LOADER_ID, null, this);


            } else {
                loadingIndicator.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }
        }
    }


}

