package com.android.internship.rssreactor.entities;

/**
 * Created by Vlados Papandos on 17.03.2016.
 */
public class FeedData {

    public String feedUrl;
    public String feedTitle;
    public String feedDate;
    public String feedPicture;

    public String getFeedDate() {
        return feedDate;
    }

    public void setFeedDate(String feedDate) {
        this.feedDate = feedDate;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedThumbUrl() {
        return feedUrl;
    }

    public void setFeedThumbUrl(String feedThumbUrl) {
        this.feedUrl = feedThumbUrl;
    }




}
