package com.axibase.webtest.service;


import com.axibase.webtest.CommonAssertions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

@Slf4j
public class AdminServiceTest extends AtsdTest {

    private static final String[] NTP_SERVERS = new String[]{"us.pool.ntp.org", "0.pool.ntp.org", "1.pool.ntp.org", "2.pool.ntp.org", "3.pool.ntp.org"};
    private static final long MAX_DIFF_TIME = 60000;
    private static final int WAIT_FOR_SERVER_RESPONSE = 5000;

    @Test
    public void checkAtsdTime() {
        assertTrue(generateAssertMessage("Time difference should not exceed 60 sec"), Math.abs(getCurrentTime() - getAtsdTime()) < MAX_DIFF_TIME);
    }

    private long getAtsdTime() {
        open("/admin/system-information");
        CommonAssertions.assertPageTitleEquals("System Information");
        AdminService adminService = new AdminService();
        String atsdDateString = adminService.getTime();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz");
            Date atsdDate = dateFormat.parse(atsdDateString);
            return atsdDate.getTime();
        } catch (Exception e) {
            fail(generateAssertMessage("Can't parse getting date row: " + atsdDateString));
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
