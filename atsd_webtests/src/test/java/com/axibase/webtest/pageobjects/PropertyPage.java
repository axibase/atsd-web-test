package com.axibase.webtest.pageobjects;

import com.axibase.webtest.CommonActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.Map;

import static com.axibase.webtest.CommonActions.createNewURL;
import static com.codeborne.selenide.Selenide.*;

public class PropertyPage {
    private static final String BASE_URL = "/properties";

    public PropertyPage(String entityName, Map<String, String> params) {
        open(createNewURL("/entities/" + CommonActions.urlEncode(entityName) + BASE_URL, params));
    }

    public String[] getTagsAndKeys() {
        $(By.xpath("//div[contains(@class,'axi-tooltip-info')]")).shouldBe(Condition.hidden);
        ElementsCollection tableCells = $$(By.xpath("//*[@id='property-widget']//div[not(contains(@class,'axi-table-column-time')) " +
                "and contains(@class, 'axi-table-cell')]"));
        return tableCells.stream().map(SelenideElement::text).toArray(String[]::new);
    }

}
