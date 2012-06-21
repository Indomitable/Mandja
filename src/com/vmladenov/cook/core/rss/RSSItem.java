package com.vmladenov.cook.core.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class RSSItem {
    private long id = 0;
    private String _title = null;
    private String _description = null;
    private String _link = null;
    private String _category = null;
    private Date _pubdate = null;

    RSSItem() {
    }

    void setTitle(String title) {
        _title = title;
    }

    void setDescription(String description) {
        _description = description;
    }

    void setLink(String link) {
        _link = link;
    }

    void setCategory(String category) {
        _category = category;
    }

    void setPubDate(String pubdate) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        try {
            Date temp = format.parse(pubdate);
            _pubdate = temp;
        } catch (ParseException e) {
            _pubdate = new Date(1900, 1, 1);
        }
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public String getLink() {
        return _link;
    }

    String getCategory() {
        return _category;
    }

    public Date getPubDate() {
        return _pubdate;
    }

    public String toString() {
        // limit how much text we display
        if (_title.length() > 42) {
            return _title.substring(0, 42) + "...";
        }
        return _title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
