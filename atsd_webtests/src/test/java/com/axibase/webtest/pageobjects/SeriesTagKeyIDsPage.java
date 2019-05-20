package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SeriesTagKeyIDsPage implements IDsPage {
    private static final String BASE_URL = "/admin/tags/series_tag_key/uids";

    public SeriesTagKeyIDsPage() {
        open(createNewURL(BASE_URL));
    }

    @Override
    public String getValuesInTable() {
        return $(By.id("buildInfo"))
                .$$(By.cssSelector("tbody > tr > td:nth-child(2n)"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList())
                .toString();
    }

}
