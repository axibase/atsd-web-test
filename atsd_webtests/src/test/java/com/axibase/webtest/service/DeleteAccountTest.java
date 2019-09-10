package com.axibase.webtest.service;

import com.axibase.webtest.CommonAssertions;
import io.qameta.allure.Flaky;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class DeleteAccountTest extends AtsdTest {
    @Test
    @Flaky
    public void deleteUser() {
        open("/admin/users/edit.xhtml");
        CommonAssertions.assertPageTitleEquals("New User");
        String testUser = "axiuser-" + System.currentTimeMillis();
        final AccountService accountService = new AccountService();
        assertTrue(generateAssertMessage("Can't create account"), accountService.createUser(testUser, testUser));
        CommonAssertions.assertPageTitleAfterLoadEquals("User: " + testUser);
        assertTrue(generateAssertMessage("Can't delete account '" + testUser + "'"), accountService.deleteUser(testUser));
        CommonAssertions.assertPageTitleAfterLoadEquals("Users");

    }

}
