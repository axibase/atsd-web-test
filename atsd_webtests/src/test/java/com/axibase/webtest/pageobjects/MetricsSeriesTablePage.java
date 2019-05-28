package com.axibase.webtest.pageobjects;

import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.net.URLEncoder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MetricsSeriesTablePage {
    private By metricList = By.id("metricList");

    @SneakyThrows
    public MetricsSeriesTablePage(String metricName) {
        open("/metrics/" + URLEncoder.encode(metricName, "UTF-8") + "/series");
    }

    public boolean isSeriesPresent() {
        return !$(metricList).$$("tbody > tr").isEmpty();
    }

}
