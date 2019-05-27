package com.axibase.webtest.forecasts.jobs;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.ElementUtils;
import com.axibase.webtest.pages.ForecastJobsEditPage;
import com.axibase.webtest.pages.ForecastViewerPage;
import com.axibase.webtest.pages.PortalViewPage;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Issue;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertEquals;

public class TransitionToForecastViewerPageTest extends AtsdTest {
    private ForecastJobsEditPage forecastJobsEditPage;
    private ForecastViewerPage forecastViewerPage;
    private PortalViewPage portalViewPage;
    private static final String URL_FORECAST_JOBS = "/forecast/settings/edit.xhtml";
    private static final String METRIC = "metric-forecast-viewer";
    private static final String ENTITY_1 = "entity-forecast-viewer-1";
    private static final String ENTITY_2 = "entity-forecast-viewer-2";
    private static final String ENTITY_3 = "entity-forecast-viewer-3";
    private static final String VALUE_1 = "1";
    private static final String VALUE_50 = "50";
    private static final String VALUE_100 = "100";
    private static final String ALGORITHM_SSA = "SSA";
    private static final String ALGORITHM_HW = "Holt-Winters";
    private static final String START_DATE = "2019-04-30T00:00:00Z";
    private static final String FORECAST_END_TIME = "2019-05-06T23:00:00Z";

