package com.axibase.webtest.cases;

import com.axibase.webtest.pages.MetricsForEntityPage;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@RequiredArgsConstructor
public class MetricSearchTest extends AtsdTest {
    private MetricsForEntityPage metricsForEntityPage;

    @DataProvider(name = "filters")
    public static Object[][] data() {
        return new Object[][]{{"j*_*", 18}, {"uy", 0}, {"asd", 0}, {"j?m_*", 15}, {"j?", 0}, {"en?ity*", 1}};
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        Map<String, String> params = new HashMap<>();
        params.put("mask", "");
        params.put("limit", "1000");
        metricsForEntityPage = new MetricsForEntityPage("atsd", params);
    }

    @Test(dataProvider = "filters")
    public void testWildcardSearch(String filter, int countOfRecords) {
        ElementsCollection metrics = metricsForEntityPage.setQuerySearch(filter)
                .search()
                .getMetrics();
        assertEquals("Wrong count of records with filter: " + filter, countOfRecords, metrics.size());
        for (SelenideElement metric : metrics) {
            assertTrue(String.format("Wrong metric name: filter: '%s' metric name: '%s'", filter, metric.text()),
                    metric.text().matches(filter.replace("*", ".*").replace("?", ".")));
        }
    }

}
