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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormat_54_2Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_String() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";

        CSVFormat result = original.withRecordSeparator(newSeparator);
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(newSeparator, result.getRecordSeparator());

        // also check that other fields remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_String_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withRecordSeparator((String) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\r';

        CSVFormat result = original.withRecordSeparator(newSeparator);
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(String.valueOf(newSeparator), result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_privateMethodInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke private withRecordSeparator(char)
        Method method = CSVFormat.class.getDeclaredMethod("withRecordSeparator", char.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(format, '\n');

        assertNotNull(result);
        assertNotSame(format, result);
        assertEquals("\n", result.getRecordSeparator());
    }
}