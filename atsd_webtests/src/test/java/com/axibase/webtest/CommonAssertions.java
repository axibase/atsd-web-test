package com.axibase.webtest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static com.axibase.webtest.CommonActions.executeWithElement;
import static com.axibase.webtest.PageUtils.urlPath;
import static com.axibase.webtest.service.AtsdTest.generateAssertMessage;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.title;
import static org.testng.AssertJUnit.*;

public class CommonAssertions {

    /**
     * Check that element passes HTML validation
     *
     * @param errorMessage - message that will be shown if element is invalid
     * @param element      - element that will be checked
     */
    public static void assertValid(String errorMessage, WebElement element) {
        String script = "return element.checkValidity()";
        Boolean result = executeWithElement(element, script);
        assertTrue(errorMessage, result);
    }

    /**
     * Check that element doesn't passes HTML validation
     *
     * @param errorMessage - message that will be shown if element is valid
     * @param element      - element that will be checked
     */
    public static void assertInvalid(String errorMessage, WebElement element) {
        String script = "return element.checkValidity()";
        Boolean result = executeWithElement(element, script);
        assertFalse(errorMessage, result);
    }

    /**
     * Compare element's value and expected value
     *
     * @param errorMessage  - message that will be shown if element is valid
     * @param expectedValue - expected value
     * @param element       - element that will be checked
     */
    public static void assertValueAttributeOfElement(String errorMessage, String expectedValue, WebElement element) {
        assertEquals(errorMessage, expectedValue, element.getAttribute("value"));
    }

    /**
     * Compare current url with expected
     *
     * @param expectedUrl - expected URL of the target page
     */
    public static void assertPageUrlPathEquals(String expectedUrl) {
        assertEquals("Wrong page", expectedUrl, urlPath());
    }

    /**
     * Compare current page title with expected value
     *
     * @param expectedTitle expected title
     */
    public static void assertPageTitleEquals(String expectedTitle) {
        assertEquals(generateAssertMessage("Title should be '" + expectedTitle + "'"), expectedTitle, title());
    }

    /**
     * Compare page title with expected value. If page is not ready, method will wait up to 3 seconds.
     *
     * @param expectedTitle expected title
     */
    public static void assertPageTitleAfterLoadEquals(String expectedTitle) {
        Wait().withMessage(() -> "Page title must be set to " + expectedTitle)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofMillis(200))
                .until(ExpectedConditions.titleIs(expectedTitle));
    }

}
