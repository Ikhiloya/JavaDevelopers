package com.loya.android.javadevelopers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/*
* {@link EarthquakeAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link Earthquake} objects.
* */
public class DeveloperAdapter extends ArrayAdapter<Developer> {


    /**
     * Tag to be used for logging
     */
    private static final String LOG_TAG = DeveloperAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context    The current context. Used to inflate the layout file.
     * @param developers A List of AndroidFlavor objects to display in a list
     */
    public DeveloperAdapter(Activity context, ArrayList<Developer> developers) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for three TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, developers);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list
        Developer currentDeveloper = getItem(position);

        //  String  imageUrl, profileUrl;
        // Find the TextView in the list.xml layout with the ID magnitude
        TextView username = (TextView) listItemView.findViewById(R.id.username);
        username.setText(currentDeveloper.getmUserName());

        ImageView profilePic = (ImageView) listItemView.findViewById(R.id.profile_image);

        //this uses the Picasso library to convert the image url from the github query to an image
        Picasso.with(getContext())
                .load(currentDeveloper.getmImageUrl())
                .placeholder(R.drawable.ic_account_circle_black_24dp) //placeholder image to display before the image is loaded
                .error(R.drawable.ic_highlight_off_black_24dp)  // image to display if there is an error such as loss of  internet connection
                .resize(50, 50)
                .centerCrop()
                .into(profilePic);

        // Return the whole list item layout (containing a TextView and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}