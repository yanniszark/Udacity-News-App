package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * A {@link StoryLoader} is a Loader for downloading stories
 * from the Guardian API.
 */

public class StoryLoader extends AsyncTaskLoader<List<Story>> {
    /** Tag for log messages */
    private static final String LOG_TAG = StoryLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link StoryLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Story> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Story> stories = QueryUtils.fetchStoryData(mUrl);
        return stories;
    }
}
