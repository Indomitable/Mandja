package com.vmladenov.cook.core.html;

import android.util.Pair;
import com.vmladenov.cook.core.GlobalStrings;
import org.apache.http.util.ByteArrayBuffer;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class HtmlHelper {
    public String getContent(String url) {
        try {
            URL uri = new URL(url);
            // URL url = new URL("http://feeds.feedburner.com/gotvetesmen");
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(
                        connection.getInputStream());
                ByteArrayBuffer buffer = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bufferedInputStream.read()) != -1) {
                    buffer.append((byte) current);
                }
                String html = new String(buffer.toByteArray());
                return html;
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public static ArrayList<Link> getBulletList(String url) {
        try {
            Parser parser = new Parser(url);

            HasAttributeFilter classFilter = new HasAttributeFilter("class", "bullet");
            TagNameFilter tagFilter = new TagNameFilter("div");
            AndFilter combineFilter = new AndFilter(tagFilter, classFilter);
            NodeList list = parser.parse(combineFilter);
            ArrayList<Link> bulletList = new ArrayList<Link>();
            for (int i = 0; i < list.size(); i++) {
                Div div = (Div) list.elementAt(i);
                LinkTag linkTag = (LinkTag) div.getFirstChild();
                String linkUrl = linkTag.extractLink();
                if (!linkUrl.startsWith(url)) continue;

                Link link = new Link();
                TextNode text = (TextNode) linkTag.getFirstChild();
                link.setCaption(text.getText());
                link.setLink(linkUrl);
                bulletList.add(link);
            }
            return bulletList;
        } catch (ParserException e) {
            return null;
        }
    }

    public static ArrayList<Tag> FindChildren(Node node, String tagName,
                                              Pair<String, String> attribute) {
        ArrayList<Tag> children = new ArrayList<Tag>();
        getInnerChilds(node, tagName, attribute, children);
        // NodeList list = node.getChildren();
        // for(int i = 0; i < list.size(); i++)
        // {
        // Node child =list.elementAt(i);
        // if (child instanceof Tag)
        // {
        // Tag tag = (Tag)child;
        // if (tag.getTagName().equalsIgnoreCase(tagName))
        // children.add(tag);
        // }
        // }
        return children;
    }

    private static void getInnerChilds(Node node, String tagName, Pair<String, String> attribute,
                                       ArrayList<Tag> children) {
        NodeList list = node.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Node child = list.elementAt(i);

            NodeList innerChildren = child.getChildren();
            if (innerChildren.size() > 0) getInnerChilds(child, tagName, attribute, children);

            if (child instanceof Tag) {
                Tag tag = (Tag) child;
                if ((tagName == null || (tagName != null && tag.getTagName().equalsIgnoreCase(
                        tagName)))
                        && (attribute == null || (attribute != null && checkAttribute(tag,
                        attribute)))) {
                    children.add(tag);
                }
            }
        }
    }

    private static Boolean checkAttribute(Tag tag, Pair<String, String> attribute) {
        String attributeValue = tag.getAttribute(attribute.first);
        if (attributeValue != null) {
            if (attribute.second.endsWith("%") && attribute.second.startsWith("%")) {
                String searchString = attribute.second.replace("%", "");
                if (attributeValue.contains(searchString)) return true;
            } else if (attribute.second.endsWith("%")) {
                String searchString = attribute.second.replace("%", "");
                if (attributeValue.startsWith(searchString)) return true;
            } else if (attribute.second.startsWith("%")) {
                String searchString = attribute.second.replace("%", "");
                if (attributeValue.endsWith(searchString)) return true;
            } else {
                if (attributeValue.equalsIgnoreCase(attributeValue)) return true;
            }
        }
        return false;
    }

    public static SmallPreview getPreviewFromBlock2(Div block2) {
        SmallPreview preview = new SmallPreview();
        Div divBody = (Div) block2.getFirstChild();
        Node thumbDiv = divBody.getFirstChild();
        if (thumbDiv instanceof Div) // There is a image in block_2
        {
            Node firstthumbNode = thumbDiv.getFirstChild();
            if (firstthumbNode instanceof LinkTag) // If image has a link to a
            // big view
            {
                LinkTag linkToBigView = (LinkTag) firstthumbNode;
                preview.bigViewUrl = linkToBigView.extractLink();

                ImageTag image = (ImageTag) linkToBigView.getFirstChild();
                preview.imageUrl = image.extractImageLocn();
                preview.title = image.getAttribute("alt");
            } else if (firstthumbNode instanceof ImageTag) // If there is only a
            // image (in
            // useful/dictionary)
            {
                ImageTag image = (ImageTag) firstthumbNode;
                preview.imageUrl = image.extractImageLocn();
                preview.title = image.getAttribute("alt");
            }
        }

        // Start reading description;
        StringBuilder builder = new StringBuilder();
        NodeList list = divBody.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Node child = list.elementAt(i);
            if (child instanceof TextNode) builder.append(((TextNode) child).getText());
        }

        preview.description = builder.toString();

        return preview;
    }

    public static Integer getNextPageNumber(Node node) {
        Parser parser = new Parser();
        try {
            parser.setInputHTML(node.toHtml());
            HasAttributeFilter classFilter = new HasAttributeFilter("title", GlobalStrings.NextPage);
            TagNameFilter tagFilter = new TagNameFilter("a");
            AndFilter combineFilter = new AndFilter(tagFilter, classFilter);
            NodeList list = parser.parse(combineFilter);
            if (list.size() == 1) {
                LinkTag tag = (LinkTag) list.elementAt(0);
                String link = tag.extractLink();
                return Integer.parseInt(link.substring(link.lastIndexOf('=') + 1));
            } else
                return null;
        } catch (ParserException e) {
            return null;
        }

    }
}
