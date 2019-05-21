package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SeriesTagKeyIDsPage {
    private static final String BASE_URL = "/admin/tags/series_tag_key/uids";

    public SeriesTagKeyIDsPage() {
        open(createNewURL(BASE_URL));
    }

    public SelenideElement getTable() {
        return $(By.id("buildInfo"));
    }

}
