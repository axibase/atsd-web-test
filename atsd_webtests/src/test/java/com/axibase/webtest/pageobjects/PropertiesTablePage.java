package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PropertiesTablePage {
    private static final String BASE_URL = "/properties";

    public PropertiesTablePage(String entityName) {
        open("/entities/" + CommonActions.urlEncode(entityName) + BASE_URL);
    }

    public boolean isPropertyPresent(String propertyName) {
        return ArrayUtils.contains(
                getColumnValuesByColumnName($(By.id("property-types-table")), "Type"), propertyName);
    }


}
