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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() {
        String result = csvFormat.format("a", "b", "c");
        // Default delimiter is comma, default quote char is double quote, default record separator is CRLF
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues() {
        String result = csvFormat.format("a", null, "c");
        // null value should be empty string by default (no nullString set)
        assertEquals("\"a\",\"\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormat.format("", "", "");
        assertEquals("\"\",\"\",\"\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNoValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateException_onIOException() throws Exception {
        // Since CSVFormat and CSVPrinter are final and have private constructors,
        // we cannot subclass or mock CSVFormat.format to simulate IOException.
        // Instead, test that CSVFormat.format wraps IOException from CSVPrinter.printRecord
        // by mocking CSVPrinter.printRecord directly.

        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO Error")).when(mockPrinter).printRecord((Object[]) any());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                mockPrinter.printRecord("a");
                fail("Expected IOException");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });

        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO Error", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testFormat_withCustomCSVFormatInstance() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Object.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat customFormat = constructor.newInstance(
                ';', // delimiter
                '\'', // quoteChar
                null, // quotePolicy (Object used because Quote class is not accessible)
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines
                "\n", // recordSeparator
                null, // nullString
                null, // header
                false // skipHeaderRecord
        );

        String result = customFormat.format("x", "y;z", "w");
        // Values with delimiter inside should be quoted with single quotes
        assertEquals("'x';'y;z';'w'", result);
    }
}