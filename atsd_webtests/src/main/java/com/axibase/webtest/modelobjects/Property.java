package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Property {
    private String entityName;
    private String propType;
    private String[] keyNames;
    private String[] keyValues;
    private String[] tagNames;
    private String[] tagValues;

}
