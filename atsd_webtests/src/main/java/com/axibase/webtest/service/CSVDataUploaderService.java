package com.axibase.webtest.service;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import static com.codeborne.selenide.Selenide.*;

public class CSVDataUploaderService extends Service {
    private static final String PARSER_IMPORT_PATH = "/csv/configs/import";
    private static final String CSV_IMPORT_PATH = "/data/csv/parsers/parser/";
    private static final String PARSERS_TABLE_PATH = "/csv/configs";

    private Set<String> uploadedParsers = new HashSet<>();

    public CSVDataUploaderService() {
        open(PARSERS_TABLE_PATH);
        for (SelenideElement parser : $$(By.xpath("//*/tbody/tr"))) {
            uploadedParsers.add(parser.$(By.xpath("td/a")).getText());
        }
    }

    public void uploadParser(String fullPathToParser, String parserName) {
        uploadParserByPath(fullPathToParser);
        uploadedParsers.add(parserName);
    }

    public void uploadWithParser(String pathToCSV, String parserName) {
        uploadParserByName(parserName);
        uploadCSVFileByPath(pathToCSV, parserName);
    }

    private void uploadParserByName(String parserName) {
        if (!uploadedParsers.contains(parserName)) {
            String pathToParser = getPathToXMLFileByName(parserName);
            uploadParser(pathToParser, parserName);
        }
    }

    private String getPathToXMLFileByName(String parserName) {
        return CSVDataUploaderService.class.getResource(parserName + ".xml").getFile();
    }

    private void uploadParserByPath(String pathToParser) {
        open(PARSER_IMPORT_PATH);
        $(By.name("file")).sendKeys(pathToParser);
        $(By.xpath(".//input[@type='submit']")).click();
    }

    @SneakyThrows
    private void uploadCSVFileByPath(String pathToCSVFile, String parserName) {
        open(CSV_IMPORT_PATH + URLEncoder.encode(parserName, "UTF-8"));
        $(By.id("file")).sendKeys(pathToCSVFile);
        $(By.id("upload-btn")).click();
    }

}
