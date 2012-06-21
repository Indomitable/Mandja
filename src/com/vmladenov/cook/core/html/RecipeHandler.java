package com.vmladenov.cook.core.html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class RecipeHandler extends DefaultHandler {
    Recipe _recipe = null;
    Boolean _readingRecipe;

    final int RecipeDetailsNone = -1;
    final int RecipeDetailsTitle = 0;
    final int RecipeDetailsImage = 1;
    final int RecipeDetailsProducts = 2;
    final int RecipeDetailsDescription = 3;

    int _currentRecipeDetailsState = RecipeDetailsNone;
    String _currentContent = "";

    @Override
    public void startDocument() throws SAXException {
        _recipe = new Recipe();
        _readingRecipe = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (localName.equals("div")) {
            String className = attributes.getValue("class");
            if (className != null) {
                if (className.equals("recipe_details")) {
                    _readingRecipe = true;
                    _currentRecipeDetailsState = RecipeDetailsNone;
                    return;
                }
                if (_readingRecipe && className.equals("title")) {
                    _currentRecipeDetailsState = RecipeDetailsTitle;
                    return;
                }
                if (_readingRecipe && className.equals("main_image")) {
                    _currentRecipeDetailsState = RecipeDetailsImage;
                    return;
                }
                if (_readingRecipe && className.equals("products")) {
                    _currentRecipeDetailsState = RecipeDetailsProducts;
                    return;
                }
                if (_readingRecipe && className.equals("description")) {
                    _currentRecipeDetailsState = RecipeDetailsDescription;
                    return;
                }
            }
            return;
        }

        if (_readingRecipe && _currentRecipeDetailsState == RecipeDetailsImage
                && localName.equals("img")) {
            String imgSource = attributes.getValue("src");
            _recipe.setImageUrl(imgSource);
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (_readingRecipe) {
            if (localName.equals("h1") && _currentRecipeDetailsState == RecipeDetailsTitle) {
                _recipe.setTitle(_currentContent);
                _currentContent = "";
                _currentRecipeDetailsState = RecipeDetailsNone;
                return;
            }

            if (localName.equals("div") && _currentRecipeDetailsState == RecipeDetailsProducts) {
                _recipe.setProducts(_currentContent);
                _currentContent = "";
                _currentRecipeDetailsState = RecipeDetailsNone;
                return;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        _currentContent += new String(ch, start, length);
    }

    public Recipe getReciple() {
        return _recipe;
    }
}
