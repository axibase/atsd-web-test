package com.axibase.webtest;

import com.codeborne.selenide.SelenideElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.axibase.webtest.CommonConditions.clickable;
import static com.codeborne.selenide.Selenide.*;

public class CommonActions {

    /**
     * Clicks drop-down arrow and then clicks <b>Import</b> button.
     */
    public static void clickImport() {
        $(By.className("caret")).click();
        $(By.linkText("Import")).click();
    }

    /**
     * Clicks <b>Delete</b> button and confirms item delete in dialog window.
     */
    public static void deleteRecord() {
        $(By.className("btn-edit"))
                .$(By.className("caret")).click();
        $(By.xpath("//*[.='Delete'] | " +
                "//*[@value='Delete']")).click();
        $(By.className("btn-confirm"))
                .shouldBe(clickable)
                .click();
        if ($(By.className("btn-confirm")).exists()) {
            $(By.className("btn-confirm")).click();
            System.out.println("oooops");

        }
        Wait().withTimeout(Duration.ofSeconds(2))
                .until(condition -> !$(By.className("btn-confirm")).exists());

    }

    /**
     * Sets <b>Select All</b> option and deletes records.
     */
    public static void deleteAllRecords() {
        // i means case insensitive. There is "Select all" at "csv/configs", and "Select All" at other pages.
        $("input[title='select all' i]").setSelected(true);
        deleteRecord();
    }

    /**
     * Find CodeMirror editor window and send text to it
     *
     * @param relatedTextArea - next to the CodeMirror element
     * @param command         - text to send
     */
    public static void sendTextToCodeMirror(SelenideElement relatedTextArea, String command) {
        if (!relatedTextArea.getTagName().equals("textarea")) {
            throw new IllegalStateException("this is not a textarea");
        }
        actions().sendKeys(relatedTextArea.$(By.xpath("./following-sibling::*[contains(@class,CodeMirror)]")),
                command).build().perform();
    }

    /**
     * Uploads specified file via <b>Choose File</b> button.
     *
     * @param file file to be uploaded
     */
    public static void uploadFile(String file) {
        $("input[type='file']").sendKeys(file);
        $("input[type='submit']").click();
    }

    /**
     * Create URL with params from map
     *
     * @param URLPrefix -  prefix without params
     * @param params    - string params
     * @return - new URL
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
     * @param URLPrefix -  prefix without params
     * @return - new URL
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
     * @param URLPrefix   - prefix without params
     * @param paramKeys   - string representation of key parameters
     * @param paramValues - string representation of value parameters
     * @return - new URL
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

    /**
     * Find checkbox by its value and select it
     *
     * @param value - value of the checkbox
     */
    public static void clickCheckboxByValueAttribute(String value) {
        String xpath = String.format("//*/input[@type='checkbox' and @value='%s']", value);
        $(By.xpath(xpath)).click();
    }

    /**
     * get all tags in the table
     *
     * @param table - the table with tags
     * @return - string representation of tags in the table
     */
    public static String getValuesInTable(SelenideElement table) {
        return table
                .$$("tbody > tr > td:nth-child(2n)")
                .stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList())
                .toString();
    }

}
