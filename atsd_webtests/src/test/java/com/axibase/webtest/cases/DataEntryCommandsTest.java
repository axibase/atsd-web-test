package com.axibase.webtest.cases;

import com.axibase.webtest.CommonActions;
import com.axibase.webtest.pageobjects.*;
import com.axibase.webtest.service.AtsdTest;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.axibase.webtest.CommonActions.clickCheckboxByValueAttribute;
import static com.axibase.webtest.CommonAssertions.assertExpectedTagsInTable;
import static com.axibase.webtest.CommonAssertions.executeWithElement;
import static com.codeborne.selenide.Condition.*;
import static org.junit.Assert.*;

public class DataEntryCommandsTest extends AtsdTest {
    private final String ENTITY_NAME = "entity_name_data_entry_commands_test";
    private final String METRIC_NAME = "metric_name_data_entry_commands_test";
    private DataEntryPage dataEntryPage;

    @Before
    public void setUp() {
        super.setUp();
        dataEntryPage = new DataEntryPage();
    }

//    @Test
    public void testMessageAdd() {
        String messageText = "\"Message text\"";
        String type = "message_tag_type_value";
        String source = "message_tag_source_value";
        String severity = "FATAL";
        String severityNumber = "7";
        String[] tagNames = {"message_tag_name1", "message_tag_name2"};
        String[] tagValues = {"message_tag_tag_value1", "message_tag_tag_value2"};
        String insertMessage = String.format("message e:%s t:type=%s t:source=%s t:severity=%s t:%s=%s t:%s=%s m:%s",
                ENTITY_NAME, type, source, severityNumber, tagNames[0], tagValues[0], tagNames[1], tagValues[1], messageText);

        dataEntryPage.typeCommands(insertMessage).sendCommands();

        assertEntityAdd();
        assertExpectedTagsInTable("Message tag key is not added into Message Tag Key IDs: ",
                tagNames, new MessageTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Message tag value is not added into Message Tag Value IDs: ",
                tagValues, new MessageTagValueIDsPage().getTable());
        assertMessageAddByEntityName();
        assertMessageParameters(type, source, severity);
    }

    @Test
    public void testPropertyAdd() {
        String propType = "property_type";
        String[] key_names = {"property_key_name1", "property_key_name2"};
        String[] key_values = {"property_key_value1", "property_key_value2"};
        String[] tag_names = {"property_tag_name1", "property_tag_name2"};
        String[] tag_values = {"property_tag_value1", "property_tag_value2"};
        String insertMessage = String.format("property e:%s t:%s k:%s=%s k:%s=%s v:%s=%s v:%s=%s",
                ENTITY_NAME, propType, key_names[0], key_values[0], key_names[1], key_values[1],
                tag_names[0], tag_values[0], tag_names[1], tag_values[1]);

        dataEntryPage.typeCommands(insertMessage).sendCommands();

        assertEntityAdd();
        assertPropertiesAdd(propType);
        assertPropertiesKeysAndTags(propType, key_names, key_values, tag_names, tag_values);
    }

//    @Test
    public void testSeriesAdd() {
        String metricText1 = "metric_text1";
        String metricText2 = "metric_text2";
        String textAppend = "true";
        String metricValue = "10";
        String[] tagNames = {"series_tag_key1", "series_tag_key2"};
        String[] tagValues = {"series_tag_value1", "series_tag_value2"};
        String time = "1425482080";
        String insertMessage1 = String.format("series e:%s m:%s=%s0 x:%s=%s a:%s t:%s=%s t:%s=%s s:%s", ENTITY_NAME,
                METRIC_NAME, metricValue, METRIC_NAME, metricText1, textAppend, tagNames[0], tagValues[0],
                tagNames[1], tagValues[1], time);
        String insertMessage2 = String.format("series e:%s m:%s=%s0 x:%s=%s a:%s t:%s=%s t:%s=%s s:%S", ENTITY_NAME,
                METRIC_NAME, metricValue, METRIC_NAME, metricText2, textAppend, tagNames[0], tagValues[0]
                , tagNames[1], tagValues[1], time);

        dataEntryPage.typeCommands(insertMessage1 + "\n" + insertMessage2).sendCommands();

        assertExpectedTagsInTable("Series tag keys is not added into Series Tag Key IDs: ",
                tagNames, new SeriesTagKeyIDsPage().getTable());
        assertExpectedTagsInTable("Series tag values is not added into Series Tag Values IDs: ",
                tagValues, new SeriesTagValueIDsPage().getTable());
        assertSeriesAdd();
        assertEntityAdd();
        assertSeriesParams(metricText1 + "; " + metricText2, tagNames, tagValues);
    }

