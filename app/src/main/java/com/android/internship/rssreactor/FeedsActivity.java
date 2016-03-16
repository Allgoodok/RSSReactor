package com.android.internship.rssreactor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        RSSFeedDbHelper mDbHelperWrite = new RSSFeedDbHelper(this);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listviews, mDbHelperWrite.getAllCotacts());


        ListView listView = (ListView) findViewById(R.id.feedsView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
