package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Metric {
    private String metricName;
    private boolean status;
    private String label;
    private String description;
    private String dataType;
    private String interpolationMode;
    private String units;
    private String filterExpression;
    private String timeZone;
    private boolean versioning;
    private String invalidAction;
    private boolean persistent;
    private int retentionIntervalDays;
    private int minVal;
    private int maxVal;
    private String[] tagNames;
    private String[] tagValues;

}
