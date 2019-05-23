package com.axibase.webtest.pageobjects;

import com.axibase.webtest.modelobjects.Series;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.*;

public class StatisticsPage {
    private final String BASE_URL = "/series/statistics";

    private By sampleDataTable = By.id("sample-data");

    private By sampleDataTab = By.xpath("//*[@href='#sample-data-tab']");
    private By seriesTab = By.xpath("//*[@href='#series-tab']");


    public StatisticsPage(String[] paramKeys, String[] paramValues) {
        open(createNewURL(BASE_URL, paramKeys, paramValues));
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
        return getCaptionTableByName("Metric").$(By.xpath(".//tr[td[text()='Name']]")).text().split(" ")[1];
    }

    public String getEntityName() {
        getSeriesTab();
        return getCaptionTableByName("Entity").$(By.xpath(".//tr[td[text()='Name']]")).text().split(" ")[1];
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
        String[] result = new String[]{};
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

}
