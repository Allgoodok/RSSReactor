package com.android.internship.rssreactor.entities;

/**
 * Created by Vlados Papandos on 16.03.2016.
 */
public class Feeds {

    private String id;
    private String title;
    private String link;

    public String getId() {return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Feeds() {
        super();
    }

    public Feeds(String id, String title, String link){
        super();
        this.id = id;
        this.title = title;
        this.link = link;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
