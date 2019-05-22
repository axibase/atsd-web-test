package com.axibase.webtest.pages;

import com.axibase.webtest.ElementUtils;
import com.axibase.webtest.KeyValueForm;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

public class ForecastJobsEditPage {
    private WebDriver driver;

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

    private By forecastNameField = By.id("settings.name");
    private By forecastHorizonCountField = By.id("settings.storeInterval.intervalCount");
    private By forecastHorizonUnitField = By.id("settings.storeInterval.intervalUnit");

    private By storeUnderMetricField = By.id("settings.producedMetric");
    private By exportStoreLink = By.xpath("//button[@onclick='toPortalEditor();return false']/preceding-sibling::a");

    private By saveButton = By.xpath("//input[@name='save']");
    private By runButton = By.xpath("//input[@name='forecast']");

    private By forecastsTable = By.id("seriesList");

    public ForecastJobsEditPage(WebDriver driver) {
        this.driver = driver;
    }

    public ForecastJobsEditPage setStartDate(String value) {
        return setStringAndNumericValue(value, driver.findElement(startDateField));
    }

    public String getStartDate() {
        return driver.findElement(startDateField).getAttribute("value");
    }

    public ForecastJobsEditPage setIntervalCount(String value) {
        return setStringAndNumericValue(value, driver.findElement(intervalCountField));
    }

    public String getIntervalCount() {
        return driver.findElement(intervalCountField).getAttribute("value");
    }

    public ForecastJobsEditPage setIntervalUnit(String value) {
        return setSelectionValue(value, driver.findElement(intervalUnitField));
    }

    public String getIntervalUnit() {
        return ElementUtils.getSelectedOption(driver.findElement(intervalUnitField));
    }

    public ForecastJobsEditPage setEndDate(String value) {
        return setStringAndNumericValue(value, driver.findElement(endDateField));
    }

    public String getEndDate() {
        return driver.findElement(endDateField).getAttribute("value");
    }

    public ForecastJobsEditPage setMetric(String value) {
        return setStringAndNumericValue(value, driver.findElement(metricField));
    }

    public String getMetric() {
        return driver.findElement(metricField).getAttribute("value");
    }

    public List<String> getErrorMetic() {
        List<String> result = new ArrayList<>();
        for (WebElement element: driver.findElements(errorMetric)) {
            result.add(element.getText());
        }

        return result;
    }

    public ForecastJobsEditPage setEntity(String value) {
        return setStringAndNumericValue(value, driver.findElement(entityField));
    }

    public String getEntity() {
        return driver.findElement(entityField).getAttribute("value");
    }

    public ForecastJobsEditPage setTagKey(String value, int index) {
        setNeededNumberTags(index);
        return setStringAndNumericValue(value, tagValueFields.get(index - 1).getKeyField());
    }

    public ForecastJobsEditPage setTagValue(String value, int index) {
        setNeededNumberTags(index);
        return setStringAndNumericValue(value, tagValueFields.get(index - 1).getValueField());
    }

    public List<Tag> getTagValueFields() {
        isInitializeTagValueFields();
        return tagValueFields;
//        List<Map<String, String>> result = new ArrayList<>();
//        for (Tag tag: tagValueFields) {
//            Map<String, String> tagPair = new HashMap<>();
//            String key = tag.getKeyField().getAttribute("value");
//            String value = tag.getValueField().getAttribute("value");
//            tagPair.put(key, value);
//            result.add(tagPair);
//        }
//        return result;
    }

    public ForecastJobsEditPage clickAddTagButton() {
        isInitializeTagValueFields();
        driver.findElement(addTagButton).click();

        tagValueFields.add(new Tag(tagValueFields.size()));
        return this;
    }

    public ForecastJobsEditPage setSeriesSelectionIntervalCount(String value) {
        return setStringAndNumericValue(value, driver.findElement(seriesSelectionIntervalCountField));
    }

    public ForecastJobsEditPage setSeriesSelectionIntervalUnit(String value) {
        return setSelectionValue(value, driver.findElement(seriesSelectionIntervalUnitField));
    }

    public ExportPage clickExportSelectLink() {
        driver.findElement(exportSelectLink).click();
        return new ExportPage(driver);
    }

    public ForecastJobsEditPage setSampleFilter(String value) {
        return setStringAndNumericValue(value, driver.findElement(sampleFilterField));
    }

    public String getSampleFilter() {
        return driver.findElement(sampleFilterField).getAttribute("value");
    }

    public ForecastJobsEditPage setGroupBy(String value) {
        return setSelectionValue(value, driver.findElement(groupByField));
    }

