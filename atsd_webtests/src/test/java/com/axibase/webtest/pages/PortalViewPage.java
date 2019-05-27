package com.axibase.webtest.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PortalViewPage {
    private By leftArrow = By.className("axi-tooltip-arrow-left");
    private By legend = By.className("axi-legend-button");

    public SelenideElement getLeftArrow() {
        return $(leftArrow);
    }

    public ElementsCollection getLegend() {
        return $$(legend);
    }
}
