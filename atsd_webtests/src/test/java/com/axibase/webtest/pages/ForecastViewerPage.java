package com.axibase.webtest.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.axibase.webtest.CommonAssertions.executeWithElement;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.fail;

public class ForecastViewerPage {
    private By breadcrumb = By.className("breadcrumb");
    private By forecastTabsPanel = By.id("group-toggle-list");
    private By componentContainer = By.id("singular-values-container");
    private By summaryContainer = By.id("summary-container");

    private By forecastSettings = By.id("save-forecast-btn");
    private By portal = By.id("save-config-btn");
    private By downloadingPortal = By.id("get-config-btn");
    private By statistics = By.xpath("//*[@id='settings']//footer//*[@class='icon-sum']");

    private By submitButton = By.id("group-save-btn");
    private By removeButton = By.id("remove-group-btn");
    private By addButton = By.id("add-group-btn");

    private By aggregation = By.id("aggregation");
    private By interpolation = By.id("interpolation");
    private By periodCount = By.id("period-count");
    private By periodUnit = By.id("period-unit");

    private By threshold = By.id("decompose-threshold");
    private By componentCount = By.id("decompose-limit");
    private By windowLength = By.id("decompose-length");

    private By groupOff = By.id("grouping-type-none");
    private By groupAuto = By.id("grouping-type-auto");
    private By groupCount = By.id("grouping-auto-count");
    private By clustering = By.id("grouping-auto-clustering-method");
    private By stack = By.xpath("//*[@id='grouping-auto-stack']/parent::div");
    private By groupUnion1 = By.id("grouping-auto-union-0");
    private By groupUnion2 = By.id("grouping-auto-union-1");
    private By groupUnion3 = By.id("grouping-auto-union-2");
    private By groupParameterV = By.id("grouping-auto-clustering-v");
    private By groupParameterC = By.id("grouping-auto-clustering-c");
    private By groupManual = By.id("grouping-type-manual");
    private By groupComponentIndex1 = By.id("grouping-manual-groups-0");
    private By groupComponentIndex2 = By.id("grouping-manual-groups-1");
    private By groupComponentIndex3 = By.id("grouping-manual-groups-2");

    private By averagingFunction = By.id("reconstruct-avg");
    private By scoreIntervalCount = By.id("forecast-score-interval-count");
    private By scoreIntervalUnit = By.id("forecast-score-interval-unit");

    private By startDate = By.id("startdate");
    private By startTime = By.id("starttime");
    private By endDate = By.id("enddate");
    private By endTime = By.id("endtime");
    private By forecastHorizonCount = By.id("horizon-count");
    private By forecastHorizonUnit = By.id("horizon-unit");

    private By groupingByURL = By.cssSelector("#settings > .controls");

    public ForecastViewerPage() {
    }

    public void scheduleForecast() {
        $(forecastSettings).click();
    }

    public void savePortal() {
        $(portal).click();
    }

    public SelenideElement getBreadcrumbElement(int elementsNumber) {
        return $(breadcrumb).$$(By.tagName("li")).get(elementsNumber);
    }

    public void submitFormAndWait(int countOfSeconds) {
        $(submitButton).click();
        waitUntilSummaryTableIsLoaded(countOfSeconds);
    }

    public ForecastViewerPage clickSubmitButton() {
        $(submitButton).click();
        return this;
    }

    public void waitUntilSummaryTableIsLoaded(int countOfSeconds) {
        try {
            Wait().withTimeout(Duration.ofSeconds(countOfSeconds))
                    .until(condition -> $(By.xpath("//*[@id='summary-container']/table")).exists());
        } catch (TimeoutException e) {
            fail("Chart's loading time (" + countOfSeconds + " seconds) is over");
        }
    }

    public boolean isWidgetContainerLoading() {
        boolean isLoading = false;
        try {
            Wait().withTimeout(Duration.ofSeconds(1))
                    .until(condition -> $(By.xpath("//*[@id='widget-container']/" +
                            "*[@class='axi-tooltip axi-tooltip-info']/" +
                            "*[@class='axi-tooltip-inner']/" +
                            "*[text()='Loaded']")).exists());
        } catch (TimeoutException e) {
            isLoading = true;
        }
        return isLoading;
    }

    public ForecastViewerPage removeForecastTab() {
        $(removeButton).click();
        return this;
    }

