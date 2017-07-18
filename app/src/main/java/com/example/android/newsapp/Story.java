package com.example.android.newsapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * A {@link Story} object represents a news story.
 */

public class Story {

    /* Story Title */
    String title;

    /* Story Author */
    ArrayList<String> authors;

    /* Story Date */
    Date date;

    /* Story Url */
    String url;

    /* Story Section */
    String section;

    /* Story Image Url */
    String imageUrl;

    /* Public Constructor */

    public Story(String title, ArrayList<String> authors, Date date, String url, String section, String imageUrl) {
        this.title = title;
        this.authors = authors;
        this.date = date;
        this.url = url;
        this.section = section;
        this.imageUrl = imageUrl;
    }

    /* Getter Methods */
    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getSection() {
        return section;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /* Helper method to return all authors in one string */
    public String getAuthorsLine(){
        StringBuilder builder = new StringBuilder();
        if (authors.isEmpty())
            return "";
        for (String author : authors){
            builder.append(author + ", ");
        }
        builder.delete(builder.length() - 2, builder.length() - 1);
        return builder.toString();
    }
}
