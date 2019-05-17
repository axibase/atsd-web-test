package com.axibase.webtest.service;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by sild on 02.02.15.
 */
public class AdminService extends Service {
    public String getTime() {
        return $(By.xpath("//*[@id=\"serverTime\"]/tbody/tr[2]/td[2]")).getText();
    }
}
