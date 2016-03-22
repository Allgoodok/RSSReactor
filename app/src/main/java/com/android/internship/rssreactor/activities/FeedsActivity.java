package com.android.internship.rssreactor.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.internship.rssreactor.adapters.FeedsAdapter;
import com.android.internship.rssreactor.R;
import com.android.internship.rssreactor.database.RSSFeedDbHelper;
import com.android.internship.rssreactor.entities.Feeds;

import java.util.ArrayList;
import java.util.Locale;

public class FeedsActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageButton mAddButton;
    private EditText mEditText;
    private FeedsAdapter adapter;
    private RSSFeedDbHelper mDbHelperRead;
    private ArrayList<Feeds> feedsArrayList;
    static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        mListView = (ListView) findViewById(R.id.feedsView);
        mAddButton = (ImageButton) findViewById(R.id.btn_AddMore);
        mEditText = (EditText) findViewById(R.id.editText);

        mDbHelperRead = new RSSFeedDbHelper(this);

        feedsArrayList = mDbHelperRead.getAllFeeds();
        adapter = new FeedsAdapter(this, feedsArrayList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeds feedToClick = (Feeds) adapter.getItem(position);
                Intent intent = new Intent(FeedsActivity.this, FeedsItemsActivity.class);
                intent.putExtra("urlFeed", feedToClick.getLink());
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Feeds feedToDelete = (Feeds) adapter.getItem(position);
                mDbHelperRead.deleteFeed(Long.parseLong(feedToDelete.getId()));
                adapter.update(FeedsActivity.this, feedsArrayList);
                return true;
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedsActivity.this, AddFeedActivity.class);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = mEditText.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                final RSSFeedDbHelper mDbHelperWrite = new RSSFeedDbHelper(this);
                mDbHelperWrite.putFeed(data.getExtras().getString("name"), data.getExtras().getString("url"));
                adapter.update(this, feedsArrayList);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDbHelperRead.close();

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }
}
