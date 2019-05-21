package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.stream.Collectors;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class MetricPage {
    private final String BASE_URL = "/metrics/metric.xhtml";

    private By name = By.id("metric.name");
    private By label = By.id("metric.label");
    private By description = By.id("metric.description");
    private By units = By.id("metric.units");
    private By minValue = By.id("metric.minValue");
    private By maxValue = By.id("metric.maxValue");
    private By tagNames = By.className("tag-key");
    private By tagValues = By.className("tag-value");
    private By addTag = By.className("add-tag-button");

    private By enabledSwitch = By.id("metric.enabled");
    private By persistentSwitch = By.id("metric.persistent");
    private By persistentFilter = By.id("metric.filter");
    private By retentionIntervalDays = By.id("metric.retentionIntervalDays");
    private By seriesRetentionDays = By.id("metric.seriesRetentionDays");
    private By dataType = By.id("metric.dataType");
    private By timeZone = By.id("metric.timeZone");
    private By versioningSwitch = By.id("metric.versioning");
    private By invalidAction = By.id("metric.invalidValueAction");
    private By interpolation = By.id("metric.interpolate");

    public MetricPage(String[] paramKeys, String[] paramValues) {
        open(createNewURL(BASE_URL, paramKeys, paramValues));
    }

    public MetricPage openSettingsPanel() {
        By settingsPanel = By.xpath("//*[contains(@data-target,'#settingsPanel')]");
        if ($(settingsPanel).getAttribute("class").contains("collapsed")) {
            $(settingsPanel).click();
        }
        return this;
    }

    public MetricPage addTag(String tagName, String tagValue) {
        $(addTag).click();
        ElementsCollection tagNamesList = $$(tagNames);
        tagNamesList.get(tagNamesList.size() - 1).selectOption(tagName);
        ElementsCollection tagValuesList = $$(tagValues);
        tagValuesList.get(tagValuesList.size() - 1).selectOption(tagValue);
        return this;
    }

    public SelenideElement getName() {
        return $(name);
    }

    public SelenideElement getLabel() {
        return $(label);
    }

    public MetricPage setLabel(String value) {
        $(label).setValue(value);
        return this;
    }

    public SelenideElement getDescription() {
        return $(description);
    }

    public MetricPage setDescription(String value) {
        $(description).setValue(value);
        return this;
    }

    public SelenideElement getUnits() {
        return $(units);
    }

    public MetricPage setUnits(String value) {
        $(units).setValue(value);
        return this;
    }

    public SelenideElement getMinValue() {
        return $(minValue);
    }

    public MetricPage setMinValue(String value) {
        $(minValue).setValue(value);
        return this;
    }

    public SelenideElement getMaxValue() {
        return $(maxValue);
    }

    public MetricPage setMaxValue(String value) {
        $(maxValue).setValue(value);
        return this;
    }

    public String getTagNames() {
        return $$(tagNames).stream().
                map(SelenideElement::getValue).
                collect(Collectors.joining(","));
    }

    public String getTagValues() {
        return $$(tagValues).stream().
                map(SelenideElement::getValue).
                collect(Collectors.joining(","));
    }

    public SelenideElement getEnabledSwitch() {
        return $(enabledSwitch);
    }

    public MetricPage toggleEnabledSwitch(String value) {
        $(enabledSwitch).click();
        return this;
    }

    public SelenideElement getPersistentSwitch() {
        return $(persistentSwitch);
    }

    public MetricPage setPersistentSwitch(String value) {
        $(persistentSwitch).click();
        return this;
    }

    public SelenideElement getPersistentFilter() {
        return $(persistentFilter);
    }

    public MetricPage setPersistentFilter(String value) {
        $(persistentFilter).setValue(value);
        return this;
    }

    public SelenideElement getRetentionIntervalDays() {
        return $(retentionIntervalDays);
    }

    public MetricPage setRetentionIntervalDays(String value) {
        $(retentionIntervalDays).setValue(value);
        return this;
    }

    public SelenideElement getSeriesRetentionDays() {
        return $(seriesRetentionDays);
    }

    public MetricPage setSeriesRetentionDays(String value) {
        $(seriesRetentionDays).setValue(value);
        return this;
    }

    public SelenideElement getDataType() {
        return $(dataType);
    }

    public MetricPage setDataType(String value) {
        $(dataType).selectOption(value);
        return this;
    }

    public SelenideElement getTimeZone() {
        return $(timeZone);
    }

    public MetricPage setTimeZone(String value) {
        $(timeZone).selectOption(value);
        return this;
    }

    public SelenideElement getVersioningSwitch() {
        return $(versioningSwitch);
    }

    public MetricPage setVersioningSwitch(String value) {
        $(versioningSwitch).click();
        return this;
    }

    public SelenideElement getInvalidAction() {
        return $(invalidAction);
    }

    public MetricPage setInvalidAction(String value) {
        $(invalidAction).selectOption(value);
        return this;
    }

    public SelenideElement getInterpolation() {
        return $(interpolation);
    }

    public MetricPage setInterpolation(String value) {
        $(interpolation).selectOption(value);
        return this;
    }

}
