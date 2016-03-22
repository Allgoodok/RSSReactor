package com.android.internship.rssreactor.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.internship.rssreactor.R;
import com.android.internship.rssreactor.entities.FeedData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FeedDataAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<FeedData> feedItems = null;
    private ArrayList<FeedData> feedsList;


    public FeedDataAdapter(Context context, ArrayList<FeedData> objects) {
        // TODO Auto-generated constructor stub
        mContext = context;
        feedItems = objects;
        inflater = LayoutInflater.from(mContext);
        this.feedsList = new ArrayList<FeedData>();
        this.feedsList.addAll(feedItems);
    }

    public class ViewHolder {
        TextView title;
        TextView date;
        ImageView picture;
        String pictureURL;
        Bitmap bitmap;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public FeedData getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final FeedDataAdapter.ViewHolder holder;
        if (view == null) {
            holder = new FeedDataAdapter.ViewHolder();
            view = inflater.inflate(R.layout.feed_item, null);
            holder.picture = (ImageView) view.findViewById(R.id.feedPic);
            holder.title = (TextView) view.findViewById(R.id.feedTitle);
            holder.date = (TextView) view.findViewById(R.id.feedDate);

            view.setTag(holder);
        } else {
            holder = (FeedDataAdapter.ViewHolder) view.getTag();
        }
        FeedData feedData = feedItems.get(position);
        if (feedData.feedPicture != null) {
            holder.pictureURL = feedData.feedPicture;
            new DownloadAsyncTask().execute(holder);
        } else {
            holder.picture.setImageResource(R.drawable.postthumb_loading);
        }
        holder.title.setText(feedItems.get(position).getFeedTitle());
        holder.date.setText(feedItems.get(position).getFeedDate());

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        feedItems.clear();
        if (charText.length() == 0) {
            feedItems.addAll(feedsList);
        }
        else
        {
            for (FeedData wp : feedsList)
            {
                if (wp.feedTitle.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    feedItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected FeedDataAdapter.ViewHolder doInBackground(FeedDataAdapter.ViewHolder... params) {
            FeedDataAdapter.ViewHolder viewHolder = params[0];
            try {
                URL imageURL = new URL(viewHolder.pictureURL);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (IOException e) {
                Log.e("error", "Downloading Image Failed");
                viewHolder.bitmap = null;
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(FeedDataAdapter.ViewHolder result) {
            if (result.bitmap == null) {
                result.picture.setImageResource(R.drawable.postthumb_loading);
            } else {
                result.picture.setImageBitmap(result.bitmap);
            }
        }
    }
}

