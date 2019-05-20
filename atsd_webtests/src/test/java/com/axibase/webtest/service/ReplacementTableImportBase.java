package com.axibase.webtest.service;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.AssertJUnit.assertEquals;

public abstract class ReplacementTableImportBase extends AtsdTest {
    @Step
    protected void checkThatAllReplacementTablesAreShownInTheList() {
        final String[][] expectedResult = {
                {"data-availability-json", "JSON", "Tommy Crow"},
                {"graphql-queries", "GRAPHQL", "Tommy Crow"},
                {"stickers", "LIST", ""},
                {"test-after-new-editor-release-1", "SQL", "Tory Eagle"},
                {"test-text", "TEXT", "Tony Bluejay"}};
        final ElementsCollection elements = $(By.id("overviewTable")).findAll(By.xpath("./tbody/tr")).shouldHaveSize(expectedResult.length);
        for (int i = 0; i < expectedResult.length; i++) {
            final List<String> row = elements.get(i).findAll(By.xpath("./td")).exclude(Condition.cssClass("select-field")).texts();
            assertEquals("Mismatch on row " + (i + 1), Arrays.asList(expectedResult[i]), row);
        }
    }

    @Step
    protected void deleteReplacementTables() {
        CommonActions.deleteAllRecords();
    }

    @RequiredArgsConstructor
    protected enum ImportOptionReplace {
        REPLACE_EXISTING(true),
        NO_REPLACE_EXISTING(false);

        final boolean value;
    }

    @RequiredArgsConstructor
    protected enum ImportOptionAutoEnable {
        AUTO_ENABLE(true),
        NO_AUTO_ENABLE(false);

        final boolean value;
    }
}

