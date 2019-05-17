package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class EntityPage {
    private final String BASE_URL = "/entities/";

    private By entityName = By.id("entityName");
    private By label = By.id("label");
    private By tagNames = By.className("tag-key");
    private By tagValues = By.className("tag-value");
    private By addTag = By.className("add-tag-button");

    private By enabledSwitch = By.id("enabled");
    private By interpolation = By.id("interpolate");
    private By timeZone = By.id("timeZone");

    public EntityPage(String entityName) {
        open(createNewURL(BASE_URL + entityName));
    }

    public EntityPage openSettingsPanel() {
        By settingsPanel = By.xpath("//*[contains(@data-target,'#settingsPanel')]");
        if ($(settingsPanel).getAttribute("class").contains("collapsed")) {
            $(settingsPanel).click();
        }
        return this;
    }

    public EntityPage addTag(String tagName, String tagValue) {
        $(addTag).click();
        ElementsCollection tagNamesList = $$(tagNames);
        CommonActions.setValueOption(tagName, tagNamesList.get(tagNamesList.size() - 1));
        ElementsCollection tagValuesList = $$(tagValues);
        CommonActions.setValueOption(tagValue, tagValuesList.get(tagValuesList.size() - 1));
        return this;
    }

    public WebElement getEntityName() {
        return $(entityName);
    }

    public WebElement getLabel() {
        return $(label);
    }

    public EntityPage setLabel(String value) {
        CommonActions.setValueOption(value, $(label));
        return this;
    }

    public String getTagNames() {
        return $$(tagNames).stream().
                map(element -> element.getAttribute("value")).
                collect(Collectors.joining(","));
    }

    public String getTagValues() {
        return $$(tagValues).stream().
                map(element -> element.getAttribute("value")).
                collect(Collectors.joining(","));
    }

    public WebElement getEnabledSwitch() {
        return $(enabledSwitch);
    }

    public EntityPage toggleEnabledSwitch() {
        $(enabledSwitch).click();
        return this;
    }

    public WebElement getInterpolation() {
        return $(interpolation);
    }

    public EntityPage setInterpolation(String value) {
        CommonActions.setSelectionOption(value, $(interpolation));
        return this;
    }

    public WebElement getTimeZone() {
        return $(timeZone);
    }

    public EntityPage setTimeZone(String value) {
        CommonActions.setSelectionOption(value, $(timeZone));
        return this;

    }

}
