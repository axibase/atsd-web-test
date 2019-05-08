package com.axibase.webtest.service;


import com.axibase.webtest.CommonAssertions;
import org.junit.Assert;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.open;

public class EntitiesServiceTest extends AtsdTest {

    @Test
    public void checkDefaultEntity() {
        open("/admin/system-information");
        CommonAssertions.assertPageTitleEquals(SystemInfoService.TITLE);
        SystemInfoService sis = new SystemInfoService();
        String hostname = sis.getSystemInfoValue("hostname");
        Assert.assertNotNull("Can't find hostname value", hostname);
        open("/entities");
        CommonAssertions.assertPageTitleAfterLoadEquals(EntitiesService.TITLE);
        EntitiesService es = new EntitiesService();
        Assert.assertTrue(generateAssertMessage("Should be more than 1 row in entities table"), es.getEntitiesCount() > 1);
        Assert.assertNotNull(generateAssertMessage("Can't find 'atsd' cell"), es.getEntityByName("atsd"));
        //Assert.assertNotNull(generateAssertMessage("Can't find 'entity-1' cell"), es.getEntityByName("entity-1"));
        Assert.assertNotNull(generateAssertMessage("Can't find " + hostname + " cell"), es.getEntityByName(hostname.toLowerCase()));
    }
}
