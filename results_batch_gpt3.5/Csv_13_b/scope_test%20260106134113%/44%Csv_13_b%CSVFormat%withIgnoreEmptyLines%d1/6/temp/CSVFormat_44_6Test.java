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

public class CSVFormat_44_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrueCreatesNewInstanceWithTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines(true);

        assertNotSame(format, newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalseCreatesNewInstanceWithFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat newFormat = format.withIgnoreEmptyLines(false);

        assertNotSame(format, newFormat);
        assertFalse(newFormat.getIgnoreEmptyLines());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesDoesNotModifyOriginalInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        boolean originalIgnoreEmptyLines = format.getIgnoreEmptyLines();

        CSVFormat newFormat = format.withIgnoreEmptyLines(!originalIgnoreEmptyLines);

        assertEquals(originalIgnoreEmptyLines, format.getIgnoreEmptyLines());
        assertNotEquals(originalIgnoreEmptyLines, newFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesPrivateMethodViaReflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method withIgnoreEmptyLinesMethod = CSVFormat.class.getDeclaredMethod("withIgnoreEmptyLines", boolean.class);
        withIgnoreEmptyLinesMethod.setAccessible(true);

        CSVFormat newFormatTrue = (CSVFormat) withIgnoreEmptyLinesMethod.invoke(format, true);
        assertTrue(newFormatTrue.getIgnoreEmptyLines());

        CSVFormat newFormatFalse = (CSVFormat) withIgnoreEmptyLinesMethod.invoke(format, false);
        assertFalse(newFormatFalse.getIgnoreEmptyLines());
    }
}