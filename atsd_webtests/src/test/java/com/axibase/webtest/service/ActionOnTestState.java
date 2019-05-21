package com.axibase.webtest.service;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Date;

import static com.axibase.webtest.service.AccountService.CREATE_ACCOUNT_TITLE;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

@Slf4j
@RequiredArgsConstructor
public class ActionOnTestState implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        final String testClass = result.getTestClass().getRealClass().getSimpleName();
        final String method = result.getMethod().getMethodName();
        final String fileName = screenshot(testClass + "_" + method + "_" + ISO8601Utils.format(new Date()));
        log.info("Screenshot saved to '{}'", fileName);
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (!hasWebDriverStarted()) {
            open("/");
        }
        if (!CreateAdminAccountTest.class.equals(result.getTestClass().getRealClass())) {
            createAdminUserIfItDoesNotExist();
        }
    }

    private void createAdminUserIfItDoesNotExist() {
        if (CREATE_ACCOUNT_TITLE.equals(title())) {
            final AccountService accountService = new AccountService();
            if (!accountService.createAdmin()) {
                throw new IllegalStateException("Admin account failed to be created");
            }
            new LoginService().logout();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // do nothing
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // do nothing
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // do nothing
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // do nothing
    }

    @Override
    public void onStart(ITestContext context) {
        // do nothing
    }

}