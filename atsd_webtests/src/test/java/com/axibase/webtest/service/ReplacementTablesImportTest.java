package com.axibase.webtest.service;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReplacementTablesImportTest extends AtsdTest {
    private static final String XML_FILE = ReplacementTablesImportTest.class.getResource("replacement-table" + File.separator + "xml-file.xml").getFile();

    private String[][] expectedResult = {
            {"data-availability-json", "JSON", "Tommy Crow"},
            {"graphql-queries", "GRAPHQL", "Tommy Crow"},
            {"stickers", "LIST", ""},
            {"test-after-new-editor-release-1", "SQL", "Tory Eagle"},
            {"test-text", "TEXT", "Tony Bluejay"}};

    @Before
    public void setUp() {
        super.setUp();
        goToReplacementTablesImportPage();
    }

    @Test
    public void testImportDataImportPage() {
        setReplaceExisting(false);
        sendFilesFromReplacementTable(XML_FILE);

        goToReplacementTablesPage();
        assertTrue("Wrong table content", checkTable($(By.id("overviewTable"))));
    }

    @Test
    public void testImportDataWithReplaceImportPage() {
        setReplaceExisting(false);
        sendFilesFromReplacementTable(XML_FILE);
        setReplaceExisting(true);
        sendFilesFromReplacementTable(XML_FILE);

        goToReplacementTablesPage();
        assertTrue("Wrong table content", checkTable($(By.id("overviewTable"))));
    }

    @After
    public void cleanUp() {
        deleteReplacementTables();
    }

    private void sendFilesFromReplacementTable(String file) {
        $(By.id("putTable")).find(By.xpath(".//input[@type='file']")).uploadFile(new File(file));
        $(By.xpath(".//input[@type='submit']")).click();
        $$(By.xpath("//*/span[@class='successMessage']")).shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    private void setReplaceExisting(boolean on) {
        $(By.xpath("*//input[@name='replace']")).setSelected(on);
    }

    private void goToReplacementTablesPage() {
        $(By.xpath("//*/a/span[contains(text(),'Data')]")).click();
        $(By.xpath("//*/a[contains(text(),'Replacement Tables')]")).click();
        assertEquals("Wrong page", urlPath(), "/replacement-tables/");
    }

    private void goToReplacementTablesImportPage() {
        goToReplacementTablesPage();

        $(By.xpath("//*/button/span[@class='caret']")).click();
        $(By.xpath("//*/a[text()='Import']")).click();
        assertEquals("Wrong page", urlPath(), "/replacement-tables/import");
    }

    private void deleteReplacementTables() {
        $(By.xpath("//*/input[@title='Select All']")).setSelected(true);
        $(By.xpath("//*/button/span[@class='caret']")).click();
        $(By.xpath("//*/input[@type='submit' and @value='Delete']")).click();
        $(By.xpath("//*[@id=\"confirm-modal\"]/div/button[contains(text(), 'Yes')]")).waitUntil(Condition.visible, 1000).click();
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
