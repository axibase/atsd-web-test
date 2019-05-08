package com.axibase.webtest.service;

import org.junit.Before;
import org.junit.Rule;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.WebDriverRunner.url;

public abstract class AtsdTest {
    static {
        Config.getInstance().init();
    }

    @Rule
    public final ActionOnTestState action = new ActionOnTestState();

    @Before
    public void setUp() {
        login();
    }

    public static String generateAssertMessage(String thread) {
        return thread + "\n" + "url: " + url() + "\n";
    }

    private void login() {
        open("/");
        if (LoginService.TITLE.equals(title())) {
            LoginService ls = new LoginService();
            if (!ls.loginAsAdmin()) {
                throw new BadLoginException("Can not login as admin");
            }
        } else {
            throw new BadLoginException("Expected login page title is '" + LoginService.TITLE + "' but there is '" + title());
        }
    }
}
