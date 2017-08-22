package com.loya.android.javadevelopers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


/**
 * Loads a list of Java Developers by using an AsyncTask to perform the
 * network request to the given URL from Github.
 */
public class DeveloperLoader extends AsyncTaskLoader<List<Developer>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = DeveloperLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link DeveloperLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public DeveloperLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, this.getContext().getString(R.string.in_onStartLoading));
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Developer> loadInBackground() {
        Log.v(LOG_TAG, this.getContext().getString(R.string.in_LoadInBackground));
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of developers.
        List<Developer> developers = QueryUtils.fetchEarthquakeData(mUrl);
        return developers;
    }
}