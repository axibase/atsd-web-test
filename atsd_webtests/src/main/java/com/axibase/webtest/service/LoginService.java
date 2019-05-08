package com.axibase.webtest.service;


import org.openqa.selenium.By;

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
        $("#atsd_user").setValue(login);
        $("#atsd_pwd").setValue(password);
        $(By.name("commit")).click();
        return "Axibase Time Series Database".equals(title());
    }

    public boolean logout() {
        open(LOGOUT_URL);
        return LOGIN_URL.equals(urlPath());
    }
}
