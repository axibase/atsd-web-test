package com.axibase.webtest.service;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.WebDriverRunner.url;

@Listeners(ActionOnTestState.class)
public abstract class AtsdTest {
    static {
        Config.getInstance().init();
    }

    @BeforeMethod
    public void setUp() {
        login();
    }

    @AfterMethod(alwaysRun = true)
    public void logout() {
        new LoginService().logout();
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
            throw new BadLoginException("Expected login page title is '" + LoginService.TITLE + "' but actual is '" + title() + "'");
        }
    }

}
