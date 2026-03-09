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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_33_6Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");

        // When
        CSVFormat newCsvFormat = csvFormat.withHeader(header);

        // Then
        assertEquals(header, newCsvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");

        // When
        CSVFormat newCsvFormat = csvFormat.withHeader((String[]) null);

        // Then
        assertEquals(null, newCsvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() {
        // Given
        String[] header = {};
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");

        // When
        CSVFormat newCsvFormat = csvFormat.withHeader(header);

        // Then
        assertEquals(header, newCsvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_PrivateConstructorInvocation() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");
        String[] header = {"Name", "Age", "City"};

        // When
        CSVFormat newCsvFormat = invokePrivateConstructor(csvFormat, "CSVFormat", char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class, header);

        // Then
        assertEquals(header, newCsvFormat.getHeader());
    }

    private CSVFormat invokePrivateConstructor(Object object, String methodName, Class<?>... parameterTypes)
            throws Exception {
        Constructor<?> constructor = object.getClass().getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(object, ',', '"', null, null, null, false, true, "\r\n", null, new String[0], false, false);
    }
}