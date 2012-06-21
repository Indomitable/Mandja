package com.vmladenov.cook.core.html;

public final class Link {
    private String caption;
    private String link;

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return getCaption();
    }
}
