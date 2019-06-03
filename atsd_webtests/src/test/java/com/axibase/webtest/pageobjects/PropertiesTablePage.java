package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PropertiesTablePage {
    private static final String BASE_URL = "/properties";

    public PropertiesTablePage(String entityName) {
        open("/entities/" + CommonActions.urlEncode(entityName) + BASE_URL);
    }

    public boolean isPropertyPresent(String propertyName) {
        return Arrays.asList(getColumnValuesByColumnName($(By.id("property-types-table")), "Type"))
                .contains(propertyName);
    }


}
