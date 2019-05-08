package com.axibase.webtest.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class AdminServiceTest extends AtsdTest {

    private static final String[] NTP_SERVERS = new String[]{"us.pool.ntp.org", "0.pool.ntp.org", "1.pool.ntp.org", "2.pool.ntp.org", "3.pool.ntp.org"};
    private static final long MAX_DIFF_TIME = 60000;
    private static final int WAIT_FOR_SERVER_RESPONSE = 5000;

    @Test
    public void checkAtsdTime() {
        assertTrue(generateAssertMessage("Time should be different not more 60 sec"), Math.abs(getCurrentTime() - getAtsdTime()) < MAX_DIFF_TIME);
    }

    private long getAtsdTime() {
        open( "/admin/system-information");
        assertEquals("title should be System Information", "System Information", title());
        AdminService adminService = new AdminService();
        String atsdDateString = adminService.getTime();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz");
            Date atsdDate = dateFormat.parse(atsdDateString);
            return atsdDate.getTime();
        } catch (Exception e) {
            Assert.fail(generateAssertMessage("Cant parse getting date row: " + atsdDateString));
        }
        return 0;
    }

    private long getCurrentTime() {
        long currentTime = 0;
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(WAIT_FOR_SERVER_RESPONSE);
        try {
            client.open();
            for (String server : NTP_SERVERS) {
                try {
                    InetAddress ioe = InetAddress.getByName(server);
                    TimeInfo info = client.getTime(ioe);
                    TimeStamp ntpTime = TimeStamp.getNtpTime(info.getReturnTime());
                    return ntpTime.getTime();
                } catch (Exception e2) {
                    log.error("Can't get response from server: {}.", server);
                }
            }
        } catch (SocketException se) {
            log.error("Can't open client session", se);
        } finally {
            client.close();
        }
        return currentTime;
    }
}
