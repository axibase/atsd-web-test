package com.axibase.webtest.dataproviders;

import com.axibase.webtest.modelobjects.*;
import org.testng.annotations.DataProvider;

public class DataEntryTestDataProvider {
    @DataProvider(name = "entityTest")
    public static Object[][] entityData() {
        return new Object[][]{{"entity e:dataentrycommandstest_testentityisadded_entity-1 b:true l:label i:PREVIOUS z:CET" +
                " t:entity_tag_name1=entity_tag_value1 t:entity_tag_name2=entity_tag_value2",
                new Entity().setEntityName("dataentrycommandstest_testentityisadded_entity-1")
                        .setStatus(true)
                        .setLabel("label")
                        .setInterpolation("PREVIOUS")
                        .setTimeZone("CET")
                        .setTagNames(new String[]{"entity_tag_name1", "entity_tag_name2"})
                        .setTagValues(new String[]{"entity_tag_value1", "entity_tag_value2"})},

                {"entity e:dataentrycommandstest_testentityisadded_entity-2",
                        new Entity().setEntityName("dataentrycommandstest_testentityisadded_entity-2")}
        };
    }

    @DataProvider(name = "metricTest")
    public static Object[][] metricData() {
        return new Object[][]{{"metric m:dataentrycommandstest_testmetricisadded_metric-1 b:false l:label_metric d:descr_metric" +
                " p:long i:previous u:Celsius f:value>0 z:CET v:true a:DISCARD pe:true rd:20 min:10 max:100" +
                " t:metric_tag_name1=metric_tag_value1 t:metric_tag_name2=metric_tag_value2",
                new Metric().setMetricName("dataentrycommandstest_testmetricisadded_metric-1")
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

                {"metric m:dataentrycommandstest_testmetricisadded_metric-2",
                        new Metric().setMetricName("dataentrycommandstest_testmetricisadded_metric-2")}
        };
    }

    @DataProvider(name = "propertyTest")
    public static Object[][] propertyData() {
        return new Object[][]{{"property e:dataentrycommandstest_testpropertyisadded_entity-1 t:property_type" +
                " k:property_key_name1=property_key_value1 k:property_key_name2=property_key_value2" +
                " v:property_tag_name1=property_tag_value1 v:property_tag_name2=property_tag_value2",
                new Property()
                        .setEntityName("dataentrycommandstest_testpropertyisadded_entity-1")
                        .setPropType("property_type")
                        .setKeyNames(new String[]{"property_key_name1", "property_key_name2"})
                        .setKeyValues(new String[]{"property_key_value1", "property_key_value2"})
                        .setTagNames(new String[]{"property_tag_name1", "property_tag_name2"})
                        .setTagValues(new String[]{"property_tag_value1", "property_tag_value2"})},

                {"property e:dataentrycommandstest_testpropertyisadded_entity-2 t:property_type2" +
                        " v:property_tag_name=property_tag_value",
                        new Property()
                                .setEntityName("dataentrycommandstest_testpropertyisadded_entity-2")
                                .setPropType("property_type2")
                                .setTagNames(new String[]{"property_tag_name"})
                                .setTagValues(new String[]{"property_tag_value"})}
        };
    }

    @DataProvider(name = "messageTest")
    public static Object[][] messageData() {
        return new Object[][]{{"message e:dataentrycommandstest_testmessageisadded_entity-1 t:type=message_tag_type_value" +
                " t:source=message_tag_source_value t:severity=FATAL t:message_tag_name1=message_tag_tag_value1" +
                " t:message_tag_name2=message_tag_tag_value2 m:\"Message text\"",
                new Message().setEntityName("dataentrycommandstest_testmessageisadded_entity-1")
                        .setType("message_tag_type_value")
                        .setSource("message_tag_source_value")
                        .setSeverity("FATAL")
                        .setTagNames(new String[]{"message_tag_name1", "message_tag_name2"})
                        .setTagValues(new String[]{"message_tag_tag_value1", "message_tag_tag_value2"})
                        .setMessageText("Message text")},

                {"message e:dataentrycommandstest_testmessageisadded_entity-2 m:\"Message text\"",
                        new Message().setEntityName("dataentrycommandstest_testmessageisadded_entity-2")
                                .setMessageText("Message text")},

                {"message e:dataentrycommandstest_testmessageisadded_entity-3 t:source=test_source",
                        new Message().setEntityName("dataentrycommandstest_testmessageisadded_entity-3")
                                .setSource("test_source")}
        };
    }

    @DataProvider(name = "seriesTest")
    public static Object[][] seriesData() {
        return new Object[][]{{"series e:dataentrycommandstest_testseriesisadded_entity-1" +
                " m:dataentrycommandstest_testseriesisadded_metric-1=10" +
                " x:dataentrycommandstest_testseriesisadded_metric-1=metric_text1 a:true t:series_tag_key1=series_tag_value1" +
                " t:series_tag_key2=series_tag_value2 s:1425482080 \n" +

                "series e:dataentrycommandstest_testseriesisadded_entity-1  m:dataentrycommandstest_testseriesisadded_metric-1=10" +
                " x:dataentrycommandstest_testseriesisadded_metric-1=metric_text2 a:true t:series_tag_key1=series_tag_value1" +
                " t:series_tag_key2=series_tag_value2 s:1425482080",

                new Series().setEntityName("dataentrycommandstest_testseriesisadded_entity-1")
                        .setMetricName("dataentrycommandstest_testseriesisadded_metric-1")
                        .setMetricValue("10.0")
                        .setMetricText("metric_text1; metric_text2")
                        .setTagNames(new String[]{"series_tag_key1", "series_tag_key2"})
                        .setTagValues(new String[]{"series_tag_value1", "series_tag_value2"})
        },

                {"series e:dataentrycommandstest_testseriesisadded_entity-2 " +
                        "m:dataentrycommandstest_testseriesisadded_metric-2=10",

                        new Series().setEntityName("dataentrycommandstest_testseriesisadded_entity-2")
                                .setMetricName("dataentrycommandstest_testseriesisadded_metric-2")
                                .setMetricValue("10.0")},

                {"series e:dataentrycommandstest_testseriesisadded_entity-3" +
                        " x:dataentrycommandstest_testseriesisadded_metric-3=metric_text",

                        new Series().setEntityName("dataentrycommandstest_testseriesisadded_entity-3")
                                .setMetricName("dataentrycommandstest_testseriesisadded_metric-3")
                                .setMetricText("metric_text")}
        };
    }

    @DataProvider(name = "freemarkerCommandTest")
    public static Object[][] commandData() {
        return new Object[][]{
                {"<#assign values={\"dataentrycommandstest_testcommandwithassignmap_metric-1\": 10," +
                        " \"dataentrycommandstest_testcommandwithassignmap_metric-2\": 100} />\n" +
                        "<#list values as metric, value>\n" +
                        "series e:dataentrycommandstest_testcommandwithassignmap_entity m:${metric}=${value}\n" +
                        "</#list>",
                        new String[]{"dataentrycommandstest_testcommandwithassignmap_metric-1",
                                "dataentrycommandstest_testcommandwithassignmap_metric-2"}},

                {"<#list 1..5 as i> \n" +
                        "series e:dataentrycommandstest_testcommandwithsimplelistiteration_entity " +
                        "m:dataentrycommandstest_testcommandwithsimplelistiteration_metric-${i}=10 \n" +
                        "</#list> ",
                        new String[]{"dataentrycommandstest_testcommandwithsimplelistiteration_metric-1",
                                "dataentrycommandstest_testcommandwithsimplelistiteration_metric-2",
                                "dataentrycommandstest_testcommandwithsimplelistiteration_metric-3",
                                "dataentrycommandstest_testcommandwithsimplelistiteration_metric-4",
                                "dataentrycommandstest_testcommandwithsimplelistiteration_metric-5"}},

                {"<#list 1..5 as i> \n" +
                        "series e:dataentrycommandstest_testcommandwithiteratorandcalculation_entity " +
                        "m:dataentrycommandstest_testcommandwithiteratorandcalculation_metric-${i*2}=10 \n" +
                        "</#list> ",
                        new String[]{"dataentrycommandstest_testcommandwithiteratorandcalculation_metric-2",
                                "dataentrycommandstest_testcommandwithiteratorandcalculation_metric-4",
                                "dataentrycommandstest_testcommandwithiteratorandcalculation_metric-6",
                                "dataentrycommandstest_testcommandwithiteratorandcalculation_metric-8",
                                "dataentrycommandstest_testcommandwithiteratorandcalculation_metric-10"}},

                {"<#assign startIndex=10/>\n" +
                        "<#list 1..5 as i>\n" +
                        "series e:dataentrycommandstest_testcommandwithvalueassign_entity " +
                        "m:dataentrycommandstest_testcommandwithvalueassign_metric-${i * startIndex}=5\n" +
                        "</#list>",
                        new String[]{"dataentrycommandstest_testcommandwithvalueassign_metric-10",
                                "dataentrycommandstest_testcommandwithvalueassign_metric-20",
                                "dataentrycommandstest_testcommandwithvalueassign_metric-30",
                                "dataentrycommandstest_testcommandwithvalueassign_metric-40",
                                "dataentrycommandstest_testcommandwithvalueassign_metric-50"}}
        };
    }

    // 4 and 5 - index of examples in the command helper window
    @DataProvider(name = "exampleSyntaxTest")
    public static Object[][] exampleData() {
        return new Object[][]{
                {4, "<#list 1..20 as i>\n" +
                        "series s:${(nowSeconds - i * 60)?c} e:{entity} m:{metric-name}=${(60 - 2*i)?c}\n" +
                        "</#list>"},
                {5, "<#assign values={\"metric-1\": 10, \"metric-2\": 100} />\n" +
                        "<#list values as metric, value>\n" +
                        "series e:{entity} m:${metric}=${value}\n" +
                        "</#list>"}
        };
    }

    @DataProvider(name = "invalidFreemarkerCommandTest")
    public static Object[][] invalidCommandData() {
        return new Object[][]{
                {"entity"},
                {"<#list 1..5 as i> \n" +
                        "series e:dataentrycommandstest_testinvalidcommands_entity " +
                        "m:dataentrycommandstest_testinvalidcommands_metric-${k*2}=10 \n" +
                        "</#list> "},
                {"<#assign />\n" +
                        "<#list 1..5 as i>\n" +
                        "series e:dataentrycommandstest_testinvalidcommands_entity " +
                        "m:dataentrycommandstest_testinvalidcommands_metric-${i * startIndex}=5\n" +
                        "</#list>"}};
    }

}
