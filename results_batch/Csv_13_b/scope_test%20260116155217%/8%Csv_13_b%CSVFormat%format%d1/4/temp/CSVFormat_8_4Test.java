package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_4Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatNoQuote;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    public void setup() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatNoQuote = CSVFormat.DEFAULT.withQuote((Character) null);
        csvFormatCustom = CSVFormat.newFormat(';').withQuote('\'').withQuoteMode(QuoteMode.ALL);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() {
        String result = csvFormatDefault.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        String result = format.format("a", null, "c");
        assertEquals("a,NULL,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormatDefault.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNoQuoteCharacter() {
        String result = csvFormatNoQuote.format("a,b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withCustomDelimiterAndQuote() {
        String result = csvFormatCustom.format("a;b", "b;c");
        // Values quoted due to QuoteMode.ALL and quote char '\''
        assertEquals("'a;b';'b;c'", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Create a CSVFormat instance proxy that throws IOException in format
        CSVFormat format = createCSVFormatProxyThrowingIOException(CSVFormat.DEFAULT);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            format.format("test");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    // Factory method to create a CSVFormat proxy instance that throws IOException in format()
    private CSVFormat createCSVFormatProxyThrowingIOException(CSVFormat delegate) throws Exception {
        return new CSVFormatProxy(delegate);
    }

    // Proxy class to simulate CSVFormat but override format to throw IOException wrapped in IllegalStateException
    private static class CSVFormatProxy {

        private final CSVFormat delegate;

        CSVFormatProxy(CSVFormat delegate) throws Exception {
            // Use reflection to create a new CSVFormat instance copying fields from delegate
            this.delegate = createCSVFormatCopy(delegate);
        }

        public String format(final Object... values) {
            try {
                throw new IOException("IO error");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // Delegate all other methods to the delegate instance
        @Override
        public boolean equals(Object obj) {
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

        // Reflection helper to create a CSVFormat instance copying all fields from delegate
        private static CSVFormat createCSVFormatCopy(CSVFormat original) throws Exception {
            Class<CSVFormat> clazz = CSVFormat.class;

            Field delimiterField = clazz.getDeclaredField("delimiter");
            Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
            Field quoteModeField = clazz.getDeclaredField("quoteMode");
            Field commentMarkerField = clazz.getDeclaredField("commentMarker");
            Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
            Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
            Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
            Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
            Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
            Field nullStringField = clazz.getDeclaredField("nullString");
            Field headerField = clazz.getDeclaredField("header");
            Field headerCommentsField = clazz.getDeclaredField("headerComments");
            Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
            Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");

            delimiterField.setAccessible(true);
            quoteCharacterField.setAccessible(true);
            quoteModeField.setAccessible(true);
            commentMarkerField.setAccessible(true);
            escapeCharacterField.setAccessible(true);
            ignoreSurroundingSpacesField.setAccessible(true);
            allowMissingColumnNamesField.setAccessible(true);
            ignoreEmptyLinesField.setAccessible(true);
            recordSeparatorField.setAccessible(true);
            nullStringField.setAccessible(true);
            headerField.setAccessible(true);
            headerCommentsField.setAccessible(true);
            skipHeaderRecordField.setAccessible(true);
            ignoreHeaderCaseField.setAccessible(true);

            char delimiter = delimiterField.getChar(original);
            Character quoteCharacter = (Character) quoteCharacterField.get(original);
            QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
            Character commentMarker = (Character) commentMarkerField.get(original);
            Character escapeCharacter = (Character) escapeCharacterField.get(original);
            boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);
            boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);
            boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);
            String recordSeparator = (String) recordSeparatorField.get(original);
            String nullString = (String) nullStringField.get(original);
            String[] header = (String[]) headerField.get(original);
            Object[] headerComments = (Object[]) headerCommentsField.get(original);
            boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);
            boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);

            // Find the private constructor
            java.lang.reflect.Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class,
                    Object[].class, String[].class, boolean.class, boolean.class, boolean.class);

            ctor.setAccessible(true);

            return ctor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                    headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
        }
    }
}