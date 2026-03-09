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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter,
                                      Character quoteCharacter,
                                      QuoteMode quoteMode,
                                      Character commentMarker,
                                      Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines,
                                      String recordSeparator,
                                      String nullString,
                                      String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        // For headerComments, allowMissingColumnNames, ignoreHeaderCase, use defaults false/ null
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false);
    }

    @Test
    @Timeout(8000)
    void testEquals_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(';', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_differentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\'', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_differentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '!', null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_differentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '/',
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_differentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "null", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_differentLength() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_differentContent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a", "c"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                true, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLines() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecord() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, true);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_differentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        String[] header = new String[]{"col1", "col2"};
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "NULL", header, true);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "NULL", header, true);
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }
}