    public ForecastJobsEditPage setRequiredTagKeys(String value) {
        return setStringAndNumericValue(value, driver.findElement(requiredTagKeysField));
    }

    public ForecastJobsEditPage clickAutoAggregateCheckBox() {
        driver.findElement(autoAggregateCheckBox).click();
        return this;
    }

    public boolean isSelectedAutoAggregate() {
        return driver.findElement(autoAggregateCheckBox).isSelected();
    }

    public String getAggregationPeriodCount() {
        return driver.findElement(aggregationPeriodCountField).getAttribute("value");
    }

    public ForecastJobsEditPage setAggregationPeriodCount(String value) {
        return setStringAndNumericValue(value, driver.findElement(aggregationPeriodCountField));
    }

    public String getAggregationPeriodUnit() {
        return ElementUtils.getSelectedOption(driver.findElement(aggregationPeriodUnitField));
    }

    public ForecastJobsEditPage setAggregationPeriodUnit(String value) {
        return setSelectionValue(value, driver.findElement(aggregationPeriodUnitField));
    }

    public String getAggregateStatistic() {
        return ElementUtils.getSelectedOption(driver.findElement(aggregateStatisticField));
    }

    public ForecastJobsEditPage setAggregateStatistic(String value) {
        return setSelectionValue(value, driver.findElement(aggregateStatisticField));
    }

    public ForecastJobsEditPage setAlgorithm(String value) {
        return setSelectionValue(value, driver.findElement(algorithmField));
    }

    public ForecastJobsEditPage setForecastName(String value) {
        return setStringAndNumericValue(value, driver.findElement(forecastNameField));
    }

    public String getForecastName() {
        return driver.findElement(forecastNameField).getAttribute("value");
    }

    public ForecastJobsEditPage setForecastHorizonCountField(String value) {
        return setStringAndNumericValue(value, driver.findElement(forecastHorizonCountField));
    }

    public String getForecastHorizonCountField() {
        return driver.findElement(forecastHorizonCountField).getAttribute("value");
    }

    public ForecastJobsEditPage setForecastHorizonUnitField(String value) {
        return setStringAndNumericValue(value, driver.findElement(forecastHorizonUnitField));
    }

    public String getForecastHorizonUnitField() {
        return  ElementUtils.getSelectedOption(driver.findElement(forecastHorizonUnitField));
    }

    public ForecastJobsEditPage setStoreUnderMetric(String value) {
        return setStringAndNumericValue(value, driver.findElement(storeUnderMetricField));
    }

    public String getStoreUnderMetric() {
        return driver.findElement(storeUnderMetricField).getAttribute("value");
    }

    public ExportPage clickExportStoreLink() {
        driver.findElement(exportStoreLink).click();
        return new ExportPage(driver);
    }

    public ForecastJobsEditPage clickSaveButton() {
        driver.findElement(saveButton).click();
        return new ForecastJobsEditPage(driver);
    }

    public ForecastJobsEditPage clickRunButton() {
        driver.findElement(runButton).click();
        return new ForecastJobsEditPage(driver);
    }

    public Set<List<String>> getListForecasts() {
        Set<List<String>> result = new HashSet<>();

        if (driver.findElements(forecastsTable).isEmpty()) {
            return result;
        }

        List<WebElement> rows = driver.findElement(forecastsTable).findElements(By.xpath("./tbody/tr"));
        for (WebElement row: rows) {
            List<String> listValueOfCells = new ArrayList<>();
            List<WebElement> elements = row.findElements(By.xpath("./td"));
            for (WebElement element: elements) {
                listValueOfCells.add(element.getText());
            }
            result.add(listValueOfCells);
        }
        return result;
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
            for (int i = 0; i < driver.findElements(tagPairs).size(); i++) {
                tagValueFields.add(new Tag(i));
            }
        }
    }

    private ForecastJobsEditPage setStringAndNumericValue(String value, WebElement element) {
        element.clear();
        element.sendKeys(value);
        return this;
    }

    private ForecastJobsEditPage setSelectionValue(String value, WebElement element) {
        Select select = new Select(element);
        select.selectByValue(value);
        return this;
    }

    private class Tag extends KeyValueForm {

        Tag(int id) {
            setKeyField("tags" + id + ".key");
            setValueField("tags" + id + ".value");
        }

        @Override
        public void setKeyField(String inspector) {
            this.keyField = driver.findElement(By.id(inspector));
        }

        @Override
        public void setValueField(String inspector) {
            this.valueField = driver.findElement(By.id(inspector));
        }
    }
}
