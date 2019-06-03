package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;

@Data
@Accessors(chain = true)
public class Message {
    private String entityName;
    private String messageText = "";
    private String type = "default";
    private String source = "default";
    private String severity = "UNDEFINED";
    private String[] tagNames = ArrayUtils.EMPTY_STRING_ARRAY;
    private String[] tagValues = ArrayUtils.EMPTY_STRING_ARRAY;

}
