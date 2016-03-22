package com.android.internship.rssreactor.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.internship.rssreactor.adapters.FeedDataAdapter;
import com.android.internship.rssreactor.R;
import com.android.internship.rssreactor.entities.FeedData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class FeedsItemsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, IGNORETAG;
    }

    private Context context;
    private ListView mListFeedItems;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FeedDataAdapter adapter;
    private Timer timer;
    private ImageButton mButtonBack;
    private EditText mEditText;
    private GetRSSFeedFeature getRSSFeedFeature;
    private String urlFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_items);

        urlFeed = getIntent().getExtras().getString("urlFeed");



        mButtonBack = (ImageButton) findViewById(R.id.btnBack);
        mEditText = (EditText) findViewById(R.id.searchItem);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_feed);


        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setRefreshing(true);
        mListFeedItems = (ListView) findViewById(R.id.listFeedItems);
        timer = new Timer();

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedsItemsActivity.this, FeedsActivity.class);
                startActivity(intent);
            }
        });

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        getRSSFeedFeature = new GetRSSFeedFeature(context, urlFeed);
                        getRSSFeedFeature.execute();
                    }
                });

            }
        }, 0, 120000);

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if(getRSSFeedFeature.getStatus() == AsyncTask.Status.FINISHED) {
                    String text = mEditText.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        mListFeedItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedData feedItem = (FeedData) adapter.getItem(position);
                Intent intent = new Intent(FeedsItemsActivity.this, FeedItemWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("itemUrl", feedItem.feedUrl);
                bundle.putString("itemTitle", feedItem.feedTitle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onRefresh() {
        getRSSFeedFeature = new GetRSSFeedFeature(this, urlFeed);
        getRSSFeedFeature.execute();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onPause(){
        super.onPause();
        timer.cancel();
    }


    private class GetRSSFeedFeature extends AsyncTask<String, Integer, ArrayList<FeedData>>{
        private RSSXMLTag currentTag;
        private Context context;
        private String urlFeed;

        public GetRSSFeedFeature(Context context, String urlFeed) {
            this.context = context;
            this.urlFeed = urlFeed;
        }

        @Override
        protected ArrayList<FeedData> doInBackground(String... params) {
            // TODO Auto-generated method stub
            InputStream is = null;
            ArrayList<FeedData> postDataList = new ArrayList<FeedData>();
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            try {
                URL url = new URL(urlFeed);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                is = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null);

                int eventType = xpp.getEventType();
                FeedData pdData = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("item")) {
                            pdData = new FeedData();
                            currentTag = RSSXMLTag.IGNORETAG;
                        } else if (xpp.getName().equals("title")) {
                            currentTag = RSSXMLTag.TITLE;
                        } else if (xpp.getName().equals("link")) {
                            currentTag = RSSXMLTag.LINK;
                        } else if (xpp.getName().equals("pubDate")) {
                            currentTag = RSSXMLTag.DATE;
                        }else if (xpp.getName().equals("thumbnail")){
                                pdData.feedPicture = xpp.getAttributeValue(null, "url");
                                currentTag = RSSXMLTag.CONTENT;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item")) {
                            Date dateGMT=calendar.getTime();
                            Date postDate = dateFormat.parse(pdData.feedDate);
                            long milliseconds = postDate.getTime();
                            long now = dateGMT.getTime();
                            pdData.feedDate = (String) DateUtils.getRelativeTimeSpanString(milliseconds, now, DateUtils.MINUTE_IN_MILLIS);
                            postDataList.add(pdData);
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        String content = xpp.getText();
                        if (pdData != null) {
                            switch (currentTag) {
                                case TITLE:
                                    if (content.length() != 0) {
                                        if (pdData.feedTitle != null) {
                                            pdData.feedTitle += content;
                                        } else {
                                            pdData.feedTitle = content;
                                        }
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0) {
                                        if (pdData.feedUrl != null) {
                                            pdData.feedUrl += content;
                                        } else {
                                            pdData.feedUrl = content;
                                        }
                                    }
                                    break;
                                case DATE:
                                    if (content.length() != 0) {
                                        if (pdData.feedDate != null) {
                                            pdData.feedDate += content;
                                        } else {
                                            pdData.feedDate = content;
                                        }
                                    }
                                    break;
                                case CONTENT:

                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    eventType = xpp.next();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Intent intent = new Intent(FeedsItemsActivity.this, FeedsActivity.class);
                startActivity(intent);
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   catch (ParseException e) {
                e.printStackTrace();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return postDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedData> result) {
            // TODO Auto-generated method stub
            mSwipeRefreshLayout.setRefreshing(true);
            adapter = new FeedDataAdapter(context, result);
            mListFeedItems.setAdapter(adapter);
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }
}