    public ForecastViewerPage addForecastTab() {
        $(addButton).click();
        return this;
    }

    public ForecastViewerPage switchForecastTab(String nameOfForecast) {
        $(forecastTabsPanel).find(By.linkText(nameOfForecast)).click();
        return this;
    }

    public ForecastViewerPage switchStack() {
        $(stack).click();
        return this;
    }

    public void waitUntilTooltipIsShown(SelenideElement element) {
        element.hover();
        try {
            Wait().withTimeout(Duration.ofMillis(500))
                    .pollingEvery(Duration.ofMillis(100))
                    .until(condition -> $(By.className("tooltip")).exists());
        } catch (TimeoutException e) {
            fail("Tooltip's time is over");
        }
        //that is for tooltip does not cover the button after tooltip is shown
        $("body").hover();
        Wait().withTimeout(Duration.ofSeconds(1)).
                until(condition -> !$(".tooltip").exists());
    }

    public ForecastViewerPage setDecomposeOptions(String threshold, String componentCount, String windowLength) {
        setThreshold(threshold);
        setComponentCount(componentCount);
        setWindowLength(windowLength);
        return this;
    }

    public ForecastViewerPage setRegularizeOptions(String aggregation, String interpolation, String periodCount, String periodUnit) {
        setAggregation(aggregation);
        setInterpolation(interpolation);
        setPeriodCount(periodCount);
        setPeriodUnit(periodUnit);
        return this;
    }

    public ForecastViewerPage setGroupAutoOptions(String countOfGroups, String clustering, String union1, String union2, String union3) {
        $(By.id("grouping-type-auto")).click();
        setGroupCount(countOfGroups);
        setClustering(clustering);
        switchStack();
        setGroupUnion1(union1);
        setGroupUnion2(union2);
        setGroupUnion3(union3);
        return this;
    }

    public ForecastViewerPage setGroupManualOptions(String group1, String group2, String group3) {
        $(groupManual).click();
        setGroupComponentIndex1(group1);
        setGroupComponentIndex2(group2);
        setGroupComponentIndex3(group3);
        return this;
    }

    public ForecastViewerPage setForecastOptions(String average, String scoreIntervalCount, String scoreIntervalUnit) {
        setAveragingFunction(average);
        setScoreIntervalCount(scoreIntervalCount);
        setScoreIntervalUnit(scoreIntervalUnit);
        return this;
    }

    public String getGroupedByURLTags() {
        return $(groupingByURL).$(By.tagName("ul")).getText();
    }

    public String getGroupedByURLText() {
        return $(groupingByURL).getText();

    }

    public int getCountOfForecastsInWidgetContainer() {
        return $$("#widget-container rect.axi-legend-button").size();
    }

    public int getCountOfHistoryChartsInWidgetContainer() {
        return $$("#widget-container circle.axi-legend-button").size();
    }

    public int getCountOfActiveComponentsInComponentContainer() {
        return $(componentContainer).$$(By.xpath("//*[@fill='green' and not(@class)]")).size();
    }

    public int getCountOfPassiveComponentsInComponentContainer() {
        return $(componentContainer).$$(By.xpath("//*[@fill='silver' and not(@class)]")).size();
    }

    public String getNameOfForecastInSummaryTable(SelenideElement forecast) {
        return forecast.$(By.xpath("../..")).getText().replace("\n(√λ)", "");
    }

    public String[] getForecastTabNames() {
        return $(forecastTabsPanel).getText().split("\n");
    }

    public SelenideElement getComponentContainer() {
        return $(componentContainer);
    }

    public ElementsCollection getSummaryContainerForecastsSingularValueLinks() {
        return $(summaryContainer).$$(By.xpath("//a[text()='(√λ)']"));
    }

    public List<String> getSummaryContainerForecastNames() {
        return $(summaryContainer).$$(By.xpath("//thead//th")).stream().
                map(SelenideElement::getText).collect(Collectors.toList());
    }

    public ElementsCollection getForecastsTabs() {
        return $(forecastTabsPanel).$$(By.tagName("li"));
    }

    public boolean isForecastAddButtonPresent() {
        return (Boolean) executeWithElement($(addButton), "return arguments[0]!=null");
    }

    public boolean isForecastAddButtonVisible() {
        return $(addButton).isDisplayed();
    }

