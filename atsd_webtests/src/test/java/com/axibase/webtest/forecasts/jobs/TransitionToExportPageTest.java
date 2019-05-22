package com.axibase.webtest.forecasts.jobs;

import com.axibase.webtest.KeyValueForm;
import com.axibase.webtest.pages.ExportPage;
import com.axibase.webtest.pages.ForecastJobsEditPage;
import com.axibase.webtest.service.AtsdTest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransitionToExportPageTest extends AtsdTest {
    private ForecastJobsEditPage forecastJobsEditPage;
    private ExportPage exportPage;
    private static final String URL_FORECAST_JOBS = url + "/forecast/settings/edit.xhtml";
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
        private final boolean aggregateCheckBox;
        private final List<String> aggregationChosenField;
        private final String aggregationIntervalCountField;
        private final String aggregationIntervalUnitField;
    }

    @Before
    public void setUp() throws Exception{
        this.login();
        forecastJobsEditPage = new ForecastJobsEditPage(driver);
        driver.get(URL_FORECAST_JOBS);
    }


    @Test
    public void testSelectExportDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportEntity() {
        forecastJobsEditPage.setEntity(ENTITY);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportStartDate() {
        forecastJobsEditPage.setStartDate(START_DATE);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportEndDate() {
        forecastJobsEditPage.setEndDate(END_DATE);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportTag1Value1() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 1);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 1);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportTag1Value1InThirdRows() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 3);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 3);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportTag1Value1Tag2Value2() {
        forecastJobsEditPage.setTagKey("something-tag-name-1", 1);
        forecastJobsEditPage.setTagValue("something-tag-value-1", 1);
        forecastJobsEditPage.setTagKey("something-tag-name-2", 2);
        forecastJobsEditPage.setTagValue("something-tag-value-2", 2);
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportIntervalCount() {
        forecastJobsEditPage.setIntervalCount("2");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testSelectExportIntervalUnit() {
        forecastJobsEditPage.setIntervalUnit("WEEK");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.HISTORY);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

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
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAggregateDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        goToExportWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAggregationPeriodCount() {
        forecastJobsEditPage.setAggregationPeriodCount("3");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        goToExportWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAggregationPeriodUnit() {
        forecastJobsEditPage.setAggregationPeriodUnit("MONTH");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        goToExportWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAggregateStatistic() {
        forecastJobsEditPage.setAggregateStatistic("MAX");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        goToExportWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAggregateAllFields() {
        forecastJobsEditPage.setAggregationPeriodCount("4");
        forecastJobsEditPage.setAggregationPeriodUnit("DAY");
        forecastJobsEditPage.setAggregateStatistic("COUNT");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        AggregateForm expectedParameters = getAggregateFormOfForecastJobsEditPage();
        goToExportWindow();
        AggregateForm actualParameters = getAggregateFormOfExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testAutoAggregate() {
        if (!forecastJobsEditPage.isSelectedAutoAggregate()) {
            forecastJobsEditPage.clickAutoAggregateCheckBox();
        }
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        goToExportWindow();

        assertFalse("Mismatch of data", exportPage.isSelectedAggregate());
    }

    @Test
    public void testFilterDefault() {
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        goToExportWindow();

        assertFalse("Mismatch of data", exportPage.isSelectedFilter());
    }

    @Test
    public void testFilterIsSelected() {
        forecastJobsEditPage.setSampleFilter("value < 40");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        goToExportWindow();

        assertTrue("Mismatch of data", exportPage.isSelectedFilter());
    }

    @Test
    public void testSampleFilter() {
        forecastJobsEditPage.setSampleFilter("value >= 50");
        ((JavascriptExecutor)driver).executeScript("scroll(0,-1000);");
        exportPage = forecastJobsEditPage.clickExportSelectLink();

        String expectedParameter = forecastJobsEditPage.getSampleFilter();
        goToExportWindow();
        String actualParameter = exportPage.getValueFilter();

        assertEquals("Mismatch of data", expectedParameter, actualParameter);
    }

    @Test
    public void testStoreDefault() {
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        assertEquals("Mismatch of data", 1, forecastJobsEditPage.getErrorMetic().size());
    }

    @Test
    public void testStoreExportMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricEntity() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEntity(ENTITY);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

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
                forecastJobsEditPage.getForecastHorizonCountField(),
                forecastJobsEditPage.getForecastHorizonUnitField(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString()
        );
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

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
                forecastJobsEditPage.getForecastHorizonCountField(),
                forecastJobsEditPage.getForecastHorizonUnitField(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString()
        );
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricStartDateIntervalUnit() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setIntervalUnit("WEEK");
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = new SelectForm(
                METRIC,
                false,
                EMPTY_STR,
                new ArrayList<>(Arrays.asList(
                        new Tag(EMPTY_STR, EMPTY_STR))),
                "2019-05-07T00:00:00.000Z",
                forecastJobsEditPage.getForecastHorizonCountField(),
                forecastJobsEditPage.getForecastHorizonUnitField(),
                EMPTY_STR,
                PlaceOfCall.FORECAST.toString()
        );
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricEndDate() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEndDate(END_DATE);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricTag1Value1() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

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
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricTag1Value1InThirdRow() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setTagKey("tag-name-1", 3);
        forecastJobsEditPage.setTagValue("tag-value-1", 3);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportAllFields() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setEntity(ENTITY);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setIntervalCount("3");
        forecastJobsEditPage.setIntervalUnit("DAY");
        forecastJobsEditPage.setEndDate(END_DATE);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setTagKey("tag-name-2", 2);
        forecastJobsEditPage.setTagValue("tag-value-2", 2);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Test
    public void testStoreExportMetricForecastNameIsEmpty() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        goToExportWindow();

        assertEquals("Mismatch of data", exportPage.getForecastName(), EMPTY_STR);
    }

    @Test
    public void testStoreExportMetricForecastNameIsEmptyDataType() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        goToExportWindow();

        assertEquals("Mismatch of data", exportPage.getDataType(), PlaceOfCall.FORECAST.toString());
    }

    @Test
    public void testStoreExportMetricForecastNameIsEmptyIsSelectedAggregate() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        goToExportWindow();

        assertFalse("Mismatch of data", exportPage.isSelectedAggregate());
    }

    @Test
    public void testStoreExportMetricForecastName() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        goToExportWindow();

        assertEquals("Mismatch of data", exportPage.getForecastName(), FORECAST_NAME);
    }

    @Test
    public void testStoreExportMetricStoreUnderMetric() {
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickSaveButton();
        exportPage = forecastJobsEditPage.clickExportStoreLink();

        SelectForm expectedParameters = getCheckedParametersFromForecastJobsEditPage(PlaceOfCall.FORECAST);
        goToExportWindow();
        SelectForm actualParameters = getCheckedParametersFromExportPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    private void goToExportWindow() {
        driver.close();
        for (String windowName: driver.getWindowHandles()) {
            driver.switchTo().window(windowName);
        }
    }

    private SelectForm getCheckedParametersFromForecastJobsEditPage(PlaceOfCall placeOfCall) {
        String entity = forecastJobsEditPage.getEntity();

        boolean enabledEntityName = false;
        if (!entity.isEmpty()) {
            enabledEntityName = true;
        }

        List<Tag> tagPairs = new ArrayList<>();
        for (KeyValueForm keyValueForm: forecastJobsEditPage.getTagValueFields()) {
            String key = keyValueForm.getKey();
            String value = keyValueForm.getValue();
            if (!key.isEmpty() || !value.isEmpty()) {
                tagPairs.add(new Tag(key, value));
            }
        }
        if (tagPairs.size() == 0) {
            tagPairs.add(new Tag(EMPTY_STR, EMPTY_STR));
        }

        String metric;
        String startDate = forecastJobsEditPage.getStartDate();
        String endDate = forecastJobsEditPage.getEndDate();
        String dateType;
        String intervalCount;
        String intervalUnit;
        switch (placeOfCall) {
            case FORECAST: dateType = PlaceOfCall.FORECAST.toString();
                metric = forecastJobsEditPage.getStoreUnderMetric();
                if (metric.isEmpty()) {
                    metric = forecastJobsEditPage.getMetric();
                } else {
                    dateType = PlaceOfCall.HISTORY.toString();
                }
                intervalCount = forecastJobsEditPage.getForecastHorizonCountField();
                intervalUnit = forecastJobsEditPage.getForecastHorizonUnitField();

                if (endDate.isEmpty()) {
                    if (startDate.isEmpty()) {
                        startDate = NOW;
                    }
                } else {
                    startDate = endDate;
                    endDate = EMPTY_STR;
                }
//                if (tagPairs.size() == 0) {
//                    tagPairs.add(new Tag(EMPTY_STR, EMPTY_STR));
//                }

                break;
            case HISTORY: dateType = PlaceOfCall.HISTORY.toString();
                metric = forecastJobsEditPage.getMetric();
                intervalCount = forecastJobsEditPage.getIntervalCount();

                if (startDate.isEmpty() && endDate.isEmpty()) {
                    endDate = NOW;
                }

                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    intervalUnit = "DAY";
                } else {
                    intervalUnit = forecastJobsEditPage.getIntervalUnit();
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
                exportPage.getMetric(),
                exportPage.isEnabledEntityNameField(),
                exportPage.getEntityName(),
                tagPairs,
                exportPage.getStartDate(),
                exportPage.getIntervalCount(),
                exportPage.getIntervalUnit(),
                exportPage.getEndDate(),
                exportPage.getDataType());
    }

    private AggregateForm getAggregateFormOfForecastJobsEditPage() {
        List<String> listAggregationChosen = Arrays.asList(forecastJobsEditPage.getAggregateStatistic());
        return new AggregateForm(
                true,
                listAggregationChosen,
                forecastJobsEditPage.getAggregationPeriodCount(),
                forecastJobsEditPage.getAggregationPeriodUnit());
    }

    private AggregateForm getAggregateFormOfExportPage() {
        return new AggregateForm(
                exportPage.isSelectedAggregate(),
                exportPage.getAggregationChosen(),
                exportPage.getAggregationPeriodCount(),
                exportPage.getAggregationPeriodUnit());
    }
}
