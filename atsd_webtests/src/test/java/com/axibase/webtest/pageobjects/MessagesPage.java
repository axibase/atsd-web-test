package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class MessagesPage {
    private static final String BASE_URL = "/messages";

    private By type = By.id("type");
    private By source = By.id("source");
    private By entity = By.id("entity");
    private By severity = By.id("severity");
    private By messagesList = By.id("messagesList");
    private By search = By.xpath("//*/button[text()='Search']");
    private By tableHeader = By.className("panel__header");

    public MessagesPage() {
        open(createNewURL(BASE_URL));
    }

    public void search() {
        $(search).click();
    }

    public MessagesPage openFilterPanel() {
        if ($(tableHeader).getAttribute("class").contains("collapsed")) {
            $(tableHeader).click();
        }
        return this;
    }

    public int getCountOfMessages() {
        return $(messagesList).findElements(By.xpath("./tbody/tr")).size();
    }

    public MessagesPage setEntity(String name) {
        CommonActions.setValueOption(name, $(entity));
        return this;
    }

    public String[] getMessagesSeverity() {
        return $$(By.xpath("//td[contains(@id,'severity')]"))
                .stream()
                .map(element->element.getAttribute("data-value"))
                .toArray(String[]::new);
    }

    public MessagesPage setSeverity(String value) {
        CommonActions.setSelectionOption(value, $(severity));
        return this;
    }

    public MessagesPage setSource(String value) {
        CommonActions.setValueOption(value, $(source));
        return this;
    }

    public MessagesPage setType(String value) {
        CommonActions.setSelectionOption(value, $(type));
        return this;
    }

}
