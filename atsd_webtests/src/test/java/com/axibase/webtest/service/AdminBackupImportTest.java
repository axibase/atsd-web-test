package com.axibase.webtest.service;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.CommonAssertions;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionAutoEnable.AUTO_ENABLE;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionAutoEnable.NO_AUTO_ENABLE;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.NO_REPLACE_EXISTING;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.REPLACE_EXISTING;
import static com.codeborne.selenide.Selenide.$;

@RequiredArgsConstructor
public class AdminBackupImportTest extends ReplacementTableImportBase {
    @DataProvider(name = "files")
    public static Object[][] data() {
        return new Object[][]{
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "xml-file.xml").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "zip-archive.zip").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "tar-archive.tar").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "bz2-archive.tar.bz2").getFile()},
                {AdminBackupImportTest.class.getResource("replacement-table" + File.separator + "gz-archive.tar.gz").getFile()}};
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        goToAdminImportBackupPage();
    }

    @Test(dataProvider = "files")
    public void testImportDataImportBackupPage(String testFile) {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, NO_AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test(dataProvider = "files")
    public void testImportDataImportBackupPageWithReplace(String testFile) {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, NO_AUTO_ENABLE);
        sendFilesOnAdminImportBackup(testFile, REPLACE_EXISTING, NO_AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test(dataProvider = "files")
    public void testImportDataImportBackupPageWithAutoEnable(String testFile) {
        sendFilesOnAdminImportBackup(testFile, NO_REPLACE_EXISTING, AUTO_ENABLE);
        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @AfterMethod
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
