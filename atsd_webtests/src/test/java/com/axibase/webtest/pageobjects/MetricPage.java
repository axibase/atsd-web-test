package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        CommonActions.setValueOption(tagName, tagNamesList.get(tagNamesList.size() - 1));
        ElementsCollection tagValuesList = $$(tagValues);
        CommonActions.setValueOption(tagValue, tagValuesList.get(tagValuesList.size() - 1));
        return this;
    }

    public WebElement getName() {
        return $(name);
    }

    public WebElement getLabel() {
        return $(label);
    }

    public MetricPage setLabel(String value) {
        CommonActions.setValueOption(value, $(label));
        return this;
    }

    public WebElement getDescription() {
        return $(description);
    }

    public MetricPage setDescription(String value) {
        CommonActions.setValueOption(value, $(description));
        return this;
    }

    public WebElement getUnits() {
        return $(units);
    }

    public MetricPage setUnits(String value) {
        CommonActions.setValueOption(value, $(units));
        return this;
    }

    public WebElement getMinValue() {
        return $(minValue);
    }

    public MetricPage setMinValue(String value) {
        CommonActions.setValueOption(value, $(minValue));
        return this;
    }

    public WebElement getMaxValue() {
        return $(maxValue);
    }

    public MetricPage setMaxValue(String value) {
        CommonActions.setValueOption(value, $(maxValue));
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

    public MetricPage toggleEnabledSwitch(String value) {
        $(enabledSwitch).click();
        return this;
    }

    public WebElement getPersistentSwitch() {
        return $(persistentSwitch);
    }

    public MetricPage setPersistentSwitch(String value) {
        $(persistentSwitch).click();
        return this;
    }

    public WebElement getPersistentFilter() {
        return $(persistentFilter);
    }

    public MetricPage setPersistentFilter(String value) {
        CommonActions.setValueOption(value, $(persistentFilter));
        return this;
    }

    public WebElement getRetentionIntervalDays() {
        return $(retentionIntervalDays);
    }

    public MetricPage setRetentionIntervalDays(String value) {
        CommonActions.setValueOption(value, $(retentionIntervalDays));
        return this;
    }

    public WebElement getSeriesRetentionDays() {
        return $(seriesRetentionDays);
    }

    public MetricPage setSeriesRetentionDays(String value) {
        CommonActions.setValueOption(value, $(seriesRetentionDays));
        return this;
    }

    public WebElement getDataType() {
        return $(dataType);
    }

    public MetricPage setDataType(String value) {
        CommonActions.setSelectionOption(value, $(dataType));
        return this;
    }

    public WebElement getTimeZone() {
        return $(timeZone);
    }

    public MetricPage setTimeZone(String value) {
        CommonActions.setSelectionOption(value, $(timeZone));
        return this;
    }

    public WebElement getVersioningSwitch() {
        return $(versioningSwitch);
    }

    public MetricPage setVersioningSwitch(String value) {
        $(versioningSwitch).click();
        return this;
    }

    public WebElement getInvalidAction() {
        return $(invalidAction);
    }

    public MetricPage setInvalidAction(String value) {
        CommonActions.setSelectionOption(value, $(invalidAction));
        return this;
    }

    public WebElement getInterpolation() {
        return $(interpolation);
    }

    public MetricPage setInterpolation(String value) {
        CommonActions.setSelectionOption(value, $(interpolation));
        return this;
    }

}
