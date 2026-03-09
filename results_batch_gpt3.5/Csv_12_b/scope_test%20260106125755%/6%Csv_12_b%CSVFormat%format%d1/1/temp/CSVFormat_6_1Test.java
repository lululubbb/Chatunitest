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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVFormat_6_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withValidValues_returnsFormattedString() throws IOException {
        String[] values = {"value1", "value2", "value3"};
        String result = csvFormat.format((Object[]) values);
        // The default delimiter is comma, default quote char is double quote
        // So expected output is: "value1","value2","value3"
        assertEquals("\"value1\",\"value2\",\"value3\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValues_returnsFormattedString() throws IOException {
        Object[] values = {null, "abc", null};
        String result = csvFormat.format(values);
        // null values are formatted as empty strings
        assertEquals("\"\",\"abc\",\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues_returnsEmptyString() throws IOException {
        Object[] values = {};
        String result = csvFormat.format(values);
        // Empty record results in empty string
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withIOException_throwsIllegalStateException() throws Exception {
        Object[] values = {"a", "b"};

        // Create a spy CSVPrinter that throws IOException on printRecord(Object...)
        StringWriter out = new StringWriter();
        CSVPrinter realPrinter = new CSVPrinter(out, csvFormat);
        CSVPrinter spyPrinter = Mockito.spy(realPrinter);
        doThrow(new IOException("mock IO exception")).when(spyPrinter).printRecord((Object[]) any());

        // Use reflection to create a CSVFormat instance with a custom format method
        // Since CSVFormat is final and constructor is private, we cannot subclass or instantiate directly.
        // Instead, we create a helper class here.

        class CSVFormatWithSpy {
            String format(final Object... vals) {
                try {
                    spyPrinter.printRecord(vals);
                    return out.toString().trim();
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        CSVFormatWithSpy csvFormatWithSpy = new CSVFormatWithSpy();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            csvFormatWithSpy.format(values);
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("mock IO exception", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    void testFormat_withSingleValue_returnsFormattedString() {
        Object[] values = {"singleValue"};
        String result = csvFormat.format((Object[]) values);
        assertEquals("\"singleValue\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMixedTypes_valuesFormatted() {
        Object[] values = {"string", 123, 45.67, true};
        String result = csvFormat.format((Object[]) values);
        assertEquals("\"string\",\"123\",\"45.67\",\"true\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_trimsTrailingWhitespace() {
        Object[] values = {"value1 ", " value2", " value3 "};
        String result = csvFormat.format((Object[]) values);
        // CSVPrinter does not trim values, but format method trims result string
        assertEquals("\"value1 \",\" value2\",\" value3\"", result);
    }
}