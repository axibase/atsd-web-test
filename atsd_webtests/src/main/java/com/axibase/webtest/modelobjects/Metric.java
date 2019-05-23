package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Metric {
    private String metricName;
    private boolean status = true;
    private String label = "";
    private String description = "";
    private String dataType = "FLOAT";
    private String interpolationMode = "LINEAR";
    private String units = "";
    private String filterExpression = "";
    private String timeZone = "";
    private boolean versioning = false;
    private String invalidAction = "NONE";
    private boolean persistent = true;
    private String retentionIntervalDays = "0";
    private String minVal = "";
    private String maxVal = "";
    private String[] tagNames = new String[]{};
    private String[] tagValues = new String[]{};

}
