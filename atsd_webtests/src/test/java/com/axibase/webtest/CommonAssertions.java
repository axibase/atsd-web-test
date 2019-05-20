package com.axibase.webtest;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.axibase.webtest.service.AtsdTest.generateAssertMessage;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.*;

public class CommonAssertions {

    /**
     * Check that element passes HTML validation
     *
     * @param errorMessage - message that will be shown if element is invalid
     * @param element      - element that will be checked
     */
    public static void assertValid(String errorMessage, SelenideElement element) {
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
    public static void assertInvalid(String errorMessage, SelenideElement element) {
        String script = "return element.checkValidity()";
        Boolean result = executeWithElement(element, script);
        assertFalse(errorMessage, result);
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
                .until(driver -> driver.getTitle().equals(expectedTitle));
    }

    /**
     * Execute JavaScript with element as a parameter.
     *
     * @param element element that will be used in script
     * @param script  script to be executed
     * @param <T>     type of returned expression
     * @return script execution result
     */
    public static <T> T executeWithElement(SelenideElement element, String script) {
        String iifeScript = "return (function (element) {" + script + ";})(arguments[0])";
        return executeJavaScript(iifeScript, element);
    }


    /**
     * Check that string contains all passed values
     *
     * @param errorMessage message that will be shown if the string doesn't contains a value
     * @param values       values that must be a part of full string
     * @param string       main string that have to contain all values
     */
    @Step("Check if the given string contains \"{values}\" ")
    public static void assertStringContainsValues(String errorMessage, String[] values, String string) {
        for (String value : values) {
            assertTrue(errorMessage + value, string.contains(value));
        }
    }

}
