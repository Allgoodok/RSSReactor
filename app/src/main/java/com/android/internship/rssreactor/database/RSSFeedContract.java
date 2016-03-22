package com.android.internship.rssreactor.database;

import android.provider.BaseColumns;

/**
 * Created by Vlados Papandos on 15.03.2016.
 */
public final class RSSFeedContract {
    public RSSFeedContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Feeds";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LINK = "link";


    }
}
