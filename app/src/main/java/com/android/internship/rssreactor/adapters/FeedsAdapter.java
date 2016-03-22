package com.android.internship.rssreactor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.internship.rssreactor.R;
import com.android.internship.rssreactor.database.RSSFeedDbHelper;
import com.android.internship.rssreactor.entities.Feeds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vlados Papandos on 17.03.2016.
 */
public class FeedsAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<Feeds> feeds = null;
    private ArrayList<Feeds> arraylist;

    public FeedsAdapter(Context context, List<Feeds> feeds) {
        mContext = context;
        this.feeds = feeds;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Feeds>();
        this.arraylist.addAll(feeds);
    }

    public class ViewHolder {
        TextView title;
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Feeds getItem(int position) {
        return feeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listviews, null);
            holder.title = (TextView) view.findViewById(R.id.label);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(feeds.get(position).getTitle());

        return view;
    }




    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        feeds.clear();
        if (charText.length() == 0) {
            feeds.addAll(arraylist);
        }
        else
        {
            for (Feeds wp : arraylist)
            {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    feeds.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void update(Context context, ArrayList<Feeds> feedsArrayList){
        RSSFeedDbHelper rssFeedDbHelper = new RSSFeedDbHelper(context);

        feedsArrayList.clear();
        feedsArrayList = rssFeedDbHelper.getAllFeeds();
        feeds.addAll(feedsArrayList);

        notifyDataSetChanged();

        rssFeedDbHelper.close();
    }

}

