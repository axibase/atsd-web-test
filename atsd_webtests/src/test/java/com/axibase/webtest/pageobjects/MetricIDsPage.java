package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MetricIDsPage {
    private static final String BASE_URL = "/admin/metrics/uids";

    public MetricIDsPage() {
        open(BASE_URL);
    }

    public SelenideElement getTable() {
        return $(By.id("buildInfo"));
    }

}
