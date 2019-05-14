package com.axibase.webtest.service;

import com.codeborne.selenide.CollectionCondition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.File;

import static com.axibase.webtest.PageUtils.urlPath;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.NO_REPLACE_EXISTING;
import static com.axibase.webtest.service.ReplacementTableImportBase.ImportOptionReplace.REPLACE_EXISTING;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.Assert.assertEquals;

public class ReplacementTablesImportTest extends ReplacementTableImportBase {
    private static final String XML_FILE = ReplacementTablesImportTest.class.getResource("replacement-table" + File.separator + "xml-file.xml").getFile();

    @Before
    public void setUp() {
        super.setUp();
        goToReplacementTablesImportPage();
    }

    @Test
    public void testImportDataImportPage() {
        sendFilesFromReplacementTable(XML_FILE, NO_REPLACE_EXISTING);

        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test
    public void testImportDataWithReplaceImportPage() {
        sendFilesFromReplacementTable(XML_FILE, NO_REPLACE_EXISTING);
        sendFilesFromReplacementTable(XML_FILE, REPLACE_EXISTING);

        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @After
    public void cleanUp() {
        deleteReplacementTables();
    }

    private void sendFilesFromReplacementTable(String file, ImportOptionReplace replaceExisting) {
        $(By.xpath("*//input[@name='replace']")).setSelected(replaceExisting.value);
        $(By.id("putTable")).find(By.xpath(".//input[@type='file']")).uploadFile(new File(file));
        $(By.xpath(".//input[@type='submit']")).click();
        $$(By.xpath("//*/span[@class='successMessage']")).shouldHave(CollectionCondition.sizeGreaterThan(0));
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

}
