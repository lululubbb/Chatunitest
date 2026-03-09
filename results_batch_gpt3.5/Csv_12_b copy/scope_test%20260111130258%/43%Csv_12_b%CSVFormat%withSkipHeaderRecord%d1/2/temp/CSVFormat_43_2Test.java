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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_43_2Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        boolean skipHeaderRecord = true;
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, null, false, false, "\r\n", null, null, false, false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        boolean skipHeaderRecord = false;
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, null, false, false, "\r\n", null, null, true, false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordUsingMockito() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.withSkipHeaderRecord(skipHeaderRecord)).thenReturn(csvFormat);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }
}