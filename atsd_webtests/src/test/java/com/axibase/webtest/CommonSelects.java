package com.axibase.webtest;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CommonSelects {

    /**
     * Find element's label by its for attribute
     *
     * @param element - element that linked with tooltip
     * @return - label with tooltip
     */
    @Step("Get element's tooltip")
    public static SelenideElement getElementTooltipByFor(SelenideElement element) {
        String elementsTag = element.getTagName();
        if (!(elementsTag.equals("input") || elementsTag.equals("select") || elementsTag.equals("textarea"))) {
            throw new IllegalStateException("Wrong selector");
        }
        return element.$(By.xpath("ancestor::label|" +
                "//*/label[@for='" + element.getAttribute("id") + "']"));
    }

    /**
     * Get interval in the next format: count_value-unit_value
     *
     * @param countElement - element that is contained count value
     * @param unitElement  - element that is contained unit value
     * @return - string that is represented formatted interval value
     */
    public static String getFormattedInterval(SelenideElement countElement, SelenideElement unitElement) {
        return countElement.getValue() + "-" + unitElement.getValue();
    }


    /**
     * Get value of the switch element (true/false)
     *
     * @param element - the switch element
     * @return - value of the switch element
     */
    public static boolean getValueOfSwitchElement(SelenideElement element) {
        return CommonActions.executeWithElement(element, "return element.checked");
    }

}
