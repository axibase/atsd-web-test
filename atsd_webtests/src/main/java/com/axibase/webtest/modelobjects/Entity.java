package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;

@Data
@Accessors(chain = true)
public class Entity {
    private String entityName;
    private boolean status = true;
    private String label = "";
    private String interpolation = "";
    private String timeZone = "";
    private String[] tagNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] tagValues = ArrayUtils.EMPTY_STRING_ARRAY;

}
