package com.axibase.webtest.pages;

import com.axibase.webtest.KeyValueForm;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ForecastJobsEditPage {
    private By startDateField = By.id("settings.startTime");
    private By intervalCountField = By.id("settings.selectionInterval.intervalCount");
    private By intervalUnitField = By.id("settings.selectionInterval.intervalUnit");
    private By endDateField = By.id("settings.endTime");
    private By metricField = By.id("settings.metric");
    private By errorMetric = By.xpath("//span[@data-error-for='settings.metric']");
    private By entityField = By.id("settings.entity");
    private By tagPairs = By.className("tag-pair");
    private List<Tag> tagValueFields;
    private By addTagButton = By.xpath("//button[@onclick='addTag()']");
    private By seriesSelectionIntervalCountField = By.id("settings.seriesSelectionInterval.intervalCount");
    private By seriesSelectionIntervalUnitField = By.id("settings.seriesSelectionInterval.intervalUnit");
    private By exportSelectLink = By.id("export-series-link");

    private By sampleFilterField = By.id("settings.samplesFilter");

    private By groupByField = By.id("groupingType");
    private By requiredTagKeysField = By.id("settings.requiredTagKeys");

    private By autoAggregateCheckBox = By.id("settings.autoAveragingInterval");
    private By aggregationPeriodCountField = By.id("settings.averagingInterval.intervalCount");
    private By aggregationPeriodUnitField = By.id("settings.averagingInterval.intervalUnit");
    private By aggregateStatisticField = By.id("settings.aggregateStatistics");

    private By algorithmField = By.id("settings.algorithm");
    private By scoreIntervalCountField = By.id("settings.scoreInterval.intervalCount");
    private By scoreIntervalUnitField = By.id("settings.scoreInterval.intervalUnit");

    private By forecastNameField = By.id("settings.name");
    private By forecastHorizonCountField = By.id("settings.storeInterval.intervalCount");
    private By forecastHorizonUnitField = By.id("settings.storeInterval.intervalUnit");

    private By storeUnderMetricField = By.id("settings.producedMetric");
    private By exportStoreLink = By.xpath("//button[@onclick='toPortalEditor();return false']/preceding-sibling::a");

    private By saveButton = By.xpath("//input[@name='save']");
    private By runButton = By.xpath("//input[@name='forecast']");

    private By forecastsTable = By.id("seriesList");
    private By viewStoredForecastLink = By.xpath("//a[@data-original-title='View stored forecast']");
    private By openForecastViewerLink = By.xpath("//a[@data-original-title='Open forecast viewer']");

    private class Tag extends KeyValueForm {
        Tag(int id) {
            setKeyField("tags" + id + ".key");
            setValueField("tags" + id + ".value");
        }

        @Override
        public void setKeyField(String inspector) {
            this.keyField = $(By.id(inspector));
        }

        @Override
        public void setValueField(String inspector) {
            this.valueField = $(By.id(inspector));
        }
    }

    public ForecastJobsEditPage setStartDate(String value) {
        $(startDateField).setValue(value);
        return this;
    }

    public SelenideElement getStartDate() {
        return $(startDateField);
    }

    public ForecastJobsEditPage setIntervalCount(String value) {
        $(intervalCountField).setValue(value);
        return this;
    }

    public SelenideElement getIntervalCount() {
        return $(intervalCountField);
    }

    public ForecastJobsEditPage setIntervalUnit(String value) {
        $(intervalUnitField).selectOption(value);
        return this;
    }

    public SelenideElement getIntervalUnit() {
        return $(intervalUnitField);
    }

    public ForecastJobsEditPage setEndDate(String value) {
        $(endDateField).setValue(value);
        return this;
    }

    public SelenideElement getEndDate() {
        return $(endDateField);
    }

    public ForecastJobsEditPage setMetric(String value) {
        $(metricField).setValue(value);
        return this;
    }

    public SelenideElement getMetric() {
        return $(metricField);
    }

    public void isPresentErrorMetric() {
        $(errorMetric).shouldBe(Condition.visible);
    }

    public ForecastJobsEditPage setEntity(String value) {
        $(entityField).setValue(value);
        return this;
    }

    public SelenideElement getEntity() {
        return $(entityField);
    }

    public ForecastJobsEditPage setTagKey(String value, int index) {
        setNeededNumberTags(index);
        tagValueFields.get(index - 1).getKeyField().setValue(value);
        return this;
    }

    public ForecastJobsEditPage setTagValue(String value, int index) {
        setNeededNumberTags(index);
        tagValueFields.get(index - 1).getValueField().setValue(value);
        return this;
    }

    public List<Tag> getTagValueFields() {
        isInitializeTagValueFields();
        return tagValueFields;
    }

    public ForecastJobsEditPage clickAddTagButton() {
        isInitializeTagValueFields();
        $(addTagButton).click();

        tagValueFields.add(new Tag(tagValueFields.size()));
        return this;
    }

    public ForecastJobsEditPage setSeriesSelectionIntervalCount(String value) {
        $(seriesSelectionIntervalCountField).setValue(value);
        return this;
    }

    public ForecastJobsEditPage setSeriesSelectionIntervalUnit(String value) {
        $(seriesSelectionIntervalUnitField).selectOption(value);
        return this;
    }

    public ExportPage clickExportSelectLink() {
        $(exportSelectLink).click();
        return new ExportPage();
    }

    public ForecastJobsEditPage setSampleFilter(String value) {
        $(sampleFilterField).setValue(value);
        return this;
    }

    public SelenideElement getSampleFilter() {
        return $(sampleFilterField);
    }

    public ForecastJobsEditPage setGroupBy(String value) {
        $(groupByField).selectOption(value);
        return this;
    }

    public SelenideElement getGroupBy() {
        return $(groupByField);
    }

    public ForecastJobsEditPage setRequiredTagKeys(String value) {
        $(requiredTagKeysField).setValue(value);
        return this;
    }

    public SelenideElement getRequiredTagKeys() {
        return $(requiredTagKeysField).shouldBe(Condition.visible);
    }

    public ForecastJobsEditPage clickAutoAggregateCheckBox() {
        $(autoAggregateCheckBox).click();
        return this;
    }

    public boolean isSelectedAutoAggregate() {
        return $(autoAggregateCheckBox).isSelected();
    }

    public ForecastJobsEditPage setAggregationPeriodCount(String value) {
        $(aggregationPeriodCountField).setValue(value);
        return this;
    }

    public SelenideElement getAggregationPeriodCount() {
        return $(aggregationPeriodCountField);
    }

    public ForecastJobsEditPage setAggregationPeriodUnit(String value) {
        $(aggregationPeriodUnitField).selectOption(value);
        return this;
    }

    public SelenideElement getAggregationPeriodUnit() {
        return $(aggregationPeriodUnitField);
    }

    public ForecastJobsEditPage setAggregateStatistic(String value) {
        $(aggregateStatisticField).selectOption(value);
        return this;
    }

    public SelenideElement getAggregateStatistic() {
        return $(aggregateStatisticField);
    }

    public ForecastJobsEditPage setAlgorithm(String value) {
        $(algorithmField).selectOption(value);
        return this;
    }

    public ForecastJobsEditPage setScoreIntervalCount(String value) {
        $(scoreIntervalCountField).setValue(value);
        return this;
    }

    public SelenideElement getScoreIntervalCount() {
        return $(scoreIntervalCountField);
    }

    public ForecastJobsEditPage setScoreIntervalUnit(String value) {
        $(scoreIntervalUnitField).selectOption(value);
        return this;
    }

    public SelenideElement getScoreIntervalUnit() {
        return $(scoreIntervalUnitField);
    }

    public ForecastJobsEditPage setForecastName(String value) {
        $(forecastNameField).setValue(value);
        return this;
    }

    public SelenideElement getForecastName() {
        return $(forecastNameField);
    }

    public ForecastJobsEditPage setForecastHorizonCount(String value) {
        $(forecastHorizonCountField).setValue(value);
        return this;
    }

    public SelenideElement getForecastHorizonCount() {
        return $(forecastHorizonCountField);
    }

    public ForecastJobsEditPage setForecastHorizonUnit(String value) {
        $(forecastHorizonUnitField).selectOption(value);
        return this;
    }

    public SelenideElement getForecastHorizonUnit() {
        return $(forecastHorizonUnitField);
    }

    public ForecastJobsEditPage setStoreUnderMetric(String value) {
        $(storeUnderMetricField).setValue(value);
        return this;
    }

    public SelenideElement getStoreUnderMetric() {
        return $(storeUnderMetricField);
    }

    public ExportPage clickExportStoreLink() {
        $(exportStoreLink).click();
        return new ExportPage();
    }

    public ForecastJobsEditPage clickSaveButton() {
        $(saveButton).click();
        return new ForecastJobsEditPage();
    }

    public ForecastJobsEditPage clickRunButton() {
        $(runButton).click();
        return new ForecastJobsEditPage();
    }

    public ElementsCollection getForecastTable() {
        return $$(forecastsTable);
    }

    public PortalViewPage clickViewStoredForecast() {
        $(viewStoredForecastLink).click();
        return new PortalViewPage();
    }

    public ForecastViewerPage clickOpenForecastViewer() {
        $(openForecastViewerLink).click();
        return new ForecastViewerPage();
    }

    private void setNeededNumberTags(int index) {
        isInitializeTagValueFields();

        while (tagValueFields.size() < index) {
            clickAddTagButton();
        }
    }

    private void isInitializeTagValueFields() {
        if (tagValueFields == null) {
            tagValueFields = new ArrayList<>();
            for (int i = 0; i < $$(tagPairs).size(); i++) {
                tagValueFields.add(new Tag(i));
            }
        }
    }
}
