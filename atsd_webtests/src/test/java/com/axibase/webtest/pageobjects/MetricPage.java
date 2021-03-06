package com.axibase.webtest.pageobjects;

import com.axibase.webtest.modelobjects.Metric;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.Map;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.axibase.webtest.CommonSelects.getValueOfSwitchElement;
import static com.codeborne.selenide.Selenide.*;

public class MetricPage {
    private static final String BASE_URL = "/metrics/metric.xhtml";

    private final By name = By.id("metric.name");
    private final By label = By.id("metric.label");
    private final By description = By.id("metric.description");
    private final By units = By.id("metric.units");
    private final By minValue = By.id("metric.minValue");
    private final By maxValue = By.id("metric.maxValue");
    private final By tagNames = By.className("tag-key");
    private final By tagValues = By.className("tag-value");
    private final By addTag = By.className("add-tag-button");

    private final By enabledSwitch = By.id("metric.enabled");
    private final By persistentSwitch = By.id("metric.persistent");
    private final By persistentFilter = By.id("metric.filter");
    private final By retentionIntervalDays = By.id("metric.retentionIntervalDays");
    private final By seriesRetentionDays = By.id("metric.seriesRetentionDays");
    private final By dataType = By.id("metric.dataType");
    private final By timeZone = By.id("metric.timeZone");
    private final By versioningSwitch = By.id("metric.versioning");
    private final By invalidAction = By.id("metric.invalidValueAction");
    private final By interpolation = By.id("metric.interpolate");

    public MetricPage(Map<String, String> params) {
        open(createNewURL(BASE_URL, params));
        openSettingsPanel();
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

    public String[] getTagNames() {
        return $$(tagNames).stream()
                .map(SelenideElement::getValue)
                .filter(tag -> !tag.isEmpty())
                .toArray(String[]::new);
    }

    public String[] getTagValues() {
        return $$(tagValues).stream()
                .map(SelenideElement::getValue)
                .filter(tag -> !tag.isEmpty())
                .toArray(String[]::new);

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

    public Metric getMetric() {
        return new Metric().setMetricName(this.getName().val())
                .setStatus(getValueOfSwitchElement(this.getEnabledSwitch()))
                .setLabel(this.getLabel().val())
                .setDescription(this.getDescription().val())
                .setDataType(this.getDataType().val())
                .setInterpolationMode(this.getInterpolation().val())
                .setUnits(this.getUnits().val())
                .setFilterExpression(this.getPersistentFilter().val())
                .setTimeZone(this.getTimeZone().val())
                .setVersioning(getValueOfSwitchElement(this.getVersioningSwitch()))
                .setInvalidAction(this.getInvalidAction().val())
                .setPersistent(getValueOfSwitchElement(this.getPersistentSwitch()))
                .setRetentionIntervalDays(this.getRetentionIntervalDays().val())
                .setMinVal(this.getMinValue().val())
                .setMaxVal(this.getMaxValue().val())
                .setTagNames(this.getTagNames())
                .setTagValues(this.getTagValues());
    }

}
