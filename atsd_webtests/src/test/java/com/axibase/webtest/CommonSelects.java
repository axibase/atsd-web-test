package com.axibase.webtest;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CommonSelects {

    /**
     * Find element's label by its for attribute
     *
     * @param element - element that linked with tooltip
     * @return - label with tooltip
     */
    @Step("Get element's tooltip")
    public static WebElement getElementTooltipByFor(WebElement element) {
        String elementsTag = element.getTagName();
        if (!(elementsTag.equals("input") || elementsTag.equals("select") || elementsTag.equals("textarea"))) {
            throw new IllegalStateException("Wrong selector");
        }
        return element.findElement(By.xpath("ancestor::label|" +
                "//*/label[@for='" + element.getAttribute("id") + "']"));

    }

    /**
     * Get interval in the next format: count_value-unit_value
     *
     * @param countElement - element that is contained count value
     * @param unitElement  - element that is contained unit value
     * @return - string that is represented formatted interval value
     */
    public static String getFormattedInterval(WebElement countElement, WebElement unitElement) {
        return countElement.getAttribute("value") + "-" + unitElement.getAttribute("value");
    }

}
