package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class EntitiesTablePage implements Table {
    private final String BASE_URL = "/entities";

    private By searchQuery = By.id("searchQuery");

    public EntitiesTablePage() {
        open(createNewURL(BASE_URL));
    }

    @Override
    public boolean isRecordPresent(String name) {
        String xpathToEntity = String.format("//*[@id='entitiesList']//a[text()='%s']", name);
        try {
            Wait()
                    .withTimeout(Duration.ofSeconds(2))
                    .until(condition -> $(By.xpath(xpathToEntity)).exists());
        } catch (TimeoutException th) {
            refresh();
        }
        return !$$(By.xpath(xpathToEntity)).isEmpty();
    }

    @Override
    public void searchRecordByName(String name) {
        WebElement searchQueryElement = $(searchQuery);
        CommonActions.setValueOption(name, searchQueryElement);
        searchQueryElement.submit();
    }

}
