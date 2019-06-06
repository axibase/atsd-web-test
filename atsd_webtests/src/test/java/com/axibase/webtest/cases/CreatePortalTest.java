package com.axibase.webtest.cases;

import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.service.AtsdTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.axibase.webtest.CommonConditions.clickable;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class CreatePortalTest extends AtsdTest {

    @Test
    public void createPortal() {
        $(By.linkText("Portals")).click();
        boolean panelVisible = $(By.xpath("//h4[normalize-space(text())='Portals']")).isDisplayed();
        assertTrue(generateAssertMessage("Portal panel should be visible"), panelVisible);

        $(By.linkText("Create")).click();
        CommonAssertions.assertPageTitleAfterLoadEquals("New Portal");

        $(By.id("name")).sendKeys("Test Portal");
        String config = "[configuration]\\n" +
                "  height-units = 2\\n" +
                "  width-units = 2\\n" +
                "  time-span = 12 hour\\n" +
                "\\n" +
                "[group]\\n" +
                "\\n" +
                "  [widget]\\n" +
                "    type = chart\\n" +
                "    [series]\\n" +
                "      entity = my-entity\\n" +
                "      metric = my-metric\\n" +
                "\\n" +
                "  [widget]\\n" +
                "    type = gauge\\n" +
                "    thresholds = 0, 60, 80, 100\\n" +
                "    [series]\\n" +
                "      entity = my-entity\\n" +
                "      metric = my-metric";

        executeJavaScript("document.querySelector('.CodeMirror').CodeMirror.setValue('" + config + "');");
        $(By.id("save-button")).shouldBe(clickable).click();
        $(By.id("save-button")).shouldHave(cssClass("btn-success"));
        CommonAssertions.assertPageTitleAfterLoadEquals("Portal Test Portal");
        $(By.id("view-button")).shouldBe(clickable).click();
        $(By.id("view-name-button")).shouldBe(clickable).click();
        final WebDriver driver = getWebDriver();
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        assertEquals(generateAssertMessage("Exactly 2 new tabs must be opened"), 3, tabs.size());
        for (int i = 1; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));
            assertNotEquals(0, $$(By.className("widget")).size(), generateAssertMessage("No widgets for portal"));
            driver.close();
        }
        driver.switchTo().window(tabs.get(0));
        // The size of the window changes after close -> switch
        driver.manage().window().setSize(new Dimension(1280, 720));
    }

}