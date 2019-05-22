package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Entity {
    private String entityName;
    private boolean status;
    private String label;
    private String interpolation;
    private String timeZone;
    private String[] tagNames;
    private String[] tagValues;

}
