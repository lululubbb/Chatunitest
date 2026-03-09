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

public class CSVFormat_34_1Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withIgnoreEmptyLines(true).withAllowMissingColumnNames(false);
        CSVFormat expected = CSVFormat.newFormat(',').withIgnoreEmptyLines(true).withAllowMissingColumnNames(true);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames", boolean.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(csvFormat, true);

        // Then
        assertEquals(expected.getDelimiter(), result.getDelimiter());
        assertEquals(expected.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(expected.getQuoteMode(), result.getQuoteMode());
        assertEquals(expected.getCommentMarker(), result.getCommentMarker());
        assertEquals(expected.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(expected.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(expected.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(expected.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(expected.getNullString(), result.getNullString());
        assertArrayEquals(expected.getHeader(), result.getHeader());
        assertEquals(expected.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertTrue(result.getAllowMissingColumnNames());
    }
}