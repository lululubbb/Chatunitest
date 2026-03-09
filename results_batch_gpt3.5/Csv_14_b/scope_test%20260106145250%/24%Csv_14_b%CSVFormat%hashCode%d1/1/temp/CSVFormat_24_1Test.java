package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_defaultInstances_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentDelimiter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullQuoteMode() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullQuoteCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullEscapeCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullNullString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_booleanFlags() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());

        CSVFormat formatFalse = format
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreHeaderCase(false)
                .withIgnoreEmptyLines(false)
                .withSkipHeaderRecord(false);
        int expectedFalse = computeExpectedHashCode(formatFalse);
        assertEquals(expectedFalse, formatFalse.hashCode());
        assertNotEquals(expected, expectedFalse);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullRecordSeparator() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_headerArray() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());

        CSVFormat formatEmpty = CSVFormat.DEFAULT.withHeader();
        int expectedEmpty = computeExpectedHashCode(formatEmpty);
        assertEquals(expectedEmpty, formatEmpty.hashCode());

        assertNotEquals(expected, expectedEmpty);
    }

    private int computeExpectedHashCode(CSVFormat format) throws Exception {
        final int prime = 31;
        int result = 1;

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(format);
        result = prime * result + delimiter;

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(format);
        result = prime * result + (quoteMode == null ? 0 : quoteMode.hashCode());

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Object quoteCharacter = quoteCharacterField.get(format);
        result = prime * result + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Object commentMarker = commentMarkerField.get(format);
        result = prime * result + (commentMarker == null ? 0 : commentMarker.hashCode());

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Object escapeCharacter = escapeCharacterField.get(format);
        result = prime * result + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Object nullString = nullStringField.get(format);
        result = prime * result + (nullString == null ? 0 : nullString.hashCode());

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);
        result = prime * result + (ignoreHeaderCase ? 1231 : 1237);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        result = prime * result + (ignoreEmptyLines ? 1231 : 1237);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
        result = prime * result + (skipHeaderRecord ? 1231 : 1237);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        Object recordSeparator = recordSeparatorField.get(format);
        result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object[] header = (Object[]) headerField.get(format);
        result = prime * result + Arrays.hashCode(header);

        return result;
    }
}