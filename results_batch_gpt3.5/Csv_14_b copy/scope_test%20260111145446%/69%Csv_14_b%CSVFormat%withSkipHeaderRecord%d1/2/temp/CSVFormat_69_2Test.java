package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_69_2Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withSkipHeaderRecord(false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordReflection() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withSkipHeaderRecord(false);

        // When
        try {
            Method method = CSVFormat.class.getDeclaredMethod("withSkipHeaderRecord", boolean.class);
            method.setAccessible(true);
            CSVFormat result = (CSVFormat) method.invoke(csvFormat, skipHeaderRecord);

            // Then
            assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        boolean allowMissingColumnNames = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withSkipHeaderRecord(false);

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames(allowMissingColumnNames);

        // Then
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }

    // You can add more test cases for different scenarios to improve coverage

}