    public boolean isForecastRemoveButtonPresent() {
        return (Boolean) executeWithElement($(removeButton), "return arguments[0]!=null");
    }

    public boolean isForecastRemoveButtonVisible() {
        return $(removeButton).isDisplayed();
    }

    public int getCountOfActiveComponentsInSingularValueContainer(int forecastNumber) {
        int count = 0;
        int maxCountOfActiveComponents = 20;
        String componentsAsString = $(By.xpath(String.
                format("//*[@id='summary-container']//tbody/tr[2]/td[%d]", forecastNumber + 1))).getText();
        for (String str : componentsAsString.split(";")) {
            String[] components = str.split("-");
            if (Integer.parseInt(components[0]) > maxCountOfActiveComponents) break;
            if (components.length > 1) {
                if (Integer.parseInt(components[1]) > maxCountOfActiveComponents) break;
                count += (Integer.parseInt(components[1]) - Integer.parseInt(components[0]) + 1);
            } else {
                count++;
            }
        }
        return count;
    }

    public SelenideElement getDownloadingPortal() {
        return $(downloadingPortal);
    }

    public SelenideElement getStatistics() {
        return $(statistics);
    }

    public SelenideElement getForecastSettings() {
        return $(forecastSettings);
    }

    public SelenideElement getPortal() {
        return $(portal);
    }

    public SelenideElement getStackElement() {
        return $(By.id("grouping-auto-stack"));
    }

    public SelenideElement getGroupAutoElement() {
        return $(groupAuto);
    }

    public SelenideElement getGroupManualElement() {
        return $(groupManual);
    }

    public SelenideElement getGroupParameterV() {
        return $(groupParameterV);
    }

    public ForecastViewerPage setGroupParameterV(String value) {
        setNumericOption(value, $(groupParameterV));
        return this;
    }

    public SelenideElement getGroupParameterC() {
        return $(groupParameterC);
    }

    public ForecastViewerPage setGroupParameterC(String value) {
        setNumericOption(value, $(groupParameterC));
        return this;
    }

    public SelenideElement getSubmitButton() {
        return $(submitButton);
    }

    public SelenideElement getAggregation() {
        return $(aggregation);
    }

    public ForecastViewerPage setAggregation(String value) {
        setSelectionOption(value, $(aggregation));
        return this;
    }

    public SelenideElement getInterpolation() {
        return $(interpolation);
    }

    public ForecastViewerPage setInterpolation(String value) {
        setSelectionOption(value, $(interpolation));
        return this;
    }

    public SelenideElement getPeriodCount() {
        return $(periodCount);
    }

    public ForecastViewerPage setPeriodCount(String value) {
        setNumericOption(value, $(periodCount));
        return this;
    }

    public SelenideElement getPeriodUnit() {
        return $(periodUnit);
    }

    public ForecastViewerPage setPeriodUnit(String value) {
        SelenideElement intervalInput = $(periodUnit).$(By.xpath(".."));
        intervalInput.$(By.tagName("button")).click();
        intervalInput.$(By.className("dropdown-menu")).$(By.partialLinkText(value)).click();
        return this;
    }

    public SelenideElement getThreshold() {
        return $(threshold);
    }

    public ForecastViewerPage setThreshold(String value) {
        setNumericOption(value, $(threshold));
        return this;
    }

    public SelenideElement getComponentCount() {
        return $(componentCount);
    }

    public ForecastViewerPage setComponentCount(String value) {
        setNumericOption(value, $(componentCount));
        return this;
    }

    public SelenideElement getWindowLength() {
        return $(windowLength);
    }

    public ForecastViewerPage setWindowLength(String value) {
        setNumericOption(value, $(windowLength));
        return this;
    }

    public boolean getGroupOff() {
        return $(groupOff).isSelected();
    }

    public ForecastViewerPage setGroupOff() {
        $(groupOff).click();
        return this;
    }

    public boolean getGroupAuto() {
        return $(groupAuto).isSelected();
    }

    public ForecastViewerPage setGroupAuto() {
        $(groupAuto).click();
        return this;
    }

    public SelenideElement getGroupCount() {
        return $(groupCount);
    }

    public ForecastViewerPage setGroupCount(String value) {
        setNumericOption(value, $(groupCount));
        return this;
    }

    public SelenideElement getClustering() {
        return $(clustering);
    }

