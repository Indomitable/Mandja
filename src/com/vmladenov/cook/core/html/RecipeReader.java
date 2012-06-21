package com.vmladenov.cook.core.html;

import android.util.Pair;
import com.vmladenov.cook.core.GlobalStrings;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RecipeReader {
    private final String url;

    // private final Context _context;

    public RecipeReader() {
        this("");
    }

    public RecipeReader(String url) {
        this.url = url;
    }

    public Recipe getRecipe() {
        return this.BuildRecipe();
    }

    // Get Single Recipe

    private Recipe BuildRecipe() {
        Recipe recipe = new Recipe();
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
                            recipe.setTitle(ParseTitle(currentRecipeDetail).trim());
                            String url = getUrl(currentRecipeDetail);
                            recipe.setId(getId(url));
                            recipe.setUrl(url);
                        }
                        if (classAttr.equalsIgnoreCase("main_image")) {
                            String imgUrl = ParseImageUrl(currentRecipeDetail);
                            if (imgUrl == "") {
                                recipe.setId(0);
                                return recipe;
                            } else
                                recipe.setImageUrl(ParseImageUrl(currentRecipeDetail));
                        }
                        if (classAttr.equalsIgnoreCase("products")) {
                            String products = ParseContentList(currentRecipeDetail);
                            recipe.setProducts(products);
                        }
                        if (classAttr.equalsIgnoreCase("description")) {
                            String description = recipe.getDescription();
                            String temp = ParseContentList(currentRecipeDetail);
                            if (description == null || description == "")
                                recipe.setDescription(temp);
                            else {
                                recipe.setNotes(temp);
                            }
                        }
                        if (classAttr.equalsIgnoreCase("date")) {
                            String date = ParseContentList(currentRecipeDetail).replace("\n", "");

                            recipe.setDate(date);
                        }

                    }
                }
            }
        } catch (ParserException e) {
            return null;
        }
        return recipe;
    }

    private String ParseTitle(TagNode titleNode) {
        TagNode innerTag = (TagNode) titleNode.getFirstChild();
        Node innerTag2 = innerTag.getFirstChild();
        if (innerTag2 instanceof TextNode) // When the recipe name is a text
        {

            String title = ((TextNode) innerTag2).getText();
            return title.substring(title.indexOf(':') + 1);
        } else
        // When the recipe name is a link
        {
            TextNode text = (TextNode) innerTag2.getFirstChild();
            return text.getText();
        }
    }

    private String getUrl(TagNode titleNode) {
        TagNode innerTag = (TagNode) titleNode.getFirstChild();
        Node innerTag2 = innerTag.getFirstChild();
        if (innerTag2 instanceof TextNode) // When the recipe name is a text
        {
            return this.url;
        } else
        // When the recipe name is a link
        {
            LinkTag text = (LinkTag) innerTag;
            return text.extractLink();
        }

    }

    private String ParseImageUrl(TagNode imgNode) {
        TagNode aHrefTag = (TagNode) imgNode.getFirstChild();
        ImageTag image = (ImageTag) aHrefTag.getFirstChild();
        if (image == null) // nolink
            return "";
        return image.extractImageLocn().replace("/thumbs_250", "");
    }

    private String ParseContentList(TagNode content) {
        StringBuilder builder = new StringBuilder();
        NodeList contentList = content.getChildren();
        for (int i = 0; i < contentList.size(); i++) {
            Node line = contentList.elementAt(i);
            if (line instanceof TextNode) {
                builder.append(line.getText().trim() + "\n");
            }
        }
        return builder.toString();
    }

    // Alphabetic

    public Pair<ArrayList<SmallPreview>, Integer> getPagedReciples(String baseUrl,
                                                                   Integer pageNumber) {

        String pageUrl = baseUrl + pageNumber;
        try {
            Parser parser = new Parser(pageUrl);

            HasAttributeFilter filter = new HasAttributeFilter("id", "page_data_container");
            NodeList list = parser.parse(filter);
            if (list.size() != 1) return null;
            Node pageContainer = list.elementAt(0);

            ArrayList<SmallPreview> recipes = getRecipes(pageContainer);
            Integer nextPageNumber = HtmlHelper.getNextPageNumber(pageContainer);
            Pair<ArrayList<SmallPreview>, Integer> pair = new Pair<ArrayList<SmallPreview>, Integer>(
                    recipes, nextPageNumber);
            return pair;

        } catch (ParserException e) {
            return null;
        }
    }

    private ArrayList<SmallPreview> getRecipes(Node node) {
        ArrayList<SmallPreview> recipes = new ArrayList<SmallPreview>();
        Parser parser = new Parser();
        try {
            parser.setInputHTML(node.toHtml());

            HasAttributeFilter classFilter = new HasAttributeFilter("class", "recipe_list");
            TagNameFilter tagFilter = new TagNameFilter("div");
            AndFilter combineFilter = new AndFilter(tagFilter, classFilter);
            NodeList list = parser.parse(combineFilter);

            for (int i = 0; i < list.size(); i++) {
                SmallPreview recipe = getRecipeFromPageContainer((Div) list.elementAt(i));
                recipes.add(recipe);
            }

            return recipes;
        } catch (ParserException e) {
            return null;
        }

    }

    private SmallPreview getRecipeFromPageContainer(Div div) {
        SmallPreview recipe = new SmallPreview();

        // Image
        Div divImage = (Div) div.getChild(1);
        ImageTag imageTag = (ImageTag) divImage.getFirstChild().getFirstChild();
        recipe.imageUrl = imageTag.extractImageLocn();

        // Recipe title and url
        LinkTag ahrefNode = (LinkTag) div.getChild(2);
        recipe.bigViewUrl = ahrefNode.extractLink();

        TextNode titleNode = (TextNode) ahrefNode.getFirstChild().getNextSibling();
        recipe.title = titleNode.getText();

        TextNode addedOnNode = (TextNode) div.getChild(5);
        String addedOn = addedOnNode.getText();
        addedOn = addedOn.substring(0, addedOn.lastIndexOf(','));
        recipe.description = addedOn;
        return recipe;
    }

    // Categories
    public ArrayList<Link> getCategories() {
        return HtmlHelper.getBulletList(GlobalStrings.RecipeCategoryUrl);
    }

    // Sub categories
    public ArrayList<Link> getSubCategories() {
        try {
            Parser parser = new Parser(url);
            HasAttributeFilter classFilter = new HasAttributeFilter("class", "advice_categories");

            Node node = parser.parse(classFilter).elementAt(1);
            Parser nodeParser = new Parser(node.toHtml());

            HasAttributeFilter classFilter1 = new HasAttributeFilter("class", "bullet");
            TagNameFilter tagFilter = new TagNameFilter("div");
            AndFilter combineFilter = new AndFilter(tagFilter, classFilter1);
            NodeList list = nodeParser.parse(combineFilter);

            ArrayList<Link> categories = new ArrayList<Link>();
            for (int i = 0; i < list.size(); i++) {
                Div div = (Div) list.elementAt(i);
                LinkTag linkTag = (LinkTag) div.getFirstChild();
                String linkUrl = linkTag.extractLink();

                Link link = new Link();
                TextNode text = (TextNode) linkTag.getFirstChild();
                link.setCaption(text.getText());
                link.setLink(linkUrl);
                categories.add(link);
            }
            return categories;
        } catch (ParserException e) {
            return null;
        }
    }

    public long getId(String url) {
        Pattern p = Pattern.compile("id=(\\d+)");
        Matcher m = p.matcher(url);
        if (m.find()) return Long.parseLong(m.group(1));
        return 0;
    }
}
