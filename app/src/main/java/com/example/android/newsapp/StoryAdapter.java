package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link StoryAdapter} is a listview adapter for Story Objects
 */

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(@NonNull Context context, @NonNull List<Story> objects) {
        super(context, -1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        View listItemView = convertView;
        Story story = getItem(position);
        /* Check if we have a spare view */
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_story, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }
        else
            holder = (ViewHolder) listItemView.getTag();

        /* Display story title */
        holder.titleTextView.setText(story.getTitle());
        /* Display story author */
        holder.authorTextView.setText(story.getAuthorsLine());
        /*Display story publish date */
        Date date = story.getDate();
        if (date != null)
            holder.dateTextView.setText(formatDate(date));
        /*Display story section */
        holder.sectionTextView.setText(story.getSection());
        /*Display story thumbnail if available */
        String imageUrl = story.getImageUrl();
        if (!imageUrl.isEmpty())
            Glide
                    .with(getContext())
                    .load(imageUrl)
                    .into(holder.thumbnailImageView);
        else
            holder.thumbnailImageView.setVisibility(View.GONE);

        return listItemView;
    }


    /* Implement ViewHolder pattern for increased performance */
    static class ViewHolder{
        @BindView(R.id.story_title)
        TextView titleTextView;
        @BindView(R.id.story_author)
        TextView authorTextView;
        @BindView(R.id.story_date)
        TextView dateTextView;
        @BindView(R.id.story_section)
        TextView sectionTextView;
        @BindView(R.id.story_image)
        ImageView thumbnailImageView;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}
