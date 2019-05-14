package com.axibase.webtest.service;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.CommonAssertions;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionAutoEnable.AUTO_ENABLE;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionAutoEnable.NO_AUTO_ENABLE;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.NO_REPLACE_EXISTING;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.REPLACE_EXISTING;
import static com.codeborne.selenide.Selenide.$;

@RequiredArgsConstructor
@RunWith(value = Parameterized.class)
public class AdminBackupImportTest extends ReplacementTableImportBase {

    private final String testFile;

    @Parameterized.Parameters(name = "{index} {0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "xml-file.xml").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "zip-archive.zip").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "tar-archive.tar").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "bz2-archive.tar.bz2").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "gz-archive.tar.gz").getFile()}};

        return Arrays.asList(data);
    }

    @Before
    public void setUp() {
        super.setUp();
        goToAdminImportBackupPage();
    }

    @Test
    public void testImportDataImportBackupPage() {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, NO_AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test
    public void testImportDataImportBackupPageWithReplace() {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, NO_AUTO_ENABLE);
        sendFilesOnAdminImportBackup(testFile, REPLACE_EXISTING, NO_AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test
    public void testImportDataImportBackupPageWithAutoEnable() {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @After
    public void cleanUp() {
        deleteReplacementTables();
    }

    private void sendFilesOnAdminImportBackup(String file, ImportOptionReplace replaceExisting, ImportOptionAutoEnable autoEnable) {
        $(By.id("replaceExisting")).setSelected(replaceExisting.value);
        $(By.id("autoEnable")).setSelected(autoEnable.value);
        CommonActions.uploadFile(file);
    }

    private void goToReplacementTablesPage() {
        $(By.linkText("Data")).click();
        $(By.linkText("Replacement Tables")).click();
        CommonAssertions.assertPageUrlPathEquals("/replacement-tables/");
    }

    private void goToAdminImportBackupPage() {
        $(By.linkText("Settings")).click();
        $(By.linkText("Diagnostics")).hover();
        $(By.linkText("Backup Import")).click();
        CommonAssertions.assertPageUrlPathEquals("/admin/import-backup");
    }
}
