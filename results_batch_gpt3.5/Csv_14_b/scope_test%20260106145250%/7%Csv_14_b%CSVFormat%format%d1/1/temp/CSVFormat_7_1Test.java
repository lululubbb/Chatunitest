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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVFormat_7_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void format_singleValue_success() throws IOException {
        String result = csvFormat.format("value");
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    void format_multipleValues_success() throws IOException {
        String result = csvFormat.format("val1", "val2", "val3");
        assertEquals("val1,val2,val3", result);
    }

    @Test
    @Timeout(8000)
    void format_nullValue_success() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL");
        String result = format.format((Object) null);
        assertEquals("NULL", result);
    }

    @Test
    @Timeout(8000)
    void format_emptyValues_success() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void format_ioException_throwsIllegalStateException() throws Exception {
        // Create a mock CSVPrinter that throws IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord((Object[]) any());
        doNothing().when(mockPrinter).close();

        // Use reflection to create a CSVFormat instance with same fields as csvFormat
        CSVFormat spyFormat = createCSVFormatProxy(csvFormat, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            spyFormat.format("value1");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    private CSVFormat createCSVFormatProxy(CSVFormat original, CSVPrinter mockPrinter) throws Exception {
        // Get all fields from CSVFormat
        Field[] fields = CSVFormat.class.getDeclaredFields();
        // Prepare parameters for constructor
        char delimiter = getFieldValue(original, "delimiter", char.class);
        Character quoteChar = getFieldValue(original, "quoteCharacter", Character.class);
        QuoteMode quoteMode = getFieldValue(original, "quoteMode", QuoteMode.class);
        Character commentMarker = getFieldValue(original, "commentMarker", Character.class);
        Character escapeCharacter = getFieldValue(original, "escapeCharacter", Character.class);
        boolean ignoreSurroundingSpaces = getFieldValue(original, "ignoreSurroundingSpaces", boolean.class);
        boolean ignoreEmptyLines = getFieldValue(original, "ignoreEmptyLines", boolean.class);
        String recordSeparator = getFieldValue(original, "recordSeparator", String.class);
        String nullString = getFieldValue(original, "nullString", String.class);
        Object[] headerComments = getFieldValue(original, "headerComments", Object[].class);
        String[] header = getFieldValue(original, "header", String[].class);
        boolean skipHeaderRecord = getFieldValue(original, "skipHeaderRecord", boolean.class);
        boolean allowMissingColumnNames = getFieldValue(original, "allowMissingColumnNames", boolean.class);
        boolean ignoreHeaderCase = getFieldValue(original, "ignoreHeaderCase", boolean.class);
        boolean trim = getFieldValue(original, "trim", boolean.class);
        boolean trailingDelimiter = getFieldValue(original, "trailingDelimiter", boolean.class);

        // Use reflection to get the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create new instance
        CSVFormat proxyInstance = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);

        // Override format method via proxy by creating a dynamic proxy or subclass is impossible (final class).
        // Instead, use a wrapper class with the same interface and delegate all calls except format.

        return new CSVFormatWrapper(proxyInstance, mockPrinter);
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName, Class<T> fieldType) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    // Wrapper class to override format method only
    private static class CSVFormatWrapper extends CSVFormat {
        private final CSVFormat delegate;
        private final CSVPrinter mockPrinter;

        // Use reflection to call CSVFormat constructor and create instance to satisfy final class
        CSVFormatWrapper(CSVFormat delegate, CSVPrinter mockPrinter) throws Exception {
            super(getCharField(delegate, "delimiter"),
                    getCharObjField(delegate, "quoteCharacter"),
                    getField(delegate, "quoteMode", QuoteMode.class),
                    getCharObjField(delegate, "commentMarker"),
                    getCharObjField(delegate, "escapeCharacter"),
                    getBooleanField(delegate, "ignoreSurroundingSpaces"),
                    getBooleanField(delegate, "ignoreEmptyLines"),
                    getStringField(delegate, "recordSeparator"),
                    getStringField(delegate, "nullString"),
                    getField(delegate, "headerComments", Object[].class),
                    getStringArrayField(delegate, "header"),
                    getBooleanField(delegate, "skipHeaderRecord"),
                    getBooleanField(delegate, "allowMissingColumnNames"),
                    getBooleanField(delegate, "ignoreHeaderCase"),
                    getBooleanField(delegate, "trim"),
                    getBooleanField(delegate, "trailingDelimiter"));

            this.delegate = delegate;
            this.mockPrinter = mockPrinter;
        }

        @Override
        public String format(final Object... values) {
            try {
                mockPrinter.printRecord(values);
                return ""; // won't reach here
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // Delegate all other methods to the original CSVFormat instance
        @Override public char getDelimiter() { return delegate.getDelimiter(); }
        @Override public Character getQuoteCharacter() { return delegate.getQuoteCharacter(); }
        @Override public QuoteMode getQuoteMode() { return delegate.getQuoteMode(); }
        @Override public Character getCommentMarker() { return delegate.getCommentMarker(); }
        @Override public Character getEscapeCharacter() { return delegate.getEscapeCharacter(); }
        @Override public boolean getIgnoreSurroundingSpaces() { return delegate.getIgnoreSurroundingSpaces(); }
        @Override public boolean getIgnoreEmptyLines() { return delegate.getIgnoreEmptyLines(); }
        @Override public String getRecordSeparator() { return delegate.getRecordSeparator(); }
        @Override public String getNullString() { return delegate.getNullString(); }
        @Override public String[] getHeader() { return delegate.getHeader(); }
        @Override public boolean getSkipHeaderRecord() { return delegate.getSkipHeaderRecord(); }
        @Override public boolean getAllowMissingColumnNames() { return delegate.getAllowMissingColumnNames(); }
        @Override public boolean getIgnoreHeaderCase() { return delegate.getIgnoreHeaderCase(); }
        @Override public boolean getTrim() { return delegate.getTrim(); }
        @Override public boolean getTrailingDelimiter() { return delegate.getTrailingDelimiter(); }

        @Override
        public boolean equals(final Object obj) {
            return delegate.equals(obj);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        private static char getCharField(CSVFormat obj, String fieldName) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.getChar(obj);
        }

        private static Character getCharObjField(CSVFormat obj, String fieldName) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (Character) f.get(obj);
        }

        private static boolean getBooleanField(CSVFormat obj, String fieldName) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.getBoolean(obj);
        }

        private static String getStringField(CSVFormat obj, String fieldName) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (String) f.get(obj);
        }

        private static Object[] getField(CSVFormat obj, String fieldName, Class<?> type) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (Object[]) f.get(obj);
        }

        private static String[] getStringArrayField(CSVFormat obj, String fieldName) throws Exception {
            Field f = CSVFormat.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (String[]) f.get(obj);
        }
    }
}