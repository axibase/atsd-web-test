package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.net.URLEncoder;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class MetricsForEntityPage {
    private By searchQuery = By.id("searchQuery");
    private By searchButton = By.cssSelector("input[type='submit']");

    @SneakyThrows
    public MetricsForEntityPage(String entityName) {
        open("/entities/" + URLEncoder.encode(entityName, "UTF-8") + "/metrics");
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
        int index = $$("#metricList > thead > tr > th").stream()
                .map(SelenideElement::text).collect(Collectors.toList()).indexOf("Metric");

        return $$("#metricList > tbody > tr > td:nth-child(" + (index +1) + "n)")
                .stream().map(SelenideElement::text).toArray(String[]::new);
    }

}
