package com.axibase.webtest.cases;

import com.axibase.webtest.dataproviders.DataEntryTestDataProvider;
import com.axibase.webtest.modelobjects.*;
import com.axibase.webtest.pageobjects.*;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static org.testng.Assert.*;

public class DataEntryCommandsTest extends AtsdTest {
    private DataEntryPage dataEntryPage;
    private String entityName;
    private String metricName;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        dataEntryPage = new DataEntryPage();
    }

    @Parameters({"insertMessage", "expectedMessage"})
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

    @Parameters({"insertMessage", "expectedProperty"})
    @Test(dataProvider = "propertyTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testPropertyIsAdded(String insertMessage, Property expectedProperty) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedProperty.getEntityName();

        assertEntityAdds();
        assertPropertyAdds(expectedProperty.getPropType());
        assertPropertyKeysAndTags(expectedProperty);
    }

    @Parameters({"insertMessage", "expectedSeries"})
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

    @Parameters({"insertMessage", "expectedMetric"})
    @Test(dataProvider = "metricTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testMetricIsAdded(String insertMessage, Metric expectedMetric) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        metricName = expectedMetric.getMetricName();

        assertMetricAdds(expectedMetric.getMetricName());
        Metric createdMetric = new MetricPage(new HashMap<String, String>() {{
            put("metricName", expectedMetric.getMetricName());
        }})
                .getMetric();
        assertEquals(expectedMetric, createdMetric, "Wrong created metric\n");
    }

    @Parameters({"insertMessage", "expectedEntity"})
    @Test(dataProvider = "entityTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testEntityIsAdded(String insertMessage, Entity expectedEntity) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        entityName = expectedEntity.getEntityName();

        assertEntityAdds();
        Entity createdEntity = new EntityPage(expectedEntity.getEntityName()).getEntity();
        assertEquals(expectedEntity, createdEntity, "Wrong created entity\n");
    }

    @Test
    public void testMessageIsNotAdded() {
        entityName = "data-entry-commands-test_message-is-not-added";
        String insertMessage = "message e:" + entityName;
        dataEntryPage.typeCommands(insertMessage).sendCommands();

        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertFalse(entitiesTablePage.isRecordPresent(entityName), "Entity is added\n");

        MessagesPage messagesPage = new MessagesPage().setEntity(entityName).search();
        assertEquals(messagesPage.getCountOfMessages(), 0, "Message is added into table\n");
    }

    @Parameters({"insertMessage", "expectedMetrics"})
    @Test(dataProvider = "freemarkerCommandTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testValidCommands(String insertMessage, String[] expectedMetrics) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        for (String metric : expectedMetrics) {
            assertTrue(metricsTablePage.isRecordPresent(metric), "Metric " + metric + " is not added\n");
        }
    }

    @Parameters({"exampleIndex", "expectedCommand"})
    @Test(dataProvider = "exampleSyntaxTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void textExamples(int exampleIndex, String expectedCommand) {
        dataEntryPage.openHelpCommands()
                .copyExampleByIndex(exampleIndex);
        assertEquals(expectedCommand, dataEntryPage.getCommandsWindowText(), "Command is wrong copied\n");

        dataEntryPage.validate();
        assertTrue(dataEntryPage.isCommandValidated(), "Command: \n" + expectedCommand + "\nis not passed validation\n");
    }

    @Parameters({"insertMessage"})
    @Test(dataProvider = "invalidFreemarkerCommandTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testInvalidCommands(String insertMessage) {
        dataEntryPage.typeCommands(insertMessage).sendCommands().validate();

        assertFalse(dataEntryPage.isCommandValidated(), "Wrong command is accepted");
    }

    @Step("Check the entity adds into entities table")
    private void assertEntityAdds() {
        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertTrue(entitiesTablePage.isRecordPresent(entityName), "Entity is not added\n");
    }

    @Step("Check the property adds")
    private void assertPropertyAdds(String propertyType) {
        PropertiesTablePage propertiesTablePage = new PropertiesTablePage(entityName);
        assertTrue(propertiesTablePage.isPropertyPresent(propertyType), "Property is not added\n");
    }

    @Step("Check message adds into message table by its entity name")
    private void assertMessageAddByEntityName() {
        MessagesPage messagesPage = new MessagesPage().setEntity(entityName).search();
        assertTrue(messagesPage.getCountOfMessages() > 0, "Message is not added into table\n");
    }

    @Step("Check properties keys and tags")
    private void assertPropertyKeysAndTags(Property expectedProperty) {
        PropertyPage propertiesPage = new PropertyPage(expectedProperty.getEntityName(),
                new HashMap<String, String>() {{
                    put("type", expectedProperty.getPropType());
                }});

        Object[] allUnits = Stream.of(expectedProperty.getKeyNames(), expectedProperty.getKeyValues(),
                expectedProperty.getTagNames(), expectedProperty.getTagValues())
                .flatMap(Arrays::stream)
                .toArray();
        Assert.assertEqualsNoOrder(allUnits, propertiesPage.getTagsAndKeys());
    }

    @Step("Check the series adds by appropriate metric")
    private void assertSeriesAdds() {
        MetricsSeriesTablePage metricsSeriesTablePage = new MetricsSeriesTablePage(metricName);
        assertTrue(metricsSeriesTablePage.isSeriesPresent(), "Series is not added\n");
    }

    @Step("Check series parameters")
    private void assertSeriesParams(Series expectedSeries) {
        StatisticsPage statisticsPage = new StatisticsPage(
                new HashMap<String, String>() {{
                    put("entity", entityName);
                    put("metric", metricName);
                    for (int i = 0; i < expectedSeries.getTagNames().length; i++) {
                        put(expectedSeries.getTagNames()[i], expectedSeries.getTagValues()[i]);
                    }
                }});

        Series createdSeries = statisticsPage.getSeries();
        assertEquals(expectedSeries, createdSeries, "Wrong created series\n");
    }

    @Step("Check the metric adds into metrics table")
    private void assertMetricAdds(String metricName) {
        MetricIDsPage metricIDsPage = new MetricIDsPage();
        assertTrue(Arrays.toString(getColumnValuesByColumnName(metricIDsPage.getTable(),
                "Metric")).contains(metricName), "Metric is not added into Metric IDs table\n");

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        metricsTablePage.searchRecordByName(metricName);
        assertTrue(metricsTablePage.isRecordPresent(metricName), "Metric is not added into table on Metric Page\n");
    }

    @Step("Check message parameters")
    private void assertMessageParameters(Message expectedMessage) {
        MessagesPage messagesPage = new MessagesPage().setEntity(expectedMessage.getEntityName()).search();

        assertEquals(1, messagesPage.getCountOfMessages(),
                "Wrong count of messages with the entity: " + expectedMessage.getEntityName());
        assertEquals(expectedMessage, messagesPage.getMessage(), "Wrong created message\n");
    }

    @Step("Check if the given table contains \"{tags}\" ")
    private void assertExpectedTagsInTable(String errorMessage, String[] tags, SelenideElement table) {
        for (String value : tags) {
            assertTrue(Arrays.toString(getColumnValuesByColumnName(table, "Name")).contains(value),
                    errorMessage + value);
        }
    }

}
