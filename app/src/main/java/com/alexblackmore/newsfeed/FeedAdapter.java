package com.alexblackmore.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    public static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<NewsStory> stories;


    public FeedAdapter(@NonNull Context context, int resource, List<NewsStory> stories) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.stories = stories;
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //This is the constraint layout viewgroup
        View view = layoutInflater.inflate(layoutResource, parent, false);

        //These are the views within it
        TextView tvTitle = (TextView) view.findViewById(R.id.title_tv);
        TextView tvDesc = (TextView) view.findViewById(R.id.desc_tv);
        TextView tvLink = (TextView) view.findViewById(R.id.link_tv);

        NewsStory currentStory = stories.get(position);

        tvTitle.setText(currentStory.getTitle());
        tvDesc.setText(currentStory.getDescription());
        tvLink.setText(currentStory.getLink());

        return view;
    }
}
