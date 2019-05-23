package com.axibase.webtest.dataproviders;

import com.axibase.webtest.modelobjects.*;
import org.testng.annotations.DataProvider;

public class DataEntryTestDataProvider {
    @DataProvider(name = "entityTest")
    public static Object[][] entityData() {
        return new Object[][]{{"entity e:data-entry-commands-test_entity-test b:true l:label i:PREVIOUS z:CET" +
                " t:entity_tag_name1=entity_tag_value1 t:entity_tag_name2=entity_tag_value2",
                new Entity().setEntityName("data-entry-commands-test_entity-test")
                        .setStatus(true)
                        .setLabel("label")
                        .setInterpolation("PREVIOUS")
                        .setTimeZone("CET")
                        .setTagNames(new String[]{"entity_tag_name1", "entity_tag_name2"})
                        .setTagValues(new String[]{"entity_tag_value1", "entity_tag_value2"})},

                {"entity e:data-entry-commands-test_entity-test2",
                        new Entity().setEntityName("data-entry-commands-test_entity-test2")}
        };
    }

    @DataProvider(name = "metricTest")
    public static Object[][] metricData() {
        return new Object[][]{{"metric m:metric_name_data_entry_commands_test_metric-test b:false l:label_metric d:descr_metric" +
                " p:long i:previous u:Celsius f:value>0 z:CET v:true a:DISCARD pe:true rd:20 min:10 max:100" +
                " t:metric_tag_name1=metric_tag_value1 t:metric_tag_name2=metric_tag_value2",
                new Metric().setMetricName("metric_name_data_entry_commands_test_metric-test")
                        .setStatus(false)
                        .setLabel("label_metric")
                        .setDescription("descr_metric")
                        .setDataType("LONG")
                        .setInterpolationMode("PREVIOUS")
                        .setUnits("Celsius")
                        .setFilterExpression("value>0")
                        .setTimeZone("CET")
                        .setVersioning(true)
                        .setInvalidAction("DISCARD")
                        .setPersistent(true)
                        .setRetentionIntervalDays("20")
                        .setMinVal("10")
                        .setMaxVal("100")
                        .setTagNames(new String[]{"metric_tag_name1", "metric_tag_name2"})
                        .setTagValues(new String[]{"metric_tag_value1", "metric_tag_value2"})},

                {"metric m:metric_name_data_entry_commands_test_metric-test2",
                        new Metric().setMetricName("metric_name_data_entry_commands_test_metric-test2")}
        };
    }

    @DataProvider(name = "propertyTest")
    public static Object[][] propertyData() {
        return new Object[][]{{"property e:entity_name_data_entry_commands_test_property-test t:property_type" +
                " k:property_key_name1=property_key_value1 k:property_key_name2=property_key_value2" +
                " v:property_tag_name1=property_tag_value1 v:property_tag_name2=property_tag_value2",
                new Property()
                        .setEntityName("entity_name_data_entry_commands_test_property-test")
                        .setPropType("property_type")
                        .setKeyNames(new String[]{"property_key_name1", "property_key_name2"})
                        .setKeyValues(new String[]{"property_key_value1", "property_key_value2"})
                        .setTagNames(new String[]{"property_tag_name1", "property_tag_name2"})
                        .setTagValues(new String[]{"property_tag_value1", "property_tag_value2"})},

                {"property e:entity_name_data_entry_commands_test_property-test2 t:property_type2" +
                        " v:property_tag_name=property_tag_value",
                        new Property()
                                .setEntityName("entity_name_data_entry_commands_test_property-test2")
                                .setPropType("property_type2")
                                .setTagNames(new String[]{"property_tag_name"})
                                .setTagValues(new String[]{"property_tag_value"})}
        };
    }

    @DataProvider(name = "messageTest")
    public static Object[][] messageData() {
        return new Object[][]{{"message e:entity_name_data_entry_commands_test_message-test t:type=message_tag_type_value" +
                " t:source=message_tag_source_value t:severity=FATAL t:message_tag_name1=message_tag_tag_value1" +
                " t:message_tag_name2=message_tag_tag_value2 m:\"Message text\"",
                new Message().setEntityName("entity_name_data_entry_commands_test_message-test")
                        .setType("message_tag_type_value")
                        .setSource("message_tag_source_value")
                        .setSeverity("FATAL")
                        .setTagNames(new String[]{"message_tag_name1", "message_tag_name2"})
                        .setTagValues(new String[]{"message_tag_tag_value1", "message_tag_tag_value2"})
                        .setMessageText("Message text")},

                {"message e:entity_name_data_entry_commands_test_message-test2 m:\"Message text\"",
                        new Message().setEntityName("entity_name_data_entry_commands_test_message-test2")
                                .setMessageText("Message text")},

                {"message e:entity_name_data_entry_commands_test_message-test3 t:source=test_source",
                        new Message().setEntityName("entity_name_data_entry_commands_test_message-test3")
                                .setSource("test_source")}
        };
    }

    @DataProvider(name = "seriesTest")
    public static Object[][] seriesData() {
        return new Object[][]{{"series e:entity_name_data_entry_commands_test_series-test" +
                " m:metric_name_data_entry_commands_test_series-test=10" +
                " x:metric_name_data_entry_commands_test_series-test=metric_text1 a:true t:series_tag_key1=series_tag_value1" +
                " t:series_tag_key2=series_tag_value2 s:1425482080 \n" +

                "series e:entity_name_data_entry_commands_test_series-test  m:metric_name_data_entry_commands_test_series-test=10" +
                " x:metric_name_data_entry_commands_test_series-test=metric_text2 a:true t:series_tag_key1=series_tag_value1" +
                " t:series_tag_key2=series_tag_value2 s:1425482080",

                new Series().setEntityName("entity_name_data_entry_commands_test_series-test")
                        .setMetricName("metric_name_data_entry_commands_test_series-test")
                        .setMetricValue("10.0")
                        .setMetricText("metric_text1; metric_text2")
                        .setTagNames(new String[]{"series_tag_key1", "series_tag_key2"})
                        .setTagValues(new String[]{"series_tag_value1", "series_tag_value2"})
        },

                {"series e:entity_name_data_entry_commands_test_series-test2 " +
                        "m:metric_name_data_entry_commands_test_series-test2=10",

                        new Series().setEntityName("entity_name_data_entry_commands_test_series-test2")
                                .setMetricName("metric_name_data_entry_commands_test_series-test2")
                                .setMetricValue("10.0")},

                {"series e:entity_name_data_entry_commands_test_series-test3 " +
                        " x:metric_name_data_entry_commands_test_series-test3=metric_text",

                        new Series().setEntityName("entity_name_data_entry_commands_test_series-test3")
                                .setMetricName("metric_name_data_entry_commands_test_series-test3")
                                .setMetricText("metric_text")}
        };
    }

}
