package com.axibase.webtest.cases;

import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.service.AccountService;
import com.axibase.webtest.service.AtsdTest;
import io.qameta.allure.Issue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.*;

public class CreateUserGroupTest extends AtsdTest {
    private static final String TEST_USER = "axiuser";
    private AccountService accountService;

    @Before
    public void setUp() {
        super.setUp();
        open("/admin/users/edit.xhtml");
        accountService = new AccountService();
        accountService.createUser(TEST_USER, TEST_USER);
    }

    @After
    public void tearDown() {
        // Configure ATSD as it was before test
        open("/admin/users/edit.xhtml?user=" + TEST_USER);
        accountService.deleteUser(TEST_USER);
    }

    @Test
    @Issue("5179")
    public void createUserGroup() {
        open("/");

        $(By.linkText("Settings")).click();
        boolean submenuVisible = $(By.xpath("//h4[normalize-space(text())='Settings']")).isDisplayed();
        assertTrue(generateAssertMessage("Submenu should be visible"), submenuVisible);

        $(By.linkText("User Groups")).click();
        CommonAssertions.assertPageTitleAfterLoadEquals("User Groups");

        $(By.linkText("Create")).click();
        CommonAssertions.assertPageTitleAfterLoadEquals("New User Group");

        $(By.id("userGroup.name")).sendKeys("Test Group");
        $(By.name("save")).click();

        $(By.linkText("Members")).click();
        $(By.id("members0.groupMember")).click(); // Select the first user

        $(By.linkText("Entity Permissions")).click();
        $(By.id("allEntityGroupsRead")).click();

        $(By.name("update")).click();

        // Check that configuration saved correctly
        WebElement allEntitiesReadCheckbox = $(By.id("allEntityGroupsRead"));
        assertTrue(generateAssertMessage("'All Entities Read' should be enabled"), allEntitiesReadCheckbox.isSelected());

        WebElement allEntitiesWriteCheckbox = $(By.id("allEntityGroupsWrite"));
        assertFalse(generateAssertMessage("'All Entities Write' should be disabled"), allEntitiesWriteCheckbox.isSelected());

        $(By.linkText("Members")).click();
        WebElement userCheckbox = $(By.id("members0.groupMember"));
        assertTrue(generateAssertMessage("Check box for user '" + TEST_USER + "' should be enabled"), userCheckbox.isSelected());

        // Add additional settings
        $(By.linkText("Portal Permissions")).click();
        $(By.id("portalPermissionsModels0.accessGranted")).click(); // Select the first portal

        $(By.linkText("Entity Permissions")).click();
        $(By.id("entityGroupPermissionModels1.write")).click(); // Select the second entity group

        $(By.name("update")).click();

        // Check that configuration saved correctly
        allEntitiesReadCheckbox = $(By.id("allEntityGroupsRead"));
        assertTrue(generateAssertMessage("'All Entities Read' should be enabled"), allEntitiesReadCheckbox.isSelected());

        allEntitiesWriteCheckbox = $(By.id("allEntityGroupsWrite"));
        assertFalse(generateAssertMessage("'All Entities Write' should be disabled"), allEntitiesWriteCheckbox.isSelected());

        WebElement secondEntityGroupWriteCheckbox = $(By.id("entityGroupPermissionModels1.write"));
        assertTrue(generateAssertMessage("Write checkbox for the second Entity Group should be enabled"), secondEntityGroupWriteCheckbox.isSelected());

        $(By.linkText("Members")).click();
        userCheckbox = $(By.id("members0.groupMember"));
        assertTrue(generateAssertMessage("Check box for user '" + TEST_USER + "' should be enabled"), userCheckbox.isSelected());

        $(By.linkText("Portal Permissions")).click();
        WebElement firstPortalCheckbox = $(By.id("portalPermissionsModels0.accessGranted"));
        assertTrue(generateAssertMessage("Check box for first portal should be enabled"), firstPortalCheckbox.isSelected());
    }
}

