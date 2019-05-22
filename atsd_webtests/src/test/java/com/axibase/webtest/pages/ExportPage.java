package com.axibase.webtest.pages;

import com.axibase.webtest.ElementUtils;
import com.axibase.webtest.KeyValueForm;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ExportPage {
    private WebDriver driver;

    private By metricField = By.id("metric");
    private By entityNameField = By.id("entityFilterValue");
    private By tagPairs = By.xpath("//div[@class='tag-entry input-prepend input-append']");
    private List<Tag> tagValueFields;
    private By startDateField = By.id("startTime");
    private By intervalCountField = By.id("selectionInterval.intervalCount");
    private By intervalUnitField = By.id("selectionInterval.intervalUnit");
    private By endDateField = By.id("endTime");

    private By dataTypeRadio = By.xpath("//input[@name='exportType' and @checked='checked']");
    private By forecastName = By.id("forecastName");

    private By filterCheckBox = By.id("useFilter");
    private By valueFilterField = By.id("valueFilter");

    private By aggregateCheckbox = By.id("aggregate");
    private By aggregationChosenField = By.id("aggregations");
    private By aggregationPeriodCountField = By.id("aggregateInterval.intervalCount");
    private By aggregationPeriodUnitField = By.id("aggregateInterval.intervalUnit");
    private By interpolationField = By.id("interpolation.type");

    public ExportPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getMetric() {
        return driver.findElement(metricField).getAttribute("value");
    }

    public String getEntityName() {
        return driver.findElement(entityNameField).getAttribute("value");
    }

    public boolean isEnabledEntityNameField() {
        return driver.findElement(entityNameField).isEnabled();
    }

    public List<Tag> getTagPairs() {
        isInitializeTagValueFields();
        return tagValueFields;
//        List<Map<String, String>> result = new ArrayList<>();
//        for (Tag tag: tagValueFields) {
//            Map<String, String> tagPair = new HashMap<>();
//            String key = driver.findElement(tag.getKeyField()).getAttribute("value");
//            if (key == null) {
//                key = "";
//            }
//            String value = driver.findElement(tag.getValueField()).getAttribute("value");
//            if (value == null) {
//                value = "";
//            }
//            tagPair.put(key, value);
//            result.add(tagPair);
//        }
//        return result;
    }

    public String getStartDate() {
        return driver.findElement(startDateField).getAttribute("value");
    }

    public String getIntervalCount() {
        return driver.findElement(intervalCountField).getAttribute("value");
    }

    public String getIntervalUnit() {
        return ElementUtils.getSelectedOption(driver.findElement(intervalUnitField));
    }

    public String getEndDate() {
        return driver.findElement(endDateField).getAttribute("value");
    }

    public String getDataType() {
        return driver.findElement(dataTypeRadio).getAttribute("value");
    }

    public String getForecastName() {
        return driver.findElement(forecastName).getAttribute("value");
    }

    public boolean isSelectedFilter() {
        return driver.findElement(filterCheckBox).isSelected();
    }

    public String getValueFilter() {
        return driver.findElement(valueFilterField).getAttribute("value");
    }

    public boolean isSelectedAggregate() {
        return driver.findElement(aggregateCheckbox).isSelected();
    }

    public List<String> getAggregationChosen() {
        List<String> listChosen = new ArrayList<>();
        for (WebElement chosen: driver.findElements(aggregationChosenField)) {
            listChosen.add(ElementUtils.getSelectedOption(driver.findElement(aggregationChosenField)));
        }
        return listChosen;
    }

    public String getAggregationPeriodCount() {
        return driver.findElement(aggregationPeriodCountField).getAttribute("value");
    }

    public String getAggregationPeriodUnit() {
        return ElementUtils.getSelectedOption(driver.findElement(aggregationPeriodUnitField));
    }

    public String getInterpolation() {
        return ElementUtils.getSelectedOption(driver.findElement(interpolationField));
    }

    private void isInitializeTagValueFields() {
        if (tagValueFields == null) {
            tagValueFields = new ArrayList<>();
            for (int i = 0; i < driver.findElements(tagPairs).size(); i++) {
                tagValueFields.add(new Tag(i));
            }
        }
    }

    private class Tag extends KeyValueForm {

        Tag(int id) {
            setKeyField("//input[@name='tags[" + id + "].key']");
            setValueField("//input[@name='tags[" + id + "].value']");
        }

        @Override
        public void setKeyField(String inspector) {
            this.keyField = driver.findElement(By.xpath(inspector));
        }

        @Override
        public void setValueField(String inspector) {
            this.valueField = driver.findElement(By.xpath(inspector));
        }
    }
}
