package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class CSVFormat_5_6Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorAndDefaults() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Test with all null/false/default parameters
        CSVFormat format = constructor.newInstance(',',
                (Character) null,
                null,
                (Character) null,
                (Character) null,
                false,
                true,
                "\r\n",
                null,
                (Object[]) null,
                (String[]) null,
                false,
                false,
                false,
                false,
                false,
                false);

        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());
        assertFalse(format.getTrim());
        assertFalse(format.getTrailingDelimiter());
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorWithVariousParameters() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"h1", "h2"};

        CSVFormat format = constructor.newInstance('|',
                Character.valueOf('"'),
                QuoteMode.ALL_NON_NULL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                false,
                "\n",
                "NULL",
                headerComments,
                header,
                true,
                true,
                true,
                true,
                true,
                true);

        assertEquals('|', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(header, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorValidateThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char invalidDelimiter = '\n';
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(invalidDelimiter,
                    (Character) null,
                    null,
                    (Character) null,
                    (Character) null,
                    false,
                    true,
                    "\n",
                    null,
                    (Object[]) null,
                    (String[]) null,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false);
        });
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}