package com.axibase.webtest.cases;

import com.axibase.webtest.pages.DataEntryPage;
import com.axibase.webtest.pages.MetricsForEntityPage;
import com.axibase.webtest.service.AtsdTest;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@RequiredArgsConstructor
public class MetricSearchTest extends AtsdTest {
    private MetricsForEntityPage metricsForEntityPage;

    private final static String[] ALL_METRICS = new String[]{"metricsearchtest_contains-hyphen1", "metricsearchtest_contains-hyphen2",
            "metricsearchtest_contains.point1", "metricsearchtest_contains.point2", "metricsearchtest_contains.point3",
            "metricsearchtest_contains_underscore"};

    @DataProvider(name = "masks")
    public static Object[][] data() {
        return new Object[][]{{"*con*.*", "metricsearchtest_contains.point1", "metricsearchtest_contains.point2", "metricsearchtest_contains.point3"},
                {"*contains?po*", "metricsearchtest_contains.point1", "metricsearchtest_contains.point2", "metricsearchtest_contains.point3"},
                {"*ins?*", ALL_METRICS},
                {"*ins-*", "metricsearchtest_contains-hyphen1", "metricsearchtest_contains-hyphen2"},
                {"*s_*", "metricsearchtest_contains_underscore"},
                {"*3", "metricsearchtest_contains.point3"},
                {"", ALL_METRICS},
                {"*", ALL_METRICS},
                {"?*", ALL_METRICS},
                {"*?", ALL_METRICS},
                {"*.", new String[]{}},
                {"metricsearchtest", ALL_METRICS},
                {" metricsearchtest ", ALL_METRICS},
                {"some-bad-mask", new String[]{}},
                {"cd s", new String[]{}},
                {"metric*searchtest*", ALL_METRICS},
                {"metric*searchtest", new String[]{}}
        };
    }

    @BeforeClass
    public void generateData() {
        super.setUp();
        new DataEntryPage().sendCommands("<#list 1..2 as i> \n" +
                "series e:metricsearchtest_entity m:metricsearchtest_contains-hyphen${i}=${i} \n" +
                "</#list>",

                "<#list 1..3 as i> \n" +
                "series e:metricsearchtest_entity m:metricsearchtest_contains.point${i}=${i} \n" +
                "</#list>",

                "series e:metricsearchtest_entity m:metricsearchtest_contains_underscore=1");
        super.logout();
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        metricsForEntityPage = new MetricsForEntityPage("metricsearchtest_entity");
    }

    @Step("Check mask: {mask}")
    @Test(dataProvider = "masks")
    public void testWildcardSearch(String mask, String[] expectedMetrics) {
        String[] receivedMetrics = metricsForEntityPage.search(mask)
                .getMetricNames();
        Assert.assertEqualsNoOrder(expectedMetrics, receivedMetrics);
    }

}
