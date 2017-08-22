package com.loya.android.javadevelopers;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving the list of Java Developers in Lagos from Github API
 */
public final class QueryUtils {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final String ITEMS = "items";
    public static final String LOGIN = "login";
    public static final String AVATAR_URL = "avatar_url";
    public static final String HTML_URL = "html_url";
    public static final String QUERY_UTILS = "QueryUtils";
    public static final String GET = "GET";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Developer} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Developer> extractFeatureFromJson(String developerJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(developerJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding developers to
        List<Developer> developers = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(developerJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of features (or Developer).
            JSONArray developerArray = baseJsonResponse.getJSONArray(ITEMS);

            // For each developer in the developerArray, create an {@link Developer} object
            for (int i = 0; i < developerArray.length(); i++) {

                // Get a single developer at position i within the list of developers
                JSONObject currentDeveloper = developerArray.getJSONObject(i);


                //Extract “login” for the username of the developer
                String username = currentDeveloper.getString(LOGIN);

                //Extract "avatar_url"(profile image) of the developer
                String imageUrl = currentDeveloper.getString(AVATAR_URL);


                //Extract the "url"of the developer
                String profileUrl = currentDeveloper.getString(HTML_URL);


                // Create a new {@link Developer} object with the username, imageUrl
                // and profileUrl from the JSON response.
                Developer developer = new Developer(username, imageUrl, profileUrl);

                // Add the new {@link Developer} to the list of developers.
                developers.add(developer);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(QUERY_UTILS, String.valueOf(R.string.problem_parsing_the_earthquake_JSON_results), e);

        }

        // Return the list of developers
        return developers;
    }


    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(GET);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the USGS dataset and return a list of {@link Developer} objects.
     */
    public static List<Developer> fetchEarthquakeData(String requestUrl) {
        //forcing the background thread to pause execution and wait for 2 seconds (which is 2000 milliseconds),
//        // before proceeding to execute the rest of lines of code in this method
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Developer}s
        List<Developer> developers = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Developer}s
        return developers;
    }

}




