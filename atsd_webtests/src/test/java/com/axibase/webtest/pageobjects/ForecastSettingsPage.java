package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static com.codeborne.selenide.Selenide.$;

public class ForecastSettingsPage {
    private final By groupingType = By.id("groupingType");
    private final By groupingTags = By.id("settings.requiredTagKeys");

    public String getGroupingType() {
        return new Select($(groupingType)).getFirstSelectedOption().getText();
    }

    public String getGroupingTags() {
        return $(groupingTags).getValue();
    }

}
