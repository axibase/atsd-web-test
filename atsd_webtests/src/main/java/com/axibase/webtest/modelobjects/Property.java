package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;

@Data
@Accessors(chain = true)
public class Property {
    private String entityName;
    private String propType="";
    private String[] keyNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] keyValues = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] tagNames;
    private String[] tagValues;

}
