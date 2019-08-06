package com.axibase.webtest.service;


import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.codeborne.selenide.Selenide.*;

public class LoginService extends Service {
    public static final String TITLE = "Login";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGIN_URL = "/login";

    public boolean loginAsAdmin() {
        final Config config = Config.getInstance();
        return login(config.getLogin(), config.getPassword());
    }

    public boolean login(String login, String password) {
        $(By.id("atsd_user")).setValue(login);
        $(By.id("atsd_pwd")).setValue(password);
        $(By.name("commit")).click();
        return "Axibase Time Series Database".equals(title());
    }

    public boolean logout() {
        open(LOGOUT_URL);
        handleOnBeforeUnload();
        return LOGIN_URL.equals(urlPath());
    }

    private void handleOnBeforeUnload() {
        try {
            Alert onBeforeUnload = Wait()
                    .withTimeout(Duration.ofMillis(50))
                    .pollingEvery(Duration.ofMillis(10))
                    .until(ExpectedConditions.alertIsPresent());
            onBeforeUnload.accept();
        } catch (NoAlertPresentException | TimeoutException e) {
            // It is OK, no onBeforeUnload hook is set
        }
    }
}
