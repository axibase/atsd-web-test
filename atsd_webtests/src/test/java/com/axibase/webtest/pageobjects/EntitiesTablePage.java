package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class EntitiesTablePage implements Table {
    private static final String BASE_URL = "/entities";

    private final By searchQuery = By.id("searchQuery");

    public EntitiesTablePage() {
        open(BASE_URL);
    }

    @Override
    public boolean isRecordPresent(String name) {
        searchRecordByName(name);
        String xpathToEntity = String.format("//*[@id='entitiesList']//a[text()='%s']", name);
        try {
            Wait().withTimeout(Duration.ofSeconds(2))
                    .until(condition -> $(By.xpath(xpathToEntity)).exists());
        } catch (TimeoutException th) {
            refresh();
        }
        return !$$(By.xpath(xpathToEntity)).isEmpty();
    }

    @Override
    public void searchRecordByName(String name) {
        $(searchQuery).setValue(name).submit();
    }

}
