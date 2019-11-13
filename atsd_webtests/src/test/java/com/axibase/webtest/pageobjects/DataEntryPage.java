package com.axibase.webtest.pageobjects;

import com.axibase.webtest.service.CodeEditor;
import com.axibase.webtest.service.InvalidDataEntryCommandException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.sendTextToCodeMirror;
import static com.codeborne.selenide.Selenide.*;

public class DataEntryPage {
    private static final String BASE_URL = "/metrics/entry";

    private final By sendButton = By.cssSelector("button[value=send]");
    private final By formStatuses = By.cssSelector("form.commands .form-status");

    public DataEntryPage() {
        open(BASE_URL);
    }

    public DataEntryPage typeCommands(String command) {
        sendTextToCodeMirror($(By.name("commands")), command);
        return this;
    }

    public DataEntryPage sendCommands() {
        $(sendButton).click();
        return this;
    }

    public boolean isCommandInserted() {
        return $$(formStatuses).first().text().contains("Malformed commands: 0.");
    }

    public boolean isCommandValidated(int expectedCount) {
        return $$(formStatuses).first().text().contains("Valid commands: " + expectedCount + ".");
    }

    public DataEntryPage openHelpCommands() {
        By commandsHelp = By.cssSelector("span[data-target=\"#commandsHelpPanel\"]");
        if ($(commandsHelp).getAttribute("data-toggle").contains("collapse")) {
            $(commandsHelp).click();
        }
        return this;
    }

    public String getCommandsWindowText() {
        return $(".CodeMirror-code").text();
    }

    public DataEntryPage copyExampleByIndex(int index) {
        $$("#commandsHelpText > span").get(index).findAll(By.xpath("./span")).last().click();
        return this;
    }

    public DataEntryPage validate() {
        $("button[value=validate]").click();
        return this;
    }

    public DataEntryPage sendCommands(String... commands) {
        typeCommands(commands);
        $(sendButton).click();
        if (isCommandFailed()) {
            throw new InvalidDataEntryCommandException($(".alert-error").text());
        }
        return this;
    }

    private boolean isCommandFailed() {
        return $$(".put-form.commands .alert-error").size() > 0;
    }

    private void typeCommands(String[] commands) {
        CodeEditor codeEditor = new CodeEditor($(By.name("commands")));
        codeEditor.sendKeys(StringUtils.join(commands, " \n"));
    }

}
