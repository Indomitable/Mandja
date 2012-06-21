package com.vmladenov.cook.core.rss;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class RSSHandler extends DefaultHandler {
    private RSSFeed _feed;
    private RSSItem _currentItem;

    private final int READ_CHANNEL = 0;
    private final int READ_ITEM = 1;

    int _currentState = -1;
    String _currentContent = "";

    public RSSFeed getFeed() {
        return _feed;
    }

    @Override
    public void startDocument() throws SAXException {
        _feed = new RSSFeed();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        _currentContent = "";
        if (localName.equals("item")) {
            _currentItem = new RSSItem();
            _currentState = READ_ITEM;
            return;
        }

        if (localName.equals("channel")) {
            _currentState = READ_CHANNEL;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (_currentState == READ_CHANNEL) {
            if (localName.equals("title")) {
                _feed.setTitle(_currentContent);
                return;
            }
            if (localName.equals("link")) {
                _feed.setLink(_currentContent);
                return;
            }
            if (localName.equals("pubDate")) {
                _feed.setPubDate(_currentContent);
                return;
            }
            if (localName.equals("lastBuildDate")) {
                _feed.setLastBuildDate(_currentContent);
                return;
            }
            if (localName.equals("webMaster")) {
                _feed.setWebMaster(_currentContent);
                return;
            }
            return;
        }

        if (_currentState == READ_ITEM) {
            if (localName.equals("title")) {
                _currentItem.setTitle(_currentContent);
                return;
            }
            if (localName.equals("description")) {
                _currentItem.setDescription(_currentContent);
                return;
            }
            if (localName.equals("origLink")) {
                _currentItem.setLink(_currentContent);
                String sid = _currentContent.substring(_currentContent.lastIndexOf("id=") + 3);
                _currentItem.setId(Long.parseLong(sid));
                return;
            }
            if (localName.equals("category")) {
                _currentItem.setCategory(_currentContent);
                return;
            }
            if (localName.equals("pubDate")) {
                _currentItem.setPubDate(_currentContent);
                return;
            }

            if (localName.equals("item")) {
                // add our item to the list!
                _feed.addItem(_currentItem);
                return;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        _currentContent += new String(ch, start, length);
    }

}
