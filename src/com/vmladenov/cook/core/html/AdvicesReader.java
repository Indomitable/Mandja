package com.vmladenov.cook.core.html;

import com.vmladenov.cook.core.GlobalStrings;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;

public class AdvicesReader {
    public ArrayList<Link> getAdviceCategories() {
        return HtmlHelper.getBulletList(GlobalStrings.AdvicesUrl);
    }

    public ArrayList<SmallPreview> getAdvices(String categoryUrl) {
        try {
            Parser parser = new Parser(categoryUrl);

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
