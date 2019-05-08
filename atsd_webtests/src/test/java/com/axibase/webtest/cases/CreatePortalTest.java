package com.axibase.webtest.cases;

import com.axibase.webtest.service.AtsdTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CreatePortalTest extends AtsdTest {

    @Test
    public void createPortal() {
        $(By.xpath("//a[normalize-space(text())='Portals']")).click();
        boolean panelVisible = $(By.xpath("//h4[normalize-space(text())='Portals']")).isDisplayed();
        assertTrue(generateAssertMessage("Portal panel should be visible"), panelVisible);

        $(By.xpath("//a[normalize-space(text())='Create']")).click();
        assertEquals(generateAssertMessage("Title should be 'New Portal'"), "New Portal", title());

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
        $(By.id("save-button")).click();
        assertEquals(generateAssertMessage("Title should be 'Portal Test Portal'"), "Portal Test Portal", title());
        $(By.id("view-button")).click();
        $(By.id("view-name-button")).click();
        final WebDriver driver = getWebDriver();
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        assertEquals(generateAssertMessage("Exactly 2 new tabs must be opened"), 3, tabs.size());

        for (int i = 1; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));
            assertNotEquals(generateAssertMessage("No widgets for portal"), 0, $$(By.className("widget")).size());
            driver.close();
        }
        driver.switchTo().window(tabs.get(0));
    }
}