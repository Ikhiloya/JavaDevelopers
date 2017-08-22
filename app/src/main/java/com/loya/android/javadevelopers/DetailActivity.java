package com.loya.android.javadevelopers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public static final String TEXT_PLAIN = "text/plain";
    private TextView developerUsername;
    private ImageView developerImage;
    private TextView developerUrl;
    private Button shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //code to add an UP icon to go back to the parent activity
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final String username = getIntent().getStringExtra(DeveloperActivity.USERNAME);
        String imageUrl = getIntent().getStringExtra(DeveloperActivity.IMAGE_URL);
        final String profileUrl = getIntent().getStringExtra(DeveloperActivity.PROFILE_URL);
        shareButton = (Button) findViewById(R.id.shareButton);


        developerUsername = (TextView) findViewById(R.id.detail_activity_username);
        developerImage = (ImageView) findViewById(R.id.detail_activity_image);
        developerUrl = (TextView) findViewById(R.id.url_link);

        developerUsername.setText(username);
        developerUrl.setText(profileUrl);


        //Loads the image using the picasso library image loader
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .resize(200, 200)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_highlight_off_black_24dp)
                .centerCrop()
                .into(developerImage);

        developerUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri developerUrl = Uri.parse(profileUrl);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, developerUrl);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType(TEXT_PLAIN);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_intent_message) + username + "\n" + profileUrl);
                // check if the destination device has an app to share this content
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


}
