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

public class CSVFormat_44_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreEmptyLines(true);

        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());
        // Original remains unchanged
        assertTrue(original.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreEmptyLines(false);

        assertNotNull(result);
        assertFalse(result.getIgnoreEmptyLines());
        // Original remains unchanged
        assertTrue(original.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesDoesNotAffectOtherFields() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesReflectionInvoke() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getMethod("withIgnoreEmptyLines", boolean.class);

        CSVFormat resultTrue = (CSVFormat) method.invoke(original, true);
        CSVFormat resultFalse = (CSVFormat) method.invoke(original, false);

        assertTrue(resultTrue.getIgnoreEmptyLines());
        assertFalse(resultFalse.getIgnoreEmptyLines());
    }
}