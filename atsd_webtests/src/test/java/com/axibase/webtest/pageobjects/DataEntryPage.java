package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.sendTextToCodeMirror;
import static com.codeborne.selenide.Selenide.*;

public class DataEntryPage {
    private final String BASE_URL = "/metrics/entry";

    private By sendButton = By.cssSelector("button[value=send]");
    private By formStatuses = By.cssSelector("form.commands .form-status");

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

    public boolean isCommandInserted(int count) {
        return $$(formStatuses).first().text().contains(count + " commands successfully processed");
    }

    public boolean isCommandValidated() {
        return $$(formStatuses).first().text().equals("All commands are valid");
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

}
