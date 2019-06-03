package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;

@Data
@Accessors(chain = true)
public class Series {
    private String entityName;
    private String metricName;
    private String metricText="";
    private String metricValue="NaN";
    private String[] tagNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] tagValues = ArrayUtils.EMPTY_STRING_ARRAY;

}
