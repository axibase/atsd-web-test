package com.axibase.webtest.service;

import com.axibase.webtest.CommonAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by sild on 02.02.15.
 */
public class LoginServiceTest extends AtsdTest {
    @BeforeMethod
    @Override
    public void setUp() {
        // skip sign in
    }

    @Test
    public void testLogin() {
        CommonAssertions.assertPageTitleEquals(LoginService.TITLE);
        LoginService ls = new LoginService();
        assertTrue("Can't login on page", ls.loginAsAdmin());
    }

    @Test
    public void wrongLogin() {
        CommonAssertions.assertPageTitleEquals(LoginService.TITLE);
        final LoginService ls = new LoginService();
        final String wrongLogin = "123";
        final String wrongPassword = "123";
        final String rightLogin = Config.getInstance().getLogin();
        final String rightPassword = Config.getInstance().getPassword();
        assertFalse("Should return to login page with wrong login", ls.login(wrongLogin, rightPassword));
        assertFalse("Should return to login page with wrong password", ls.login(rightLogin, wrongPassword));
    }

    @Test
    public void testLogout() {
        CommonAssertions.assertPageTitleEquals(LoginService.TITLE);
        LoginService ls = new LoginService();
        assertTrue("Can't login on page", ls.loginAsAdmin());
        assertTrue("Can't logout from page", ls.logout());
    }

}