    @Data
    @RequiredArgsConstructor
    private class CheckedFields {
        private final String aggregation;
        private final String periodCount;
        private final String periodUnit;
        private final String scoreIntervalCount;
        private final String scoreIntervalUnit;
        private final String startDate;
        private final String endDate;
        private final String horizonCount;
        private final String horizonUnit;
        private final String valueSample;
        private final int legendSeriesCount;
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
        String generalEntity = " e:entity-forecast-viewer-";
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "<#assign startDateMs=\"" + START_DATE + "\"?datetime.iso?long />\n" +
                "<#list 0..199 as x>\n" +
                generalSeries + VALUE_1 + generalEntity + "1\n" +
                generalSeries + VALUE_50 + generalEntity + "2\n" +
                generalSeries + VALUE_100 + generalEntity + "3\n" +
                "</#list>");
        $(By.cssSelector("button[value=send]")).click();
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        forecastJobsEditPage = new ForecastJobsEditPage();
        open(URL_FORECAST_JOBS);
        forecastJobsEditPage.setStartDate(START_DATE);
        forecastJobsEditPage.setMetric(METRIC);
    }

    @Issue("6236")
    @Test
    public void testTransitionToPortalViewEntity2HW() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_HW);
        forecastJobsEditPage.setIntervalCount("1");
        forecastJobsEditPage.setIntervalUnit("Week");
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();
        portalViewPage = forecastJobsEditPage.clickViewStoredForecast();

        ElementUtils.goToNextWindow();

        portalViewPage.getLegend().shouldHaveSize(2);
        String actualSampleValue = getLastSampleValue();
        assertEquals("Mismatch of data", VALUE_50, actualSampleValue);
    }

    @Issue("6236")
    @Test
    public void testTransitionToPortalViewEntity3SSA() {
        forecastJobsEditPage.setEntity(ENTITY_3);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setIntervalCount("1");
        forecastJobsEditPage.setIntervalUnit("Week");
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();
        portalViewPage = forecastJobsEditPage.clickViewStoredForecast();

        ElementUtils.goToNextWindow();

        portalViewPage.getLegend().shouldHaveSize(2);
        String actualSampleValue = getLastSampleValue();
        assertEquals("Mismatch of data", VALUE_100, actualSampleValue);
    }

    @Issue("6236")
    @Test
    public void testTransitionToForecastViewerEntity1SetInterval() {
        forecastJobsEditPage.setEntity(ENTITY_1);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setIntervalCount("7");
        forecastJobsEditPage.setIntervalUnit("Day");
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();
        forecastViewerPage = forecastJobsEditPage.clickOpenForecastViewer();

        CheckedFields expectedParameters = getCheckedFieldsFromForecastJobsEditPage(START_DATE, FORECAST_END_TIME, VALUE_1);
        ElementUtils.goToNextWindow();
        CheckedFields actualParameters = getCheckedFieldsFromForecastViewerPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testTransitionToForecastViewerEntity2SetEndDate() {
        forecastJobsEditPage.setEntity(ENTITY_2);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setEndDate("2019-05-04T00:00:00.000Z");
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();
        forecastViewerPage = forecastJobsEditPage.clickOpenForecastViewer();

        CheckedFields expectedParameters = getCheckedFieldsFromForecastJobsEditPage(START_DATE, "2019-05-03T23:00:00Z", VALUE_50);
        ElementUtils.goToNextWindow();
        CheckedFields actualParameters = getCheckedFieldsFromForecastViewerPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    @Issue("6236")
    @Test
    public void testTransitionToForecastViewerEntity3SetAllFields() {
        forecastJobsEditPage.setEntity(ENTITY_3);
        forecastJobsEditPage.setAlgorithm(ALGORITHM_SSA);
        forecastJobsEditPage.setIntervalCount("1");
        forecastJobsEditPage.setIntervalUnit("Week");
        forecastJobsEditPage.setAggregationPeriodCount("60");
        forecastJobsEditPage.setAggregationPeriodUnit("Minute");
        forecastJobsEditPage.setAggregateStatistic("Max");
        forecastJobsEditPage.setScoreIntervalCount("7");
        forecastJobsEditPage.setScoreIntervalUnit("Day");
        forecastJobsEditPage.setForecastHorizonCount("48");
        forecastJobsEditPage.setForecastHorizonUnit("Hour");
        forecastJobsEditPage = forecastJobsEditPage.clickRunButton();
        forecastViewerPage = forecastJobsEditPage.clickOpenForecastViewer();

        CheckedFields expectedParameters = getCheckedFieldsFromForecastJobsEditPage(START_DATE, FORECAST_END_TIME, VALUE_100);
        ElementUtils.goToNextWindow();
        CheckedFields actualParameters = getCheckedFieldsFromForecastViewerPage();

        assertEquals("Mismatch of data", expectedParameters, actualParameters);
    }

    private CheckedFields getCheckedFieldsFromForecastJobsEditPage(String startDate, String endDate, String valueSample) {
        return new CheckedFields(
                forecastJobsEditPage.getAggregateStatistic().getSelectedValue().toUpperCase(),
                forecastJobsEditPage.getAggregationPeriodCount().getValue(),
                forecastJobsEditPage.getAggregationPeriodUnit().getSelectedValue().toLowerCase(),
                forecastJobsEditPage.getScoreIntervalCount().getValue(),
                forecastJobsEditPage.getScoreIntervalUnit().getSelectedValue().toLowerCase(),
                startDate,
                endDate,
                forecastJobsEditPage.getForecastHorizonCount().getValue(),
                forecastJobsEditPage.getForecastHorizonUnit().getSelectedValue().toLowerCase(),
                valueSample,
                2);
    }

    private CheckedFields getCheckedFieldsFromForecastViewerPage() {
        return new CheckedFields(
                forecastViewerPage.getAggregation().getSelectedValue(),
                forecastViewerPage.getPeriodCount().getValue(),
                forecastViewerPage.getPeriodUnit().getValue(),
                forecastViewerPage.getScoreIntervalCount().getValue(),
                forecastViewerPage.getScoreIntervalUnit().getValue(),
                getFullDate(forecastViewerPage.getStartDate(), forecastViewerPage.getStartTime()),
                getFullDate(forecastViewerPage.getEndDate(), forecastViewerPage.getEndTime()),
                forecastViewerPage.getForecastHorizonCount().getValue(),
                forecastViewerPage.getForecastHorizonUnit().getValue(),
                forecastViewerPage.getWidgetContainer().find(By.xpath("./div[last()]/div[@class='axi-tooltip-inner']")).getText(),
                forecastViewerPage.getCountOfForecastsInWidgetContainer() + forecastViewerPage.getCountOfHistoryChartsInWidgetContainer());
    }

    private String getFullDate(SelenideElement date, SelenideElement time) {
        return date.getValue() + "T" + time.getValue() + "Z";
    }

    private String getLastSampleValue() {
        return portalViewPage.getLeftArrow().find(By.xpath("./div[@class='axi-tooltip-inner']")).getText();
    }
}
