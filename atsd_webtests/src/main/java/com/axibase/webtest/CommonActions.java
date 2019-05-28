package com.axibase.webtest;

import com.codeborne.selenide.SelenideElement;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;

import java.net.URISyntaxException;
import java.time.Duration;
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
        // If the confirm dialog is not confirmed
        if ($(By.className("btn-confirm")).exists()) {
            $(By.className("btn-confirm")).click();
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
     * Create encoded URL with params from map
     *
     * @param URLPrefix - prefix without params
     * @param params    - string params
     * @return - new URL
     */
    public static String createNewURL(String URLPrefix, Map<String, String> params) {
        try {
            final URIBuilder uriBuilder = new URIBuilder().setPath(URLPrefix);
            params.forEach(uriBuilder::addParameter);
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URL", e);
        }
    }

    /**
     * Get all values in the specified column
     *
     * @param table      - the table with tags
     * @param columnName - column name it the header of the table
     * @return - column values
     */
    public static String[] getColumnValuesByColumnName(SelenideElement table, String columnName) {
        int index = table.$$("thead > tr > th").stream()
                .map(SelenideElement::text).collect(Collectors.toList()).indexOf(columnName);

        return table.$$("tbody > tr > td:nth-child(" + (index + 1) + "n)")
                .stream()
                .map(SelenideElement::text)
                .toArray(String[]::new);
    }

}
