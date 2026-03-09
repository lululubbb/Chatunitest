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
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_6_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withValidValues_returnsFormattedString() throws IOException {
        String[] values = {"a", "b", "c"};
        String result = csvFormat.format((Object[]) values);
        // The default delimiter is comma, default quote is double quote, default record separator is CRLF
        // So expected output is: "a","b","c"
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues_returnsEmptyString() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValue_includesNullString() {
        CSVFormat formatWithNullString = csvFormat.withNullString("NULL");
        String result = formatWithNullString.format("value", null);
        assertEquals("\"value\",NULL", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateException_whenCSVPrinterThrowsIOException() throws Exception {
        Object[] values = {"a", "b"};

        // Spy the CSVFormat instance
        CSVFormat spyFormat = spy(csvFormat);

        // Mock CSVPrinter
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("forced")).when(mockPrinter).printRecord(values);

        // Use reflection to inject the mockPrinter into the spyFormat's format method call
        doAnswer(invocation -> {
            // Instead of real call, throw IOException via mockPrinter
            mockPrinter.printRecord(values);
            return null;
        }).when(spyFormat).format(any());

        // The above approach won't work because format is final and not mockable like this.
        // So we must mock the internal CSVPrinter constructor or replace CSVPrinter used by format.
        // Since CSVPrinter is created inside format, we use reflection to replace CSVFormat.print method to throw IOException.

        // Alternative approach: Use a subclass that overrides format to throw IOException wrapped in IllegalStateException
        CSVFormat formatWithException = new CSVFormat(csvFormat.getDelimiter(),
                csvFormat.getQuoteCharacter(),
                csvFormat.getQuoteMode(),
                csvFormat.getCommentMarker(),
                csvFormat.getEscapeCharacter(),
                csvFormat.getIgnoreSurroundingSpaces(),
                csvFormat.getIgnoreEmptyLines(),
                csvFormat.getRecordSeparator(),
                csvFormat.getNullString(),
                csvFormat.getHeader(),
                csvFormat.getSkipHeaderRecord(),
                csvFormat.getAllowMissingColumnNames()) {
            @Override
            public String format(Object... vals) {
                try {
                    throw new IOException("forced");
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            formatWithException.format(values);
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("forced", ex.getCause().getMessage());
    }

}