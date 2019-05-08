package com.axibase.webtest.service;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class AccountService extends Service {
    public static final String CREATE_ACCOUNT_TITLE = "Create Account";

    public boolean createAdmin() {
        final Config config = Config.getInstance();
        return createUser(config.getLogin(), config.getPassword());
    }

    public boolean createUser(String login, String password) {
        $(By.id("userBean.username")).setValue(login);
        $(By.id("userBean.password")).setValue(password);
        $(By.id("repeatPassword")).setValue(password);
        $(By.xpath("//input[@type='submit']")).click();
        final String errors = $$(".field__error").stream()
                .map(WebElement::getText)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(", "));
        if (StringUtils.isNotEmpty(errors)) {
            throw new IllegalStateException(errors);
        }
        return true;
    }

    @SneakyThrows
    public boolean deleteUser(String login) {
        if (title().equals("User " + login)) {
            final SelenideElement deleteButton = $(By.name("delete"));
            final List<WebElement> dropdownToggle = deleteButton.findElements(By.xpath("../../../button[@data-toggle='dropdown']"));
            if (!dropdownToggle.isEmpty()) {
                dropdownToggle.get(0).click();
            }
            deleteButton.click();

            $(".btn-confirm")
                    .should(Condition.appear)
                    .shouldHave(Condition.text("Yes"))
                    .click();
            return true;
        }
        return false;
    }
}
