package com.axibase.webtest.forecasts;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.CommonSelects;
import com.axibase.webtest.pageobjects.ForecastViewerPage;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.*;

public class ForecastPageTestRegardlessOfData extends AtsdTest {
    private ForecastViewerPage forecastViewerPage;
    private static final String START_PAGE = "/series/forecast?entity=entity-for-regardless-of-data-test&" +
            "metric=metric-for-regardless-of-data-test&" +
            "startDate=2015-03-04T14:24:40.000Z&horizonInterval=10-MINUTE&period=5-SECOND";

    @BeforeMethod
    public void setUp() {
        super.setUp();
        loadData();
        forecastViewerPage = new ForecastViewerPage();
        open(START_PAGE);
    }

    @Test
    public void testUnionFieldInvalid() {
        forecastViewerPage.setGroupAuto()
                .setGroupCount("1")
                .switchStack();
        String[] variants = {"Aa", "A1", "A.2", "a", ".A", "1", "и", "AA-", "A:", "A,"};
        for (String variant : variants) {
            forecastViewerPage.setGroupUnion2(variant);
            CommonAssertions.assertInvalid("Invalid variant:" + variant + " is accepted",
                    forecastViewerPage.getGroupUnion2());
        }
    }

    @Test
    public void testUnionFieldValid() {
        forecastViewerPage.setGroupAuto()
                .setGroupCount("1")
                .switchStack();
        String[] variants = {"AA", "A", "AA;A", "A-AA", "A;B-A"};
        for (String variant : variants) {
            forecastViewerPage.setGroupUnion2(variant);
            CommonAssertions.assertValid("Valid variant:" + variant + " is not accepted",
                    forecastViewerPage.getGroupUnion2());
        }
    }

    @Test
    public void testComponentIndexesFieldInvalid() {
        forecastViewerPage.setGroupManual();
        String[] variants = {"a", ".A", "и", "3F", "3a", "3;A", "AA", "A", "AA;A", "A-AA", "A;B-A", "B-A"};
        for (String variant : variants) {
            forecastViewerPage.setGroupComponentIndex1(variant);
            CommonAssertions.assertInvalid("Invalid variant:" + variant + " is accepted",
                    forecastViewerPage.getGroupComponentIndex1());
        }
    }

    @Test
    public void testComponentIndexesFieldValid() {
        forecastViewerPage.setGroupManual();
        String[] variants = {"11", "2", "32;5", "1-23", "1;3-4"};
        for (String variant : variants) {
            forecastViewerPage.setGroupComponentIndex1(variant);
            CommonAssertions.assertValid("Valid variant:" + variant + " is not accepted",
                    forecastViewerPage.getGroupComponentIndex1());
        }
    }

