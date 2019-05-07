package com.axibase.webtest.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.axibase.webtest.service.AccountService.CREATE_ACCOUNT_TITLE;

@RequiredArgsConstructor
public class ActionOnTestState extends TestWatcher {
    private final AtsdTest tests;

    @Override
    protected void failed(Throwable e, Description description) {
        if (AtsdTest.driver != null) { // driver = null for ExportServiceTest methods
            String clazz = description.getTestClass().getSimpleName();
            String method = description.getMethodName();
            String classAndMethod = clazz + "_" + method;
            takeScreenshot(classAndMethod);
        }
    }

    private void takeScreenshot(String classAndMethod) {
        String filepath = Config.getInstance().getScreenshotDir() + "/"
                + classAndMethod + "_"
                + System.currentTimeMillis()
                + ".png";
        System.out.println(AtsdTest.driver);
        File scrFile = ((TakesScreenshot) AtsdTest.driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(filepath), true);
            System.out.println("Screenshot saved to '" + filepath + "'");
        } catch (IOException ex) {
            System.out.println("Can't save screenshot to '" + filepath + "'");
        }
    }

    @Override
    protected void finished(Description description) {
        if (AtsdTest.driver != null) { // driver = null for ExportServiceTest methods
            new LoginService(AtsdTest.driver).logout();
        }
    }

    @Override
    protected void starting(Description description) {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless");
        opts.addArguments("--no-sandbox");
        opts.addArguments("--window-size=1280,720");
        if (AtsdTest.driver == null) { // driver = null for ExportServiceTest methods
            AtsdTest.driver = new ChromeDriver(opts);
            AtsdTest.driver.manage().window().setSize(new Dimension(1280, 720));
            AtsdTest.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            AtsdTest.driver.navigate().to(AtsdTest.url);
        }
        if (!(tests instanceof AccountServiceTest) && CREATE_ACCOUNT_TITLE.equals(AtsdTest.driver.getTitle())) {
            final AccountService accountService = new AccountService(AtsdTest.driver);
            if (!accountService.createAdmin()) {
                throw new IllegalStateException("Admin account failed to be created");
            }
            AtsdTest.driver.navigate().to(AtsdTest.url);
        }
    }
}