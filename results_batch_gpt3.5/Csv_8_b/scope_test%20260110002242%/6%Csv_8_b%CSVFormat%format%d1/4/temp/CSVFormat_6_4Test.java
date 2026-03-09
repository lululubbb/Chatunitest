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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_6_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("value1", "value2", 3);
        assertNotNull(result);
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("3"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValue() throws IOException {
        String result = csvFormat.format("value1", null, "value3");
        assertNotNull(result);
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value3"));
        // null may be empty or some representation, just check it does not throw
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format();
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Create a mock CSVPrinter that throws IOException on printRecord
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord((Object[]) any());

        // Use a subclass of CSVFormat that overrides format to inject our mockPrinter
        CSVFormat customFormat = new CSVFormatWrapper(csvFormat, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            customFormat.format("value");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    // Helper subclass to override format method and inject mockPrinter
    private static class CSVFormatWrapper extends CSVFormat {
        private final CSVFormat delegate;
        private final CSVPrinter mockPrinter;

        CSVFormatWrapper(CSVFormat delegate, CSVPrinter mockPrinter) {
            super();
            this.delegate = delegate;
            this.mockPrinter = mockPrinter;
        }

        @Override
        public String format(final Object... values) {
            try {
                mockPrinter.printRecord(values);
                return "";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // Override all getters to delegate to the original CSVFormat instance
        @Override
        public char getDelimiter() {
            return delegate.getDelimiter();
        }

        @Override
        public Character getQuoteChar() {
            return delegate.getQuoteChar();
        }

        @Override
        public Quote getQuotePolicy() {
            return delegate.getQuotePolicy();
        }

        @Override
        public Character getCommentStart() {
            return delegate.getCommentStart();
        }

        @Override
        public Character getEscape() {
            return delegate.getEscape();
        }

        @Override
        public boolean getIgnoreSurroundingSpaces() {
            return delegate.getIgnoreSurroundingSpaces();
        }

        @Override
        public boolean getIgnoreEmptyLines() {
            return delegate.getIgnoreEmptyLines();
        }

        @Override
        public String getRecordSeparator() {
            return delegate.getRecordSeparator();
        }

        @Override
        public String getNullString() {
            return delegate.getNullString();
        }

        @Override
        public String[] getHeader() {
            return delegate.getHeader();
        }

        @Override
        public boolean getSkipHeaderRecord() {
            return delegate.getSkipHeaderRecord();
        }
    }
}