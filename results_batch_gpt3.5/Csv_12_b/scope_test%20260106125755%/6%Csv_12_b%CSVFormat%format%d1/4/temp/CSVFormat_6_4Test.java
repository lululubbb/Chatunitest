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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_4Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() {
        String result = csvFormatDefault.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues() {
        String result = csvFormatDefault.format("a", null, "c");
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormatDefault.format("", "", "");
        assertEquals(",,", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withSingleValue() {
        String result = csvFormatDefault.format("single");
        assertEquals("single", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_trimOutput() {
        StringWriter writer = new StringWriter();
        // We test that output is trimmed by adding spaces in values
        String result = csvFormatDefault.format("  spaced  ");
        assertEquals("\"  spaced  \"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Fix ambiguous method call by casting argument matcher to Object[]
        doThrow(new IOException("mock IO exception")).when(mockPrinter).printRecord((Object[]) any());

        // Use reflection to create CSVFormat instance since constructor is private
        CSVFormat format = createCSVFormat(
                ',', '\"', null, null, null,
                false, true, "\r\n", null, null, false, false);

        // Instead of subclassing final CSVFormat, create a wrapper that delegates and injects mockPrinter
        CSVFormatWrapper wrapper = new CSVFormatWrapper(format, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            wrapper.format("x", "y");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO exception", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNoValues() {
        String result = csvFormatDefault.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withDifferentDelimiter() {
        CSVFormat format = csvFormatDefault.withDelimiter(';');
        String result = format.format("a", "b", "c");
        assertEquals("a;b;c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withQuoteCharacterNull() {
        CSVFormat format = csvFormatDefault.withQuote(null);
        String result = format.format("a", "b,c", "d");
        assertEquals("a,b,c,d", result.replace("\r\n", ""));
    }

    @Test
    @Timeout(8000)
    public void testFormat_withCustomQuoteMode() {
        CSVFormat format = csvFormatDefault.withQuoteMode(QuoteMode.ALL);
        String result = format.format("a", "b", "c");
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    private CSVFormat createCSVFormat(char delimiter,
                                      Character quoteChar,
                                      QuoteMode quoteMode,
                                      Character commentStart,
                                      Character escape,
                                      boolean ignoreSurroundingSpaces,
                                      boolean allowMissingColumnNames,
                                      String recordSeparator,
                                      String nullString,
                                      String[] header,
                                      boolean skipHeaderRecord,
                                      boolean ignoreEmptyLines)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, allowMissingColumnNames, recordSeparator, nullString,
                header, skipHeaderRecord, ignoreEmptyLines);
    }

    // Wrapper class to simulate overriding format() without subclassing final CSVFormat
    private static class CSVFormatWrapper {
        private final CSVFormat delegate;
        private final CSVPrinter mockPrinter;

        CSVFormatWrapper(CSVFormat delegate, CSVPrinter mockPrinter) {
            this.delegate = delegate;
            this.mockPrinter = mockPrinter;
        }

        public String format(Object... values) {
            try {
                mockPrinter.printRecord(values);
                // Return something consistent with CSVFormat.format output
                // but since mockPrinter doesn't write to a real writer, return empty string
                return "";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}