package com.axibase.webtest.forecasts.jobs;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.pages.ForecastJobsEditPage;
import com.axibase.webtest.service.AtsdTest;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalculationOfForecastsTest extends AtsdTest {
    private ForecastJobsEditPage forecastJobsEditPage;
    private static final String URL_FORECAST_JOBS = url + "/forecast/settings/edit.xhtml";
    private static final String METRIC = "metric-forecast-jobs";
    private static final String ENTITY_1 = "entity-forecast-jobs-1";
    private static final String ENTITY_2 = "entity-forecast-jobs-2";
    private static final String HOLT_WINTERS = "Holt-Winters";
    private static final String ALGORITHM_HW = "HOLT_WINTERS";
    private static final String ALGORITHM_SSA = "SSA";
    private static final String METRIC_ENTITY_ALL_TAGS = "METRIC_ENTITY_ALL_TAGS";
    private static final String METRIC_ENTITY = "METRIC_ENTITY";
    private static final String METRIC_ENTITY_DEFINED_TAGS = "METRIC_ENTITY_DEFINED_TAGS";
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

    @Before
    public void setUp() throws Exception{
        this.login();
        insertSeries();
        forecastJobsEditPage = new ForecastJobsEditPage(driver);
        driver.get(URL_FORECAST_JOBS);
        forecastJobsEditPage.setStartDate("2019-04-30T00:00:00.000Z");
        forecastJobsEditPage.setIntervalCount("1");
        forecastJobsEditPage.setIntervalUnit("WEEK");
        forecastJobsEditPage.setMetric(METRIC);
        forecastJobsEditPage.setSeriesSelectionIntervalCount("1");
        forecastJobsEditPage.setSeriesSelectionIntervalUnit("DAY");
    }


    public void insertSeries() {
        driver.get(AtsdTest.url + "/metrics/entry");
        String generalSeries = "series d:${(startDateMs + x * 1000 * 3600)?number_to_datetime?iso_utc} m:" + METRIC + "=";
        String generalEntity = " e:entity-forecast-jobs-";
        CommonActions.sendTextToCodeMirror(driver.findElement(By.name("commands")), "<#assign startDateMs=\"2019-04-30T00:00:00.000Z\"?datetime.iso?long />\n" +
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
        driver.findElement(By.cssSelector("button[value=send]")).click();
    }

    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

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

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag12GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag21GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWIsEmptyGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultHWWithoutEntityTag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWAllEntitiesGroupByMetricEntity() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag21GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag11Tag22GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWAllEntitiesGroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultHWEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultHWEntity1ForecastName() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_2_2, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWEntity2StoredUnderMetric() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, EMPTY_STR, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, TAG_1_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, TAG_2_1, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultHWAllEntitiesForecastNameStoredUnderMetric() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        String newTagFromStore = "forecast-name=" + FORECAST_NAME;
        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_1 + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2  + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_2 + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_2_2 + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, TAG_1_1 + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, TAG_2_1 + ", " + newTagFromStore, HOLT_WINTERS, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }




























    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

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

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag12GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag21GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAIsEmptyGroupByMetricEntityAllTags() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultSSAWithoutEntityTag11GroupByMetricEntityAllTags() {
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntity() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag21GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag11Tag22GroupByMetricEntity() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAAllEntitiesGroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultSSAEntity1Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity1Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag11GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-1", 1);
        forecastJobsEditPage.setTagValue("tag-value-1", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, EMPTY_STR, METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2Tag22GroupByMetricEntityDefinedTagsWithTag1Tag2() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_DEFINED_TAGS);
        forecastJobsEditPage.setTagKey("tag-name-2", 1);
        forecastJobsEditPage.setTagValue("tag-value-2", 1);
        forecastJobsEditPage.setRequiredTagKeys("tag-name-1 tag-name-2");
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();
        assertTrue("Wrong output forecasts", actualForecasts.isEmpty());
    }

    @Test
    public void testResultSSAEntity1ForecastName() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setForecastName(ALGORITHM_SSA);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_1_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, FORECAST_TABLE, FORECAST_NAME, METRIC, ENTITY_1, TAG_2_2, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAEntity2StoredUnderMetric() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, EMPTY_STR, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, TAG_1_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, EMPTY_STR, STORED_METRIC, ENTITY_2, TAG_2_1, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }

    @Test
    public void testResultSSAAllEntitiesForecastNameStoredUnderMetric() {
        forecastJobsEditPage.setGroupBy(METRIC_ENTITY_ALL_TAGS);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setForecastName(FORECAST_NAME);
        forecastJobsEditPage.setStoreUnderMetric(STORED_METRIC);
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();

        Set<List<String>> actualForecasts = forecastJobsEditPage.getListForecasts();

        String newTagFromStore = "forecast-name=" + FORECAST_NAME;
        Set<List<String>> expectedForecast = new HashSet<>(Arrays.asList(
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_1 + ", " + newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_1 + ", " + TAG_2_2  + ", " + newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_1_2 + ", " + newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_1, TAG_2_2 + ", " + newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, TAG_1_1 + ", " + newTagFromStore, ALGORITHM_SSA, FORECAST_START_TIME, FORECAST_END_TIME),
                Arrays.asList(EMPTY_STR, EMPTY_STR, D_TABLE, FORECAST_NAME, STORED_METRIC, ENTITY_2, TAG_2_1 + ", " + newTagFromStore, ALGORITHM_SSA
                        , FORECAST_START_TIME, FORECAST_END_TIME)));

        assertEquals("Wrong output forecasts", expectedForecast, actualForecasts);
    }
}
