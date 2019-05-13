package com.axibase.webtest.service;

import com.axibase.webtest.CommonAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.Assert.assertTrue;

@Ignore("Enable after fix")
@RunWith(value = Parameterized.class)
public class AdminBackupImportTest extends AtsdTest {

    private String testFile;
    private String[][] expectedResult = {
            {"data-availability-json", "JSON", "Tommy Crow"},
            {"graphql-queries", "GRAPHQL", "Tommy Crow"},
            {"stickers", "LIST", ""},
            {"test-after-new-editor-release-1", "SQL", "Tory Eagle"},
            {"test-text", "TEXT", "Tony Bluejay"}};

    public AdminBackupImportTest(String file) {
        testFile = file;
    }

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
        setReplaceExisting(false);
        setAutoEnable(false);
        sendFilesOnAdminImportBackup(testFile);

        goToReplacementTablesPage();

        assertTrue("Wrong table content",
                checkTable($(By.id("overviewTable"))));
    }

    @Test
    public void testImportDataImportBackupPageWithReplace() {
        setReplaceExisting(false);
        setAutoEnable(false);
        sendFilesOnAdminImportBackup(testFile);

        setReplaceExisting(true);
        setAutoEnable(false);
        sendFilesOnAdminImportBackup(testFile);

        goToReplacementTablesPage();

        assertTrue("Wrong table content",
                checkTable($(By.id("overviewTable"))));
    }

    @Test
    public void testImportDataImportBackupPageWithAutoEnable() {
        setReplaceExisting(false);
        setAutoEnable(true);
        sendFilesOnAdminImportBackup(testFile);

        goToReplacementTablesPage();

        assertTrue("Wrong table content",
                checkTable($(By.id("overviewTable"))));
    }

    @After
    public void cleanUp() {
        deleteReplacementTables();
    }

    private void sendFilesOnAdminImportBackup(String file) {
        WebElement putTable = $(By.id("putTable"));
        WebElement inputFile = putTable.findElement(By.xpath(".//input[@type='file']"));
        WebElement submitButton = $(By.xpath(".//input[@type='submit']"));

        inputFile.sendKeys(file);
        submitButton.click();
    }

    private void setReplaceExisting(boolean on) {
        $(By.id("replaceExisting")).setSelected(on);
    }

    private void setAutoEnable(boolean on) {
        $(By.id("autoEnable")).setSelected(on);
    }

    private void goToReplacementTablesPage() {
        $(By.xpath("//*/a/span[contains(text(),'Data')]")).click();
        $(By.xpath("//*/a[contains(text(),'Replacement Tables')]")).click();
        CommonAssertions.assertPageUrlPathEquals("/replacement-tables/");
    }

    private void goToAdminImportBackupPage() {
        Actions action = new Actions(getWebDriver());

        action.moveToElement($(By.xpath("//*/a/span[contains(text(),'Settings')]"))).click();
        action.moveToElement($(By.xpath("//*/a[contains(text(),'Diagnostics')]")));
        action.moveToElement($(By.xpath("//*/a[contains(text(),'Backup Import')]"))).click();
        action.build().perform();

        CommonAssertions.assertPageUrlPathEquals("/admin/import-backup");
    }

    private void deleteReplacementTables() {
        $(By.xpath("//*/input[@title='Select All']")).setSelected(true);
        $(By.xpath("//*/button/span[@class='caret']")).click();
        $(By.xpath("//*/input[@type='submit' and @value='Delete']")).click();
        $(".btn-confirm")
                .should(appear)
                .shouldHave(text("Yes"))
                .click();
    }

    private boolean checkTable(WebElement table) {
        List<WebElement> findElements = table.findElements(By.xpath("./tbody/tr"));
        if (findElements.size() != expectedResult.length) {
            return false;
        }

        for (int i = 0; i < findElements.size(); i++) {
            List<WebElement> tdList = findElements.get(i).findElements(By.xpath("./td"));
            if (!tdList.get(1).getText().equals(expectedResult[i][0]) &&
                    !tdList.get(2).getText().equals(expectedResult[i][1]) &&
                    !tdList.get(3).getText().equals(expectedResult[i][2])) {
                return false;
            }
        }

        return true;
    }
}
