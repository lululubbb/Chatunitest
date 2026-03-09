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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                      Character commentMarker, Character escapeCharacter, String nullString,
                                      String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord, String recordSeparator) throws Exception {
        // Use reflection to invoke private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        var constructor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode,
                commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord,
                false, false, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    public void testEquals_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterNullVsNotNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, null, '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '\'', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerNullVsNotNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', null, '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '!', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterNullVsNotNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', null,
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '!',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringNullVsNotNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null", new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null1", new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null2", new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_headerNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "c"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpacesNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLinesNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, false, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecordNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorNullVsNotNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_allFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null", new String[]{"a", "b"}, true, false, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null", new String[]{"a", "b"}, true, false, true, "\r\n");
        assertTrue(format1.equals(format2));
    }
}