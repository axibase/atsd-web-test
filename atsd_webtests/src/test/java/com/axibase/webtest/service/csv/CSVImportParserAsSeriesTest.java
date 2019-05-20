package com.axibase.webtest.service.csv;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.CommonAssertions;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.CollectionCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.testng.AssertJUnit.assertTrue;

public class CSVImportParserAsSeriesTest extends AtsdTest {
    private static final String PARSER_NAME = "test-atsd-import-series-parser";
    private static final String PATH_TO_PARSER = CSVImportParserAsSeriesTest.class.getResource("test-atsd-import-series-parser.xml").getFile();

    @BeforeMethod
    public void setUp() {
        super.setUp();
        goToCSVParsersImportPage();
    }

    @AfterMethod
    public void cleanup() {
        goToCSVParsersPage();
        CommonActions.deleteAllRecords();
    }

    @Test
    public void testImportCSVParserPage() {
        setReplaceExisting(false);
        sendParserIntoTableWithoutReplacement();

        goToCSVParsersPage();


        final boolean isParserPresented = Optional.of($("#configurationList > tbody"))
                .map(WebElement::getText)
                .map(text -> text.contains(PARSER_NAME))
                .orElse(false);
        assertTrue("Parser is not added into table", isParserPresented);
    }

    @Test
    public void testImportCSVParserWithReplace() {
        setReplaceExisting(false);
        sendParserIntoTableWithoutReplacement();
        setReplaceExisting(true);
        sendParserIntoTableWithReplacement();

        goToCSVParsersPage();
        assertTrue("Parser is not added into table",
                $(By.cssSelector("#configurationList > tbody")).getText().contains(PARSER_NAME));
    }

    private void sendParserIntoTableWithoutReplacement() {
        CommonActions.uploadFile(PATH_TO_PARSER);
        $$(By.className("successMessage")).shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    private void sendParserIntoTableWithReplacement() {
        sendParserIntoTableWithoutReplacement();
        assertTrue("There was no replacement",
                $(By.className("successMessage")).getText().matches(".*replaced:\\s[1-9]\\d*"));
    }

    private void setReplaceExisting(boolean on) {
        $(By.name("overwrite")).setSelected(on);
    }

    private void goToCSVParsersPage() {
        $(By.linkText("Data")).click();
        $(By.linkText("CSV Parsers")).click();
        CommonAssertions.assertPageUrlPathEquals("/csv/configs");
    }

    private void goToCSVParsersImportPage() {
        goToCSVParsersPage();
        CommonActions.clickImport();
        CommonAssertions.assertPageUrlPathEquals("/csv/configs/import");
    }

}
