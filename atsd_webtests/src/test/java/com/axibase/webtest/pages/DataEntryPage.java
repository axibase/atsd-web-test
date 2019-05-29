package com.axibase.webtest.pages;

import com.axibase.webtest.service.CodeEditor;
import com.axibase.webtest.service.InvalidDataEntryCommandException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DataEntryPage {
    private final static String BASE_URL = "/metrics/entry";

    private final static By SEND_BUTTON = By.cssSelector("button[value=send]");

    public DataEntryPage() {
        open(BASE_URL);
    }

    public DataEntryPage sendCommands(String... commands) {
        typeCommands(commands);
        $(SEND_BUTTON).click();
        if (isCommandFailed()) {
            throw new InvalidDataEntryCommandException($(".alert-error").text());
        }
        return this;
    }

    private boolean isCommandFailed() {
        return !$("form[action='/metrics/entry']").text().contains("commands successfully processed");
    }

    private void typeCommands(String[] commands) {
        CodeEditor codeEditor = new CodeEditor($(By.name("commands")));
        codeEditor.sendKeys(StringUtils.join(commands, " \n"));
    }

}
