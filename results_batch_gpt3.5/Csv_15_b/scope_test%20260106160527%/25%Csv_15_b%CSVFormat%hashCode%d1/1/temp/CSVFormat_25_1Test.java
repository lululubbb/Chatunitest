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
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_25_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstance() {
        int expected = 1;
        int prime = 31;

        expected = prime * expected + csvFormat.getDelimiter();
        expected = prime * expected + (csvFormat.getQuoteMode() == null ? 0 : csvFormat.getQuoteMode().hashCode());
        expected = prime * expected + (csvFormat.getQuoteCharacter() == null ? 0 : csvFormat.getQuoteCharacter().hashCode());
        expected = prime * expected + (csvFormat.getCommentMarker() == null ? 0 : csvFormat.getCommentMarker().hashCode());
        expected = prime * expected + (csvFormat.getEscapeCharacter() == null ? 0 : csvFormat.getEscapeCharacter().hashCode());
        expected = prime * expected + (csvFormat.getNullString() == null ? 0 : csvFormat.getNullString().hashCode());
        expected = prime * expected + (csvFormat.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (csvFormat.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (csvFormat.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (csvFormat.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + (csvFormat.getRecordSeparator() == null ? 0 : csvFormat.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(csvFormat.getHeader());

        assertEquals(expected, csvFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_VariousFields() throws Exception {
        CSVFormat custom = CSVFormat.DEFAULT;
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        delimiterField.setChar(custom, ';');

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        quoteModeField.set(custom, QuoteMode.ALL);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        quoteCharField.set(custom, '"');

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(custom, '#');

        Field escapeCharField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharField.setAccessible(true);
        escapeCharField.set(custom, '\\');

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(custom, "NULL");

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreSurroundingSpacesField.setBoolean(custom, true);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        ignoreHeaderCaseField.setBoolean(custom, false);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        ignoreEmptyLinesField.setBoolean(custom, false);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        skipHeaderRecordField.setBoolean(custom, true);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        recordSeparatorField.set(custom, "\n");

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = new String[] {"a", "b"};
        headerField.set(custom, header);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ';';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('"').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + "\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, custom.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullFieldsAndFalseBooleans() throws Exception {
        CSVFormat custom = CSVFormat.DEFAULT;

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        quoteModeField.set(custom, null);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        quoteCharField.set(custom, null);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(custom, null);

        Field escapeCharField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharField.setAccessible(true);
        escapeCharField.set(custom, null);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(custom, null);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreSurroundingSpacesField.setBoolean(custom, false);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        ignoreHeaderCaseField.setBoolean(custom, false);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        ignoreEmptyLinesField.setBoolean(custom, false);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        skipHeaderRecordField.setBoolean(custom, false);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        recordSeparatorField.set(custom, null);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(custom, null);

        int prime = 31;
        int expected = 1;
        expected = prime * expected + custom.getDelimiter();
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0;
        expected = prime * expected + Arrays.hashCode(null);

        assertEquals(expected, custom.hashCode());
    }

}