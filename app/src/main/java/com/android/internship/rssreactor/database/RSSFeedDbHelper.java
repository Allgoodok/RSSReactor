package com.android.internship.rssreactor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.internship.rssreactor.entities.Feeds;
import com.android.internship.rssreactor.database.RSSFeedContract.FeedEntry;

import java.util.ArrayList;

/**
 * Created by Vlados Papandos on 15.03.2016.
 */
public class RSSFeedDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedsTable.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_LINK + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    private static final String SQL_PUT_ENTRY =
            "INSERT INTO " + FeedEntry.TABLE_NAME + " (" + FeedEntry.COLUMN_NAME_TITLE + ", " + FeedEntry.COLUMN_NAME_LINK + ") VALUES"
            + "('BBC News', 'http://feeds.bbci.co.uk/news/rss.xml')";

    public RSSFeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_PUT_ENTRY);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public ArrayList<Feeds> getAllFeeds()
    {
        ArrayList<Feeds> array_list = new ArrayList<Feeds>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Feeds", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Feeds temp = new Feeds(res.getString(res.getColumnIndex(FeedEntry._ID)), res.getString(res.getColumnIndex(FeedEntry.COLUMN_NAME_TITLE)), res.getString(res.getColumnIndex(FeedEntry.COLUMN_NAME_LINK)));
            array_list.add(temp);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    public void putFeed(String title, String link) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_LINK, link);


        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                null,
                values);

        db.close();
    }

    public void deleteFeed(long rowId){

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };
        db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }


}