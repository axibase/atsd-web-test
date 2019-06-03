package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MetricsSeriesTablePage {
    private final By metricList = By.id("metricList");

    public MetricsSeriesTablePage(String metricName) {
        open("/metrics/" + CommonActions.urlEncode(metricName) + "/series");
    }

    public boolean isSeriesPresent() {
        return !$(metricList).$$("tbody > tr").isEmpty();
    }

}
