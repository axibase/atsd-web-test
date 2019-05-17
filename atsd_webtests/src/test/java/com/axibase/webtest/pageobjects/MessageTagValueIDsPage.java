package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MessageTagValueIDsPage implements IDsPage {
    private static final String BASE_URL = "/admin/tags/message_tag_value/uids";

    public MessageTagValueIDsPage() {
        open(createNewURL(BASE_URL));
    }

    @Override
    public String getValuesInTable() {
        return $(By.id("buildInfo"))
                .findElements(By.cssSelector("tbody > tr > td:nth-child(2n)"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList())
                .toString();
    }

}
