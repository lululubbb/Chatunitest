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
import java.lang.reflect.Method;

class CSVFormat_34_4Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(true);

        assertNotNull(newFormat);
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(false);

        assertNotNull(newFormat);
        assertFalse(newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testPrivateValidateMethod() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        validate.invoke(format);
    }
}