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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_69_4Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean skipHeaderRecord = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean skipHeaderRecord = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordUsingReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean skipHeaderRecord = true;

        // When
        CSVFormat newCsvFormat = invokeWithSkipHeaderRecord(csvFormat, skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    private CSVFormat invokeWithSkipHeaderRecord(CSVFormat csvFormat, boolean skipHeaderRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withSkipHeaderRecord", boolean.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, skipHeaderRecord);
    }
}