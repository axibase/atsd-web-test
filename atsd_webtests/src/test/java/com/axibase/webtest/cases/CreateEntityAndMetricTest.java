package com.axibase.webtest.cases;

import com.axibase.webtest.service.AtsdTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.*;

public class CreateEntityAndMetricTest extends AtsdTest {

    @Test
    public void createEntityAndMetric() {
        $(By.xpath("//a/span[normalize-space(text())='Data']/..")).click();
        boolean submenuVisible = $(By.xpath("//h4[normalize-space(text())='Data']")).isDisplayed();
        assertTrue(generateAssertMessage("Submenu should be visible"), submenuVisible);

        $(By.xpath("//a[normalize-space(text())='Data Entry']")).click();
        assertEquals(generateAssertMessage("Title should be 'Data Entry'"), "Data Entry", title());

        $(By.id("seriesType")).click();
        $(By.name("entity")).sendKeys("my-entity");
        $(By.name("metric")).sendKeys("my-metric");

        $(By.name("value")).sendKeys("50");
        $(By.name("series")).click();
        WebElement successMessage = $(By.cssSelector("form.series .form-status>.alert-success"));
        assertEquals(generateAssertMessage("Wrong success message"), "Series inserted successfully", successMessage.getText().trim());

        $(By.name("value")).sendKeys("150");
        $(By.name("series")).click();
        successMessage = $(By.cssSelector("form.series .form-status>.alert-success"));
        assertEquals(generateAssertMessage("Wrong success message"), "Series inserted successfully", successMessage.getText().trim());

        open("/portals/series?entity=my-entity&metric=my-metric");
        assertNotEquals(generateAssertMessage("No widgets for portal"), 0, $$(By.className("widget")).size());
    }
}
