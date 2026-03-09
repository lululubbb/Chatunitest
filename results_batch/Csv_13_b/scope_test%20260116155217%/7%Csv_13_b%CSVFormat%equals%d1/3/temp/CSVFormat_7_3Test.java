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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Using reflection to invoke private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false);
    }

    @Test
    @Timeout(8000)
    public void testEquals_sameInstance() {
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
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormat((char)(format1.getDelimiter() + 1), format1.getQuoteCharacter(),
                format1.getQuoteMode(), format1.getCommentMarker(), format1.getEscapeCharacter(),
                format1.getIgnoreSurroundingSpaces(), format1.getIgnoreEmptyLines(),
                format1.getRecordSeparator(), format1.getNullString(), format1.getHeader(),
                format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        QuoteMode otherQuoteMode = format1.getQuoteMode() == QuoteMode.ALL ? QuoteMode.MINIMAL : QuoteMode.ALL;
        CSVFormat format2 = createCSVFormat(format1.getDelimiter(), format1.getQuoteCharacter(),
                otherQuoteMode, format1.getCommentMarker(), format1.getEscapeCharacter(),
                format1.getIgnoreSurroundingSpaces(), format1.getIgnoreEmptyLines(),
                format1.getRecordSeparator(), format1.getNullString(), format1.getHeader(),
                format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_equal() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_equal() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', null,
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_equal() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_equal() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_different() throws Exception {
        String[] header1 = new String[]{"a", "b"};
        String[] header2 = new String[]{"a", "c"};
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", header1, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", header2, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_equal() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", header, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", header.clone(), false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                true, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, true, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, true);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, "\n", "NULL", null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_equal() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, "\n", "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                false, false, "\n", "NULL", null, false);
        assertTrue(format1.equals(format2));
    }
}