    @Test
    public void testMetricAdd() {
        String status = "false";
        String label = "label_metric";
        String description = "descr_metric";
        String dataType = "LONG";
        String interpolationMode = "PREVIOUS";
        String units = "Celsius";
        String filterExpression = "value>0";
        String timeZone = "CET";
        String versioning = "true";
        String invalidAction = "DISCARD";
        String persistent = "true";
        String retentionIntervalDays = "20";
        String minVal = "10";
        String maxVal = "100";
        String[] tagNames = {"metric_tag_name1", "metric_tag_name2"};
        String[] tagValues = {"metric_tag_value1", "metric_tag_value2"};
        String insertMessage = String.format("metric m:%s b:%s l:%s d:%s p:%s i:%s u:%s f:%s z:%s v:%s a:%s pe:%s " +
                        "rd:%s min:%s max:%s t:%s=%s t:%s=%s", METRIC_NAME, status, label, description, dataType,
                interpolationMode, units, filterExpression, timeZone, versioning, invalidAction, persistent,
                retentionIntervalDays, minVal, maxVal, tagNames[0], tagValues[0], tagNames[1], tagValues[1]);

        dataEntryPage.typeCommands(insertMessage).sendCommands();

        assertMetricAdd();
        checkMetricParams(status, label, description, dataType, interpolationMode, units,
                filterExpression, timeZone, versioning, invalidAction, persistent,
                retentionIntervalDays, minVal, maxVal, tagNames, tagValues);
    }

    @Test
    public void testEntityAdd() {
        String status = "true";
        String label = "label_entity";
        String timeZone = "CET";
        String interpolationMode = "PREVIOUS";
        String[] tagNames = {"entity_tag_name1", "entity_tag_name2"};
        String[] tagValues = {"entity_tag_value1", "entity_tag_value2"};
        String insertMessage = String.format("entity e:%s b:%s l:%s i:%s z:%s t:%s=%s t:%s=%s", ENTITY_NAME, status, label,
                interpolationMode, timeZone, tagNames[0], tagValues[0], tagNames[1], tagValues[1]);

        dataEntryPage.typeCommands(insertMessage).sendCommands();

        assertEntityAdd();
        assertEntityParams(status, label, interpolationMode, timeZone, tagNames, tagValues);
    }

    @Step
    @After
    public void cleanup() {
        dropRecord(new EntitiesTablePage(), ENTITY_NAME);
        dropRecord(new MetricsTablePage(), METRIC_NAME);
    }

    @Step("Drop {recordName} from table if it is exist")
    private <T extends Table> void dropRecord(T page, String recordName) {
        page.searchRecordByName(recordName);
        if (page.isRecordPresent(recordName)) {
            removeRecordByCheckbox(recordName);
        }
        assertFalse(recordName + " is not deleted", page.isRecordPresent(recordName));
    }

    private void removeRecordByCheckbox(String value) {
        clickCheckboxByValueAttribute(value);
        CommonActions.deleteRecord();
    }

    @Step("Check entity adds into entities table")
    private void assertEntityAdd() {
        EntitiesTablePage entitiesTablePage = new EntitiesTablePage();
        assertTrue("Entity is not added", entitiesTablePage.isRecordPresent(ENTITY_NAME));
    }

    @Step("Check message adds into message table by its entity name")
    private void assertMessageAddByEntityName() {
        MessagesPage messagesPage = new MessagesPage();
        messagesPage.setEntity(ENTITY_NAME)
                .search();
        assertTrue("Message is not added into table", messagesPage.getCountOfMessages() > 0);
    }

    @Step("Check property adds into entity properties table")
    private void assertPropertiesAdd(String propType) {
        PropertiesTablePage propertiesTablePage = new PropertiesTablePage(ENTITY_NAME);
        assertTrue("Property is not added", propertiesTablePage.isPropertyPresent(propType));
    }

    @Step("Check properties keys and tags")
    private void assertPropertiesKeysAndTags(String propType, String[] key_names, String[] key_values,
                                             String[] tag_names, String[] tag_values) {
        PropertiesPage propertiesPage = new PropertiesPage(ENTITY_NAME, new String[]{"type"}, new String[]{propType});
        String[] keys = ArrayUtils.addAll(key_names, key_values);
        String[] tags = ArrayUtils.addAll(tag_names, tag_values);
        String allTagsAdnKeys = propertiesPage.getTagsAndKeys();
        assertExpectedTagsInTable("There is no such key in property:", keys, allTagsAdnKeys);
        assertExpectedTagsInTable("There is no such tag in property:", tags, allTagsAdnKeys);
    }

    @Step("Check series add by appropriate metric")
    private void assertSeriesAdd() {
        MetricsSeriesTablePage metricsSeriesTablePage = new MetricsSeriesTablePage(METRIC_NAME);
        assertTrue("Series is not added", metricsSeriesTablePage.isSeriesPresent());
    }

