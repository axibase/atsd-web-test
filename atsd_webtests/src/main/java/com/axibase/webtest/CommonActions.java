package com.axibase.webtest;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.axibase.webtest.CommonConditions.clickable;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.actions;

public class CommonActions {

    /**
     * Clicks drop-down arrow and then clicks <b>Import</b> button.
     */
    public static void clickImport() {
        $(By.className("caret")).click();
        $(By.linkText("Import")).click();
    }

    /**
     * Clicks <b>Delete</b> on multi-tool button and confirms item delete in dialog window.
     */
    public static void deleteRecord() {
        $(By.className("btn-edit"))
                .$(By.className("caret")).click();
        $(byValue("Delete")).click();
        $(By.className("btn-confirm"))
                .shouldBe(clickable)
                .click();
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
     * @param text            - text to send
     */
    public static void sendTextToCodeMirror(WebElement relatedTextArea, String text) {
        if (!relatedTextArea.getTagName().equals("textarea")) {
            throw new IllegalStateException("this is not a textarea");
        }

        actions().sendKeys(relatedTextArea.findElement(By.xpath("./following-sibling::*[contains(@class,CodeMirror)]")),
                text).build().perform();
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
}
