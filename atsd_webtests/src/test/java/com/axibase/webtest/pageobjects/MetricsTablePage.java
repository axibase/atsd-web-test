package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class MetricsTablePage implements Table {
    private final String BASE_URL = "/metrics";

    private By searchQuery = By.id("searchQuery");

    public MetricsTablePage() {
        open(BASE_URL);
    }

    @Override
    public boolean isRecordPresent(String name) {
        String xpathToEntity = String.format("//*[@id='metricsList']//a[text()='%s']", name);
        return $$(By.xpath(xpathToEntity)).size() != 0;
    }

    @Override
    public void searchRecordByName(String name) {
        $(searchQuery).setValue(name).submit();
    }

}
