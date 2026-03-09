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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_37_6Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
        assertEquals(null, newCsvFormat.getQuoteMode());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals(null, newCsvFormat.getEscapeCharacter());
        assertEquals(false, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(true, newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getHeader());
        assertEquals(false, newCsvFormat.getSkipHeaderRecord());
        assertEquals(false, newCsvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_ReflectiveCall() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = invokeWithNullStringUsingReflection(csvFormat, nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
        assertEquals(null, newCsvFormat.getQuoteMode());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals(null, newCsvFormat.getEscapeCharacter());
        assertEquals(false, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(true, newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getHeader());
        assertEquals(false, newCsvFormat.getSkipHeaderRecord());
        assertEquals(false, newCsvFormat.getAllowMissingColumnNames());
    }

    private CSVFormat invokeWithNullStringUsingReflection(CSVFormat csvFormat, String nullString) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("withNullString", String.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, nullString);
    }
}