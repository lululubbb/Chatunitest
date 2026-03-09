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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintlnTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVFormat instance with trailingDelimiter = false and recordSeparator = null initially
        csvFormat = CSVFormat.DEFAULT;
        csvFormat = withTrailingDelimiter(csvFormat, false);
        csvFormat = withRecordSeparator(csvFormat, null);
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintln_noTrailingDelimiter_noRecordSeparator() throws IOException {
        // trailingDelimiter = false, recordSeparator = null
        csvFormat.println(appendable);
        verify(appendable, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintln_trailingDelimiterTrue_noRecordSeparator() throws IOException {
        // trailingDelimiter = true, recordSeparator = null
        csvFormat = withTrailingDelimiter(csvFormat, true);
        csvFormat = withRecordSeparator(csvFormat, null);
        csvFormat.println(appendable);
        // Should append delimiter only
        verify(appendable).append(String.valueOf(csvFormat.getDelimiter()));
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_noTrailingDelimiter_recordSeparatorSet() throws IOException {
        // trailingDelimiter = false, recordSeparator set to CRLF
        csvFormat = withTrailingDelimiter(csvFormat, false);
        csvFormat = withRecordSeparator(csvFormat, "\r\n");
        csvFormat.println(appendable);
        verify(appendable).append("\r\n");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_trailingDelimiterTrue_recordSeparatorSet() throws IOException {
        // trailingDelimiter = true, recordSeparator set to LF
        csvFormat = withTrailingDelimiter(csvFormat, true);
        csvFormat = withRecordSeparator(csvFormat, "\n");
        csvFormat.println(appendable);
        // Should append delimiter then recordSeparator
        verify(appendable).append(String.valueOf(csvFormat.getDelimiter()));
        verify(appendable).append("\n");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_appendThrowsIOException() throws IOException {
        csvFormat = withTrailingDelimiter(csvFormat, true);
        csvFormat = withRecordSeparator(csvFormat, "X");
        doThrow(new IOException("append failed")).when(appendable).append(any(CharSequence.class));
        IOException exception = assertThrows(IOException.class, () -> csvFormat.println(appendable));
        assertEquals("append failed", exception.getMessage());
    }

    // Helper methods using reflection to create modified CSVFormat instances
    private static CSVFormat withTrailingDelimiter(CSVFormat original, boolean trailingDelimiter) throws Exception {
        return withField(original, "trailingDelimiter", trailingDelimiter);
    }

    private static CSVFormat withRecordSeparator(CSVFormat original, String recordSeparator) throws Exception {
        return withField(original, "recordSeparator", recordSeparator);
    }

    private static CSVFormat withField(CSVFormat original, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Since CSVFormat is immutable, clone original and set field to new value
        CSVFormat copy = copyCSVFormat(original);
        field.set(copy, value);
        return copy;
    }

    private static CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        // Use the constructor with all fields to create a copy
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter)

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentStartField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");

        delimiterField.setAccessible(true);
        quoteCharField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentStartField.setAccessible(true);
        escapeField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerCommentsField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        trimField.setAccessible(true);
        trailingDelimiterField.setAccessible(true);

        char delimiter = delimiterField.getChar(original);
        Character quoteChar = (Character) quoteCharField.get(original);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
        Character commentStart = (Character) commentStartField.get(original);
        Character escape = (Character) escapeField.get(original);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);
        String recordSeparator = (String) recordSeparatorField.get(original);
        String nullString = (String) nullStringField.get(original);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);
        String[] header = (String[]) headerField.get(original);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);
        boolean trim = trimField.getBoolean(original);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(original);

        // Use constructor to create new instance
        return new CSVFormat(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);
    }
}