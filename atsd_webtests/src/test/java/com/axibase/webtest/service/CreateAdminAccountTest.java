package com.axibase.webtest.service;


import com.axibase.webtest.CommonAssertions;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.axibase.webtest.service.AccountService.CREATE_ACCOUNT_TITLE;
import static com.codeborne.selenide.Selenide.title;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class CreateAdminAccountTest extends AtsdTest {
    private AccountService accountService;

    @Before
    @Override
    public void setUp() {
        accountService = new AccountService();
    }

    @Test
    public void createAdminUser() {
        if (!LoginService.TITLE.equals(title())) {
            log.info("Trying to create admin...");
            CommonAssertions.assertPageTitleEquals(CREATE_ACCOUNT_TITLE);
            assertTrue(generateAssertMessage("Can't create account"), accountService.createAdmin());
            assertEquals(generateAssertMessage("Should get redirect on home page"), "/", urlPath());
        } else {
            log.info("User already created");
        }
    }
}
