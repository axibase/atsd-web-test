package com.axibase.webtest.service;


import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by sild on 30.01.15.
 */
public class EntitiesService extends Service {
    public static final String TITLE = "Entities";

    public int getEntitiesCount() {
        return $$(By.xpath("//*[@id='entitiesList']/tbody/tr")).size();
    }

    public String getEntityByName(String name) {
            return $(By.xpath("//*[@id='entitiesList']/descendant::tr[td//text()='" + name + "']")).getText();//fix xpath, td should be first in tr
    }
}
