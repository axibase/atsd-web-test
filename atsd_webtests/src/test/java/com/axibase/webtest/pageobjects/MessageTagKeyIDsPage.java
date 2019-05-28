package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MessageTagKeyIDsPage {
    private static final String BASE_URL = "/admin/tags/message_tag_key/uids";

    public MessageTagKeyIDsPage() {
        open(BASE_URL);
    }

    public SelenideElement getTable() {
        return $(By.id("buildInfo"));
    }

}
