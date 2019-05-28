package com.axibase.webtest.pageobjects;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.net.URLEncoder;
import java.util.Map;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class PropertyPage {
    private final String BASE_URL = "/properties";

    @SneakyThrows
    public PropertyPage(String entityName, Map<String, String> params) {
        open(createNewURL("/entities/" + URLEncoder.encode(entityName, "UTF-8") + BASE_URL, params));
    }

    public String[] getTagsAndKeys() {
        return $$(By.xpath("//*[@id='property-widget']//div[not(contains(@class,'axi-table-column-time')) " +
                "and contains(@class, 'axi-table-cell')]")).stream().map(SelenideElement::text).toArray(String[]::new);
    }

}
