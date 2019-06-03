package com.axibase.webtest.service;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CodeEditor {
    private SelenideElement codeMirrorFieldElement;

    public CodeEditor(WebElement relatedTextArea) {
        if (!relatedTextArea.getTagName().equals("textarea")) {
            throw new IllegalStateException("this is not a textarea");
        }

        WebElement codeMirrorElement = relatedTextArea.findElement(By.xpath("./following-sibling::*[contains(@class,CodeMirror)]"));
        this.codeMirrorFieldElement = Selenide.element((WebElement) CommonActions.executeWithElement(codeMirrorElement,
                "return element.CodeMirror.getInputField()"));
    }

    public void sendKeys(String command) {
        codeMirrorFieldElement.sendKeys(command);
    }

}
