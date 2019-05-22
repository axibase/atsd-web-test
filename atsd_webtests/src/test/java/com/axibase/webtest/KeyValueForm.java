package com.axibase.webtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class KeyValueForm {
//    protected String key;
//    protected String value;
    protected WebElement keyField;
    protected WebElement valueField;

    public WebElement getKeyField() {
        return this.keyField;
    }

    public WebElement getValueField() {
        return this.valueField;
    }

    public String getKey() {
        return this.keyField.getAttribute("value");
    }

    public String getValue() {
        return this.valueField.getAttribute("value");
    }

    public abstract void setKeyField(String inspector);
    public abstract void setValueField(String inspector);
}
