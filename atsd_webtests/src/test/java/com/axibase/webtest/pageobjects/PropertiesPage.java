package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class PropertiesPage {
    private final String BASE_URL = "/properties";

    public PropertiesPage(String entityName, String[] paramKeys, String[] paramValues) {
        open(createNewURL("/entities/" + entityName + BASE_URL, paramKeys, paramValues));
    }

    public String[] getTagsAndKeys() {
        return $$(By.xpath("//*[@id='property-widget']//div[not(contains(@class,'axi-table-column-time')) " +
                "and contains(@class, 'axi-table-cell')]")).stream().map(SelenideElement::text).toArray(String[]::new);
    }

}
