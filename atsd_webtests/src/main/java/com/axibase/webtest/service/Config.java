package com.axibase.webtest.service;

import com.codeborne.selenide.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {
    private static final String PROPERTY_PATH = "atsd.properties";
    private static final String CHROME_DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
    private static final String SELENIDE_BROWSER_PROPERTY_NAME = "selenide.browser";
    private static final String SELENIDE_HEADLESS_PROPERTY_NAME = "selenide.headless";
    private final String login;
    private final String password;
    private final String baseUrl;

    public void init() {
        // already initialized. Do nothing
    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    private static final class ConfigHolder {
        private static final Config INSTANCE = readConfig();

        private static Config readConfig() {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(new File(PROPERTY_PATH)));
                final String url = properties.getProperty("url");
                final String login = properties.getProperty("login");
                final String password = properties.getProperty("password");
                final String screenshotDir = properties.getProperty("screenshot_directory");
                if (url == null || login == null || password == null || screenshotDir == null) {
                    log.error("Can't read required properties");
                    System.exit(1);
                }
                final String chromedriverPath = properties.getProperty(CHROME_DRIVER_PROPERTY_NAME);
                if (chromedriverPath != null) {
                    System.setProperty(CHROME_DRIVER_PROPERTY_NAME, chromedriverPath);
                }
                Configuration.baseUrl = url;
                Configuration.screenshots = false; // create screenshots manually with proper name
                Configuration.reportsFolder = screenshotDir;
                Configuration.browser = System.getProperty(SELENIDE_BROWSER_PROPERTY_NAME, "chrome");
                Configuration.browserSize = "1024x768";
                Configuration.headless = Boolean.parseBoolean(properties.getProperty(SELENIDE_HEADLESS_PROPERTY_NAME, "true"));
                if (Configuration.headless && "chrome".equals(Configuration.browser)) {
                    System.setProperty("chromeoptions.args", "--no-sandbox");
                }
                return new Config(login, password, url);
            } catch (IOException e) {
                log.error("Can't read property file", e);
                System.exit(1);
                throw new IllegalStateException(e);
            }
        }
    }
}