    public ForecastViewerPage setClustering(String value) {
        setSelectionOption(value, $(clustering));
        return this;
    }

    public SelenideElement getGroupUnion1() {
        return $(groupUnion1);
    }

    public ForecastViewerPage setGroupUnion1(String value) {
        setNumericOption(value, $(groupUnion1));
        return this;
    }

    public SelenideElement getGroupUnion2() {
        return $(groupUnion2);
    }

    public ForecastViewerPage setGroupUnion2(String value) {
        setNumericOption(value, $(groupUnion2));
        return this;
    }

    public SelenideElement getGroupUnion3() {
        return $(groupUnion3);
    }

    public ForecastViewerPage setGroupUnion3(String value) {
        setNumericOption(value, $(groupUnion3));
        return this;
    }

    public boolean getGroupManual() {
        return $(groupManual).isSelected();
    }

    public ForecastViewerPage setGroupManual() {
        $(groupManual).click();
        return this;
    }

    public SelenideElement getGroupComponentIndex1() {
        return $(groupComponentIndex1);
    }

    public ForecastViewerPage setGroupComponentIndex1(String value) {
        setNumericOption(value, $(groupComponentIndex1));
        return this;
    }

    public SelenideElement getGroupComponentIndex2() {
        return $(groupComponentIndex2);
    }

    public ForecastViewerPage setGroupComponentIndex2(String value) {
        setNumericOption(value, $(groupComponentIndex2));
        return this;
    }

    public SelenideElement getGroupComponentIndex3() {
        return $(groupComponentIndex3);
    }

    public ForecastViewerPage setGroupComponentIndex3(String value) {
        setNumericOption(value, $(groupComponentIndex3));
        return this;
    }

    public SelenideElement getAveragingFunction() {
        return $(averagingFunction);
    }

    public ForecastViewerPage setAveragingFunction(String value) {
        setSelectionOption(value, $(averagingFunction));
        return this;
    }

    public SelenideElement getScoreIntervalCount() {
        return $(scoreIntervalCount);
    }

    public ForecastViewerPage setScoreIntervalCount(String value) {
        setNumericOption(value, $(scoreIntervalCount));
        return this;
    }

    public SelenideElement getScoreIntervalUnit() {
        return $(scoreIntervalUnit);
    }

    public ForecastViewerPage setScoreIntervalUnit(String value) {
        SelenideElement intervalInput = $(scoreIntervalUnit).$(By.xpath(".."));
        intervalInput.$(By.tagName("button")).click();
        intervalInput.$(By.className("dropdown-menu")).$(By.partialLinkText(value)).click();
        return this;
    }

    public SelenideElement getStartDate() {
        return $(startDate);
    }

    public ForecastViewerPage setStartDate(String value) {
        setNumericOption(value, $(startDate));
        return this;
    }

    public SelenideElement getStartTime() {
        return $(startTime);
    }

    public ForecastViewerPage setStartTime(String value) {
        setNumericOption(value, $(startTime));
        return this;
    }

    public SelenideElement getEndDate() {
        return $(endDate);
    }

    public ForecastViewerPage setEndDate(String value) {
        setNumericOption(value, $(endDate));
        return this;
    }

    public SelenideElement getEndTime() {
        return $(endTime);
    }

    public ForecastViewerPage setEndTime(String value) {
        setNumericOption(value, $(endTime));
        return this;
    }

    public SelenideElement getForecastHorizonCount() {
        return $(forecastHorizonCount);
    }

    public ForecastViewerPage setForecastHorizonCount(String value) {
        setNumericOption(value, $(forecastHorizonCount));
        return this;
    }

    public SelenideElement getForecastHorizonUnit() {
        return $(forecastHorizonUnit);
    }

    public ForecastViewerPage setForecastHorizonUnit(String value) {
        SelenideElement intervalInput = $(forecastHorizonUnit).$(By.xpath(".."));
        intervalInput.$(By.tagName("button")).click();
        intervalInput.$(By.className("dropdown-menu")).$(By.partialLinkText(value)).click();
        return this;
    }

    public ForecastViewerPage setNumericOption(String value, SelenideElement element) {
        element.setValue(value);
        return this;
    }

    public ForecastViewerPage setSelectionOption(String value, SelenideElement element) {
        element.selectOption(value);
        return this;
    }

}
