package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    private String entityName;
    private String messageText = "";
    private String type = "default";
    private String source = "default";
    private String severity = "UNDEFINED";
    private String[] tagNames = new String[]{};
    private String[] tagValues = new String[]{};

}
