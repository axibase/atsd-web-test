package com.axibase.webtest;

import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class ElementUtils {
    public static void goToNextWindow() {
        WebDriver driver = getWebDriver();
        driver.close();
        for (String windowName: driver.getWindowHandles()) {
            driver.switchTo().window(windowName);
        }
    }
}
