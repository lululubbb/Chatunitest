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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_37_4Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String nullString = "NULL";

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withNullString", String.class);
        method.setAccessible(true);
        CSVFormat newCsvFormat = (CSVFormat) method.invoke(csvFormat, nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
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
}