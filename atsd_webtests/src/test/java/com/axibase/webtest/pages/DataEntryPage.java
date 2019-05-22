package com.axibase.webtest.pages;

import com.axibase.webtest.service.CodeEditor;
import com.axibase.webtest.service.InvalidDataEntryCommandException;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DataEntryPage {
    private final String BASE_URL = "/metrics/entry";

    private By sendButton = By.cssSelector("button[value=send]");

    public DataEntryPage() {
        open(createNewURL(BASE_URL));
    }

    public DataEntryPage sendCommands(String... commands) {
        typeCommands(commands);
        $(sendButton).click();
        if (!isCommandInserted()) {
            throw new InvalidDataEntryCommandException($(".alert-error").text());
        }
        return this;
    }

    private boolean isCommandInserted() {
        return $("form[action='/metrics/entry']").text().contains("commands successfully processed");
    }

    private void typeCommands(String[] commands) {
        StringBuilder fullCommand = new StringBuilder();
        for (String command : commands) {
            fullCommand.append(command).append(" \n");
        }
        CodeEditor codeEditor = new CodeEditor($(By.name("commands")));
        codeEditor.setValue(fullCommand.toString());
    }

}
