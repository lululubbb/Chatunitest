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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatHashCodeTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstance() {
        int expected = computeExpectedHashCode(csvFormat);
        assertEquals(expected, csvFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentDelimiter() throws Exception {
        CSVFormat modified = csvFormat.withDelimiter(';');
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullQuoteMode() throws Exception {
        CSVFormat modified = csvFormat.withQuoteMode(null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullQuoteCharacter() throws Exception {
        CSVFormat modified = csvFormat.withQuote((Character) null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullCommentMarker() throws Exception {
        CSVFormat modified = csvFormat.withCommentMarker((Character) null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullEscapeCharacter() throws Exception {
        CSVFormat modified = csvFormat.withEscape((Character) null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullNullString() throws Exception {
        CSVFormat modified = csvFormat.withNullString(null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_BooleanFlags() throws Exception {
        CSVFormat modified = csvFormat.withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(false)
                .withIgnoreEmptyLines(false)
                .withSkipHeaderRecord(true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_RecordSeparatorNull() throws Exception {
        CSVFormat modified = csvFormat.withRecordSeparator((String) null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_HeaderArray() throws Exception {
        CSVFormat modified = csvFormat.withHeader("a", "b", "c");
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    // Helper method to compute expected hash code identical to CSVFormat.hashCode()
    private int computeExpectedHashCode(CSVFormat format) {
        try {
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
            String[] header = (String[]) headerField.get(format);
            result = prime * result + Arrays.hashCode(header);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}