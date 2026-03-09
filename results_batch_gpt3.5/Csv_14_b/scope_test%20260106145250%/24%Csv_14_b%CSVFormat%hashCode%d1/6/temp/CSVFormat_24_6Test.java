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

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_6Test {

    private CSVFormat formatDefault;
    private CSVFormat formatCustom1;
    private CSVFormat formatCustom2;

    @BeforeEach
    public void setUp() {
        formatDefault = CSVFormat.DEFAULT;

        formatCustom1 = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(false)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true)
                .withRecordSeparator("\n")
                .withHeader("col1", "col2");

        formatCustom2 = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(false)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true)
                .withRecordSeparator("\n")
                .withHeader("col1", "col2");
    }

    @Test
    @Timeout(8000)
    public void testHashCode_Defaults() {
        int expected = 1;
        final int prime = 31;

        expected = prime * expected + CSVFormat.DEFAULT.getDelimiter();
        expected = prime * expected + (CSVFormat.DEFAULT.getQuoteMode() == null ? 0 : CSVFormat.DEFAULT.getQuoteMode().hashCode());
        expected = prime * expected + (CSVFormat.DEFAULT.getQuoteCharacter() == null ? 0 : CSVFormat.DEFAULT.getQuoteCharacter().hashCode());
        expected = prime * expected + (CSVFormat.DEFAULT.getCommentMarker() == null ? 0 : CSVFormat.DEFAULT.getCommentMarker().hashCode());
        expected = prime * expected + (CSVFormat.DEFAULT.getEscapeCharacter() == null ? 0 : CSVFormat.DEFAULT.getEscapeCharacter().hashCode());
        expected = prime * expected + (CSVFormat.DEFAULT.getNullString() == null ? 0 : CSVFormat.DEFAULT.getNullString().hashCode());
        expected = prime * expected + (CSVFormat.DEFAULT.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (CSVFormat.DEFAULT.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (CSVFormat.DEFAULT.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (CSVFormat.DEFAULT.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + (CSVFormat.DEFAULT.getRecordSeparator() == null ? 0 : CSVFormat.DEFAULT.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(CSVFormat.DEFAULT.getHeader());

        assertEquals(expected, formatDefault.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_EqualObjects() {
        assertEquals(formatCustom1.hashCode(), formatCustom2.hashCode());
        assertEquals(formatCustom1.hashCode(), formatCustom1.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentObjects() {
        assertNotEquals(formatDefault.hashCode(), formatCustom1.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullFields() throws Exception {
        // Use reflection to create CSVFormat with null fields to test null handling in hashCode

        Class<CSVFormat> clazz = CSVFormat.class;
        // private constructor parameters:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
        //  Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
        //  String recordSeparator, String nullString, Object[] headerComments, String[] header,
        //  boolean skipHeaderRecord, boolean allowMissingColumnNames, boolean ignoreHeaderCase,
        //  boolean trim, boolean trailingDelimiter)

        // Prepare parameters with nulls
        char delimiter = ',';
        Character quoteChar = null;
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = false;
        String recordSeparator = null;
        String nullString = null;
        Object[] headerComments = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        // Get private constructor
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat formatNulls = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

        int expected = 1;
        final int prime = 31;

        expected = prime * expected + delimiter;
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + (ignoreSurroundingSpaces ? 1231 : 1237);
        expected = prime * expected + (ignoreHeaderCase ? 1231 : 1237);
        expected = prime * expected + (ignoreEmptyLines ? 1231 : 1237);
        expected = prime * expected + (skipHeaderRecord ? 1231 : 1237);
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(header); // header null => 0

        assertEquals(expected, formatNulls.hashCode());
    }
}