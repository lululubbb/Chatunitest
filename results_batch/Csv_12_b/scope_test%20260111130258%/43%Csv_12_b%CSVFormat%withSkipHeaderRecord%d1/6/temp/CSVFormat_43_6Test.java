package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_43_6Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() {
        // Given
        boolean skipHeaderRecord = false;
        CSVFormat csvFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordUsingReflection() throws Exception {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = invokeWithSkipHeaderRecord(csvFormat, skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
    }

    private CSVFormat invokeWithSkipHeaderRecord(CSVFormat csvFormat, boolean skipHeaderRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withSkipHeaderRecord", boolean.class);
        method.setAccessible(true);
        CSVFormat newCsvFormat = (CSVFormat) method.invoke(csvFormat, skipHeaderRecord);
        return newCsvFormat;
    }
}