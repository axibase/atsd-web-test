package com.axibase.webtest.service;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.CommonAssertions;
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
        sendFilesFromReplacementTable(NO_REPLACE_EXISTING);

        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @Test
    public void testImportDataWithReplaceImportPage() {
        sendFilesFromReplacementTable(NO_REPLACE_EXISTING);
        sendFilesFromReplacementTable(REPLACE_EXISTING);

        goToReplacementTablesPage();
        checkThatAllReplacementTablesAreShownInTheList();
    }

    @After
    public void cleanUp() {
        deleteReplacementTables();
    }

    private void sendFilesFromReplacementTable(ImportOptionReplace replaceExisting) {
        $(By.name("replace")).setSelected(replaceExisting.value);
        CommonActions.uploadFile(XML_FILE);
        $$(By.className("successMessage")).shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    private void goToReplacementTablesPage() {
        $(By.linkText("Data")).click();
        $(By.linkText("Replacement Tables")).click();
        CommonAssertions.assertPageUrlPathEquals("/replacement-tables/");
    }

    private void goToReplacementTablesImportPage() {
        goToReplacementTablesPage();
        CommonActions.clickImport();
        CommonAssertions.assertPageUrlPathEquals("/replacement-tables/import");
    }

}
