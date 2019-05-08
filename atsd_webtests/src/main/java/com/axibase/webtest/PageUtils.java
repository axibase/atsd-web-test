package com.axibase.webtest;

import com.axibase.webtest.service.Config;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static com.codeborne.selenide.WebDriverRunner.url;

@UtilityClass
public class PageUtils {
    public static String urlPath() {
        return StringUtils.substringAfter(url(), Config.getInstance().getBaseUrl());
    }
}
