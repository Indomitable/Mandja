package com.vmladenov.cook.core.html;

import android.util.Pair;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;

public class Block2ListReader {

    public Pair<ArrayList<SmallPreview>, Integer> getPagedBloc2s(String baseUrl, Integer pageNumber) {

        String pageUrl = baseUrl + pageNumber;
        try {
            Parser parser = new Parser(pageUrl);

            HasAttributeFilter filter = new HasAttributeFilter("id", "page_data_container");
            NodeList list = parser.parse(filter);
            if (list.size() != 1) return null;
            Node pageContainer = list.elementAt(0);

            ArrayList<SmallPreview> blocks = getBlocks(pageContainer);
            Integer nextPageNumber = HtmlHelper.getNextPageNumber(pageContainer);
            Pair<ArrayList<SmallPreview>, Integer> pair = new Pair<ArrayList<SmallPreview>, Integer>(
                    blocks, nextPageNumber);
            return pair;

        } catch (ParserException e) {
            return null;
        }
    }

    private ArrayList<SmallPreview> getBlocks(Node node) {
        try {
            Parser parser = new Parser(node.toHtml());
            HasAttributeFilter classFilter = new HasAttributeFilter("class", "block_2");
            TagNameFilter tagFilter = new TagNameFilter("div");
            AndFilter combineFilter = new AndFilter(tagFilter, classFilter);
            NodeList list = parser.parse(combineFilter);
            ArrayList<SmallPreview> advices = new ArrayList<SmallPreview>();
            for (int i = 0; i < list.size(); i++) {
                Div div = (Div) list.elementAt(i);

                advices.add(HtmlHelper.getPreviewFromBlock2(div));
            }

            return advices;
        } catch (ParserException e) {
            return null;
        }
    }
}
