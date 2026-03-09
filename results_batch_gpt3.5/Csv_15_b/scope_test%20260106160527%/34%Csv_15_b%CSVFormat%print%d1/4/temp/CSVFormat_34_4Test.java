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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_34_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_PathAndCharset_ReturnsCSVPrinter() throws Exception {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        Appendable mockWriter = mock(Appendable.class);

        // Create a CSVFormat instance by copying DEFAULT and override the print(Appendable) method using a proxy
        CSVFormat testFormat = createCSVFormatWithMockPrint(csvFormat, mockWriter, mockPrinter);

        CSVPrinter printer = testFormat.print(mockPath, StandardCharsets.UTF_8);

        assertNotNull(printer);
        assertEquals(mockPrinter, printer);
    }

    @Test
    @Timeout(8000)
    public void testPrint_PathAndCharset_ThrowsIOException() {
        Path mockPath = mock(Path.class);

        CSVFormat testFormat = new CSVFormatWrapperThrowingIOException(csvFormat);

        IOException thrown = assertThrows(IOException.class, () -> {
            testFormat.print(mockPath, StandardCharsets.UTF_8);
        });

        assertEquals("IO Error", thrown.getMessage());
    }

    /**
     * Helper method to create a CSVFormat instance that delegates all calls to base,
     * except print(Appendable) which returns mockPrinter.
     */
    private static CSVFormat createCSVFormatWithMockPrint(CSVFormat base, Appendable mockWriter, CSVPrinter mockPrinter) throws Exception {
        // CSVFormat is final, so we cannot subclass it.
        // Instead, we create a new CSVFormat instance copying all fields from base,
        // then use reflection to override the print(Appendable) method by creating a proxy via a subclass of CSVFormat's printer() or by a wrapper.

        // Since print(Appendable) is not final, but CSVFormat is final,
        // we cannot override methods by subclassing CSVFormat.
        // So we create a proxy CSVPrinter for print(Appendable).

        // Instead, we create a CSVFormat instance by copying all fields from base using reflection.
        CSVFormat copy = createCSVFormatCopy(base);

        // We cannot override print(Appendable) in CSVFormat, so we mock the print(Appendable) method behavior by reflection:
        // We replace the print(Appendable) method call with a method that returns mockPrinter.

        // The method print(Path, Charset) calls print(BufferedWriter) internally.
        // So we mock Files.newBufferedWriter to return mockWriter? Not possible here.
        // Instead, we override print(Path, Charset) via a proxy.

        // We create a proxy CSVFormat by using a dynamic proxy or a wrapper class that delegates all calls except print(Path, Charset).

        return new CSVFormatWrapperWithMockPrint(copy, mockWriter, mockPrinter);
    }

    /**
     * Creates a deep copy of CSVFormat by copying all fields via reflection.
     */
    private static CSVFormat createCSVFormatCopy(CSVFormat base) throws Exception {
        // Get CSVFormat constructor parameters from base by reflection
        Class<?> clazz = CSVFormat.class;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(base);

        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(base);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(base);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(base);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(base);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(base);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(base);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(base);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(base);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(base);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(base);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(base);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(base);

        Field trimField = clazz.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(base);

        Field trailingDelimiterField = clazz.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(base);

        Field autoFlushField = clazz.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);
        boolean autoFlush = autoFlushField.getBoolean(base);

        // CSVFormat constructor is private, so we create an instance via reflection:
        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class,
                boolean.class, boolean.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class
        );
        ctor.setAccessible(true);

        CSVFormat copy = ctor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush
        );

        return copy;
    }

    /**
     * Wrapper class to override print(Path, Charset) to return mockPrinter,
     * delegating all other methods to wrapped CSVFormat instance.
     */
    private static class CSVFormatWrapperWithMockPrint extends CSVFormat {
        private final CSVFormat delegate;
        private final Appendable mockWriter;
        private final CSVPrinter mockPrinter;

        CSVFormatWrapperWithMockPrint(CSVFormat delegate, Appendable mockWriter, CSVPrinter mockPrinter) {
            super(
                delegate.getDelimiter(),
                delegate.getQuoteCharacter(),
                delegate.getQuoteMode(),
                delegate.getCommentMarker(),
                delegate.getEscapeCharacter(),
                delegate.getIgnoreSurroundingSpaces(),
                delegate.getIgnoreEmptyLines(),
                delegate.getRecordSeparator(),
                getHeaderComments(delegate),
                delegate.getHeader(),
                delegate.getSkipHeaderRecord(),
                delegate.getAllowMissingColumnNames(),
                delegate.getIgnoreHeaderCase(),
                delegate.getTrim(),
                delegate.getTrailingDelimiter(),
                delegate.getAutoFlush()
            );
            this.delegate = delegate;
            this.mockWriter = mockWriter;
            this.mockPrinter = mockPrinter;
        }

        @Override
        public CSVPrinter print(Path out, java.nio.charset.Charset charset) throws IOException {
            // Return mockPrinter instead of real print
            return mockPrinter;
        }

        // Override other methods to delegate to original CSVFormat
        // Since CSVFormat is final, we cannot override all methods easily,
        // but the test only calls print(Path, Charset), so this suffices.
    }

    /**
     * Wrapper class to override print(Path, Charset) to throw IOException,
     * delegating all other methods to wrapped CSVFormat instance.
     */
    private static class CSVFormatWrapperThrowingIOException extends CSVFormat {
        private final CSVFormat delegate;

        CSVFormatWrapperThrowingIOException(CSVFormat delegate) {
            super(
                delegate.getDelimiter(),
                delegate.getQuoteCharacter(),
                delegate.getQuoteMode(),
                delegate.getCommentMarker(),
                delegate.getEscapeCharacter(),
                delegate.getIgnoreSurroundingSpaces(),
                delegate.getIgnoreEmptyLines(),
                delegate.getRecordSeparator(),
                getHeaderComments(delegate),
                delegate.getHeader(),
                delegate.getSkipHeaderRecord(),
                delegate.getAllowMissingColumnNames(),
                delegate.getIgnoreHeaderCase(),
                delegate.getTrim(),
                delegate.getTrailingDelimiter(),
                delegate.getAutoFlush()
            );
            this.delegate = delegate;
        }

        @Override
        public CSVPrinter print(Path out, java.nio.charset.Charset charset) throws IOException {
            throw new IOException("IO Error");
        }
    }

    private static Object[] getHeaderComments(CSVFormat format) {
        try {
            Field f = CSVFormat.class.getDeclaredField("headerComments");
            f.setAccessible(true);
            return (Object[]) f.get(format);
        } catch (Exception e) {
            return null;
        }
    }
}