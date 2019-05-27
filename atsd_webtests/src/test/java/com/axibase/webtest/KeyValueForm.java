package com.axibase.webtest;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class KeyValueForm {
    protected SelenideElement keyField;
    protected SelenideElement valueField;

    public SelenideElement getKeyField() {
        return this.keyField;
    }

    public SelenideElement getValueField() {
        return this.valueField;
    }

    public String getKey() {
        return this.keyField.getValue();
    }

    public String getValue() {
        return this.valueField.getValue();
    }

    public abstract void setKeyField(String inspector);
    public abstract void setValueField(String inspector);
}
