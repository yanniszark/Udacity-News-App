package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String TAG = NewsActivity.class.getSimpleName();

    private String query = "politics";

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String GUARDIAN_API_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Constant value for the story loader ID. We can choose any integer.
     */
    private static final int STORY_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private StoryAdapter mAdapter;

    /* Empty screen TextView */
    @BindView(R.id.empty_view)
    TextView emptyStateTextView;

    /* Loader to display when downloading data*/
    @BindView(R.id.loading_indicator)
    View loadingIndicatorView;

    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

        /* Create a new story adapter */
        mAdapter = new StoryAdapter(this, new ArrayList<Story>());
        /* Attach adapter to ListView */
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Find the current story that was clicked on
                Story currentStory = mAdapter.getItem(i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri storyUri = Uri.parse(currentStory.getUrl());

                // Create a new intent to view the story URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicatorView.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.device_offline);

        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(GUARDIAN_API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        /* Get preferences */
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minDate = sharedPrefs.getString(getString(R.string.settings_min_date_key), getString(R.string.settings_min_date_default_value));
        String section = sharedPrefs.getString(getString(R.string.settings_select_section_key), getString(R.string.settings_select_section_default_value));


        /* API key */
        uriBuilder.appendQueryParameter("api-key", getResources().getString(R.string.guardian_api_key));
        /* Show article contributor */
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        /* Show article thumbnail */
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        /* Minimum Date */
        uriBuilder.appendQueryParameter("from-date", minDate);
        /* Section Id */
        uriBuilder.appendQueryParameter("section", section);

        Log.v(TAG, "Quering URL: " + uriBuilder.toString());

        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        // Hide loading indicator because the data has been loaded
        loadingIndicatorView.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        emptyStateTextView.setText(R.string.no_books_found);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (stories != null && !stories.isEmpty()) {
            emptyStateTextView.setVisibility(View.GONE);
            mAdapter.addAll(stories);
        }
        else
            emptyStateTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
