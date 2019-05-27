package com.axibase.webtest.forecasts.jobs;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.pages.ForecastJobsEditPage;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Issue;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class CalculationOfForecastsTest extends AtsdTest {
    private ForecastJobsEditPage forecastJobsEditPage;
    private static final String URL_FORECAST_JOBS = "/forecast/settings/edit.xhtml";
    private static final String METRIC = "metric-forecast-jobs";
    private static final String ENTITY_1 = "entity-forecast-jobs-1";
    private static final String ENTITY_2 = "entity-forecast-jobs-2";
    private static final String ALGORITHM_HW = "Holt-Winters";
    private static final String ALGORITHM_SSA = "SSA";
    private static final String METRIC_ENTITY_ALL_TAGS = "Metric - Entity - All Tags";
    private static final String METRIC_ENTITY = "Metric - Entity";
    private static final String METRIC_ENTITY_DEFINED_TAGS = "Metric - Entity - Defined Tags";
    private static final String FORECAST_START_TIME = "2019-05-07T00:00:00Z";
    private static final String FORECAST_END_TIME = "2019-05-08T23:00:00Z";
    private static final String FORECAST_TABLE = "atsd_forecast";
    private static final String D_TABLE = "atsd_d";
    private static final String EMPTY_STR = "";
    private static final String FORECAST_NAME = "Name of forecast";
    private static final String STORED_METRIC = "new-metric";
    private static final String TAG_1_1 = "tag-name-1=tag-value-1";
    private static final String TAG_2_2 = "tag-name-2=tag-value-2";
    private static final String TAG_1_2 = "tag-name-1=tag-value-2";
    private static final String TAG_2_1 = "tag-name-2=tag-value-1";
    private static final String NEW_TAG_FROM_STORE = "forecast-name=" + FORECAST_NAME;

    /**
     * Expected sets of tags for checked forecasts
     */
    private static final Set<String> SET_EMPTY = new HashSet<>();
    private static final Set<String> SET_TAG11 = new HashSet<>(Arrays.asList(TAG_1_1));
    private static final Set<String> SET_TAG11_TAG22 = new HashSet<>(Arrays.asList(TAG_1_1, TAG_2_2));
    private static final Set<String> SET_TAG12 = new HashSet<>(Arrays.asList(TAG_1_2));
    private static final Set<String> SET_TAG22 = new HashSet<>(Arrays.asList(TAG_2_2));
    private static final Set<String> SET_TAG21 = new HashSet<>(Arrays.asList(TAG_2_1));
    private static final Set<String> SET_NEW_TAG = new HashSet<>(Arrays.asList(NEW_TAG_FROM_STORE));
    private static final Set<String> SET_TAG11_NEW_TAG = new HashSet<>(Arrays.asList(TAG_1_1, NEW_TAG_FROM_STORE));
    private static final Set<String> SET_TAG11_TAG22_NEW_TAG = new HashSet<>(Arrays.asList(TAG_1_1, TAG_2_2, NEW_TAG_FROM_STORE));
    private static final Set<String> SET_TAG12_NEW_TAG = new HashSet<>(Arrays.asList(TAG_1_2, NEW_TAG_FROM_STORE));
    private static final Set<String> SET_TAG22_NEW_TAG = new HashSet<>(Arrays.asList(TAG_2_2, NEW_TAG_FROM_STORE));
    private static final Set<String> SET_TAG21_NEW_TAG = new HashSet<>(Arrays.asList(TAG_2_1, NEW_TAG_FROM_STORE));

    @Data
    @RequiredArgsConstructor
    private class CheckedForecast {
        private final String table;
        private final String name;
        private final String metric;
        private final String entity;
        private final Set<String> tags;
        private final String algorithm;
        private final String startTime;
        private final String endTime;
    }

    @BeforeClass
    public void insertData() {
        super.setUp();
        insertSeries();
        super.logout();
    }

    private void insertSeries() {
        open("/metrics/entry");
        String generalSeries = "series d:${(startDateMs + x * 1000 * 3600)?number_to_datetime?iso_utc} m:" + METRIC + "=";
        String generalEntity = " e:entity-forecast-jobs-";
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "<#assign startDateMs=\"2019-04-30T00:00:00.000Z\"?datetime.iso?long />\n" +
                "<#list 0..199 as x>\n" +
                generalSeries + "10" + generalEntity + "1\n" +
                generalSeries + "20" + generalEntity + "1 t:" + TAG_1_1 + "\n" +
                generalSeries + "30" + generalEntity + "1 t:" + TAG_2_2 + "\n" +
                generalSeries + "40" + generalEntity + "1 t:" + TAG_1_1 + " t:" + TAG_2_2 + "\n" +
                generalSeries + "50" + generalEntity + "1 t:" + TAG_1_2 + "\n" +
                generalSeries + "60" + generalEntity + "2\n" +
                generalSeries + "70" + generalEntity + "2 t:" + TAG_1_1 + "\n" +
                generalSeries + "80" + generalEntity + "2 t:" + TAG_2_1 + "\n" +
                "</#list>");
        $(By.cssSelector("button[value=send]")).click();
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        forecastJobsEditPage = new ForecastJobsEditPage();
        open(URL_FORECAST_JOBS);
        forecastJobsEditPage.setStartDate("2019-04-30T00:00:00.000Z");
        forecastJobsEditPage.setIntervalCount("1");
        forecastJobsEditPage.setIntervalUnit("Week");
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setSeriesSelectionIntervalCount("1");
        forecastJobsEditPage.setSeriesSelectionIntervalUnit("Day");
    }

    @Issue("6236")
    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setTagKey("tag-name-2", 2);
        forecastJobsEditPage.setTagValue("tag-value-2", 2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag12GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag21GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWIsEmptyGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultHWWithoutEntityTag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWAllEntitiesGroupByMetricEntity() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag21GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11Tag22GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity1ForecastName() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWEntity2StoredUnderMetric() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_TAG11, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_TAG21, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultHWAllEntitiesForecastNameStoredUnderMetric() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG11_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG11_TAG22_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG12_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG22_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_TAG11_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_TAG21_NEW_TAG, ALGORITHM_HW, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setTagKey("tag-name-2", 2);
        forecastJobsEditPage.setTagValue("tag-value-2", 2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag12GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag21GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAIsEmptyGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultSSAWithoutEntityTag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntity() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag21GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11Tag22GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity1ForecastName() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG11_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG12, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, SET_TAG22, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAEntity2StoredUnderMetric() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_EMPTY, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_TAG11, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, SET_TAG21, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    @Issue("6236")
    @Test
    public void testResultSSAAllEntitiesForecastNameStoredUnderMetric() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<CheckedForecast> actualForecasts = getCheckedForecasts();

        Set<CheckedForecast> expectedForecasts = new HashSet<>(Arrays.asList(
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG11_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG11_TAG22_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG12_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, SET_TAG22_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_TAG11_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                new CheckedForecast(D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, SET_TAG21_NEW_TAG, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecasts, actualForecasts);
    }

    private Set<CheckedForecast> getCheckedForecasts() {
        Set<CheckedForecast> result = new HashSet<>();
        for (Map<String, String> mapOfRow: getForecastsInformation(forecastJobsEditPage.getForecastTable())) {
            Set<String> setPairs = new HashSet<>();
            String strTags = mapOfRow.get("Forecast Tags");
            if (!strTags.isEmpty()) {
                String[] massPairs = mapOfRow.get("Forecast Tags").split(", ");
                for (String pair : massPairs) {
                        setPairs.add(pair);
                }
            }
            result.add(new CheckedForecast(
                    mapOfRow.get("Table"),
                    mapOfRow.get("Forecast Name"),
                    mapOfRow.get("Forecast Metric"),
                    mapOfRow.get("Entity"),
                    setPairs,
                    mapOfRow.get("Algorithm"),
                    mapOfRow.get("Forecast Start Time"),
                    mapOfRow.get("Forecast End Time")));
        }
        return result;
    }

    public Set<Map<String, String>> getForecastsInformation(ElementsCollection forecastsTables) {
        Set<Map<String, String>> result = new HashSet<>();

        if (forecastsTables.isEmpty()) {
            return result;
        }

        SelenideElement tableOfForecast = forecastsTables.get(0);
        List<String> heads = tableOfForecast.findAll(By.tagName("th")).texts();
        for (SelenideElement row: tableOfForecast.findAll(By.xpath("./tbody/tr"))) {
            Map<String, String> mapOfRow = new HashMap<>();
            List<String> listOfCellText = row.findAll(By.xpath("./td")).texts();
            for (int i = 0; i < heads.size(); i++) {
                String key = heads.get(i);
                if(!key.isEmpty()) {
                    mapOfRow.put(key, listOfCellText.get(i));
                }
            }
            result.add(mapOfRow);
        }

        return result;
    }
}
