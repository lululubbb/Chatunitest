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
import java.lang.reflect.Constructor;

public class CSVFormat_5_2Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndValidate() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Test with typical values
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\r\n", "NULL", new Object[]{"comment1", "comment2"},
                new String[]{"header1", "header2"}, true, true, true);
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());

        // Test with null header and headerComments
        CSVFormat format2 = constructor.newInstance(
                ';', null, null, null, null,
                false, true, "\n", null, (Object[]) null,
                null, false, false, false);
        assertNotNull(format2);
        assertEquals(';', format2.getDelimiter());
        assertNull(format2.getQuoteCharacter());
        assertNull(format2.getQuoteMode());
        assertNull(format2.getCommentMarker());
        assertNull(format2.getEscapeCharacter());
        assertFalse(format2.getIgnoreSurroundingSpaces());
        assertTrue(format2.getIgnoreEmptyLines());
        assertEquals("\n", format2.getRecordSeparator());
        assertNull(format2.getNullString());
        assertNull(format2.getHeaderComments());
        assertNull(format2.getHeader());
        assertFalse(format2.getSkipHeaderRecord());
        assertFalse(format2.getAllowMissingColumnNames());
        assertFalse(format2.getIgnoreHeaderCase());
    }

}