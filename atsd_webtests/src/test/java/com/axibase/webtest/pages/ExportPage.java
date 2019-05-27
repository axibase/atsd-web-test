package com.axibase.webtest.pages;

import com.axibase.webtest.KeyValueForm;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ExportPage {
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
    private By aggregationChosenField = By.xpath("//*[@id='aggregations_chosen']/ul//span");
    private By aggregationPeriodCountField = By.id("aggregateInterval.intervalCount");
    private By aggregationPeriodUnitField = By.id("aggregateInterval.intervalUnit");
    private By interpolationField = By.id("interpolation.type");

    private class Tag extends KeyValueForm {
        Tag(int id) {
            setKeyField("//input[@name='tags[" + id + "].key']");
            setValueField("//input[@name='tags[" + id + "].value']");
        }

        @Override
        public void setKeyField(String inspector) {
            this.keyField = $(By.xpath(inspector));
        }

        @Override
        public void setValueField(String inspector) {
            this.valueField = $(By.xpath(inspector));
        }
    }

    public SelenideElement getMetric() {
        return $(metricField);
    }

    public SelenideElement getEntityName() {
        return $(entityNameField);
    }

    public boolean isEnabledEntityNameField() {
        return $(entityNameField).isEnabled();
    }

    public List<Tag> getTagPairs() {
        isInitializeTagValueFields();
        return tagValueFields;
    }

    public SelenideElement getStartDate() {
        return $(startDateField);
    }

    public SelenideElement getIntervalCount() {
        return $(intervalCountField);
    }

    public SelenideElement getIntervalUnit() {
        return $(intervalUnitField);
    }

    public SelenideElement getEndDate() {
        return $(endDateField);
    }

    public SelenideElement getDataType() {
        return $(dataTypeRadio);
    }

    public SelenideElement getForecastName() {
        return $(forecastName);
    }

    public boolean isSelectedFilter() {
        return $(filterCheckBox).isSelected();
    }

    public SelenideElement getValueFilter() {
        return $(valueFilterField);
    }

    public boolean isSelectedAggregate() {
        return $(aggregateCheckbox).isSelected();
    }

    public ElementsCollection getAggregationChosen() {
        return $$(aggregationChosenField);
    }

    public SelenideElement getAggregationPeriodCount() {
        return $(aggregationPeriodCountField);
    }

    public SelenideElement getAggregationPeriodUnit() {
        return $(aggregationPeriodUnitField);
    }

    public SelenideElement getInterpolation() {
        return $(interpolationField);
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
