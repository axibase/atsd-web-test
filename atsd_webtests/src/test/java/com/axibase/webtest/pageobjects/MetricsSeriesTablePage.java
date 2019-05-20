package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MetricsSeriesTablePage {
    private By metricList = By.id("metricList");

    public MetricsSeriesTablePage(String metricName) {

        open(createNewURL("/metrics/" + metricName + "/series"));
    }

    public boolean isSeriesPresent() {
        return !$(metricList).$$(By.cssSelector("tbody > tr")).isEmpty();
    }

}
