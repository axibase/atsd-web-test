package com.axibase.webtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static com.codeborne.selenide.Selenide.$;

public class ForecastSettingsPage {
    private By groupingType = By.id("groupingType");
    private By groupingTags = By.id("settings.requiredTagKeys");

    public String getGroupingType() {
        return new Select($(groupingType)).getFirstSelectedOption().getText();
    }

    public String getGroupingTags() {
        return $(groupingTags).getValue();
    }

}
