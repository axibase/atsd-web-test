package com.axibase.webtest.service;

import com.axibase.webtest.CommonAssertions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by sild on 02.02.15.
 */
public class MetricsServiceTest extends AtsdTest {

    @Test
    public void testDefaultMetrics() {
        String uri = "/metrics?page=1&size=1000&filter=all&mask=&tag-name=&tag-value=";
        open(uri);
        CommonAssertions.assertPageTitleAfterLoadEquals(MetricsService.TITLE);
        MetricsService ms = new MetricsService();
        assertTrue(generateAssertMessage("Row count in metricList table should be more then 2"), ms.getMetricsCount() >= 3);
        assertNotNull(generateAssertMessage("Can't find metric 'jvm_memory_used_percent'"), ms.getMetricByName("jvm_memory_used_percent"));
        assertNotNull(generateAssertMessage("Can't find metric metric_writes_per_second'"), ms.getMetricByName("metric_writes_per_second"));
    }
}
