package com.axibase.webtest.cases;

import com.axibase.webtest.service.AtsdTest;
import com.axibase.webtest.service.LoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CreatePortalTest extends AtsdTest {

    @Before
    public void setUp() {
        login();
    }

    @Test
    public void createPortal() {
        driver.findElement(By.xpath("//a[normalize-space(text())='Portals']")).click();
        boolean panelVisible = driver.findElement(By.xpath("//h4[normalize-space(text())='Portals']")).isDisplayed();
        assertTrue(generateAssertMessage("Portal panel should be visible"), panelVisible);

        driver.findElement(By.xpath("//a[normalize-space(text())='Create']")).click();
        assertEquals(generateAssertMessage("Title should be 'New Portal'"), "New Portal", driver.getTitle());

        driver.findElement(By.id("name")).sendKeys("Test Portal");
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

        ((JavascriptExecutor) driver).executeScript("document.querySelector('.CodeMirror').CodeMirror.setValue('" + config + "');");
        driver.findElement(By.id("save-button")).click();
        Boolean titleChanged = new FluentWait<>(driver)
                .withTimeout(3, TimeUnit.SECONDS)
                .pollingEvery(50, TimeUnit.MILLISECONDS)
                .withMessage("Title should be 'Portal Test Portal'")
                .until(ExpectedConditions.titleIs("Portal Test Portal"));
        assertTrue(generateAssertMessage("Title should be 'Portal Test Portal'"), titleChanged);
        driver.findElement(By.id("view-button")).click();
        driver.findElement(By.id("view-name-button")).click();
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        assertEquals(generateAssertMessage("Exactly 2 new tabs must be opened"), 3, tabs.size());

        for (int i = 1; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));
            List<WebElement> widgets = driver.findElements(By.className("widget"));
            assertNotEquals(generateAssertMessage("No widgets for portal"), 0, widgets.size());
            driver.close();
        }
        driver.switchTo().window(tabs.get(0));
    }
}