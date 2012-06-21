package com.vmladenov.tests;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TestHtmlPaser {
    public void Run() {
        String url = "http://www.gotvetesmen.com/recipes/alphabet_list/index.php?alphabet=%D0%9B&id=2856";
        try {
            Parser parser = new Parser(url);
            HasAttributeFilter filter = new HasAttributeFilter("class", "recipe_details");
            NodeList list = parser.parse(filter);
            if (list.size() > 0) {
                Node divRecipe = list.elementAt(0);
                NodeList recipeDetails = divRecipe.getChildren();

                for (int i = 0; i < recipeDetails.size(); i++) {
                    Node currentNode = recipeDetails.elementAt(i);
                    if (!(currentNode instanceof TagNode)) continue;
                    TagNode currentRecipeDetail = (TagNode) currentNode;
                    String classAttr = currentRecipeDetail.getAttribute("class");
                    if (classAttr != null) {
                        if (classAttr.equalsIgnoreCase("title")) {
                            ParseTitle(currentRecipeDetail);
                        }
                        if (classAttr.equalsIgnoreCase("main_image")) {
                            ParseImageUrl(currentRecipeDetail);
                        }
                        if (classAttr.equalsIgnoreCase("products")) {
                            ParseProducts(currentRecipeDetail);
                        }
                    }
                }
            }

        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    private String ParseTitle(TagNode titleNode) {
        TagNode h1TitleTag = (TagNode) titleNode.getFirstChild();
        TextNode text = (TextNode) h1TitleTag.getFirstChild();
        return text.getText();
    }

    private String ParseImageUrl(TagNode imgNode) {
        TagNode aHrefTag = (TagNode) imgNode.getFirstChild();
        ImageTag image = (ImageTag) aHrefTag.getFirstChild();
        return image.extractImageLocn();

    }

    private String ParseProducts(TagNode products) {
        StringBuilder builder = new StringBuilder();
        NodeList productsList = products.getChildren();
        for (int i = 0; i < productsList.size(); i++) {
            Node product = productsList.elementAt(i);
            if (product instanceof TextNode) {
                builder.append(product);
            }
        }
        return builder.toString();
    }
}
