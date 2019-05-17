package com.axibase.webtest.service;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by sild on 30.01.15.
 */
public class MetricsService extends Service {
    public static final String TITLE = "Metrics";

    public int getMetricsCount() {
        return $$(By.xpath("//*[@id='metricsList']/tbody/tr")).size();
    }

    public String getMetricByName(String name) {
        return $(By.xpath("//*[@id='metricsList']/descendant::tr[td//text()='" + name + "']")).getText();
    }
}