    @Test
    public void testPresenceOfTooltipsInRegularizeBlock() {
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getAggregation()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getInterpolation()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getPeriodCount()));
    }

    @Test
    public void testPresenceOfTooltipsInDecomposeBlock() {
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getThreshold()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getComponentCount()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getWindowLength()));
    }

    @Test
    public void testPresenceOfTooltipsInGroupBlock() {
        forecastViewerPage.setGroupAuto()
                .setGroupCount("1")
                .switchStack();

        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getGroupCount()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getClustering()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getStackElement()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getGroupUnion1()));

        forecastViewerPage.setGroupManual();
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getGroupComponentIndex1()));
    }

    @Test
    public void testPresenceOfTooltipsInForecastBlock() {
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getAveragingFunction()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getScoreIntervalCount()));
    }

    @Test
    public void testPresenceOfTooltipsInSettingsBlock() {
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getStartDate()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getEndDate()));
        forecastViewerPage.waitUntilTooltipIsShown(CommonSelects.getElementTooltipByFor(forecastViewerPage.getForecastHorizonCount()));
    }

    @Test
    public void testPresenceOfTooltipsInSettingsButtonsBlock() {
        forecastViewerPage.waitUntilTooltipIsShown(forecastViewerPage.getStatistics());
        forecastViewerPage.waitUntilTooltipIsShown(forecastViewerPage.getPortal());
        forecastViewerPage.waitUntilTooltipIsShown(forecastViewerPage.getDownloadingPortal());
        forecastViewerPage.waitUntilTooltipIsShown(forecastViewerPage.getForecastSettings());
    }

    @Test
    public void testCloneButtonWithErrorInForm() {
        forecastViewerPage.setThreshold("100")
                .addForecastTab();
        assertEquals("New tab is created but there is an error in the form", 1,
                forecastViewerPage.getForecastTabNames().length);
    }

    @Test
    public void testChangeColorOfChangedParameters() {
        checkHighlightOfSelectionElement(forecastViewerPage.getAggregation(), "Sum");
        checkHighlightOfSelectionElement(forecastViewerPage.getInterpolation(), "Previous");
        checkHighlightOfNumericElement(forecastViewerPage.getPeriodCount(), "2");
        checkHighlightOfNumericElement(forecastViewerPage.getThreshold(), "1");
        forecastViewerPage.getThreshold().sendKeys(Keys.BACK_SPACE);
        forecastViewerPage.getThreshold().sendKeys(Keys.DELETE);
        checkHighlightOfNumericElement(forecastViewerPage.getComponentCount(), "12");
        checkHighlightOfNumericElement(forecastViewerPage.getWindowLength(), "1");
        checkHighlightOfSelectionElement(forecastViewerPage.getAveragingFunction(), "Median");
        checkHighlightOfNumericElement(forecastViewerPage.getScoreIntervalCount(), "11");
    }

    @Test
    public void testFromMaxToMinTabsButtonsCase() {
        forecastViewerPage.addForecastTab()
                .addForecastTab()
                .addForecastTab()
                .addForecastTab();

        assertCountOfForecasts(5);
        forecastViewerPage.removeForecastTab()
                .removeForecastTab()
                .removeForecastTab()
                .removeForecastTab();
        assertCountOfForecasts(1);
        assertInvisibility("Remove button is broken:", forecastViewerPage.isForecastRemoveButtonPresent(),
                forecastViewerPage.isForecastRemoveButtonVisible());
    }

    @Test
    public void testFromMinToMaxTabsButtonsCase() {
        assertInvisibility("Remove button is broken:", forecastViewerPage.isForecastRemoveButtonPresent(),
                forecastViewerPage.isForecastRemoveButtonVisible());
        assertCountOfForecasts(1);
        forecastViewerPage.addForecastTab()
                .addForecastTab()
                .addForecastTab()
                .addForecastTab();
        assertCountOfForecasts(5);
        assertInvisibility("Add button is broken:", forecastViewerPage.isForecastAddButtonPresent(),
                forecastViewerPage.isForecastAddButtonVisible());
    }

    @Test
    public void testInBetweenButtonsCase() {
        assertCountOfForecasts(1);
        forecastViewerPage.addForecastTab();
        assertCountOfForecasts(2);
        assertVisibility("Remove button is broken:", forecastViewerPage.isForecastRemoveButtonPresent(),
                forecastViewerPage.isForecastRemoveButtonVisible());
        assertVisibility("Add button is broken:", forecastViewerPage.isForecastAddButtonPresent(),
                forecastViewerPage.isForecastAddButtonVisible());
        forecastViewerPage.addForecastTab()
                .removeForecastTab();
        assertCountOfForecasts(2);
        forecastViewerPage.removeForecastTab();
        assertCountOfForecasts(1);
    }

    @Test
    public void testClickableUnits() {
        forecastViewerPage.setPeriodUnit("week");
        assertEquals("Wrong period unit", "week",
                forecastViewerPage.getPeriodUnit().getValue());
        forecastViewerPage.setForecastHorizonUnit("week");
        assertEquals("Wrong horizon unit", "week",
                forecastViewerPage.getForecastHorizonUnit().getValue());
        forecastViewerPage.setScoreIntervalUnit("year");
        assertEquals("Wrong score interval unit", "year",
                forecastViewerPage.getScoreIntervalUnit().getValue());
    }

    @Test
    public void testActiveGroupOffOptionsCloning() {
        forecastViewerPage.setGroupOff()
                .addForecastTab();
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupOff());
    }

    @Test
    public void testActiveGroupAutoOptionsCloning() {
        forecastViewerPage.setGroupAutoOptions("10", "Novosibirsk", "A", "", "B-C;D")
                .setGroupParameterV("0.9")
                .setGroupParameterC("0.8")
                .addForecastTab();
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupAuto());
        assertGroupAutoOptions("10", "NOVOSIBIRSK", "A", "", "B-C;D", "cloning");
        forecastViewerPage.getGroupParameterV().shouldHave(value("0.9"));
        forecastViewerPage.getGroupParameterC().shouldHave(value("0.8"));
    }

    @Test
    public void testActiveGroupManualOptionsCloning() {
        forecastViewerPage.setGroupManualOptions("2-3", "", "2-4")
                .addForecastTab();
        assertGroupManualOptions("2-3", "", "2-4", "cloning");
    }

    @Test
    public void testSwitchTabsGroupsOptions() {
        forecastViewerPage.setGroupOff()
                .addForecastTab()
                .setGroupAutoOptions("10", "Novosibirsk", "A", "", "B-C;D")
                .addForecastTab()
                .setGroupManualOptions("2-3", "", "2-4")
                .switchForecastTab("Forecast 1");
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupOff());
        forecastViewerPage.switchForecastTab("Forecast 2");
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupAuto());
        assertGroupAutoOptions("10", "NOVOSIBIRSK", "A", "", "B-C;D", "switching");
        forecastViewerPage.switchForecastTab("Forecast 3");
        assertGroupManualOptions("2-3", "", "2-4", "switching");
    }

    @Test
    public void componentThresholdAndScoreIntervalAccessTest() {
        SelenideElement threshold = forecastViewerPage.getThreshold();
        SelenideElement intervalScore = forecastViewerPage.getScoreIntervalCount();

        assertTrue("The score interval should be displayed before the Component Threshold is filled",
                intervalScore.isDisplayed());

        threshold.setValue("5");
        assertFalse("The score interval shouldn't be displayed after the Component Threshold is filled",
                intervalScore.isDisplayed());
        threshold.click();
        threshold.sendKeys(Keys.DELETE);
        threshold.sendKeys(Keys.BACK_SPACE);

        assertTrue("The Component Threshold should be enabled before the Score Interval if filled",
                threshold.isEnabled());
        intervalScore.setValue("5");
        threshold.click();
        assertFalse("The Component Threshold shouldn't be enabled after the Score Interval if filled",
                threshold.isEnabled());
    }

    @Test
    public void componentCountAndGroupCountComparisonTest() {
        forecastViewerPage.setGroupAuto();
        CommonAssertions.assertValid("The Group Count is validated but it have not to",
                forecastViewerPage.getGroupCount());
        forecastViewerPage.getComponentCount().setValue("3");
        forecastViewerPage.getGroupCount().setValue("10");
        forecastViewerPage.clickSubmitButton();
        CommonAssertions.assertInvalid("The Group Count is not validated but it have to",
                forecastViewerPage.getGroupCount());
    }

    @Test
    public void componentThresholdBoundValidationTest() {
        CommonAssertions.assertValid("The Component Threshold is validated but it have not to",
                forecastViewerPage.getThreshold());
        forecastViewerPage.setThreshold("100");
        CommonAssertions.assertInvalid("The Component Threshold is not validated but it have to",
                forecastViewerPage.getThreshold());
    }

    @Test
    public void testSubmitButtonWithThresholdErrorInForm() {
        storeCurrentWidgetContainerInJS();
        forecastViewerPage.setThreshold("100")
                .clickSubmitButton();
        isStoredWidgetContainerEqualsNew();
        assertTrue("Submit button was submitted but there is an error in the form",
                isStoredWidgetContainerEqualsNew());
    }

    @Test
    public void testSubmitButtonWithWindowLengthErrorInForm() {
        storeCurrentWidgetContainerInJS();
        forecastViewerPage.setWindowLength("100")
                .clickSubmitButton();
        assertTrue("Submit button was submitted but there is an error in the form",
                isStoredWidgetContainerEqualsNew());
    }

    @Test
    public void testSubmitButtonWithComponentCountAndCountOfGroupsErrorInForm() {
        storeCurrentWidgetContainerInJS();
        forecastViewerPage.setGroupAuto()
                .setComponentCount("1")
                .setGroupCount("10")
                .clickSubmitButton();
        assertTrue("Submit button was submitted but there is an error in the form",
                isStoredWidgetContainerEqualsNew());
    }

    @Test
    public void testSubmitButtonWithManualGroupErrorInForm() {
        storeCurrentWidgetContainerInJS();
        forecastViewerPage.setGroupManual();
        forecastViewerPage.getGroupComponentIndex1().clear();
        forecastViewerPage.getGroupComponentIndex2().clear();
        forecastViewerPage.getGroupComponentIndex3().clear();
        forecastViewerPage.clickSubmitButton();
        assertTrue("Submit button was submitted but there is an error in the form",
                isStoredWidgetContainerEqualsNew());
    }

    private void checkHighlightOfSelectionElement(SelenideElement element, String value) {
        assertLackOfHighlight(element);
        element.selectOption(value);
        assertPresenceOfHighlight(element);
    }

    private void checkHighlightOfNumericElement(SelenideElement element, String value) {
        assertLackOfHighlight(element);
        element.setValue(value);
        assertPresenceOfHighlight(element);
    }

    private void assertPresenceOfHighlight(SelenideElement element) {
        assertTrue("Parameter with id: " + element.getAttribute("id") + "is not highlighted but it should be",
                element.getAttribute("class").contains("highlight"));
    }

    private void assertLackOfHighlight(SelenideElement element) {
        assertFalse("Parameter with id: " + element.getAttribute("id") + "is highlighted but it shouldn't",
                element.getAttribute("class").contains("highlight"));
    }

    private void assertVisibility(String errorMessage, boolean isPresent, boolean isVisible) {
        assertTrue("there is no such element on the page", isPresent);
        assertTrue(errorMessage + "should be visible but it is not", isVisible);
    }

    private void assertInvisibility(String errorMessage, boolean isPresent, boolean isVisible) {
        if (isPresent) {
            assertFalse(errorMessage + "should be not visible but it is", isVisible);
        }
    }

    private void assertCountOfForecasts(int count) {
        assertEquals("Wrong count of forecasts", count, forecastViewerPage.getForecastsTabs().size());
    }

    private void assertGroupAutoOptions(String countOfGroups, String clustering, String union1,
                                        String union2, String union3, String testType) {
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupAuto());
        forecastViewerPage.getGroupCount().shouldHave(value(countOfGroups));
        forecastViewerPage.getClustering().shouldHave(value(clustering));
        forecastViewerPage.getGroupUnion1().shouldHave(value(union1));
        forecastViewerPage.getGroupUnion2().shouldHave(value(union2));
        forecastViewerPage.getGroupUnion3().shouldHave(value(union3));
    }

    private void assertGroupManualOptions(String group1, String group2, String group3, String testType) {
        assertTrue("Wrong grouping mode", forecastViewerPage.getGroupManual());
        forecastViewerPage.getGroupComponentIndex1().shouldHave(value(group1));
        forecastViewerPage.getGroupComponentIndex2().shouldHave(value(group2));
        forecastViewerPage.getGroupComponentIndex3().shouldHave(value(group3));
    }

    private boolean isStoredWidgetContainerEqualsNew() {
        return (Boolean) executeJavaScript(
                "return document.getElementById(\"widget-container\").__innerWidget__===self.widgetContainerForAtsdTest");
    }

    private void storeCurrentWidgetContainerInJS() {
        executeJavaScript("self.widgetContainerForAtsdTest =  document.getElementById(\"widget-container\").__innerWidget__");
    }

    private void loadData() {
        open("/metrics/entry");
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "<#list 1..5 as i>\n" +
                "series s:${1425482080 - i * 600} " +
                "e:entity-for-regardless-of-data-test " +
                "m:metric-for-regardless-of-data-test=${60 - 2*i}\n" +
                "</#list>");
        $("button[value=send]").click();
    }

}
