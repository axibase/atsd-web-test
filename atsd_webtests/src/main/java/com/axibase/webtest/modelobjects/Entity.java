package com.axibase.webtest.modelobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Entity {
    private String entityName;
    private boolean status = true;
    private String label = "";
    private String interpolation = "";
    private String timeZone = "";
    private String[] tagNames = new String[]{};
    private String[] tagValues = new String[]{};

}
