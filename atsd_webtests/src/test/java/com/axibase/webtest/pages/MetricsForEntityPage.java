package com.axibase.webtest.pages;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class MetricsForEntityPage {
    private By searchQuery = By.id("searchQuery");
    private By searchButton = By.cssSelector("input[type='submit']");

    public MetricsForEntityPage(String entityName, Map<String, String> params) {
        open(CommonActions.createNewURL("/entities/" + entityName + "/metrics", params));
    }

    public MetricsForEntityPage setQuerySearch(String filter) {
        $(searchQuery).setValue(filter);
        return this;
    }

    public MetricsForEntityPage search() {
        $(searchButton).click();
        return this;
    }

    public List<String> getMetricNames() {
        return $$("#metricList > tbody > tr > td:nth-child(4n)")
                .stream().map(SelenideElement::text).collect(Collectors.toList());
    }

}
