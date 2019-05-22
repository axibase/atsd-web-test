package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Series {
    private String entityName;
    private String metricName;
    private String metricText;
    private String metricValue;
    private String[] tagNames;
    private String[] tagValues;

}
