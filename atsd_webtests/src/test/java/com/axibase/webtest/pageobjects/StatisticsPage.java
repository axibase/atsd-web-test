package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class StatisticsPage {
    private final String BASE_URL = "/series/statistics";

    public StatisticsPage(String[] paramKeys, String[] paramValues) {
        open(createNewURL(BASE_URL, paramKeys, paramValues));
    }

    public String getSeriesTags() {
        return $(By.xpath("//*/caption[contains(text(),'Series')]")).findElement(By.xpath("./..")).getText();
    }

    public void getSampleDataTab() {
        $(By.xpath("//*/a[text()='Sample Data']")).click();
    }

    public String getSampleDataTableText() {
        return $(By.xpath("//*[@id='sample-data']/table")).getText();
    }

}
