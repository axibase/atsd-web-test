package com.axibase.webtest.service;


import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SystemInfoService extends Service {
    public static final String TITLE = "System Information";

    public String getSystemInfoValue(String key) {
        return $(By.xpath("//*[@id='serverProperties']/descendant::tr[td//text()='" + key + "']/td[2]")).getText();
    }
}
