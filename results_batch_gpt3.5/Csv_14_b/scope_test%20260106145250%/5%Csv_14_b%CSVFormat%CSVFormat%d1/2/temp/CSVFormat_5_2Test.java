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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class CSVFormat_5_2Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorAndDefaults() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"h1", "h2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;

        CSVFormat csvFormat = constructor.newInstance(
                delimiter, quoteChar, quoteMode,
                commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim,
                trailingDelimiter);

        // Validate fields via getters
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, csvFormat.getHeaderComments());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, csvFormat.getIgnoreHeaderCase());
        assertEquals(trim, csvFormat.getTrim());
        assertEquals(trailingDelimiter, csvFormat.getTrailingDelimiter());

        // Validate methods that depend on fields
        assertTrue(csvFormat.isCommentMarkerSet());
        assertTrue(csvFormat.isEscapeCharacterSet());
        assertTrue(csvFormat.isQuoteCharacterSet());
        assertTrue(csvFormat.isNullStringSet());

        // Validate equals and hashCode
        CSVFormat copy = constructor.newInstance(
                delimiter, quoteChar, quoteMode,
                commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim,
                trailingDelimiter);
        assertEquals(csvFormat, copy);
        assertEquals(csvFormat.hashCode(), copy.hashCode());

        // Validate toString contains delimiter and quoteChar info
        String toString = csvFormat.toString();
        assertTrue(toString.contains(String.valueOf(delimiter)));
        assertTrue(toString.contains(String.valueOf(quoteChar)));

        // Validate static constants are non-null
        assertNotNull(CSVFormat.DEFAULT);
        assertNotNull(CSVFormat.EXCEL);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD_CSV);
        assertNotNull(CSVFormat.MYSQL);
        assertNotNull(CSVFormat.RFC4180);
        assertNotNull(CSVFormat.TDF);
    }

    @Test
    @Timeout(8000)
    void testStaticNewFormat() {
        CSVFormat format = CSVFormat.newFormat('|');
        assertEquals('|', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testStaticValueOf() {
        CSVFormat excel = CSVFormat.valueOf("EXCEL");
        assertEquals(CSVFormat.EXCEL, excel);

        CSVFormat defaultFormat = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, defaultFormat);

        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN"));
    }

    @Test
    @Timeout(8000)
    void testWithMethods() {
        CSVFormat base = CSVFormat.DEFAULT;

        CSVFormat withDelimiter = base.withDelimiter('|');
        assertEquals('|', withDelimiter.getDelimiter());

        CSVFormat withQuote = base.withQuote('\'');
        assertEquals(Character.valueOf('\''), withQuote.getQuoteCharacter());

        CSVFormat withEscape = base.withEscape('\\');
        assertEquals(Character.valueOf('\\'), withEscape.getEscapeCharacter());

        CSVFormat withComment = base.withCommentMarker('#');
        assertEquals(Character.valueOf('#'), withComment.getCommentMarker());

        CSVFormat withNullString = base.withNullString("NULL");
        assertEquals("NULL", withNullString.getNullString());

        CSVFormat withRecordSeparator = base.withRecordSeparator("\n");
        assertEquals("\n", withRecordSeparator.getRecordSeparator());

        CSVFormat withSkipHeader = base.withSkipHeaderRecord(true);
        assertTrue(withSkipHeader.getSkipHeaderRecord());

        CSVFormat withTrailingDelimiter = base.withTrailingDelimiter(true);
        assertTrue(withTrailingDelimiter.getTrailingDelimiter());

        CSVFormat withIgnoreEmptyLines = base.withIgnoreEmptyLines(false);
        assertFalse(withIgnoreEmptyLines.getIgnoreEmptyLines());

        CSVFormat withAllowMissingColumnNames = base.withAllowMissingColumnNames(true);
        assertTrue(withAllowMissingColumnNames.getAllowMissingColumnNames());

        CSVFormat withIgnoreHeaderCase = base.withIgnoreHeaderCase(true);
        assertTrue(withIgnoreHeaderCase.getIgnoreHeaderCase());

        CSVFormat withIgnoreSurroundingSpaces = base.withIgnoreSurroundingSpaces(true);
        assertTrue(withIgnoreSurroundingSpaces.getIgnoreSurroundingSpaces());

        CSVFormat withTrim = base.withTrim(true);
        assertTrue(withTrim.getTrim());

        CSVFormat withQuoteMode = base.withQuoteMode(QuoteMode.MINIMAL);
        assertEquals(QuoteMode.MINIMAL, withQuoteMode.getQuoteMode());
    }
}