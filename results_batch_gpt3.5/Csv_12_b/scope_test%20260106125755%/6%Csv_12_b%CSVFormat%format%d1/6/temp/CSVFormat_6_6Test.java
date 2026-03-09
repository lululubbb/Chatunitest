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
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNormalValues() {
        String formatted = csvFormat.format("value1", "value2", "value3");
        assertNotNull(formatted);
        assertTrue(formatted.contains("value1"));
        assertTrue(formatted.contains("value2"));
        assertTrue(formatted.contains("value3"));
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyValues() {
        String formatted = csvFormat.format();
        assertNotNull(formatted);
        assertEquals("", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValues() {
        String formatted = csvFormat.format((Object) null);
        assertNotNull(formatted);
        assertEquals("", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // Mock CSVPrinter to throw IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord(any(Object[].class));

        // Use the default CSVFormat instance and override its print method via proxy or reflection
        // Since CSVFormat is final and its constructor is private, we cannot subclass it.
        // Instead, create a spy of CSVFormat.DEFAULT and override print method via reflection.

        CSVFormat spyFormat = csvFormat;

        // Use reflection to replace the 'print' method by creating a proxy CSVPrinter
        // but since print is not final and is public, we can use a wrapper CSVFormat with a proxy CSVPrinter

        // Create a CSVFormat proxy that returns the mockPrinter when print(Appendable) is called
        CSVFormat formatWithMockPrinter = new CSVFormatWrapper(spyFormat, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            formatWithMockPrinter.format("a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    // Helper wrapper class to override print(Appendable) method
    private static class CSVFormatWrapper extends CSVFormat {
        private final CSVFormat delegate;
        private final CSVPrinter mockPrinter;

        CSVFormatWrapper(CSVFormat delegate, CSVPrinter mockPrinter) {
            super(delegate.getDelimiter(),
                    delegate.getQuoteCharacter(),
                    delegate.getQuoteMode(),
                    delegate.getCommentMarker(),
                    delegate.getEscapeCharacter(),
                    delegate.getIgnoreSurroundingSpaces(),
                    delegate.getIgnoreEmptyLines(),
                    delegate.getRecordSeparator(),
                    delegate.getNullString(),
                    delegate.getHeader(),
                    delegate.getSkipHeaderRecord(),
                    delegate.getAllowMissingColumnNames());
            this.delegate = delegate;
            this.mockPrinter = mockPrinter;
        }

        @Override
        public CSVPrinter print(final Appendable out) {
            return mockPrinter;
        }
    }
}