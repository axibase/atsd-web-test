package com.axibase.webtest.pageobjects;

import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.net.URLEncoder;
import java.util.Arrays;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PropertiesTablePage {
    private final String BASE_URL = "/properties";

    @SneakyThrows
    public PropertiesTablePage(String entityName) {
        open("/entities/" + URLEncoder.encode(entityName, "UTF-8") + BASE_URL);
    }

    public boolean isPropertyPresent(String propertyName) {
        return Arrays.asList(getColumnValuesByColumnName($(By.id("property-types-table")), "Type"))
                .contains(propertyName);
    }


}
