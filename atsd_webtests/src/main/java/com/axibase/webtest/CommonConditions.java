package com.axibase.webtest;

import com.codeborne.selenide.Condition;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConditions {
    public static final Condition clickable = Condition.and("clickable", Condition.visible, Condition.enabled);
}
