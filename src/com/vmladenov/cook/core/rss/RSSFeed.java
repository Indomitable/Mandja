package com.vmladenov.cook.core.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class RSSFeed extends ArrayList<RSSItem> {
    private static final long serialVersionUID = 3141907982015389935L;
    private String _title = null;
    private Date _pubdate = null;
    private Date _lastBuildDate = null;
    private String _webMaster = null;
    private String _link = null;

    private int _itemcount = 0;
    //private ArrayList<RSSItem> _itemlist;

    RSSFeed() {
        //_itemlist = new ArrayList<RSSItem>();
    }

    int addItem(RSSItem item) {
        this.add(item);
        _itemcount++;
        return _itemcount;
    }

    public RSSItem getItem(int location) {
        return this.get(location);
    }

    public List<RSSItem> getAllItems() {
        if (this.size() > 1) return this.subList(1, this.size());
        return this;
    }

    int getItemCount() {
        return _itemcount;
    }

    void setTitle(String title) {
        _title = title;
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

    Date getPubDate() {
        return _pubdate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        try {
            Date temp = format.parse(lastBuildDate);
            _lastBuildDate = temp;
        } catch (ParseException e) {
            _lastBuildDate = new Date(1900, 1, 1);
        }
    }

    public Date getLastBuildDate() {
        return _lastBuildDate;
    }

    public void setWebMaster(String webMaster) {
        this._webMaster = webMaster;
    }

    public String getWebMaster() {
        return _webMaster;
    }

    public void setLink(String link) {
        this._link = link;
    }

    public String getLink() {
        return _link;
    }

}
