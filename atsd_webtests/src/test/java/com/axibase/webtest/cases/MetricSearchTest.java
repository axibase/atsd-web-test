package com.axibase.webtest.cases;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.pages.MetricsForEntityPage;
import com.axibase.webtest.service.AtsdTest;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@RequiredArgsConstructor
public class MetricSearchTest extends AtsdTest {
    private MetricsForEntityPage metricsForEntityPage;

    @DataProvider(name = "filters")
    public static Object[][] data() {
        return new Object[][]{{"con*.*", "contains.point1", "contains.point2", "contains.point3"},
                {"contains?po*", "contains.point1", "contains.point2", "contains.point3"},
                {"*ins?*", "contains-hyphen1", "contains-hyphen2",
                        "contains.point1", "contains.point2", "contains.point3",
                        "contains_underscore"},
                {"*ins-*", "contains-hyphen1", "contains-hyphen2"},
                {"*s_*", "contains_underscore"},
                {"*3", "contains.point3"}};
    }

    @BeforeClass
    public void generateData() {
        super.setUp();
        open("/metrics/entry");
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "<#list 1..2 as i> \n" +
                "series e:metric-for-entity-search_entity m:contains-hyphen${i}=${i} \n" +
                "</#list>\n");
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "<#list 1..3 as i> \n" +
                "series e:metric-for-entity-search_entity m:contains.point${i}=${i} \n" +
                "</#list>\n");
        CommonActions.sendTextToCodeMirror($(By.name("commands")), "" +
                "series e:metric-for-entity-search_entity m:contains_underscore=1 \n");
        $("button[value=send]").click();
        super.logout();
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        Map<String, String> params = new HashMap<>();
        params.put("mask", "");
        params.put("limit", "1000");
        metricsForEntityPage = new MetricsForEntityPage("metric-for-entity-search_entity", params);
    }

    @Test(dataProvider = "filters")
    public void testWildcardSearch(String filter, String[] expectedMetrics) {
        List<String> metrics = metricsForEntityPage.setQuerySearch(filter)
                .search()
                .getMetricNames();
        assertEquals("Wrong count of records with filter: " + filter, expectedMetrics.length, metrics.size());
        for (String metric : expectedMetrics) {
            assertTrue(String.format("There is no expected metric: '%s' with filter: '%s'", metric, filter),
                    metrics.toString().contains(metric));
        }
    }

}
