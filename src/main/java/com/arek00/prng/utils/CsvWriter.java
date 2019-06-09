package com.arek00.prng.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CsvWriter {
    public static void writeResultSet(final ResultSet rs, final File outputFile) throws SQLException, IOException {
        if(!outputFile.exists()) {
            log.info("Creating file.");
            FileUtils.touch(outputFile);
        }

        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(rs).print(outputFile, Charset.defaultCharset());

        printer.printRecords(rs);
        printer.close();
    }
}
