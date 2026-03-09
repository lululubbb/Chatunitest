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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_49_2Test {

    @Test
    @Timeout(8000)
    void testWithNullString() throws Exception {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.MINIMAL;
        Character commentMarker = '#';
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"h1", "h2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;

        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class);
        ctor.setAccessible(true);

        CSVFormat original = ctor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase);

        // When
        String newNullString = "NULL";
        CSVFormat modified = original.withNullString(newNullString);

        // Then
        assertNotSame(original, modified);
        assertEquals(delimiter, modified.getDelimiter());
        assertEquals(quoteChar, modified.getQuoteCharacter());
        assertEquals(quoteMode, modified.getQuoteMode());
        assertEquals(commentMarker, modified.getCommentMarker());
        assertEquals(escapeCharacter, modified.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, modified.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, modified.getIgnoreEmptyLines());
        assertEquals(recordSeparator, modified.getRecordSeparator());
        assertArrayEquals(toStringArray(headerComments), modified.getHeaderComments());
        assertArrayEquals(header, modified.getHeader());
        assertEquals(skipHeaderRecord, modified.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, modified.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, modified.getIgnoreHeaderCase());
        assertEquals(newNullString, modified.getNullString());

        // Original nullString remains unchanged
        assertNull(original.getNullString());

        // Test with null nullString resets to null
        CSVFormat resetNullStringFormat = modified.withNullString(null);
        assertNull(resetNullStringFormat.getNullString());
    }

    private static String[] toStringArray(Object[] objects) {
        if (objects == null) {
            return null;
        }
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = objects[i] == null ? null : objects[i].toString();
        }
        return strings;
    }
}