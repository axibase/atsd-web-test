package com.axibase.webtest.cases;

import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.service.AtsdTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.*;

public class CreateEntityAndMetricTest extends AtsdTest {

    @Test
    public void createEntityAndMetric() {
        $(By.linkText("Data")).click();
        boolean submenuVisible = $(By.xpath("//h4[normalize-space(text())='Data']")).isDisplayed();
        assertTrue(generateAssertMessage("Submenu should be visible"), submenuVisible);

        $(By.linkText("Data Entry")).click();
        CommonAssertions.assertPageTitleAfterLoadEquals("Data Entry");

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
        assertNotEquals(0, $$(By.className("widget")).size(), generateAssertMessage("No widgets for portal"));
    }
}
