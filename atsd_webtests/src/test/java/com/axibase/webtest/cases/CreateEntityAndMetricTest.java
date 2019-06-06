package com.axibase.webtest.cases;

import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

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
        SelenideElement successMessage = $("form.series .form-status>.alert-success");
        assertEquals(generateAssertMessage("Wrong success message"), "Series inserted successfully", successMessage.getText().trim());

        $(By.name("value")).sendKeys("150");
        $(By.name("series")).click();
        successMessage = $("form.series .form-status>.alert-success");
        assertEquals(generateAssertMessage("Wrong success message"), "Series inserted successfully", successMessage.getText().trim());

        open("/portals/series?entity=my-entity&metric=my-metric");
        assertTrue(generateAssertMessage("No widgets for portal"), $$(By.className("widget")).size() != 0);
    }

}
