package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PropertiesTablePage {
    private final String BASE_URL = "/properties";

    public PropertiesTablePage(String entityName) {

        open(createNewURL("/entities/" + entityName + BASE_URL));
    }

    public boolean isPropertyPresent(String propertyName) {
        return $(By.id("property-types-table"))
                .$$(By.cssSelector("tbody > tr >td:nth-child(3n)"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(",")).contains(propertyName);
    }

}
