package com.axibase.webtest.service;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.axibase.webtest.CommonActions.executeWithElement;

public class CodeEditor {
    private SelenideElement codeMirrorFieldElement;

    public CodeEditor(WebElement relatedTextArea) {
        if (!relatedTextArea.getTagName().equals("textarea")) {
            throw new IllegalStateException("this is not a textarea");
        }

        WebElement codeMirrorElement = relatedTextArea.findElement(By.xpath("./following-sibling::*[contains(@class,CodeMirror)]"));
        this.codeMirrorFieldElement = Selenide.element((WebElement) executeWithElement(codeMirrorElement,
                "return element.CodeMirror.getInputField()"));
    }

    public void setValue(String command) {
        codeMirrorFieldElement.setValue(command);
    }

    public String text(){
        return codeMirrorFieldElement.text();
    }

}
