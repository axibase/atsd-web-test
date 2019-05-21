package com.axibase.webtest.service;


import com.google.common.net.UrlEscapers;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codeborne.selenide.Selenide.download;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by sild on 02.02.15.
 */
public class ExportServiceTest extends AtsdTest {
    private static final String EXPORT_CONFIG = "{\"m\":\"jvm_memory_used_percent\",\"si\":\"5-MINUTE\",\"t\":\"HISTORY\",\"f\":\"CSV\",\"np\":-1,\"v\":false,\"tf\":\"LOCAL\",\"ms\":false,\"ro\":false,\"te\":[],\"am\":false}";
    private static final String EXPORT_URL = "/export?settings=" + UrlEscapers.urlFormParameterEscaper().escape(EXPORT_CONFIG);

    @Test
    public void testHttpCsv() throws IOException {
        final File csvFile = download(EXPORT_URL);
        checkExportedData(csvFile);
    }

    @Test
    public void testCurlCsv() throws IOException {
        final File csvFile = File.createTempFile("output", ".csv");
        try {
            final Config config = Config.getInstance();
            final String command = "curl -u " + config.getLogin() + ":" + config.getPassword() + " -o " + csvFile.getAbsolutePath() + " " + config.getBaseUrl() + EXPORT_URL;
            Runtime.getRuntime().exec(command).waitFor();
            checkExportedData(csvFile);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new AssertionError("Can't check export due to " + e, e);
        }
    }

    private void checkExportedData(File file) throws IOException {
        try {
            final List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            final String head = lines.get(0);
            final String body = lines.get(1);
            final String expectedHeader = "Timestamp,Value,Metric,Entity,host";
            assertEquals(generateAssertMessage("Title should be equal to '" + expectedHeader + "'"), expectedHeader, head);
            assertTrue(generateAssertMessage("Body should contain 'jvm_memory_used_percent'"), body.contains("jvm_memory_used_percent"));
        } finally {
            FileUtils.forceDelete(file);
        }
    }
}
