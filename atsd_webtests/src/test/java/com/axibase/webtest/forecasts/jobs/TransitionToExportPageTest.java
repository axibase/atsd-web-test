package com.axibase.webtest.forecasts.jobs;

import com.axibase.webtest.ElementUtils;
import com.axibase.webtest.KeyValueForm;
import com.axibase.webtest.pages.ExportPage;
import com.axibase.webtest.pages.ForecastJobsEditPage;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Issue;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertEquals;

public class TransitionToExportPageTest extends AtsdTest {
    private ForecastJobsEditPage forecastJobsEditPage;
    private ExportPage exportPage;
    private static final String URL_FORECAST_JOBS = "/forecast/settings/edit.xhtml";
    private static final String NOW = "now";
    private static final String METRIC = "metric-forecast-jobs";
    private static final String ENTITY = "entity-forecast-jobs-1";
    private static final String START_DATE = "2019-04-30T00:00:00.000Z";
    private static final String END_DATE = "2019-05-07T00:00:00.000Z";
    private static final String EMPTY_STR = "";
    private static final String FORECAST_NAME = "Name of forecast";
    private static final String STORED_METRIC = "new-metric";

    private enum PlaceOfCall {
        HISTORY,
        FORECAST
    }

    @Data
    @RequiredArgsConstructor
    private class Tag {
        private final String key;
        private final String value;
    }

    @Data
    @RequiredArgsConstructor
    private class SelectForm {
        private final String metric;
        private final boolean enabledEntityName;
        private final String entityName;
        private final List<Tag> tagPairs;
        private final String startDate;
        private final String intervalCount;
        private final String intervalUnit;
        private final String endDate;
        private final String dateType;
    }

    @Data
    @RequiredArgsConstructor
    private class AggregateForm {
        private final List<String> aggregationChosenField;
        private final String aggregationIntervalCountField;
        private final String aggregationIntervalUnitField;
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        forecastJobsEditPage = new ForecastJobsEditPage();
        open(URL_FORECAST_JOBS);
    }

    @Issue("6236")
    @Test
    public void testSelectExportDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportType() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        ElementUtils.goToNextWindow();

