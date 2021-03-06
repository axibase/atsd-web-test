package com.axibase.webtest.pageobjects;

import com.axibase.webtest.modelobjects.Series;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.Map;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.*;

public class StatisticsPage {
    private static final String BASE_URL = "/series/statistics";

    private final By sampleDataTable = By.id("sample-data");

    private final By sampleDataTab = By.xpath("//*[@href='#sample-data-tab']");
    private final By seriesTab = By.xpath("//*[@href='#series-tab']");


    public StatisticsPage(Map<String, String> params) {
        open(createNewURL(BASE_URL, params));
    }

    public Series getSeries() {
        return new Series().setEntityName(this.getEntityName())
                .setMetricName(this.getMetricName())
                .setMetricValue(this.getSampleDataTableValue())
                .setMetricText(this.getSampleDataTableText())
                .setTagNames(this.getSeriesTagNames())
                .setTagValues(this.getSeriesTagValues());
    }

    public String[] getSeriesTagNames() {
        getSeriesTab();
        return Arrays.stream(getSeriesTags()).map(row -> row.split(" ")[0]).toArray(String[]::new);
    }

    public String[] getSeriesTagValues() {
        getSeriesTab();
        return Arrays.stream(getSeriesTags()).map(row -> row.split(" ")[1]).toArray(String[]::new);
    }

    public String getMetricName() {
        getSeriesTab();
        return getRowsNameValueInSeriesTab("Metric");
    }

    public String getEntityName() {
        getSeriesTab();
        return getRowsNameValueInSeriesTab("Entity");
    }

    public String getSampleDataTableText() {
        getSampleDataTab();
        return getColumnValuesByColumnName($(sampleDataTable), "Text")[0];
    }

    public String getSampleDataTableValue() {
        getSampleDataTab();
        return getColumnValuesByColumnName($(sampleDataTable), "Value")[0];

    }

    private String[] getSeriesTags() {
        getSeriesTab();
        String[] result = ArrayUtils.EMPTY_STRING_ARRAY;
        if (!$$(By.xpath("//*[contains(text(),'Series Tags')]")).isEmpty()) {
            result = getCaptionTableByName("Series Tags").$("tbody").text().split("\n");
        }
        return result;
    }

    private void getSampleDataTab() {
        $(sampleDataTab).click();
    }

    private void getSeriesTab() {
        $(seriesTab).click();
    }

    private SelenideElement getCaptionTableByName(String tableName) {
        return $(By.xpath("//caption[contains(text(),'" + tableName + "')]")).parent();
    }

    private String getRowsNameValueInSeriesTab(String metric) {
        return getCaptionTableByName(metric).$(By.xpath(".//tr[td[text()='Name']]")).text().split(" ")[1];
    }

}
