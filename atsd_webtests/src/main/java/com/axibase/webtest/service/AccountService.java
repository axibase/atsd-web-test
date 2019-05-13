package com.axibase.webtest.service;


import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    public boolean deleteUser(String login) {
        if (title().equals("User " + login)) {
            $(By.xpath("//*/button/span[@class='caret']")).click();
            $(By.xpath("//*/input[@type='submit' and @value='Delete']")).click();
            $(".btn-confirm")
                    .should(Condition.appear)
                    .shouldHave(Condition.text("Yes"))
                    .click();
            return true;
        }
        return false;
    }
}
