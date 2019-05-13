package com.axibase.webtest;

import io.qameta.allure.Step;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonActions {

    /**
     * Find CodeMirror editor window and send text to it
     *
     * @param relatedTextArea - next to the CodeMirror element
     * @param text            - text to send
     */
    @Step("Send data entry command")
    public static void sendTextToCodeMirror(WebElement relatedTextArea, String text) {
        if (!relatedTextArea.getTagName().equals("textarea")) {
            throw new IllegalStateException("This is not a textarea");
        }
        WebDriver driver = ElementUtils.getConnectedDriver(relatedTextArea);
        Actions builder = new Actions(driver);
        builder.sendKeys(relatedTextArea.findElement(By.xpath("./following-sibling::*[contains(@class,CodeMirror)]")),
                text).build().perform();
    }

    /**
     * Clear element's window and send into it value
     *
     * @param value   - value to be set
     * @param element - selected element
     */
    @Step
    public static void setValueOption(String value, WebElement element) {
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Set one of the element's selection option
     *
     * @param value   - value to be set
     * @param element - selected element
     */
    @Step
    public static void setSelectionOption(String value, WebElement element) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    /**
     * Find checkbox by its value and select it
     *
     * @param driver - current web driver
     * @param value  - value of the checkbox
     */
    public static void clickCheckboxByValueAttribute(WebDriver driver, String value) {
        String xpath = String.format("//*/input[@type='checkbox' and @value='%s']", value);
        driver.findElement(By.xpath(xpath)).click();
    }

    /**
     * Delete elements wih selected checkboxes
     *
     * @param driver- current web driver
     */
    public static void dropCheckedRecords(WebDriver driver) {
        driver.findElement(By.xpath("//*/div[@class='table-controls']//*[@data-toggle='dropdown']")).click();
        driver.findElement(By.xpath("//*/div[@class='table-controls']//a[text()='Delete']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-modal")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='confirm-modal']//button[text()='Yes']")));
        driver.findElement(By.xpath("//*[@id='confirm-modal']//button[text()='Yes']")).click();
    }

    /**
     * Create URL with params from string
     *
     * @param URLPrefix - string before params
     * @param params    - string params
     * @return - new URL from prefix and params
     */
    public static String createNewURL(String URLPrefix, Map<String, String> params) {
        List<NameValuePair> paramsForEncoding = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsForEncoding.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            return new URIBuilder(URLPrefix).addParameters(paramsForEncoding).build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URL", e);
        }
    }

    /**
     * Create URL without params from string
     *
     * @param URLPrefix - string that will be used to create URL
     * @return - new URL from prefix
     */
    public static String createNewURL(String URLPrefix) {
        try {
            return new URIBuilder(URLPrefix).build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URL", e);
        }
    }

    /**
     * Create URL with params from string
     *
     * @param URLPrefix   - string before params
     * @param paramKeys   - string representation of key parameters
     * @param paramValues - string representation of value parameters
     * @return - new URL from prefix and params
     */
    public static String createNewURL(String URLPrefix, String[] paramKeys, String[] paramValues) {
        if (paramKeys.length != paramValues.length) {
            throw new IllegalStateException("Length of parameter arrays should be equal");
        }
        List<NameValuePair> paramsForEncoding = new ArrayList<>();
        for (int i = 0; i < paramKeys.length; i++) {
            paramsForEncoding.add(new BasicNameValuePair(paramKeys[i], paramValues[i]));
        }
        try {
            return new URIBuilder(URLPrefix).addParameters(paramsForEncoding).build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URL", e);
        }
    }

}