        exportPage.getForecastName().shouldBe(Condition.not(Condition.visible));
        assertEquals("Mismatch of data", exportPage.getDataType().getValue(), PlaceOfCall.HISTORY.toString());
    }

    @Issue("6236")
    @Test
    public void testSelectExportMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportEntity() {
        forecastJobsEditPage.setEntity(ENTITY);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportStartDate() {
        forecastJobsEditPage.setStartDate(START_DATE);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportEndDate() {
        forecastJobsEditPage.setEndDate(END_DATE);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportTag1Value1() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 1);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 1);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportTag1Value1InThirdRows() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 3);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 3);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportTag1Value1Tag2Value2() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 1);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 1);
        forecastJobsEditPage.setTagKey("something-tag-name-2", 2);
        forecastJobsEditPage.setTagValue("something-tag-value-2", 2);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportIntervalCount() {
        forecastJobsEditPage.setIntervalCount("2");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportIntervalUnit() {
        forecastJobsEditPage.setIntervalUnit("Week");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testSelectExportAllFields() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEntity(ENTITY);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setEndDate(END_DATE);
        forecastJobsEditPage.setTagKey("something-tag-name-1", 1);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 1);
        forecastJobsEditPage.setTagKey("something-tag-name-2", 2);
        forecastJobsEditPage.setTagValue("something-tag-value-2", 2);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAggregateDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        ElementUtils.goToNextWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAggregationPeriodCount() {
        forecastJobsEditPage.setAggregationPeriodCount("3");
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        ElementUtils.goToNextWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAggregationPeriodUnit() {
        forecastJobsEditPage.setAggregationPeriodUnit("Month");
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        ElementUtils.goToNextWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAggregateStatistic() {
        forecastJobsEditPage.setAggregateStatistic("Max");
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        ElementUtils.goToNextWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAggregateAllFields() {
        forecastJobsEditPage.setAggregationPeriodCount("4");
        forecastJobsEditPage.setAggregationPeriodUnit("Day");
        forecastJobsEditPage.setAggregateStatistic("Count");
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        ElementUtils.goToNextWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testAutoAggregate() {
        if (!forecastJobsEditPage.isSelectedAutoAggregate()) {
            forecastJobsEditPage.clickAutoAggregateCheckBox();
        }
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        ElementUtils.goToNextWindow();
        exportPage.getAggregateCheckBox().shouldBe(Condition.not(Condition.selected));
    }

    @Issue("6236")
    @Test
    public void testFilterDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        ElementUtils.goToNextWindow();

        exportPage.getFilterCheckBox().shouldBe(Condition.not(Condition.selected));
    }

    @Issue("6236")
    @Test
    public void testSampleFilter() {
        forecastJobsEditPage.setSampleFilter("value >= 50");
        executeJavaScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        String expectedParameter = forecastJobsEditPage.getSampleFilter().getValue();
        ElementUtils.goToNextWindow();
        exportPage.getFilterCheckBox().shouldBe(Condition.selected);
        String actualParameter = exportPage.getValueFilter().getValue();

        assertEquals("Mismatch of data", expectedParameter, actualParameter);
    }

    @Issue("6236")
    @Test
    public void testStoreDefault() {
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        forecastJobsEditPage.isPresentErrorMetric();
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricEntity() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEntity(ENTITY);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricStartDate() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = new SelectForm(
                METRIC,
                false,
                EMPTY_STR,
                new ArrayList<>(Arrays.asList(
                        new Tag(EMPTY_STR, EMPTY_STR))),
                "2019-05-30T00:00:00.000Z",
                forecastJobsEditPage.getForecastHorizonCount().getValue(),
                forecastJobsEditPage.getForecastHorizonUnit().getSelectedText(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString());
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricStartDateIntervalCount() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setIntervalCount("2");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = new SelectForm(
                METRIC,
                false,
                EMPTY_STR,
                new ArrayList<>(Arrays.asList(
                        new Tag(EMPTY_STR, EMPTY_STR))),
                "2019-06-30T00:00:00.000Z",
                forecastJobsEditPage.getForecastHorizonCount().getValue(),
                forecastJobsEditPage.getForecastHorizonUnit().getSelectedText(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString());
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricStartDateIntervalUnit() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setIntervalUnit("Week");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = new SelectForm(
                METRIC,
                false,
                EMPTY_STR,
                new ArrayList<>(Arrays.asList(
                        new Tag(EMPTY_STR, EMPTY_STR))),
                "2019-05-07T00:00:00.000Z",
                forecastJobsEditPage.getForecastHorizonCount().getValue(),
                forecastJobsEditPage.getForecastHorizonUnit().getSelectedText(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString());
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricEndDate() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEndDate(END_DATE);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricTag1Value1() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricTag1Value1Tag2Value2() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setTagKey("tag-name-2", 2);
        forecastJobsEditPage.setTagValue("tag-value-2", 2);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricTag1Value1InThirdRow() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setTagKey("tag-name-1", 3);
        forecastJobsEditPage.setTagValue("tag-value-1", 3);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastNameIsEmpty() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        ElementUtils.goToNextWindow();
        SelenideElement forecastName = exportPage.getForecastName();

        forecastName.shouldBe(Condition.visible);
        assertEquals("Mismatch of data", forecastName.getValue(), EMPTY_STR);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastNameIsEmptyDataType() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        ElementUtils.goToNextWindow();

        exportPage.getForecastName().shouldBe(Condition.visible);
        assertEquals("Mismatch of data", exportPage.getDataType().getValue(), PlaceOfCall.FORECAST.toString());
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastNameIsEmptyIsSelectedAggregate() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        ElementUtils.goToNextWindow();

        exportPage.getAggregateCheckBox().shouldBe(Condition.not(Condition.selected));
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastName() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        ElementUtils.goToNextWindow();
        SelenideElement forecastName = exportPage.getForecastName();

        forecastName.shouldBe(Condition.visible);
        assertEquals("Mismatch of data", forecastName.getValue(), FORECAST_NAME);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricStoreUnderMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastHorizonCount() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setForecastHorizonCount("5");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportMetricForecastHorizonUnit() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setForecastHorizonUnit("Week");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testStoreExportAllFields() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEntity(ENTITY);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setIntervalCount("3");
        forecastJobsEditPage.setIntervalUnit("Day");
        forecastJobsEditPage.setEndDate(END_DATE);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setTagKey("tag-name-2", 2);
        forecastJobsEditPage.setTagValue("tag-value-2", 2);
        forecastJobsEditPage.setForecastHorizonCount("4");
        forecastJobsEditPage.setForecastHorizonUnit("Minute");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        ElementUtils.goToNextWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    private SelectForm getCheckedParametersFromForecastJobsEditPage(PlaceOfCall placeOfCall) {
        String entity = forecastJobsEditPage.getEntity().getValue();
        String metric;
        String startDate = forecastJobsEditPage.getStartDate().getValue();
        String endDate = forecastJobsEditPage.getEndDate().getValue();
        String dateType;
        String intervalCount;
        String intervalUnit;
        List<Tag> tagPairs = new ArrayList<>();

        boolean enabledEntityName = false;
        if (!entity.isEmpty()) {
            enabledEntityName = true;
        }

        for (KeyValueForm keyValueForm: forecastJobsEditPage.getTagValueFields()) {
            String key = keyValueForm.getKey();
            String value = keyValueForm.getValue();
            if (!key.isEmpty()) {
                tagPairs.add(new Tag(key, value));
            }
        }
        tagPairs.add(new Tag(EMPTY_STR, EMPTY_STR));

        switch (placeOfCall) {
            case FORECAST: dateType = PlaceOfCall.FORECAST.toString();
                metric = forecastJobsEditPage.getStoreUnderMetric().getValue();
                if (metric.isEmpty()) {
                    metric = forecastJobsEditPage.getMetric().getValue();
                } else {
                    dateType = PlaceOfCall.HISTORY.toString();
                }
                intervalCount = forecastJobsEditPage.getForecastHorizonCount().getValue();
                intervalUnit = forecastJobsEditPage.getForecastHorizonUnit().getSelectedText();

                if (endDate.isEmpty()) {
                    if (startDate.isEmpty()) {
                        startDate = NOW;
                    }
                } else {
                    startDate = endDate;
                    endDate = EMPTY_STR;
                }

                break;
            case HISTORY: dateType = PlaceOfCall.HISTORY.toString();
                metric = forecastJobsEditPage.getMetric().getValue();
                intervalCount = forecastJobsEditPage.getIntervalCount().getValue();
                intervalUnit = forecastJobsEditPage.getIntervalUnit().getSelectedText();


                if (startDate.isEmpty() && endDate.isEmpty()) {
                    endDate = NOW;
                }

                break;
             default: throw new IllegalStateException("Non-existing method parameter");
        }

        return new SelectForm(
                metric,
                enabledEntityName,
                entity,
                tagPairs,
                startDate,
                intervalCount,
                intervalUnit,
                endDate,
                dateType);
    }

    private SelectForm getCheckedParametersFromExportPage() {
        List<Tag> tagPairs = new ArrayList<>();
        for (KeyValueForm keyValueForm: exportPage.getTagPairs()) {
            tagPairs.add(new Tag(keyValueForm.getKey(), keyValueForm.getValue()));
        }
        return new SelectForm(
                exportPage.getMetric().getValue(),
                exportPage.isEnabledEntityNameField(),
                exportPage.getEntityName().getValue(),
                tagPairs,
                exportPage.getStartDate().getValue(),
                exportPage.getIntervalCount().getValue(),
                exportPage.getIntervalUnit().getSelectedText(),
                exportPage.getEndDate().getValue(),
                exportPage.getDataType().getValue());
    }

    private AggregateForm getAggregateFormOfForecastJobsEditPage() {
        List<String> listAggregationChosen = Arrays.asList(forecastJobsEditPage.getAggregateStatistic().getSelectedText());
        return new AggregateForm(
                listAggregationChosen,
                forecastJobsEditPage.getAggregationPeriodCount().getValue(),
                forecastJobsEditPage.getAggregationPeriodUnit().getSelectedText());
    }

    private AggregateForm getAggregateFormOfExportPage() {
        exportPage.getAggregateCheckBox().shouldBe(Condition.selected);
        return new AggregateForm(
                exportPage.getAggregationChosen().texts(),
                exportPage.getAggregationPeriodCount().getValue(),
                exportPage.getAggregationPeriodUnit().getSelectedText());
    }
}
