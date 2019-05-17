package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class MetricsTablePage implements Table {
    private final String BASE_URL = "/metrics";

    private By searchQuery = By.id("searchQuery");

    public MetricsTablePage() {

        open(createNewURL(BASE_URL));
    }

    @Override
    public boolean isRecordPresent(String name) {
        String xpathToEntity = String.format("//*[@id='metricsList']//a[text()='%s']", name);
        return $$(By.xpath(xpathToEntity)).size() != 0;
    }

    @Override
    public void searchRecordByName(String name) {
        WebElement searchQueryElement = $(searchQuery);
        CommonActions.setValueOption(name, searchQueryElement);
        searchQueryElement.submit();
    }

}
