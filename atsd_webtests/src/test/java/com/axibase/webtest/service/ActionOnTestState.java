package com.axibase.webtest.service;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.Date;

import static com.axibase.webtest.service.AccountService.CREATE_ACCOUNT_TITLE;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

@Slf4j
@RequiredArgsConstructor
public class ActionOnTestState extends TestWatcher {
    @Override
    protected void failed(Throwable e, Description description) {
        String testClass = description.getTestClass().getSimpleName();
        String method = description.getMethodName();
        final String fileName = screenshot(testClass + "_" + method + "_" + ISO8601Utils.format(new Date()));
        log.info("Screenshot saved to '{}'", fileName);
    }

    @Override
    protected void finished(Description description) {
        new LoginService().logout();
    }

    @Override
    protected void starting(Description description) {
        if (!CreateAdminAccountTest.class.equals(description.getTestClass())) {
            createAdminUserIfItDoesNotExist();
        }
    }

    private void createAdminUserIfItDoesNotExist() {
        if (!hasWebDriverStarted()) {
            open("/");
        }
        if (CREATE_ACCOUNT_TITLE.equals(title())) {
            final AccountService accountService = new AccountService();
            if (!accountService.createAdmin()) {
                throw new IllegalStateException("Admin account failed to be created");
            }
            new LoginService().logout();
        }
    }
}