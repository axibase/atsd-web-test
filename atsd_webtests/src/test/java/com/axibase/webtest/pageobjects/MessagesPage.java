package com.axibase.webtest.pageobjects;

import com.axibase.webtest.modelobjects.Message;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.*;

public class MessagesPage {
    private static final String BASE_URL = "/messages";

    private By type = By.id("type");
    private By messageType = By.xpath("//*[contains(@id,'type_')]");
    private By source = By.id("source");
    private By messageSource = By.xpath("//*[contains(@id,'source_')]");
    private By entity = By.id("entity");
    private By severity = By.id("severity");
    private By messageSeverity = By.xpath("//*[contains(@id,'severity_')]");
    private By messagesList = By.id("messagesList");
    private By search = By.xpath("//*/button[text()='Search']");
    private By tableHeader = By.className("panel__header");
    private By message = By.xpath("//*[contains(@id,'message_')]");

    public MessagesPage() {
        open(BASE_URL);
    }

    public MessagesPage search() {
        $(search).click();
        return this;
    }

    public MessagesPage openFilterPanel() {
        if ($(tableHeader).getAttribute("class").contains("collapsed")) {
            $(tableHeader).click();
        }
        return this;
    }

    public int getCountOfMessages() {
        return $(messagesList).$$(By.xpath("./tbody/tr")).size();
    }

    public MessagesPage setEntity(String name) {
        $(entity).setValue(name);
        return this;
    }

    public String getEntity() {
        openFilterPanel();
        return $(entity).val();
    }

    public String[] getMessagesSeverity() {
        return $$(By.xpath("//td[contains(@id,'severity')]"))
                .stream()
                .map(element -> element.getAttribute("data-value"))
                .toArray(String[]::new);
    }

    public MessagesPage setSeverity(String value) {
        $(severity).selectOption(value);
        return this;
    }

    public String getMessageSeverity() {
        return $(messageSeverity).getAttribute("data-value");
    }

    public MessagesPage setSource(String value) {
        $(source).setValue(value);
        return this;
    }

    public String getMessageSource() {
        return $(messageSource).text();
    }

    public MessagesPage setType(String value) {
        $(type).selectOption(value);
        return this;
    }

    public String getMessageType() {
        return $(messageType).text();
    }

    public String getMessageText() {
        return $(message).text();
    }

    public String[] getMessageTagNames() {
        String[] result = new String[]{};
        if (!getMessageTags().isEmpty()) {
            result = Arrays.stream(getMessageTags().split(","))
                    .map(tag -> StringUtils.substringBefore(tag, " = ").trim()).toArray(String[]::new);
        }
        return result;
    }

    public String[] getMessageTagValues() {
        String[] result = new String[]{};
        if (!getMessageTags().isEmpty()) {
            result = Arrays.stream(getMessageTags().split(","))
                    .map(tag -> StringUtils.substringAfter(tag, " = ").trim()).toArray(String[]::new);
        }
        return result;
    }

    public Message getMessage() {
        return new Message().setEntityName(this.getEntity())
                .setType(this.getMessageType())
                .setSource(this.getMessageSource())
                .setSeverity(this.getMessageSeverity())
                .setTagNames(this.getMessageTagNames())
                .setTagValues(this.getMessageTagValues())
                .setMessageText(this.getMessageText());
    }

    private String getMessageTags() {
        return getColumnValuesByColumnName($("#messagesList"), "Tags")[0];
    }

}
