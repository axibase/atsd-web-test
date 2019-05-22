package com.axibase.webtest.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.axibase.webtest.CommonActions.encode;
import static com.codeborne.selenide.Selenide.*;

public class MetricsForEntityPage {
    private By searchQuery = By.id("searchQuery");
    private By searchButton = By.cssSelector("input[type='submit']");

    public MetricsForEntityPage(String entityName) {
        open(createNewURL("/entities/" + encode(entityName) + "/metrics"));
    }

    public MetricsForEntityPage setQuerySearch(String query) {
        $(searchQuery).setValue(query);
        return this;
    }

    public MetricsForEntityPage search() {
        $(searchButton).click();
        return this;
    }

    public MetricsForEntityPage search(String query) {
        return setQuerySearch(query).search();
    }

    public String[] getMetricNames() {
        return $$("#metricList > tbody > tr > td:nth-child(4n)")
                .stream().map(SelenideElement::text).toArray(String[]::new);
    }

}
