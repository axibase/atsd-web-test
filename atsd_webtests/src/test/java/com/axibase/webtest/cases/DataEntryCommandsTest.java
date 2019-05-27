package com.axibase.webtest.cases;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.dataproviders.DataEntryTestDataProvider;
import com.axibase.webtest.modelobjects.*;
import com.axibase.webtest.pageobjects.*;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.axibase.webtest.CommonActions.clickCheckboxByValueAttribute;
import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static org.testng.AssertJUnit.*;

public class DataEntryCommandsTest extends AtsdTest {
    private DataEntryPage dataEntryPage;
    private String entityName;
    private String metricName;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        dataEntryPage = new DataEntryPage();
    }

    @Test(dataProvider = "messageTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testMessageIsAdded(String insertMessage, Message expectedMessage) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedMessage.getEntityName();

        assertEntityAdds();
        assertExpectedTagsInTable("Message tag key is not added into Message Tag Key IDs: ",
                expectedMessage.getTagNames(), new MessageTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Message tag value is not added into Message Tag Value IDs: ",
                expectedMessage.getTagValues(), new MessageTagValueIDsPage().getTable());
        assertMessageAddByEntityName();
        assertMessageParameters(expectedMessage);
    }

    @Test(dataProvider = "propertyTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testPropertyIsAdded(String insertMessage, Property expectedProperty) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedProperty.getEntityName();

        assertEntityAdds();
        assertPropertyAdds(expectedProperty.getPropType());
        assertPropertyKeysAndTags(expectedProperty);
    }

    @Test(dataProvider = "seriesTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testSeriesIsAdded(String insertMessage, Series expectedSeries) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedSeries.getEntityName();
        metricName = expectedSeries.getMetricName();

        assertExpectedTagsInTable("Series tag keys is not added into Series Tag Key IDs: ",
                expectedSeries.getTagNames(), new SeriesTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Series tag values is not added into Series Tag Values IDs: ",
                expectedSeries.getTagValues(), new SeriesTagValueIDsPage().getTable());
        assertSeriesAdds();
        assertEntityAdds();
        assertSeriesParams(expectedSeries);
    }

    @Test(dataProvider = "metricTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testMetricIsAdded(String insertMessage, Metric expectedMetric) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        metricName = expectedMetric.getMetricName();

        assertMetricAdds(expectedMetric.getMetricName());
        Metric createdMetric = new MetricPage(new String[]{"metricName"}, new String[]{expectedMetric.getMetricName()})
                .getMetric();
        assertEquals("Wrong created metric", expectedMetric, createdMetric);
    }

    @Test(dataProvider = "entityTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testEntityIsAdded(String insertMessage, Entity expectedEntity) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedEntity.getEntityName();

        assertEntityAdds();
        Entity createdEntity = new EntityPage(expectedEntity.getEntityName()).getEntity();
        assertEquals("Wrong created entity", expectedEntity, createdEntity);
    }

    @Test
    public void testMessageIsNotAdded() {
        entityName = "data-entry-commands-test_message-is-not-added";
        String insertMessage = "message e:" + entityName;
        dataEntryPage.typeCommands(insertMessage).sendCommands();

        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertFalse("Entity is added", entitiesTablePage.isRecordPresent(entityName));

        MessagesPage messagesPage = new MessagesPage().setEntity(entityName).search();
        assertEquals("Message is added into table", messagesPage.getCountOfMessages(), 0);
    }

    @Test(dataProvider = "commandsTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testCommands(String insertMessage, String[] expectedMetrics) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        for (String metric : expectedMetrics) {
            assertTrue("Metric " + metric + " is not added", metricsTablePage.isRecordPresent(metric));
        }
    }

    @Step
    @AfterMethod
    public void cleanup() {
        dropRecord(new EntitiesTablePage(), entityName);
        dropRecord(new MetricsTablePage(), metricName);
    }

    @Step("Drop {recordName} from a table if it is exist")
    private <T extends Table> void dropRecord(T pageWithTable, String recordName) {
        pageWithTable.searchRecordByName(recordName);
        if (pageWithTable.isRecordPresent(recordName)) {
            removeRecordByCheckbox(recordName);
        }
        assertFalse(recordName + " is not deleted", pageWithTable.isRecordPresent(recordName));
    }

    private void removeRecordByCheckbox(String value) {
        clickCheckboxByValueAttribute(value);
        CommonActions.deleteRecord();
    }

    @Step("Check the entity adds into entities table")
    private void assertEntityAdds() {
        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertTrue("Entity is not added", entitiesTablePage.isRecordPresent(entityName));
    }

    @Step("Check the property adds")
    private void assertPropertyAdds(String propertyType) {
        PropertiesTablePage propertiesTablePage = new PropertiesTablePage(entityName);
        assertTrue("Property is not added", propertiesTablePage.isPropertyPresent(propertyType));
    }

    @Step("Check message adds into message table by its entity name")
    private void assertMessageAddByEntityName() {
        MessagesPage messagesPage = new MessagesPage().setEntity(entityName).search();
        assertTrue("Message is not added into table", messagesPage.getCountOfMessages() > 0);
    }

    @Step("Check properties keys and tags")
    private void assertPropertyKeysAndTags(Property expectedProperty) {
        PropertiesPage propertiesPage = new PropertiesPage(expectedProperty.getEntityName(),
                new String[]{"type"}, new String[]{expectedProperty.getPropType()});

        Object[] allUnits = Stream.of(expectedProperty.getKeyNames(), expectedProperty.getKeyValues(),
                expectedProperty.getTagNames(), expectedProperty.getTagValues())
                .flatMap(Arrays::stream)
                .toArray();
        Assert.assertEqualsNoOrder(allUnits, propertiesPage.getTagsAndKeys());
    }

    @Step("Check the series adds by appropriate metric")
    private void assertSeriesAdds() {
        MetricsSeriesTablePage metricsSeriesTablePage = new MetricsSeriesTablePage(metricName);
        assertTrue("Series is not added", metricsSeriesTablePage.isSeriesPresent());
    }

    @Step("Check series parameters")
    private void assertSeriesParams(Series expectedSeries) {
        StatisticsPage statisticsPage = new StatisticsPage(
                Stream.concat(Stream.of("entity", "metric"), Arrays.stream(expectedSeries.getTagNames())).toArray(String[]::new),
                Stream.concat(Stream.of(entityName, metricName),
                        Arrays.stream(expectedSeries.getTagValues())).toArray(String[]::new));

        Series createdSeries = statisticsPage.getSeries();
        assertEquals("Wrong created series", expectedSeries, createdSeries);
    }

    @Step("Check the metric adds into metrics table")
    private void assertMetricAdds(String metricName) {
        MetricIDsPage metricIDsPage = new MetricIDsPage();
        assertTrue("Metric is not added into Metric IDs table",
                Arrays.toString(getColumnValuesByColumnName(metricIDsPage.getTable(), "Metric")).contains(metricName));

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        metricsTablePage.searchRecordByName(metricName);
        assertTrue("Metric is not added into table on Metric Page", metricsTablePage.isRecordPresent(metricName));
    }

    @Step("Check message parameters")
    private void assertMessageParameters(Message expectedMessage) {
        MessagesPage messagesPage = new MessagesPage().setEntity(expectedMessage.getEntityName()).search();

        assertEquals("Wrong count of messages with the entity: " + expectedMessage.getEntityName(),
                1, messagesPage.getCountOfMessages());
        assertEquals("Wrong created message", expectedMessage, messagesPage.getMessage());
    }

    @Step("Check if the given table contains \"{tags}\" ")
    private void assertExpectedTagsInTable(String errorMessage, String[] tags, SelenideElement table) {
        for (String value : tags) {
            assertTrue(errorMessage + value, Arrays.toString(getColumnValuesByColumnName(table, "Name")).contains(value));
        }
    }

}
