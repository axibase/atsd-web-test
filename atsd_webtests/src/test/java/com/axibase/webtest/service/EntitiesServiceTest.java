package com.axibase.webtest.service;


import com.axibase.webtest.CommonAssertions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class EntitiesServiceTest extends AtsdTest {

    @Test
    public void checkDefaultEntity() {
        open("/admin/system-information");
        CommonAssertions.assertPageTitleEquals(SystemInfoService.TITLE);
        SystemInfoService sis = new SystemInfoService();
        String hostname = sis.getSystemInfoValue("hostname");
        assertNotNull("Can't find hostname value", hostname);
        open("/entities");
        CommonAssertions.assertPageTitleAfterLoadEquals(EntitiesService.TITLE);
        EntitiesService es = new EntitiesService();
        assertTrue(generateAssertMessage("Should be more than 1 row in entities table"), es.getEntitiesCount() > 1);
        assertNotNull(generateAssertMessage("Can't find 'atsd' cell"), es.getEntityByName("atsd"));
        //Assert.assertNotNull(generateAssertMessage("Can't find 'entity-1' cell"), es.getEntityByName("entity-1"));
        assertNotNull(generateAssertMessage("Can't find " + hostname + " cell"), es.getEntityByName(hostname.toLowerCase()));
    }
}