    @Step("Check series parameters")
    private void assertSeriesParams(String metricText, String[] tagNames, String[] tagValues) {
        StatisticsPage statisticsPage = new StatisticsPage(
                Stream.concat(Stream.of("entity", "metric"), Arrays.stream(tagNames)).toArray(String[]::new),
                Stream.concat(Stream.of(ENTITY_NAME, METRIC_NAME), Arrays.stream(tagValues)).toArray(String[]::new));
        String allTags = statisticsPage.getSeriesTags();
        assertExpectedTagsInTable("There is no such tag name:", tagNames, allTags);
        assertExpectedTagsInTable("There is no such tag value:", tagValues, allTags);

        statisticsPage.getSampleDataTab();
        assertTrue("Metric text is not added", statisticsPage.getSampleDataTableText().contains(metricText));
    }

    @Step("Check metric adds into metrics table")
    private void assertMetricAdd() {
        MetricIDsPage metricIDsPage = new MetricIDsPage();
        assertTrue("Metric is not added into Metric IDs table",
                CommonActions.getValuesInTable(metricIDsPage.getTable()).contains(METRIC_NAME));

        MetricsTablePage metricsTablePage = new MetricsTablePage();
        metricsTablePage.searchRecordByName(METRIC_NAME);
        assertTrue("Metric is not added into table on Metric Page", metricsTablePage.isRecordPresent(METRIC_NAME));
    }

    @Step("Check metric parameters")
    private void checkMetricParams(String status, String label, String description, String dataType,
                                   String interpolationMode, String units, String filter, String timeZone,
                                   String versioning, String invalidAction, String persistent, String retentionIntervalDays,
                                   String minVal, String maxVal, String[] tagNames, String[] tagValues) {
        MetricPage metricPage = new MetricPage(new String[]{"metricName"}, new String[]{METRIC_NAME});

        assertSwitchElement("Wrong persistent", persistent, metricPage.getPersistentSwitch());
        assertSwitchElement("Wrong status", status, metricPage.getEnabledSwitch());
        assertSwitchElement("Wrong versioning", versioning, metricPage.getVersioningSwitch());
        metricPage.getLabel().shouldHave(value(label));
        metricPage.getInterpolation().shouldHave(value(interpolationMode));
        assertExpectedTagsInTable("There is no such tag name: ", tagNames, metricPage.getTagNames());
        assertExpectedTagsInTable("There is no such tag value: ", tagValues, metricPage.getTagValues());
        metricPage.getDescription().shouldHave(value(description));
        metricPage.getDataType().shouldHave(value(dataType));
        metricPage.getUnits().shouldHave(value(units));
        metricPage.getPersistentFilter().shouldHave(value(filter));
        metricPage.getMinValue().shouldHave(value(minVal));
        metricPage.getMaxValue().shouldHave(value(maxVal));
        metricPage.getRetentionIntervalDays().shouldHave(value(retentionIntervalDays));
        metricPage.getInvalidAction().shouldHave(value(invalidAction));
        metricPage.getTimeZone().shouldHave(value(timeZone));
    }

    @Step
    private void assertSwitchElement(String errorMessage, String expectedValue, SelenideElement switchButton) {
        String script = "return element.checked";
        assertEquals(errorMessage, Boolean.parseBoolean(expectedValue), executeWithElement(switchButton, script));
    }

    @Step("Check entity parameters")
    private void assertEntityParams(String status, String label, String interpolationMode,
                                    String timeZone, String[] tagNames, String[] tagValues) {
        EntityPage entityPage = new EntityPage(ENTITY_NAME);

        assertSwitchElement("Wrong status", status, entityPage.getEnabledSwitch());
        entityPage.getLabel().shouldHave(value(label));
        entityPage.getInterpolation().shouldHave(value(interpolationMode));
        assertExpectedTagsInTable("There is no such tag name: ", tagNames, entityPage.getTagNames());
        assertExpectedTagsInTable("There is no such tag value: ", tagValues, entityPage.getTagValues());
        entityPage.getTimeZone().shouldHave(value(timeZone));
    }

    @Step("Check message  parameters")
    private void assertMessageParameters(String type, String source, String severity) {
        MessagesPage messagesPage = new MessagesPage();
        messagesPage.setEntity(ENTITY_NAME).search();

        messagesPage.openFilterPanel()
                .setType(type)
                .search();
        assertTrue("Type tag is not added into message", messagesPage.getCountOfMessages() > 0);

        messagesPage.openFilterPanel()
                .setSource(source)
                .search();
        assertTrue("Source tag is not added into message", messagesPage.getCountOfMessages() > 0);

        String[] messagesSeverity = messagesPage.getMessagesSeverity();
        assertEquals("Wrong severity", severity, messagesSeverity[0]);
    }

}
