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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

public class CSVFormat_43_1Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() throws NoSuchFieldException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean skipHeaderRecord = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean result = (boolean) skipHeaderRecordField.get(newCsvFormat);

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() throws NoSuchFieldException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean skipHeaderRecord = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean result = (boolean) skipHeaderRecordField.get(newCsvFormat);

        assertFalse(result);
    }
}