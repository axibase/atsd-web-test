package com.axibase.webtest.pageobjects;

import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.axibase.webtest.CommonActions.sendTextToCodeMirror;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DataEntryPage {
    private final String BASE_URL = "/metrics/entry";

    private By sendButton = By.cssSelector("button[value=send]");

    public DataEntryPage() {
        open(createNewURL(BASE_URL));
    }

    public DataEntryPage typeCommands(String command) {
        sendTextToCodeMirror($(By.name("commands")), command);
        return this;
    }

    public DataEntryPage sendCommands() {
        $(sendButton).click();
        return this;
    }

    public boolean isCommandInserted(){
        return $("form[action='/metrics/entry']").text().contains("commands successfully processed");
    }

}
