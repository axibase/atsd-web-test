package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class PortalPage {
    private By contentWrapper = By.id("content-wrapper");

    public String getContentWrapperText() {
        return $(contentWrapper).getText();
    }

}
