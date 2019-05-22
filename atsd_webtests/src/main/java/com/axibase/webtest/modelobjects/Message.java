package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    private String entityName;
    private String messageText;
    private String type;
    private String source;
    private String severity;
    private String[] tagNames;
    private String[] tagValues;

}
