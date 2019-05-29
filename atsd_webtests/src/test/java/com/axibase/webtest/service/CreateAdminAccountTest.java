package com.axibase.webtest.service;


import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.axibase.webtest.service.AccountService.CREATE_ACCOUNT_TITLE;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.title;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class CreateAdminAccountTest extends AtsdTest {
    private AccountService accountService;

    @BeforeMethod
    @Override
    public void setUp() {
        accountService = new AccountService();
    }

    @Test
    public void createAdminUser() {
        if (!LoginService.TITLE.equals(title())) {
            log.info("Trying to create admin...");
            Wait().withTimeout(Duration.ofSeconds(2)).until(ExpectedConditions.titleIs(CREATE_ACCOUNT_TITLE));
            assertTrue(generateAssertMessage("Can't create account"), accountService.createAdmin());
            assertEquals(generateAssertMessage("Should get redirect on home page"), "/", urlPath());
        } else {
            log.info("User already created");
        }
    }
}
