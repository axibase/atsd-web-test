package com.axibase.webtest.pages;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;

public class MetricsForEntityPage {
    private By searchQuery = By.id("searchQuery");
    private By searchButton = By.cssSelector("input[type='submit']");

    public MetricsForEntityPage(String entityName, Map<String, String> params) {
        open(CommonActions.createNewURL("/entities/" + entityName + "/metrics", params));
    }

    public MetricsForEntityPage setQuerySearch(String filter){
        $(searchQuery).setValue(filter);
        return this;
    }

    public MetricsForEntityPage search(){
        $(searchButton).click();
        return this;
    }

    public ElementsCollection getMetrics(){
        return $$("#metricList > tbody > tr > td:nth-child(4n)");
    }

}
