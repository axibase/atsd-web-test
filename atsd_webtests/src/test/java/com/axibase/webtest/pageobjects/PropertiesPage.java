package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PropertiesPage {
    private final String BASE_URL = "/properties";

    public PropertiesPage(String entityName, String[] paramKeys, String[] paramValues) {

        open(createNewURL("/entities/" + entityName + BASE_URL, paramKeys, paramValues));
    }

    public String getTagsAndKeys() {
        return $(By.id("property-widget")).getText();
    }

}
