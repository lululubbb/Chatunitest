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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_37_1Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('\"', newCsvFormat.getQuoteCharacter());
        assertNull(newCsvFormat.getQuoteMode());
        assertNull(newCsvFormat.getCommentMarker());
        assertNull(newCsvFormat.getEscapeCharacter());
        assertFalse(newCsvFormat.getIgnoreSurroundingSpaces());
        assertTrue(newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertNull(newCsvFormat.getHeader());
        assertFalse(newCsvFormat.getSkipHeaderRecord());
        assertFalse(newCsvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_ReturnsNewInstance() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_DefaultInstanceUnchanged() {
        // Given
        CSVFormat defaultCsvFormat = CSVFormat.DEFAULT;
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = defaultCsvFormat.withNullString(nullString);

        // Then
        assertNotSame(defaultCsvFormat, newCsvFormat);
        assertNotEquals(nullString, defaultCsvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_ReflectiveInvocation() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");
        String nullString = "NULL";
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // When
        CSVFormat reflectiveNewCsvFormat = invokeWithNullStringUsingReflection(csvFormat, nullString);

        // Then
        assertEquals(newCsvFormat, reflectiveNewCsvFormat);
    }

    private CSVFormat invokeWithNullStringUsingReflection(CSVFormat csvFormat, String nullString) throws Exception {
        Class<?>[] cArg = {String.class};
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withNullString", cArg);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, nullString);
    }
}