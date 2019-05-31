package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.modelobjects.Entity;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonSelects.getValueOfSwitchElement;
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
        open(BASE_URL + CommonActions.urlEncode(entityName));
        openSettingsPanel();
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
        tagNamesList.get(tagNamesList.size() - 1).selectOption(tagName);
        ElementsCollection tagValuesList = $$(tagValues);
        tagValuesList.get(tagValuesList.size() - 1).selectOption(tagValue);
        return this;
    }

    public SelenideElement getEntityName() {
        return $(entityName);
    }

    public SelenideElement getLabel() {
        return $(label);
    }

    public EntityPage setLabel(String value) {
        $(label).setValue(value);
        return this;
    }

    public String[] getTagNames() {
        return $$(tagNames).stream()
                .map(SelenideElement::getValue)
                .filter(val -> !val.isEmpty())
                .toArray(String[]::new);
    }

    public String[] getTagValues() {
        return $$(tagValues).stream()
                .map(SelenideElement::getValue)
                .filter(val -> !val.isEmpty())
                .toArray(String[]::new);
    }

    public SelenideElement getEnabledSwitch() {
        return $(enabledSwitch);
    }

    public EntityPage toggleEnabledSwitch() {
        $(enabledSwitch).click();
        return this;
    }

    public SelenideElement getInterpolation() {
        return $(interpolation);
    }

    public EntityPage setInterpolation(String value) {
        $(interpolation).selectOption(value);
        return this;
    }

    public SelenideElement getTimeZone() {
        return $(timeZone);
    }

    public EntityPage setTimeZone(String value) {
        $(timeZone).selectOption(value);
        return this;

    }

    public Entity getEntity() {
        return new Entity().setEntityName(this.getEntityName().val())
                .setInterpolation(this.getInterpolation().val())
                .setLabel(this.getLabel().val())
                .setStatus(getValueOfSwitchElement(this.getEnabledSwitch()))
                .setTimeZone(this.getTimeZone().val())
                .setTagNames(this.getTagNames())
                .setTagValues(this.getTagValues());
    }


}
