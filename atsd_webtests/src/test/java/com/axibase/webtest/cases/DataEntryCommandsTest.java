package com.axibase.webtest.cases;

import com.axibase.webtest.dataproviders.DataEntryTestDataProvider;
import com.axibase.webtest.modelobjects.*;
import com.axibase.webtest.pageobjects.*;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.axibase.webtest.CommonActions.getColumnValuesByColumnName;
import static com.codeborne.selenide.Selenide.Wait;
import static org.testng.AssertJUnit.*;

public class DataEntryCommandsTest extends AtsdTest {
    private DataEntryPage dataEntryPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        dataEntryPage = new DataEntryPage();
    }

    @Parameters({"insertMessage", "expectedMessage"})
    @Test(dataProvider = "messageTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testMessageIsAdded(String insertMessage, Message expectedMessage) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        String entityName = expectedMessage.getEntityName();

        assertEntityAdds(entityName);
        assertMessageTagsAreAddedIntoIdsTable(expectedMessage);
        assertMessageAddByEntityName(entityName);
        assertMessageParameters(expectedMessage);
    }

    @Parameters({"insertMessage", "expectedProperty"})
    @Test(dataProvider = "propertyTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testPropertyIsAdded(String insertMessage, Property expectedProperty) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        String entityName = expectedProperty.getEntityName();

        assertEntityAdds(entityName);
        assertPropertyAdds(expectedProperty.getPropType(), entityName);
        assertPropertyKeysAndTags(expectedProperty);
    }

    @Parameters({"insertMessage", "expectedSeries"})
    @Test(dataProvider = "seriesTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testSeriesIsAdded(String insertMessage, Series expectedSeries) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        String entityName = expectedSeries.getEntityName();
        String metricName = expectedSeries.getMetricName();

        assertSeriesTagsAreAddedIntoIdsTable(expectedSeries);
        assertSeriesAdds(metricName);
        assertEntityAdds(entityName);
        assertSeriesParams(expectedSeries);
    }

    @Parameters({"insertMessage", "expectedMetric"})
    @Test(dataProvider = "metricTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testMetricIsAdded(String insertMessage, Metric expectedMetric) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        String metricName = expectedMetric.getMetricName();

        assertMetricIsAddedIntoIdsTable(metricName);
        assertMetricAdds(metricName);
        Metric createdMetric = new MetricPage(Collections.singletonMap("metricName", metricName))
                .getMetric();
        assertEquals("Wrong created metric\n", expectedMetric, createdMetric);
    }

    @Parameters({"insertMessage", "expectedEntity"})
    @Test(dataProvider = "entityTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testEntityIsAdded(String insertMessage, Entity expectedEntity) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        String entityName = expectedEntity.getEntityName();
        assertEntityAdds(entityName);
        Entity createdEntity = new EntityPage(expectedEntity.getEntityName()).getEntity();
        assertEquals("Wrong created entity\n", expectedEntity, createdEntity);
    }

    @Parameters({"insertMessage", "expectedMetrics"})
    @Test(dataProvider = "validFreemarkerCommandTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testValidCommands(String insertMessage, String[] expectedMetrics) {
        dataEntryPage.typeCommands(insertMessage).sendCommands();
        assertTrue(dataEntryPage.isCommandInserted());

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        for (String metric : expectedMetrics) {
            assertTrue("Metric " + metric + " is not added\n", metricsTablePage.isRecordPresent(metric));
        }
    }

    @Parameters({"exampleIndex", "expectedCommand"})
    @Test(dataProvider = "exampleSyntaxTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void textExamples(int exampleIndex, String expectedCommand) {
        dataEntryPage.openHelpCommands()
                .copyExampleByIndex(exampleIndex);
        assertEquals(expectedCommand, dataEntryPage.getCommandsWindowText(), "Command is wrong copied\n");

        dataEntryPage.validate();
        assertTrue("Command: \n" + expectedCommand + "\nis not passed validation\n", dataEntryPage.isCommandValidated());
    }

    @Parameters({"insertMessage"})
    @Test(dataProvider = "invalidFreemarkerCommandTest", dataProviderClass = DataEntryTestDataProvider.class)
    public void testInvalidCommands(String insertMessage) {
        dataEntryPage.typeCommands(insertMessage).validate();

        assertFalse("Wrong command is accepted", dataEntryPage.isCommandValidated());
    }

    @Step("Check the entity adds into entities table")
    private void assertEntityAdds(String entityName) {
        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertTrue("Entity is not added\n", entitiesTablePage.isRecordPresent(entityName));
    }

    @Step("Check the property adds")
    private void assertPropertyAdds(String propertyType, String entityName) {
        PropertiesTablePage propertiesTablePage = new PropertiesTablePage(entityName);
        assertTrue("Property is not added\n", propertiesTablePage.isPropertyPresent(propertyType));
    }

    @Step("Check message adds into message table by its entity name")
    private void assertMessageAddByEntityName(String entityName) {
        MessagesPage messagesPage = new MessagesPage().setEntity(entityName).search();
        assertTrue("Message is not added into table\n", messagesPage.getCountOfMessages() > 0);
    }

    @Step("Check properties keys and tags")
    private void assertPropertyKeysAndTags(Property expectedProperty) {
        PropertyPage propertiesPage = new PropertyPage(expectedProperty.getEntityName(),
                Collections.singletonMap("type", expectedProperty.getPropType()));

        Object[] allUnits = Stream.of(expectedProperty.getKeyNames(), expectedProperty.getKeyValues(),
                expectedProperty.getTagNames(), expectedProperty.getTagValues())
                .flatMap(Arrays::stream)
                .toArray();

        // compare all tags and keys pairs without order
        Assert.assertEqualsNoOrder(allUnits, propertiesPage.getTagsAndKeys());
    }

    @Step("Check the series adds by appropriate metric")
    private void assertSeriesAdds(String metricName) {
        MetricsSeriesTablePage metricsSeriesTablePage = new MetricsSeriesTablePage(metricName);
        assertTrue("Series is not added\n", metricsSeriesTablePage.isSeriesPresent());
    }

    @Step("Check series parameters")
    private void assertSeriesParams(Series expectedSeries) {
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("entity", expectedSeries.getEntityName());
        expectedParameters.put("metric", expectedSeries.getMetricName());
        for (int i = 0; i < expectedSeries.getTagNames().length; i++) {
            expectedParameters.put(expectedSeries.getTagNames()[i], expectedSeries.getTagValues()[i]);
        }

        StatisticsPage statisticsPage = new StatisticsPage(expectedParameters);

        Series createdSeries = statisticsPage.getSeries();
        assertEquals("Wrong created series\n", expectedSeries, createdSeries);
    }

    @Step("Check the metric adds into metrics table")
    private void assertMetricAdds(String metricName) {
        MetricsTablePage metricsTablePage = new MetricsTablePage();
        metricsTablePage.searchRecordByName(metricName);
        assertTrue("Metric is not added into table on Metric Page\n", metricsTablePage.isRecordPresent(metricName));
    }

    @Step("Check message parameters")
    private void assertMessageParameters(Message expectedMessage) {
        MessagesPage messagesPage = new MessagesPage().setEntity(expectedMessage.getEntityName()).search();

        assertEquals("Wrong count of messages with the entity: " + expectedMessage.getEntityName(), 1, messagesPage.getCountOfMessages());
        assertEquals("Wrong created message\n", expectedMessage, messagesPage.getMessage());
    }

    @Step("Check if the given IDs table tags of the expected element")
    private void assertExpectedTagsInTable(String errorMessage, String[] tags, SelenideElement table) {
        String[] tablesTags = getColumnValuesByColumnName(table, "Name");
        for (String value : tags) {
            assertTrue(errorMessage + value, ArrayUtils.contains(tablesTags, value));
        }
    }

    @Step
    private void assertMessageTagsAreAddedIntoIdsTable(Message expectedMessage) {
        assertExpectedTagsInTable("Message tag key is not added into Message Tag Key IDs: ",
                expectedMessage.getTagNames(), new MessageTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Message tag value is not added into Message Tag Value IDs: ",
                expectedMessage.getTagValues(), new MessageTagValueIDsPage().getTable());
    }

    @Step
    private void assertSeriesTagsAreAddedIntoIdsTable(Series expectedSeries) {
        assertExpectedTagsInTable("Series tag keys is not added into Series Tag Key IDs: ",
                expectedSeries.getTagNames(), new SeriesTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Series tag values is not added into Series Tag Values IDs: ",
                expectedSeries.getTagValues(), new SeriesTagValueIDsPage().getTable());
    }

    @Step
    private void assertMetricIsAddedIntoIdsTable(String metricName) {
        assertTrue("Metric is not added into Metric IDs table\n",
                ArrayUtils.contains(getColumnValuesByColumnName(new MetricIDsPage().getTable(), "Metric"), metricName));
    }

}
